package keletu.travelersbackpackplus.gear;

import com.tiviacz.travelersbackpack.component.Fluids;
import com.tiviacz.travelersbackpack.component.RenderInfo;
import com.tiviacz.travelersbackpack.init.ModDataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.ItemAccessResourceHandler;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.item.ItemResource;

public final class GearFluidHandler extends ItemAccessResourceHandler<FluidResource> {
    private final Item item;
    private final GearType type;

    public GearFluidHandler(ItemAccess access, GearItem item) {
        super(access, item.type().tankCount());
        this.item = item;
        this.type = item.type();
    }

    private FluidStack fluid(ItemResource resource, int index) {
        if (!resource.is(item)) return FluidStack.EMPTY;
        ItemStack stack = resource.toStack();
        GearTanks.migrate(stack);
        Fluids fluids = stack.getOrDefault(ModDataComponents.FLUIDS, Fluids.empty());
        return index == 0 ? fluids.leftFluidStack() : fluids.rightFluidStack();
    }

    @Override
    protected FluidResource getResourceFrom(ItemResource resource, int index) {
        return FluidResource.of(fluid(resource, index));
    }

    @Override
    protected int getAmountFrom(ItemResource resource, int index) {
        return fluid(resource, index).getAmount();
    }

    @Override
    protected ItemResource update(ItemResource resource, int index, FluidResource fluid, int amount) {
        if (!resource.is(item)) return ItemResource.EMPTY;
        ItemStack stack = resource.toStack();
        GearTanks.migrate(stack);
        resource = ItemResource.of(stack);
        Fluids previous = resource.getOrDefault(ModDataComponents.FLUIDS, Fluids.empty());
        Fluids updated = new Fluids(index == 0 ? fluid.toStack(amount) : previous.leftFluidStack().copy(),
                index == 1 ? fluid.toStack(amount) : previous.rightFluidStack().copy());
        CompoundTag render = resource.getOrDefault(ModDataComponents.RENDER_INFO, RenderInfo.EMPTY).compoundTag().copy();
        GearTanks.writeRenderInfo(render, type, updated);
        return resource.with(ModDataComponents.FLUIDS, updated).with(ModDataComponents.RENDER_INFO, new RenderInfo(render));
    }

    @Override
    public boolean isValid(int index, FluidResource resource) {
        return itemAccess.getResource().is(item);
    }

    @Override
    protected int getCapacity(int index, FluidResource resource) {
        return itemAccess.getResource().is(item) ? type.tankCapacity() : 0;
    }
}
