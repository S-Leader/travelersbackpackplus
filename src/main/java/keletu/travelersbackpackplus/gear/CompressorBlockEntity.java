package keletu.travelersbackpackplus.gear;

import com.tiviacz.travelersbackpack.util.StacksHandlerUtils;
import keletu.travelersbackpackplus.GearRegistry;
import keletu.travelersbackpackplus.menu.CompressorMenu;
import keletu.travelersbackpackplus.menu.GearTankTransfer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.FuelValues;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.fluid.FluidStacksResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

public final class CompressorBlockEntity extends BaseContainerBlockEntity {
    public static final int CAPACITY = 16000;
    private NonNullList<ItemStack> items = NonNullList.withSize(4, ItemStack.EMPTY);
    private int burn;
    private int fuel;
    private int beat;
    private long lastStroke = Long.MIN_VALUE / 2;
    public final FluidStacksResourceHandler airTank = new FluidStacksResourceHandler(1, CAPACITY) {
        @Override
        public boolean isValid(int index, FluidResource resource) {
            return AirTransfer.breathable(resource);
        }

        @Override
        public int insert(int index, FluidResource resource, int amount, TransactionContext transaction) {
            return AirTransfer.breathable(resource) ? super.insert(index, FluidResource.of(GearRegistry.AIR.get()), amount, transaction) : 0;
        }

        @Override
        protected void onContentsChanged(int index, FluidStack previous) {
            setChanged();
        }
    };

    public CompressorBlockEntity(BlockPos pos, BlockState state) {
        super(GearRegistry.COMPRESSOR_ENTITY.get(), pos, state);
    }

    public int air() {
        return StacksHandlerUtils.getFluidAmount(airTank);
    }

    public int burn() {
        return burn;
    }

    public int fuel() {
        return fuel;
    }

    @Override
    public int getContainerSize() {
        return 4;
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
        this.items = items;
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("block.travelersbackpackplus.air_compressor");
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
        return new CompressorMenu(id, inventory, this);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, CompressorBlockEntity entity) {
        if (++entity.beat < 10) return;
        entity.beat = 0;
        entity.work(level.fuelValues(), level.isEmptyBlock(pos.above()), 10);
    }

    public void work(FuelValues fuels, boolean openAir, int ticks) {
        if (burn > 0) {
            int used = Math.min(burn, ticks);
            burn -= used;
            if (openAir) StacksHandlerUtils.fill(airTank, new FluidStack(GearRegistry.AIR.get(), used * 10), false);
            setChanged();
        }
        if (burn <= 0 && openAir && air() < CAPACITY) {
            ItemStack fuelStack = getItem(0);
            int duration = fuelStack.getBurnTime(RecipeType.SMELTING, fuels);
            if (duration > 0) {
                ItemStack consumed = fuelStack.split(1);
                var remainder = consumed.getCraftingRemainder();
                if (fuelStack.isEmpty() && remainder != null) setItem(0, remainder.create());
                burn = fuel = duration;
                setChanged();
            }
        }
        ItemStacksResourceHandler io = new ItemStacksResourceHandler(2);
        StacksHandlerUtils.setStackInSlot(io, 0, getItem(1).copy());
        StacksHandlerUtils.setStackInSlot(io, 1, getItem(2).copy());
        for (int i = 0; i < 64 && AirTransfer.process(io, airTank); i++) {
            setItem(1, StacksHandlerUtils.getStackInSlot(io, 0));
            setItem(2, StacksHandlerUtils.getStackInSlot(io, 1));
        }
        fillGear(getItem(3), 50 * ticks);
    }

    public int fillGear(ItemStack stack, int maximum) {
        if (!stack.is(GearRegistry.SCUBA.get()) || maximum <= 0) return 0;
        GearState gear = new GearState(stack);
        int amount = Math.min(maximum, Math.min(air(), 6000 - gear.air()));
        if (amount <= 0) return 0;
        int drained = StacksHandlerUtils.drain(airTank, amount, false);
        gear.air(gear.air() + drained);
        gear.save(stack);
        setChanged();
        return drained;
    }

    public void pump(Player player) {
        if (level == null || level.getGameTime() - lastStroke < 4) return;
        lastStroke = level.getGameTime();
        if (!level.isEmptyBlock(worldPosition.above())) {
            player.sendOverlayMessage(Component.translatable("message.travelersbackpackplus.open_air"));
            return;
        }
        StacksHandlerUtils.fill(airTank, new FluidStack(GearRegistry.AIR.get(), 100), false);
        level.playSound(null, worldPosition, SoundEvents.PISTON_EXTEND, SoundSource.BLOCKS, 0.3F, 1.4F);
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return switch (slot) {
            case 0 -> level != null && stack.getBurnTime(RecipeType.SMELTING, level.fuelValues()) > 0;
            case 1 -> GearTankTransfer.accepts(GearType.SCUBA, stack);
            case 3 -> stack.is(GearRegistry.SCUBA.get());
            default -> false;
        };
    }

    @Override
    public void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        items = NonNullList.withSize(4, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(input, items);
        burn = input.getIntOr("burn_ticks", 0);
        fuel = input.getIntOr("fuel_ticks", 0);
        StacksHandlerUtils.setFluid(airTank, input.read("air_tank", FluidStack.OPTIONAL_CODEC).orElse(FluidStack.EMPTY));
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        ContainerHelper.saveAllItems(output, items);
        output.putInt("burn_ticks", burn);
        output.putInt("fuel_ticks", fuel);
        output.store("air_tank", FluidStack.OPTIONAL_CODEC, StacksHandlerUtils.getFluid(airTank));
    }
}
