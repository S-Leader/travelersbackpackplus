package keletu.travelersbackpackplus.client.model;

import keletu.travelersbackpackplus.gear.GearType;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public final class OriginalModels {
    public static ModelPart bake(GearType type) {
        return switch (type) {
            case COPTER -> copter();
            case JETPACK -> jetpack();
            case FLAMETHROWER -> flamethrower();
            case SCUBA -> scuba();
        };
    }

    private static ModelPart copter() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition body = mesh.getRoot().addOrReplaceChild("body", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition head = mesh.getRoot().addOrReplaceChild("head", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition Base = body.addOrReplaceChild("Base", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, 0.0F, 2.0F, 8.0F, 12.0F, 1.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 1.6F, 0.0F, 0.0F, 0.0F));
        PartDefinition TankTop = Base.addOrReplaceChild("TankTop", CubeListBuilder.create().texOffs(0, 33).addBox(0.0F, 0.0F, 0.0F, 5.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 0.0F, 3.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition TankWallLeftFront = TankTop.addOrReplaceChild("TankWallLeftFront", CubeListBuilder.create().texOffs(0, 40).addBox(4.0F, 1.0F, 0.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition TankWallLeftBack = TankTop.addOrReplaceChild("TankWallLeftBack", CubeListBuilder.create().texOffs(0, 32).addBox(4.0F, 1.0F, 4.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition TankWallRightFront = TankTop.addOrReplaceChild("TankWallRightFront", CubeListBuilder.create().texOffs(16, 40).addBox(0.0F, 1.0F, 0.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition TankWallRightBack = TankTop.addOrReplaceChild("TankWallRightBack", CubeListBuilder.create().texOffs(16, 32).addBox(0.0F, 1.0F, 4.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition TankBottom = TankTop.addOrReplaceChild("TankBottom", CubeListBuilder.create().texOffs(0, 41).addBox(0.0F, 5.0F, 0.0F, 5.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition FuelLine1 = TankBottom.addOrReplaceChild("FuelLine1", CubeListBuilder.create().texOffs(15, 48).addBox(2.0F, 1.0F, 2.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 5.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition FuelLine2 = TankBottom.addOrReplaceChild("FuelLine2", CubeListBuilder.create().texOffs(0, 48).addBox(1.0F, 1.0F, 2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 8.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition EngineBody = Base.addOrReplaceChild("EngineBody", CubeListBuilder.create().texOffs(0, 23).addBox(-2.0F, 8.0F, 3.0F, 4.0F, 4.0F, 4.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition EnginePistonLeft = EngineBody.addOrReplaceChild("EnginePistonLeft", CubeListBuilder.create().texOffs(0, 18).addBox(1.7F, 1.0F, -1.0F, 4.0F, 2.0F, 2.0F), PartPose.offsetAndRotation(0.0F, 8.0F, 5.0F, 0.0F, -0.7853981633974483F, 0.0F));
        PartDefinition EnginePistonRight = EngineBody.addOrReplaceChild("EnginePistonRight", CubeListBuilder.create().texOffs(13, 18).addBox(1.7F, 1.0F, -1.0F, 4.0F, 2.0F, 2.0F), PartPose.offsetAndRotation(0.0F, 8.0F, 5.0F, 0.0F, -2.356194490192345F, 0.0F));
        PartDefinition Axis = body.addOrReplaceChild("Axis", CubeListBuilder.create().texOffs(25, 22).addBox(-0.5F, -25.0F, -0.5F, 1.0F, 25.0F, 1.0F), PartPose.offsetAndRotation(0.0F, 8.0F, 5.75F, 0.0F, 0.0F, 0.0F));
        PartDefinition Blade1 = Axis.addOrReplaceChild("Blade1", CubeListBuilder.create().texOffs(29, 0).addBox(0.0F, -0.5F, -1.0F, 15.0F, 1.0F, 2.0F), PartPose.offsetAndRotation(0.0F, -25.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition Blade2 = Axis.addOrReplaceChild("Blade2", CubeListBuilder.create().texOffs(30, 4).addBox(-1.0F, -0.5F, 0.0F, 2.0F, 1.0F, 15.0F), PartPose.offsetAndRotation(0.0F, -25.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition Blade3 = Axis.addOrReplaceChild("Blade3", CubeListBuilder.create().texOffs(29, 0).addBox(0.0F, -0.5F, -1.0F, 15.0F, 1.0F, 2.0F), PartPose.offsetAndRotation(0.0F, -25.0F, 0.0F, 0.0F, 0.0F, 3.141592653589793F));
        PartDefinition Blade4 = Axis.addOrReplaceChild("Blade4", CubeListBuilder.create().texOffs(30, 4).addBox(-1.0F, -0.5F, 0.0F, 2.0F, 1.0F, 15.0F), PartPose.offsetAndRotation(0.0F, -25.0F, 0.0F, -3.141592653589793F, 0.0F, 0.0F));
        PartDefinition Escape1 = Base.addOrReplaceChild("Escape1", CubeListBuilder.create().texOffs(9, 35).addBox(0.0F, 0.0F, 0.0F, 2.0F, 1.0F, 1.0F), PartPose.offsetAndRotation(-4.0F, 9.0F, 4.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition Escape2 = Base.addOrReplaceChild("Escape2", CubeListBuilder.create().texOffs(38, 40).addBox(0.0F, 0.0F, 0.0F, 1.0F, 9.0F, 1.0F), PartPose.offsetAndRotation(-4.0F, 0.0F, 4.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition Escape3 = Base.addOrReplaceChild("Escape3", CubeListBuilder.create().texOffs(6, 24).addBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 2.0F), PartPose.offsetAndRotation(-4.0F, 0.0F, 5.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition EscapeFilter = Base.addOrReplaceChild("EscapeFilter", CubeListBuilder.create().texOffs(35, 28).addBox(0.0F, 0.0F, 0.0F, 2.0F, 5.0F, 2.0F), PartPose.offsetAndRotation(-4.4F, 2.0F, 3.5F, 0.0F, 0.0F, 0.0F));
        return LayerDefinition.create(mesh, 64, 64).bakeRoot();
    }

    private static ModelPart jetpack() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition body = mesh.getRoot().addOrReplaceChild("body", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition head = mesh.getRoot().addOrReplaceChild("head", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition tubeStraightRight = body.addOrReplaceChild("tubeStraightRight", CubeListBuilder.create().texOffs(0, 14).addBox(-5.8F, 0.0F, 0.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, 1.0F, 9.58F, 0.0F, 0.0F, 0.0F));
        PartDefinition tubeEndLeft = body.addOrReplaceChild("tubeEndLeft", CubeListBuilder.create().texOffs(0, 17).addBox(0.0F, 0.0F, 0.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(7.6F, 1.7F, 9.58F, 0.0F, 0.0F, -0.2617993877991494F));
        PartDefinition tubeEndRight = body.addOrReplaceChild("tubeEndRight", CubeListBuilder.create().texOffs(0, 17).addBox(-1.0F, 0.0F, 0.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(-7.7F, 1.6F, 9.58F, 0.0F, 0.0F, 0.2617993877991494F));
        PartDefinition Base = body.addOrReplaceChild("Base", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, 0.0F, 2.0F, 8.0F, 12.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 1.28F, 0.0F, 0.0F, 0.0F));
        PartDefinition tankWallLeft = body.addOrReplaceChild("tankWallLeft", CubeListBuilder.create().texOffs(3, 23).addBox(0.0F, 6.0F, 2.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 1.0F, 2.2800000000000002F, 0.0F, 0.0F, 0.0F));
        PartDefinition tankWallRight = body.addOrReplaceChild("tankWallRight", CubeListBuilder.create().texOffs(5, 17).addBox(-3.0F, 0.0F, 2.0F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 1.0F, 4.28F, 0.0F, 0.0F, 0.0F));
        PartDefinition pressureTank = Base.addOrReplaceChild("pressureTank", CubeListBuilder.create().texOffs(19, 0).addBox(-3.0F, 0.0F, 0.0F, 5.0F, 7.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 0.0F, 3.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition fireBox = Base.addOrReplaceChild("fireBox", CubeListBuilder.create().texOffs(26, 25).addBox(-3.0F, 0.0F, 0.0F, 5.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 8.0F, 3.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition tankTop = Base.addOrReplaceChild("tankTop", CubeListBuilder.create().texOffs(10, 20).addBox(0.0F, 0.0F, 0.0F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, 0.0F, 3.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition tubeStraightLeft = body.addOrReplaceChild("tubeStraightLeft", CubeListBuilder.create().texOffs(0, 14).addBox(0.0F, 0.0F, 0.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.7F, 1.0F, 9.58F, 0.0F, 0.0F, 0.0F));
        PartDefinition tankBottom = Base.addOrReplaceChild("tankBottom", CubeListBuilder.create().texOffs(10, 20).addBox(-3.0F, 1.0F, 0.0F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 8.0F, 3.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition tubeBendLeft = pressureTank.addOrReplaceChild("tubeBendLeft", CubeListBuilder.create().texOffs(0, 14).addBox(0.0F, 0.0F, 0.0F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 1.0F, 2.0F, 0.0F, -0.8080874436733745F, 0.0F));
        PartDefinition waterTube1 = tankBottom.addOrReplaceChild("waterTube1", CubeListBuilder.create().texOffs(10, 17).addBox(-2.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition tubeBendRight = pressureTank.addOrReplaceChild("tubeBendRight", CubeListBuilder.create().texOffs(0, 14).addBox(-5.0F, 0.0F, 0.0F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 1.0F, 2.0F, 0.0F, 0.7976154681614086F, 0.0F));
        PartDefinition waterTube2 = waterTube1.addOrReplaceChild("waterTube2", CubeListBuilder.create().texOffs(10, 17).addBox(-2.0F, 1.0F, 1.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        return LayerDefinition.create(mesh, 64, 32).bakeRoot();
    }

    private static ModelPart flamethrower() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition body = mesh.getRoot().addOrReplaceChild("body", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition head = mesh.getRoot().addOrReplaceChild("head", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition back = body.addOrReplaceChild("back", CubeListBuilder.create().texOffs(0, 0), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition face = head.addOrReplaceChild("face", CubeListBuilder.create().texOffs(0, 0), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition backPlate = back.addOrReplaceChild("backPlate", CubeListBuilder.create().texOffs(32, 13).addBox(-4.5F, 0.5F, 2.0F, 9.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition strapTopL = back.addOrReplaceChild("strapTopL", CubeListBuilder.create().texOffs(0, 16).addBox(-3.5F, -0.5F, -2.5F, 2.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition strapTopR = back.addOrReplaceChild("strapTopR", CubeListBuilder.create().texOffs(0, 16).addBox(1.5F, -0.5F, -2.5F, 2.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition strapFrontL = back.addOrReplaceChild("strapFrontL", CubeListBuilder.create().texOffs(48, 0).addBox(-3.5F, 0.5F, -2.5F, 2.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition strapFrontR = back.addOrReplaceChild("strapFrontR", CubeListBuilder.create().texOffs(48, 0).addBox(1.5F, 0.5F, -2.5F, 2.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition buckle = back.addOrReplaceChild("buckle", CubeListBuilder.create().texOffs(52, 17).addBox(-1.5F, 3.5F, -2.5F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition tankL = back.addOrReplaceChild("tankL", CubeListBuilder.create().texOffs(32, 0).addBox(-4.5F, 0.5F, 3.0F, 4.0F, 9.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition tankR = back.addOrReplaceChild("tankR", CubeListBuilder.create().texOffs(32, 0).addBox(0.5F, 0.5F, 3.0F, 4.0F, 9.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition capL = back.addOrReplaceChild("capL", CubeListBuilder.create().texOffs(52, 10).addBox(-4.0F, -0.5F, 3.5F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition capR = back.addOrReplaceChild("capR", CubeListBuilder.create().texOffs(52, 10).addBox(1.0F, -0.5F, 3.5F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition valveL = back.addOrReplaceChild("valveL", CubeListBuilder.create().texOffs(48, 11).addBox(-3.0F, -1.5F, 4.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition valveR = back.addOrReplaceChild("valveR", CubeListBuilder.create().texOffs(48, 11).addBox(2.0F, -1.5F, 4.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition bandTop = back.addOrReplaceChild("bandTop", CubeListBuilder.create().texOffs(0, 23).addBox(-5.0F, 2.0F, 2.5F, 10.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition bandBot = back.addOrReplaceChild("bandBot", CubeListBuilder.create().texOffs(0, 23).addBox(-5.0F, 7.0F, 2.5F, 10.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition gauge = back.addOrReplaceChild("gauge", CubeListBuilder.create().texOffs(52, 14).addBox(-1.0F, 1.0F, 7.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition manifold = back.addOrReplaceChild("manifold", CubeListBuilder.create().texOffs(16, 16).addBox(-3.0F, 9.5F, 4.5F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition hoseDown = back.addOrReplaceChild("hoseDown", CubeListBuilder.create().texOffs(48, 8).addBox(-3.0F, 10.5F, 4.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition hoseOut = back.addOrReplaceChild("hoseOut", CubeListBuilder.create().texOffs(16, 18).addBox(-5.5F, 11.5F, 4.5F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition hood = face.addOrReplaceChild("hood", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.6F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition lensL = face.addOrReplaceChild("lensL", CubeListBuilder.create().texOffs(58, 14).addBox(-3.0F, -6.0F, -5.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition lensR = face.addOrReplaceChild("lensR", CubeListBuilder.create().texOffs(58, 14).addBox(1.0F, -6.0F, -5.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition filter = face.addOrReplaceChild("filter", CubeListBuilder.create().texOffs(54, 0).addBox(-1.5F, -3.5F, -6.5F, 3.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition filterRim = face.addOrReplaceChild("filterRim", CubeListBuilder.create().texOffs(54, 5).addBox(-2.0F, -4.0F, -5.0F, 4.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        return LayerDefinition.create(mesh, 64, 64).bakeRoot();
    }

    private static ModelPart scuba() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition body = mesh.getRoot().addOrReplaceChild("body", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition head = mesh.getRoot().addOrReplaceChild("head", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition back = body.addOrReplaceChild("back", CubeListBuilder.create().texOffs(0, 0), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition face = head.addOrReplaceChild("face", CubeListBuilder.create().texOffs(0, 0), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition backPlate = back.addOrReplaceChild("backPlate", CubeListBuilder.create().texOffs(12, 10).addBox(-4.0F, 0.5F, 2.0F, 8.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition strapTopL = back.addOrReplaceChild("strapTopL", CubeListBuilder.create().texOffs(36, 10).addBox(-3.5F, -0.5F, -2.5F, 2.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition strapTopR = back.addOrReplaceChild("strapTopR", CubeListBuilder.create().texOffs(36, 10).addBox(1.5F, -0.5F, -2.5F, 2.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition strapFrontL = back.addOrReplaceChild("strapFrontL", CubeListBuilder.create().texOffs(30, 10).addBox(-3.5F, 0.5F, -2.5F, 2.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition strapFrontR = back.addOrReplaceChild("strapFrontR", CubeListBuilder.create().texOffs(30, 10).addBox(1.5F, 0.5F, -2.5F, 2.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition buckle = back.addOrReplaceChild("buckle", CubeListBuilder.create().texOffs(56, 13).addBox(-1.5F, 3.5F, -2.5F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition tankL = back.addOrReplaceChild("tankL", CubeListBuilder.create().texOffs(0, 0).addBox(-3.5F, 0.5F, 3.0F, 3.0F, 10.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition tankR = back.addOrReplaceChild("tankR", CubeListBuilder.create().texOffs(0, 0).addBox(0.5F, 0.5F, 3.0F, 3.0F, 10.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition capTopL = back.addOrReplaceChild("capTopL", CubeListBuilder.create().texOffs(56, 10).addBox(-3.0F, -0.5F, 3.5F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition capTopR = back.addOrReplaceChild("capTopR", CubeListBuilder.create().texOffs(56, 10).addBox(1.0F, -0.5F, 3.5F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition capBotL = back.addOrReplaceChild("capBotL", CubeListBuilder.create().texOffs(56, 10).addBox(-3.0F, 10.5F, 3.5F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition capBotR = back.addOrReplaceChild("capBotR", CubeListBuilder.create().texOffs(56, 10).addBox(1.0F, 10.5F, 3.5F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition bandTop = back.addOrReplaceChild("bandTop", CubeListBuilder.create().texOffs(36, 17).addBox(-4.0F, 2.5F, 2.5F, 8.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition bandBot = back.addOrReplaceChild("bandBot", CubeListBuilder.create().texOffs(36, 17).addBox(-4.0F, 7.5F, 2.5F, 8.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition manifold = back.addOrReplaceChild("manifold", CubeListBuilder.create().texOffs(52, 15).addBox(-2.5F, -1.5F, 4.0F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition valveL = back.addOrReplaceChild("valveL", CubeListBuilder.create().texOffs(8, 13).addBox(-2.5F, -2.5F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition valveR = back.addOrReplaceChild("valveR", CubeListBuilder.create().texOffs(8, 13).addBox(1.5F, -2.5F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition hoseUp = back.addOrReplaceChild("hoseUp", CubeListBuilder.create().texOffs(52, 10).addBox(1.5F, -5.5F, 4.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition visor = face.addOrReplaceChild("visor", CubeListBuilder.create().texOffs(0, 19).addBox(-4.0F, -6.0F, -5.0F, 8.0F, 4.0F, 1.0F, new CubeDeformation(0.2F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition strap = face.addOrReplaceChild("strap", CubeListBuilder.create().texOffs(12, 0).addBox(-4.0F, -5.5F, -4.0F, 8.0F, 2.0F, 8.0F, new CubeDeformation(0.6F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition regulator = face.addOrReplaceChild("regulator", CubeListBuilder.create().texOffs(0, 13).addBox(-1.5F, -2.5F, -5.5F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition hoseSide = face.addOrReplaceChild("hoseSide", CubeListBuilder.create().texOffs(44, 0).addBox(4.0F, -3.5F, -5.0F, 1.0F, 1.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition hoseFront = face.addOrReplaceChild("hoseFront", CubeListBuilder.create().texOffs(0, 16).addBox(1.5F, -3.5F, -5.5F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition hoseBack = face.addOrReplaceChild("hoseBack", CubeListBuilder.create().texOffs(0, 16).addBox(2.0F, -3.5F, 4.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        return LayerDefinition.create(mesh, 64, 64).bakeRoot();
    }
}
