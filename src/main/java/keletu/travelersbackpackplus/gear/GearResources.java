package keletu.travelersbackpackplus.gear;

import com.tiviacz.travelersbackpack.inventory.BackpackWrapper;
import com.tiviacz.travelersbackpack.inventory.upgrades.tanks.TanksUpgrade;
import com.tiviacz.travelersbackpack.util.StacksHandlerUtils;
import keletu.travelersbackpackplus.TravelersBackpackPlus;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.fluid.FluidStacksResourceHandler;
import net.neoforged.neoforge.transfer.transaction.Transaction;

import java.util.List;
import java.util.function.Predicate;

public final class GearResources {
    public static final TagKey<Fluid> FUELS = TagKey.create(Registries.FLUID, TravelersBackpackPlus.id("fuels"));
    public static final Predicate<FluidResource> FUEL = resource -> !resource.isEmpty() && resource.getFluid().is(FUELS);
    public static final Predicate<FluidResource> WATER = resource -> resource.is(Fluids.WATER);

    public static int available(BackpackWrapper wrapper, Predicate<FluidResource> predicate) {
        return wrapper.getUpgrade(TanksUpgrade.class).map(tanks -> tanks(wrapper, tanks).stream().filter(tank -> predicate.test(tank.getResource(0))).mapToInt(tank -> tank.getAmountAsInt(0)).sum()).orElse(0);
    }

    public static boolean consume(BackpackWrapper wrapper, int amount, Predicate<FluidResource> predicate) {
        if (amount <= 0) return true;
        if (available(wrapper, predicate) < amount) return false;
        TanksUpgrade tanks = wrapper.getUpgrade(TanksUpgrade.class).orElseThrow();
        try (Transaction tx = Transaction.openRoot()) {
            int remaining = amount;
            for (FluidStacksResourceHandler tank : tanks(wrapper, tanks)) {
                FluidResource resource = tank.getResource(0);
                if (predicate.test(resource)) remaining -= tank.extract(0, resource, remaining, tx);
                if (remaining == 0) break;
            }
            if (remaining != 0) return false;
            tx.commit();
            return true;
        }
    }

    public static int burnFuel(BackpackWrapper wrapper, ServerPlayer player) {
        var storage = wrapper.getStorage();
        for (int slot = 0; slot < Math.min(1, storage.size()); slot++) {
            ItemStack fuel = StacksHandlerUtils.getStackInSlot(storage, slot);
            int burn = fuel.getBurnTime(RecipeType.SMELTING, player.level().fuelValues());
            if (burn <= 0) continue;
            ItemStack consumed = StacksHandlerUtils.extractItem(storage, slot, 1, false);
            if (consumed.isEmpty()) continue;
            var remainder = consumed.getCraftingRemainder();
            if (remainder != null) player.getInventory().placeItemBackInInventory(remainder.create());
            return burn;
        }
        return 0;
    }

    private static List<FluidStacksResourceHandler> tanks(BackpackWrapper wrapper, TanksUpgrade upgrade) {

        if (wrapper.getBackpackStack().getItem() instanceof GearItem gear) {
            return switch (gear.type()) {
                case COPTER, JETPACK -> List.of(upgrade.getLeftTank());
                case FLAMETHROWER -> List.of(upgrade.getLeftTank(), upgrade.getRightTank());
                case SCUBA -> List.of();
            };
        }

        return List.of(upgrade.getLeftTank(), upgrade.getRightTank());
    }
}
