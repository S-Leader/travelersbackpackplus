package keletu.travelersbackpackplus.menu;

import com.tiviacz.travelersbackpack.common.ServerActions;
import com.tiviacz.travelersbackpack.inventory.BackpackWrapper;
import com.tiviacz.travelersbackpack.inventory.menu.BackpackBlockEntityMenu;
import com.tiviacz.travelersbackpack.inventory.menu.BackpackItemMenu;
import com.tiviacz.travelersbackpack.inventory.upgrades.tanks.TanksUpgrade;
import com.tiviacz.travelersbackpack.util.StacksHandlerUtils;
import keletu.travelersbackpackplus.GearRegistry;
import keletu.travelersbackpackplus.gear.GearEvents;
import keletu.travelersbackpackplus.gear.GearItem;
import keletu.travelersbackpackplus.gear.GearState;
import keletu.travelersbackpackplus.gear.GearType;
import keletu.travelersbackpackplus.menu.layout.GearArrangement;
import keletu.travelersbackpackplus.menu.layout.WidgetLayout;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.item.ResourceHandlerSlot;

public final class GearMenu extends BackpackItemMenu {
    private TanksUpgrade tanks;
    private SimpleContainerData data;
    private ItemStacksResourceHandler fluidSlots;
    private int playerFirst;
    private ItemStack parent;
    private BackpackBlockEntityMenu blockMenu;
    public GearType type;
    public WidgetLayout layout;

    public GearMenu(int id, Inventory inventory, BackpackWrapper wrapper) {
        super(id, inventory, wrapper);
    }

    private GearMenu(int id, Inventory inventory, RegistryFriendlyByteBuf buffer) {
        super(id, inventory, buffer);
    }

    @Override
    public MenuType<?> getType() {
        return placed() ? GearRegistry.GEAR_BLOCK_MENU.get() : GearRegistry.GEAR_ITEM_MENU.get();
    }

