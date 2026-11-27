package keletu.travelersbackpackplus.client;

import keletu.travelersbackpackplus.gear.GearType;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;

public final class GearPlacedState extends BlockEntityRenderState {
    public GearType type;
    public float rotation;
}
