package keletu.travelersbackpackplus.mixin;

import com.tiviacz.travelersbackpack.inventory.BackpackWrapper;
import com.tiviacz.travelersbackpack.inventory.upgrades.tanks.TanksUpgrade;
import keletu.travelersbackpackplus.gear.GearItem;
import keletu.travelersbackpackplus.gear.GearType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = TanksUpgrade.class, remap = false)
public abstract class GearTanksUpgradeMixin {

    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lcom/tiviacz/travelersbackpack/inventory/BackpackWrapper;getBackpackTankCapacity()I", ordinal = 1), require = 1)
    private int travelersbackpackplus$rightTankCapacity(BackpackWrapper wrapper) {
        if (wrapper.getBackpackStack().getItem() instanceof GearItem gear) {
            if (gear.type() == GearType.COPTER || gear.type() == GearType.JETPACK || gear.type() == GearType.SCUBA) {
                return 0;
            }
        }

        return wrapper.getBackpackTankCapacity();
    }

    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lcom/tiviacz/travelersbackpack/inventory/BackpackWrapper;getBackpackTankCapacity()I", ordinal = 0), require = 1)
    private int travelersbackpackplus$leftTankCapacity(BackpackWrapper wrapper) {
        if (wrapper.getBackpackStack().getItem() instanceof GearItem gear) {
            if (gear.type() == GearType.SCUBA) {
                return 0;
            }
        }

        return wrapper.getBackpackTankCapacity();
    }
}