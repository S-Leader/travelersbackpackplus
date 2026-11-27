package keletu.travelersbackpackplus.mixin;

import com.tiviacz.travelersbackpack.inventory.BackpackContainer;
import com.tiviacz.travelersbackpack.inventory.BackpackWrapper;
import com.tiviacz.travelersbackpack.inventory.menu.BackpackItemMenu;
import keletu.travelersbackpackplus.gear.GearItem;
import keletu.travelersbackpackplus.menu.GearMenu;
import net.minecraft.world.entity.player.Inventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BackpackContainer.class)
public abstract class GearItemMenuMixin {
    @Redirect(method = "createMenu", at = @At(value = "NEW", target = "com/tiviacz/travelersbackpack/inventory/menu/BackpackItemMenu"), require = 2)
    private BackpackItemMenu travelersbackpackplus$layout(int id, Inventory inventory, BackpackWrapper wrapper) {
        return wrapper.getBackpackStack().getItem() instanceof GearItem
                ? new GearMenu(id, inventory, wrapper) : new BackpackItemMenu(id, inventory, wrapper);
    }
}
