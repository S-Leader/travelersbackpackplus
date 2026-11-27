package keletu.travelersbackpackplus.client;

import keletu.travelersbackpackplus.gear.GearType;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.neoforged.neoforge.fluids.FluidStack;

public final class GearPlacedState extends BlockEntityRenderState {
    public GearType type;
    public float rotation;
    public FluidStack fluid = FluidStack.EMPTY;
}
