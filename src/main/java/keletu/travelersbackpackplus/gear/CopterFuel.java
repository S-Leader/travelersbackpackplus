package keletu.travelersbackpackplus.gear;

import com.tiviacz.travelersbackpack.inventory.BackpackWrapper;
import com.tiviacz.travelersbackpack.inventory.upgrades.tanks.TanksUpgrade;
import keletu.travelersbackpackplus.GearConfig;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.transfer.fluid.FluidResource;

import java.util.List;

public final class CopterFuel {
    public static final int INTERVAL = 3;
    public static final List<String> DEFAULT_MULTIPLIERS = List.of("fuel=1.0", "oil=1.5", "bioethanol=1.2", "biofuel=1.0", "creosote=1.8", "coal=0.8", "biomass=1.5");

    public static boolean validMultiplier(Object value) {
        if (!(value instanceof String entry)) return false;
        int separator = entry.indexOf('=');
        if (separator <= 0 || Identifier.tryParse(entry.substring(0, separator).trim()) == null) return false;
        try {
            double multiplier = Double.parseDouble(entry.substring(separator + 1).trim());
            return multiplier > 0 && Double.isFinite(multiplier);
        } catch (NumberFormatException error) {
            return false;
        }
    }

    public static double multiplier(Identifier fluid, List<? extends String> entries) {
        double general = 1;
        double exact = Double.NaN;
        for (String entry : entries) {
            if (!validMultiplier(entry)) continue;
            int separator = entry.indexOf('=');
            String name = entry.substring(0, separator).trim();
            double value = Double.parseDouble(entry.substring(separator + 1).trim());
            if (name.equals(fluid.toString())) exact = value;
            else if (name.equals(fluid.getPath())) general = value;
        }
        return Double.isNaN(exact) ? general : exact;
    }

    public static double multiplier(FluidResource fluid) {
        return GearResources.FUEL.test(fluid) ? multiplier(BuiltInRegistries.FLUID.getKey(fluid.getFluid()), GearConfig.COPTER_FUEL_MULTIPLIERS.get()) : 0;
    }

    public static int cost(int base, int hoverExtra, int climbExtra, boolean hoverMode, boolean descend, boolean climb, double multiplier) {
        long amount = Math.max(0, base);
        if (hoverMode && !descend) amount += Math.max(0, hoverExtra);
        if (climb) amount += Math.max(0, climbExtra);
        if (!(multiplier > 0) || !Double.isFinite(multiplier)) return 0;
        return Math.max(0, (int) Math.ceil(amount * multiplier - 1.0E-4));
    }

    public static boolean consume(BackpackWrapper wrapper, boolean hoverMode, boolean descend, boolean climb) {
        int available = GearResources.available(wrapper, GearResources.FUEL);
        if (available == 0) return false;
        FluidResource fluid = wrapper.getUpgrade(TanksUpgrade.class).orElseThrow().getLeftTank().getResource(0);
        int amount = cost(GearConfig.COPTER_COST.get(), GearConfig.COPTER_HOVER_COST.get(), GearConfig.COPTER_CLIMB_COST.get(), hoverMode, descend, climb, multiplier(fluid));
        return GearResources.consume(wrapper, Math.min(available, amount), GearResources.FUEL);
    }
}
