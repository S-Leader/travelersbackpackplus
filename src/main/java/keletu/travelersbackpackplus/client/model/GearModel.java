package keletu.travelersbackpackplus.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import keletu.travelersbackpackplus.gear.GearType;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.rendertype.RenderTypes;

public final class GearModel extends Model<GearPose> {
    private final ModelPart rotor;
    private final ModelPart tankBase;
    private final ModelPart tankTop;

    public GearModel(ModelPart root, GearType type, boolean head) {
        super(root, RenderTypes::entityCutout);
        rotor = type == GearType.COPTER && !head ? root.getChild("Axis") : null;
        tankBase = !head && type.tankCount() == 1 ? root.getChild("Base") : null;
        tankTop = tankBase == null ? null : tankBase.getChild(type == GearType.COPTER ? "TankTop" : "tankTop");
    }

    public void translateTank(PoseStack poses) {
        root.translateAndRotate(poses);
        tankBase.translateAndRotate(poses);
        tankTop.translateAndRotate(poses);
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
