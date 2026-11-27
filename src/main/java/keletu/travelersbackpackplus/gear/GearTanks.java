package keletu.travelersbackpackplus.gear;

import com.tiviacz.travelersbackpack.component.Fluids;
import com.tiviacz.travelersbackpack.component.RenderInfo;
import com.tiviacz.travelersbackpack.init.ModDataComponents;
import com.tiviacz.travelersbackpack.inventory.UpgradeManager;
import com.tiviacz.travelersbackpack.inventory.upgrades.tanks.TanksUpgrade;
import com.tiviacz.travelersbackpack.item.upgrade.TanksUpgradeItem;
import com.tiviacz.travelersbackpack.util.StacksHandlerUtils;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.neoforged.neoforge.fluids.FluidStack;

public final class GearTanks extends TanksUpgrade {
    private boolean synchronizing;

    public GearTanks(UpgradeManager manager) {
        super(manager, -1, activeFluids(manager.getWrapper().getBackpackStack()));
    }

    public static Fluids activeFluids(ItemStack stack) {
        Fluids fluids = stack.getOrDefault(ModDataComponents.FLUIDS, Fluids.empty());
        int count = ((GearItem) stack.getItem()).type().tankCount();
        return new Fluids(count > 0 ? fluids.leftFluidStack().copy() : FluidStack.EMPTY,
                count > 1 ? fluids.rightFluidStack().copy() : FluidStack.EMPTY);
    }

    public static boolean migrate(ItemStack stack) {
        if (!(stack.getItem() instanceof GearItem gear)) return false;
        boolean changed = removeUpgrade(stack, ModDataComponents.UPGRADES.get());
        changed |= removeUpgrade(stack, ModDataComponents.STARTER_UPGRADES.get());
        if (gear.type().tankCount() == 0) {
            RenderInfo current = stack.getOrDefault(ModDataComponents.RENDER_INFO, RenderInfo.EMPTY);
            if (current.hasTanks()) {
                CompoundTag tag = current.compoundTag().copy();
                tag.remove(RenderInfo.LEFT_TANK);
                tag.remove(RenderInfo.RIGHT_TANK);
                tag.remove(RenderInfo.CAPACITY);
                stack.set(ModDataComponents.RENDER_INFO, new RenderInfo(tag));
                changed = true;
            }
        }
        return changed;
    }

    private static boolean removeUpgrade(ItemStack stack, DataComponentType<ItemContainerContents> component) {
        ItemContainerContents contents = stack.get(component);
        if (contents == null) return false;
        NonNullList<ItemStack> items = NonNullList.withSize(contents.getSlots(), ItemStack.EMPTY);
        contents.copyInto(items);
        boolean changed = false;
        for (int slot = 0; slot < items.size(); slot++) {
            ItemStack upgrade = items.get(slot);
            if (!(upgrade.getItem() instanceof TanksUpgradeItem)) continue;
            if (!stack.has(ModDataComponents.FLUIDS))
                stack.set(ModDataComponents.FLUIDS, upgrade.getOrDefault(ModDataComponents.FLUIDS, Fluids.empty()));
            items.set(slot, ItemStack.EMPTY);
            changed = true;
        }
        if (changed) stack.set(component, ItemContainerContents.fromItems(items));
        return changed;
    }

    public static void writeRenderInfo(CompoundTag tag, GearType type, Fluids fluids) {
        tag.remove(RenderInfo.LEFT_TANK);
        tag.remove(RenderInfo.RIGHT_TANK);
        tag.remove(RenderInfo.CAPACITY);
        if (type.tankCount() == 0) return;
        tag.put(RenderInfo.LEFT_TANK, FluidStack.OPTIONAL_CODEC.encodeStart(NbtOps.INSTANCE, fluids.leftFluidStack()).getOrThrow());
        if (type.tankCount() > 1)
            tag.put(RenderInfo.RIGHT_TANK, FluidStack.OPTIONAL_CODEC.encodeStart(NbtOps.INSTANCE, fluids.rightFluidStack()).getOrThrow());
        tag.putInt(RenderInfo.CAPACITY, type.tankCapacity());
    }

    @Override
    public void writeToRenderData(CompoundTag tag) {
        GearType type = ((GearItem) getWrapper().getBackpackStack().getItem()).type();
        writeRenderInfo(tag, type, new Fluids(StacksHandlerUtils.getFluid(getLeftTank()), StacksHandlerUtils.getFluid(getRightTank())));
    }

    @Override
    public <D> void updateDataHolderUnchecked(DataComponentType<D> component, D value) {
        if (synchronizing) return;
        if (component == ModDataComponents.FLUIDS.get() && value instanceof Fluids(
                FluidStack leftFluidStack, FluidStack rightFluidStack
        )) {
            ItemStack stack = getWrapper().getBackpackStack();
            GearType type = ((GearItem) stack.getItem()).type();
            Fluids previous = stack.getOrDefault(ModDataComponents.FLUIDS, Fluids.empty());
            getWrapper().setDataAndSync(ModDataComponents.FLUIDS.get(), new Fluids(leftFluidStack,
                    type.tankCount() > 1 ? rightFluidStack : previous.rightFluidStack()));
        } else {
            getWrapper().setDataAndSync(component, value);
        }
    }

    @Override
    public ItemStack getDataHolderStack() {
        return getWrapper().getBackpackStack();
    }

    @Override
    public boolean isTabOpened() {
        return false;
    }

    @Override
    public void syncClients(ItemStack backpack) {
        synchronizing = true;
        try {
            setFluids(activeFluids(backpack));
        } finally {
            synchronizing = false;
        }
    }
}
