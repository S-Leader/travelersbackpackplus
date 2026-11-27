package keletu.travelersbackpackplus.client.model;

import keletu.travelersbackpackplus.gear.GearType;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.rendertype.RenderTypes;

public final class GearModel extends Model<GearPose> {
    private final ModelPart rotor;

    public GearModel(ModelPart root, GearType type, boolean head) {
        super(root, RenderTypes::entityCutout);
        rotor = type == GearType.COPTER && !head ? root.getChild("Axis") : null;
    }

    @Override
    public void setupAnim(GearPose state) {
        super.setupAnim(state);
        if (rotor != null) {
            rotor.visible = state.rotorVisible();
            rotor.yRot = state.rotorAngle();
        }
    }
}
