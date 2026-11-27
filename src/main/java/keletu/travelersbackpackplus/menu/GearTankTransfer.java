package keletu.travelersbackpackplus.menu;

import com.tiviacz.travelersbackpack.inventory.BackpackWrapper;
import com.tiviacz.travelersbackpack.inventory.upgrades.tanks.TanksUpgrade;
import com.tiviacz.travelersbackpack.util.StacksHandlerUtils;
import keletu.travelersbackpackplus.GearRegistry;
import keletu.travelersbackpackplus.gear.AirTransfer;
import keletu.travelersbackpackplus.gear.GearResources;
import keletu.travelersbackpackplus.gear.GearState;
import keletu.travelersbackpackplus.gear.GearType;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.ResourceHandlerUtil;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.fluid.FluidStacksResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidUtil;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.transaction.Transaction;

import java.util.function.Predicate;

public final class GearTankTransfer {
    private static Predicate<FluidResource> filter(GearType type) {
        return switch (type) {
            case COPTER, FLAMETHROWER -> GearResources.FUEL;
            case JETPACK -> GearResources.WATER;
            case SCUBA -> AirTransfer::breathable;
        };
    }

    public static boolean accepts(GearType type, ItemStack stack) {
        if (stack.isEmpty()) return false;
        var handler = ItemAccess.forStack(stack).getCapability(Capabilities.Fluid.ITEM);
        if (handler == null) return false;
        var fluid = FluidUtil.getFirstStackContained(stack);
        return fluid.isEmpty() || filter(type).test(FluidResource.of(fluid));
    }

    public static boolean process(GearType type, BackpackWrapper wrapper, TanksUpgrade tanks, ItemStacksResourceHandler slots) {
        if (type == GearType.SCUBA) {
            GearState state = new GearState(wrapper.getBackpackStack());
            FluidStacksResourceHandler air = new FluidStacksResourceHandler(1, 6000);
            StacksHandlerUtils.setFluid(air, new FluidStack(GearRegistry.AIR.get(), state.air()));
            if (!AirTransfer.process(slots, air)) return false;
            state.air(StacksHandlerUtils.getFluidAmount(air));
            state.save(wrapper.getBackpackStack());
            wrapper.saveHandler.run();
            return true;
        }
        ItemStack input = StacksHandlerUtils.getStackInSlot(slots, 0);
        if (!accepts(type, input)) return false;
        ItemStacksResourceHandler temporary = new ItemStacksResourceHandler(1);
        StacksHandlerUtils.setStackInSlot(temporary, 0, input.copyWithCount(1));
        var itemFluids = ItemAccess.forHandlerIndexStrict(temporary, 0).getCapability(Capabilities.Fluid.ITEM);
        if (itemFluids == null) return false;
        int moved = 0;
        try (Transaction transaction = Transaction.openRoot()) {
            var tank = tanks.getLeftTank();
            moved = ResourceHandlerUtil.move(itemFluids, tank, filter(type), Integer.MAX_VALUE, transaction);
            if (moved == 0 && type == GearType.FLAMETHROWER) {
                tank = tanks.getRightTank();
                moved = ResourceHandlerUtil.move(itemFluids, tank, filter(type), Integer.MAX_VALUE, transaction);
            }
            if (moved == 0)
                moved = ResourceHandlerUtil.move(tanks.getLeftTank(), itemFluids, filter(type), Integer.MAX_VALUE, transaction);
            if (moved == 0 && type == GearType.FLAMETHROWER) {
                moved = ResourceHandlerUtil.move(tanks.getRightTank(), itemFluids, filter(type), Integer.MAX_VALUE, transaction);
            }
            if (moved == 0) return false;
            ItemStack result = StacksHandlerUtils.getStackInSlot(temporary, 0);
            if (!result.isEmpty() && slots.insert(1, ItemResource.of(result), result.getCount(), transaction) != result.getCount())
                return false;
            if (slots.extract(0, ItemResource.of(input), 1, transaction) != 1) return false;
            transaction.commit();
        }
        return true;
    }
}
