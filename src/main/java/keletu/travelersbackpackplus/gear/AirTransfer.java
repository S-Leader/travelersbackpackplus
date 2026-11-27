package keletu.travelersbackpackplus.gear;

import com.tiviacz.travelersbackpack.util.StacksHandlerUtils;
import keletu.travelersbackpackplus.GearRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.ResourceHandlerUtil;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.fluid.FluidStacksResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.transaction.Transaction;

public final class AirTransfer {
    public static boolean breathable(FluidResource resource) {
        String name = BuiltInRegistries.FLUID.getKey(resource.getFluid()).getPath();
        return !resource.isEmpty() && (name.equals("air") || name.equals("oxygen"));
    }

    public static boolean process(ItemStacksResourceHandler slots, FluidStacksResourceHandler tank) {
        ItemStack input = StacksHandlerUtils.getStackInSlot(slots, 0);
        if (input.isEmpty()) return false;
        ItemStacksResourceHandler temporary = new ItemStacksResourceHandler(1);
        StacksHandlerUtils.setStackInSlot(temporary, 0, input.copyWithCount(1));
        var fluids = ItemAccess.forHandlerIndexStrict(temporary, 0).getCapability(Capabilities.Fluid.ITEM);
        if (fluids == null) return false;
        try (Transaction transaction = Transaction.openRoot()) {
            var resource = ResourceHandlerUtil.findExtractableResource(fluids, AirTransfer::breathable, transaction);
            int moved = 0;
            if (resource != null && !resource.isEmpty()) {
                int room = StacksHandlerUtils.getCapacity(tank) - StacksHandlerUtils.getFluidAmount(tank);
                moved = fluids.extract(resource, Math.max(0, room), transaction);
                if (moved > 0 && tank.insert(0, FluidResource.of(GearRegistry.AIR.get()), moved, transaction) != moved)
                    return false;
            } else {
                moved = ResourceHandlerUtil.move(tank, fluids, AirTransfer::breathable, Integer.MAX_VALUE, transaction);
            }
            if (moved == 0) return false;
            ItemStack result = StacksHandlerUtils.getStackInSlot(temporary, 0);
            if (!result.isEmpty() && slots.insert(1, ItemResource.of(result), result.getCount(), transaction) != result.getCount())
                return false;
            if (slots.extract(0, ItemResource.of(input), 1, transaction) != 1) return false;
            transaction.commit();
            return true;
        }
    }
}
