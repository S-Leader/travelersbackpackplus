package keletu.travelersbackpackplus.mixin;

import com.tiviacz.travelersbackpack.inventory.upgrades.tanks.TanksUpgrade;
import com.tiviacz.travelersbackpack.item.HoseItem;
import keletu.travelersbackpackplus.gear.GearItem;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.fluid.FluidStacksResourceHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = HoseItem.class, remap = false)
public abstract class GearHoseMixin {
    @Inject(method = "getSelectedFluidTank", at = @At("HEAD"), cancellable = true)
    private void travelersbackpackplus$singleTank(ItemStack stack, TanksUpgrade tanks, CallbackInfoReturnable<FluidStacksResourceHandler> callback) {
        if (tanks.getWrapper().getBackpackStack().getItem() instanceof GearItem gear && gear.type().tankCount() == 1)
            callback.setReturnValue(tanks.getLeftTank());
    }
}
