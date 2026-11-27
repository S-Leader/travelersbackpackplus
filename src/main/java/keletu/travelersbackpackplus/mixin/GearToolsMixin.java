package keletu.travelersbackpackplus.mixin;

import com.tiviacz.travelersbackpack.attachment.AttachmentUtils;
import com.tiviacz.travelersbackpack.common.ServerActions;
import keletu.travelersbackpackplus.gear.GearItem;
import net.minecraft.world.entity.player.Player;
import net.neoforged.fml.ModList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerActions.class)
public abstract class GearToolsMixin {
    @Inject(method = "swapTool", at = @At("HEAD"), cancellable = true)
    private static void travelersbackpackplus$noTools(Player player, int slot, int button, CallbackInfo callback) {
        if (AttachmentUtils.getWearingBackpack(player).getItem() instanceof GearItem) callback.cancel();
    }

    @Inject(method = "equipBackpack(Lnet/minecraft/world/entity/player/Player;Z)V", at = @At("HEAD"), cancellable = true)
    private static void travelersbackpackplus$curiosEquipment(Player player, boolean equip, CallbackInfo callback) {
        if (ModList.get().isLoaded("curios") && (equip ? player.getMainHandItem() : AttachmentUtils.getWearingBackpack(player)).getItem() instanceof GearItem)
            callback.cancel();
    }
}
