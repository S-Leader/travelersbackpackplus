package keletu.travelersbackpackplus.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.tiviacz.travelersbackpack.client.model.StackModelPart;
import com.tiviacz.travelersbackpack.client.renderer.BackpackLayer;
import keletu.travelersbackpackplus.client.GearRender;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = BackpackLayer.class, remap = false)
public abstract class BackpackLayerMixin {
    @Inject(method = "renderBackpackLayer", at = @At("HEAD"), cancellable = true)
    private static void travelersbackpackplus$render(HumanoidModel<?> parent, PoseStack poses, SubmitNodeCollector collector, int light, HumanoidRenderState state, ItemStackRenderState itemState, StackModelPart tools, ItemStack stack, CallbackInfo callback) {
        if (GearRender.worn(parent, poses, collector, light, state, stack)) callback.cancel();
    }
}
