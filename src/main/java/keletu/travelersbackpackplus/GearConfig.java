package keletu.travelersbackpackplus;

import net.neoforged.neoforge.common.ModConfigSpec;

public final class GearConfig {
    public static final ModConfigSpec SPEC;
    public static final ModConfigSpec.IntValue FLIGHT_CEILING;
    public static final ModConfigSpec.IntValue COPTER_COST;
    public static final ModConfigSpec.IntValue JET_COST;
    public static final ModConfigSpec.IntValue AIR_COST;
    public static final ModConfigSpec.DoubleValue FLAME_DAMAGE;
    public static final ModConfigSpec.BooleanValue FLAME_PVP;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        FLIGHT_CEILING = builder.defineInRange("flightCeiling", 90, -64, 4096);
        COPTER_COST = builder.defineInRange("copterFuelPerThreeTicks", 6, 1, 1000);
        JET_COST = builder.defineInRange("jetSteamPerTick", 10, 1, 1000);
        AIR_COST = builder.defineInRange("scubaAirPerSecond", 10, 1, 1000);
        FLAME_DAMAGE = builder.defineInRange("flamethrowerDamage", 4.0, 0.0, 100.0);
        FLAME_PVP = builder.define("flamethrowerPvp", false);
        SPEC = builder.build();
    }
}
