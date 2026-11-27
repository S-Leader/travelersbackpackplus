package keletu.travelersbackpackplus.mixin;

import com.tiviacz.travelersbackpack.blockentity.BackpackBlockEntity;
import com.tiviacz.travelersbackpack.inventory.menu.BackpackBlockEntityMenu;
import keletu.travelersbackpackplus.gear.GearItem;
import keletu.travelersbackpackplus.menu.GearMenu;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BackpackBlockEntity.class)
public abstract class GearBlockMenuMixin {
    @Inject(method = "createMenu", at = @At("RETURN"), cancellable = true)
    private void travelersbackpackplus$open(int id, Inventory inventory, Player opener, CallbackInfoReturnable<AbstractContainerMenu> callback) {
        if (callback.getReturnValue() instanceof BackpackBlockEntityMenu nativeMenu
                && nativeMenu.getWrapper().getBackpackStack().getItem() instanceof GearItem) {
            callback.setReturnValue(GearMenu.fromBlockMenu(nativeMenu, inventory));
        }
    }
}
