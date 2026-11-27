package keletu.travelersbackpackplus.mixin;

import com.tiviacz.travelersbackpack.client.screens.HudOverlay;
import keletu.travelersbackpackplus.client.GearTankHud;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HudOverlay.class)
public abstract class GearTankHudMixin {
    @Inject(method = "renderOverlay", at = @At("HEAD"), cancellable = true)
    private static void travelersbackpackplus$singleTank(ItemStack stack, Minecraft client, GuiGraphicsExtractor graphics, CallbackInfo callback) {
        if (GearTankHud.render(stack, client, graphics)) callback.cancel();
    }
}