    @Override
    public void addSlots() {
        data = new SimpleContainerData(12);
        fluidSlots = new ItemStacksResourceHandler(2);
        this.parent = wrapper.getBackpackStack();
        this.type = ((GearItem) parent.getItem()).type();
        this.layout = switch (type) {
            case COPTER -> GearArrangement.copter();
            case JETPACK -> GearArrangement.jetpack();
            case FLAMETHROWER -> GearArrangement.flamethrower();
            case SCUBA -> GearArrangement.scuba();
        };
        this.tanks = wrapper.getUpgrade(TanksUpgrade.class).orElseThrow();
        if (!inventory.player.level().isClientSide()) {
            var storage = wrapper.getStorage();
            for (int slot = 0; slot < storage.size(); slot++) {
                ItemStack stored = StacksHandlerUtils.getStackInSlot(storage, slot);
                if (type == GearType.JETPACK && slot == 0 && stored.getBurnTime(RecipeType.SMELTING, inventory.player.level().fuelValues()) > 0)
                    continue;
                ItemStack recovered = StacksHandlerUtils.extractItem(storage, slot, 64, false);
                if (!recovered.isEmpty()) inventory.player.getInventory().placeItemBackInInventory(recovered);
            }
            var tools = wrapper.getTools();
            for (int slot = 0; slot < tools.size(); slot++) {
                ItemStack recovered = StacksHandlerUtils.extractItem(tools, slot, 64, false);
                if (!recovered.isEmpty()) inventory.player.getInventory().placeItemBackInInventory(recovered);
            }
        }
        addSlot(new ResourceHandlerSlot(fluidSlots, fluidSlots::set, 0, 8, 7) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return GearTankTransfer.accepts(type, stack);
            }
        });
        addSlot(new ResourceHandlerSlot(fluidSlots, fluidSlots::set, 1, 8, 37) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }
        });
        if (type == GearType.JETPACK) {
            var storage = wrapper.getStorage();
            addSlot(new ResourceHandlerSlot(storage, storage::set, 0, 50, 57) {
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return stack.getBurnTime(RecipeType.SMELTING, inventory.player.level().fuelValues()) > 0;
                }
            });
        }
        playerFirst = slots.size();
        BACKPACK_INV_END = playerFirst;
        PLAYER_INV_START = playerFirst;
        for (int slot = 0; slot < 36; slot++) {
            int index = slot < 27 ? slot + 9 : slot - 27;
            addSlot(new Slot(inventory, index, 8 + slot % 9 * 18, slot < 27 ? 84 + slot / 9 * 18 : 142) {
                @Override
                public boolean mayPickup(Player player) {
                    return getItem() != parent;
                }

                @Override
                public boolean mayPlace(ItemStack stack) {
                    return getItem() != parent;
                }
            });
        }
        PLAYER_HOT_END = slots.size();
        unmodifiableSlotCount = slots.size();
        updateData();
        addDataSlots(data);
    }

    public static GearMenu fromItem(int id, Inventory inventory, RegistryFriendlyByteBuf buffer) {
        return new GearMenu(id, inventory, buffer);
    }

    public static GearMenu fromBlock(int id, Inventory inventory, RegistryFriendlyByteBuf buffer) {
        return fromBlockMenu(new BackpackBlockEntityMenu(id, inventory, buffer), inventory);
    }

    public static GearMenu fromBlockMenu(BackpackBlockEntityMenu nativeMenu, Inventory inventory) {
        GearMenu menu = new GearMenu(nativeMenu.containerId, inventory, nativeMenu.getWrapper());
        menu.blockMenu = nativeMenu;
        return menu;
    }

    public boolean equipped() {
        return wrapper.getOpenContext().isEquipped();
    }

    public boolean placed() {
        return wrapper.getOpenContext().isBlockEntity();
    }

    public boolean enabled() {
        return data.get(0) != 0;
    }

    public int temperature() {
        return data.get(1);
    }

    public int burn() {
        return data.get(2);
    }

    public int steam() {
        return data.get(3);
    }

    public int air() {
        return data.get(4);
    }

    public int burnTotal() {
        return Math.max(1, data.get(11));
    }

    public boolean canControl() {
        return !equipped() || wrapper.getBackpackOwner() == inventory.player;
    }

    public boolean showsEquipButton() {
        return !placed() && canControl() && !ModList.get().isLoaded("curios");
    }

    public FluidStack fluid(int index) {
        int offset = 5 + index * 3;
        return new FluidStack(BuiltInRegistries.FLUID.byId(data.get(offset)), data.get(offset + 1));
    }

    public int capacity(int index) {
        return data.get(7 + index * 3);
    }

    private void updateData() {
        GearState state = new GearState(wrapper.getBackpackStack());
        data.set(0, state.enabled() ? 1 : 0);
        data.set(1, state.temperature());
        data.set(2, state.burn());
        data.set(3, state.steam());
        data.set(4, state.air());
        data.set(11, state.burnTotal());
        for (int i = 0; i < 2; i++) {
            var tank = i == 0 ? tanks.getLeftTank() : tanks.getRightTank();
            FluidStack fluid = StacksHandlerUtils.getFluid(tank);
            data.set(5 + i * 3, BuiltInRegistries.FLUID.getId(fluid.getFluid()));
            data.set(6 + i * 3, fluid.getAmount());
            data.set(7 + i * 3, StacksHandlerUtils.getCapacity(tank));
        }
    }

    @Override
    public void broadcastChanges() {
        if (!inventory.player.level().isClientSide()) {
            GearTankTransfer.process(type, wrapper, tanks, fluidSlots);
            updateData();
        }
        super.broadcastChanges();
    }

    @Override
    public boolean stillValid(Player player) {
        return blockMenu == null ? super.stillValid(player) : blockMenu.stillValid(player);
    }

    @Override
    public void clicked(int slot, int button, ContainerInput input, Player player) {
        if (!equipped() && !placed() && input == ContainerInput.SWAP
                && inventory.getItem(button) == parent) return;
        super.clicked(slot, button, input, player);
    }

    @Override
    public boolean clickMenuButton(Player player, int button) {
        if (!(player instanceof ServerPlayer serverPlayer) || !stillValid(player)) return false;
        if (button == 1 && equipped() && wrapper.getBackpackOwner() == player && type != GearType.SCUBA) {
            GearEvents.control(serverPlayer, 0, 1);
            broadcastChanges();
            return true;
        }
        if (button != 0 || !showsEquipButton()) return false;
        if (!equipped() && player.getMainHandItem() != parent) return false;
        boolean wasEquipped = equipped();
        ServerActions.equipBackpack(player, !wasEquipped);
        return true;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        if (index < 0 || index >= slots.size()) return ItemStack.EMPTY;
        Slot slot = slots.get(index);
        if (!slot.hasItem() || !slot.mayPickup(player)) return ItemStack.EMPTY;
        ItemStack stack = slot.getItem();
        ItemStack original = stack.copy();
        boolean moved = index < playerFirst ? moveItemStackTo(stack, playerFirst, slots.size(), true)
                : type == GearType.JETPACK && slots.get(2).mayPlace(stack) ? moveItemStackTo(stack, 2, 3, false)
                : moveItemStackTo(stack, 0, 1, false);
        if (!moved) return ItemStack.EMPTY;
        slot.setByPlayer(stack.isEmpty() ? ItemStack.EMPTY : stack);
        slot.onTake(player, stack);
        return original;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        if (!player.level().isClientSide()) {
            for (int i = 0; i < 2; i++) {
                ItemStack remaining = StacksHandlerUtils.extractItem(fluidSlots, i, 64, false);
                if (!remaining.isEmpty()) player.getInventory().placeItemBackInInventory(remaining);
            }
        }
        wrapper.invalidateUserDirect(player);
    }
}
