package keletu.travelersbackpackplus.gear;

import keletu.travelersbackpackplus.GearRegistry;
import net.minecraft.world.level.material.EmptyFluid;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.fluids.FluidType;

public final class AirFluid extends EmptyFluid {
    @Override
    protected boolean isEmpty() {
        return false;
    }

    @Override
    public int getAmount(FluidState state) {
        return 1;
    }

    @Override
    public boolean isSource(FluidState state) {
        return true;
    }

    @Override
    public FluidType getFluidType() {
        return GearRegistry.AIR_TYPE.get();
    }
}
