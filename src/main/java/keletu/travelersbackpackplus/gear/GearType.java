package keletu.travelersbackpackplus.gear;

public enum GearType {
    COPTER("copter_pack"), JETPACK("steam_jetpack"), FLAMETHROWER("flamethrower_pack"), SCUBA("scuba_gear");

    private final String path;

    GearType(String path) {
        this.path = path;
    }

    public String path() {
        return path;
    }

    public int tankCapacity() {
        return switch (this) {
            case COPTER, JETPACK -> 6000;
            case FLAMETHROWER -> 4000;
            case SCUBA -> 3000;
        };
    }
}
