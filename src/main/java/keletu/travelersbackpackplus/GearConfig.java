package keletu.travelersbackpackplus;

import keletu.travelersbackpackplus.gear.CopterFuel;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;

public final class GearConfig {
    public static final ModConfigSpec SPEC;
    public static final ModConfigSpec.IntValue FLIGHT_CEILING;
    public static final ModConfigSpec.IntValue COPTER_COST;
    public static final ModConfigSpec.IntValue COPTER_HOVER_COST;
    public static final ModConfigSpec.IntValue COPTER_CLIMB_COST;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> COPTER_FUEL_MULTIPLIERS;
    public static final ModConfigSpec.IntValue JET_COST;
    public static final ModConfigSpec.IntValue AIR_COST;
    public static final ModConfigSpec.DoubleValue FLAME_DAMAGE;
    public static final ModConfigSpec.BooleanValue FLAME_PVP;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        FLIGHT_CEILING = builder.defineInRange("flightCeiling", 90, -64, 4096);
        COPTER_COST = builder.defineInRange("copterFuelPerThreeTicks", 6, 1, 1000);
        COPTER_HOVER_COST = builder.defineInRange("copterHoverExtraFuelPerThreeTicks", 6, 0, 1000);
        COPTER_CLIMB_COST = builder.defineInRange("copterClimbExtraFuelPerThreeTicks", 6, 0, 1000);
        COPTER_FUEL_MULTIPLIERS = builder.defineListAllowEmpty("copterFuelMultipliers", CopterFuel.DEFAULT_MULTIPLIERS, () -> "fuel=1.0", CopterFuel::validMultiplier);
        JET_COST = builder.defineInRange("jetSteamPerTick", 10, 1, 1000);
        AIR_COST = builder.defineInRange("scubaAirPerSecond", 10, 1, 1000);
        FLAME_DAMAGE = builder.defineInRange("flamethrowerDamage", 4.0, 0.0, 100.0);
        FLAME_PVP = builder.define("flamethrowerPvp", false);
        SPEC = builder.build();
    }
}
