package keletu.travelersbackpackplus.mixin;

import com.tiviacz.travelersbackpack.inventory.BackpackWrapper;
import com.tiviacz.travelersbackpack.inventory.UpgradeManager;
import com.tiviacz.travelersbackpack.inventory.upgrades.UpgradeBase;
import com.tiviacz.travelersbackpack.inventory.upgrades.tanks.TanksUpgrade;
import com.tiviacz.travelersbackpack.item.upgrade.TanksUpgradeItem;
import com.tiviacz.travelersbackpack.item.upgrade.UpgradeItem;
import keletu.travelersbackpackplus.gear.GearItem;
import keletu.travelersbackpackplus.gear.GearTanks;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(value = UpgradeManager.class, remap = false)
public abstract class GearUpgradeManagerMixin {
    @Shadow
    @Final
    public BackpackWrapper wrapper;
    @Unique
    private GearTanks travelersbackpackplus$tanks;

    @Inject(method = "<init>(Lcom/tiviacz/travelersbackpack/inventory/BackpackWrapper;)V", at = @At("RETURN"))
    private void travelersbackpackplus$createTanks(BackpackWrapper backpack, CallbackInfo callback) {
        if (backpack.getBackpackStack().getItem() instanceof GearItem gear && gear.type().tankCount() > 0)
            travelersbackpackplus$tanks = new GearTanks((UpgradeManager) (Object) this);
    }

    @Inject(method = "getUpgrade", at = @At("HEAD"), cancellable = true)
    private <T extends UpgradeBase<T>> void travelersbackpackplus$inherentTanks(Class<T> type, CallbackInfoReturnable<Optional<T>> callback) {
        if (type == TanksUpgrade.class && wrapper.getBackpackStack().getItem() instanceof GearItem)
            callback.setReturnValue(Optional.ofNullable(travelersbackpackplus$tanks).map(type::cast));
    }

    @Inject(method = "canAddUpgrade", at = @At("HEAD"), cancellable = true)
    private void travelersbackpackplus$noTankPlugin(UpgradeItem item, CallbackInfoReturnable<Boolean> callback) {
        if (item instanceof TanksUpgradeItem && wrapper.getBackpackStack().getItem() instanceof GearItem)
            callback.setReturnValue(false);
    }
}
