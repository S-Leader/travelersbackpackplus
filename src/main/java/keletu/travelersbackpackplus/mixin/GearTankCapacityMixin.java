package keletu.travelersbackpackplus.mixin;

import com.tiviacz.travelersbackpack.inventory.BackpackWrapper;
import keletu.travelersbackpackplus.gear.GearItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BackpackWrapper.class)
public abstract class GearTankCapacityMixin {
    @Shadow
    protected ItemStack stack;
    @Shadow
    private int tanksCapacity;

    @Inject(method = "setBackpackTankCapacity", at = @At("RETURN"))
    private void travelersbackpackplus$capacity(CallbackInfo callback) {
        if (stack.getItem() instanceof GearItem item) tanksCapacity = item.type().tankCapacity();
    }
}
