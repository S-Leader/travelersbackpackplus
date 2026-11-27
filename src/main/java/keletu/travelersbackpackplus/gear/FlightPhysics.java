package keletu.travelersbackpackplus.gear;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public final class FlightPhysics {
    public static double lift(double y, double velocity, double speed, int ceiling) {
        return y >= ceiling ? Math.max(-0.3, speed - (y - ceiling) / 100.0) : Math.max(velocity, speed);
    }

    public static double thrust(double y, double velocity, int ceiling) {
        if (y >= ceiling) return Math.max(-0.3, 0.5 - (y - ceiling) / 100.0);
        return Math.min(velocity + (velocity < 0 ? 0.56 : 0.14), 0.5);
    }

    public static void apply(Player player, GearType type, boolean powered, boolean jump, boolean descend, boolean hover, int ceiling) {
        if (player.isPassenger() || player.isFallFlying() || player.getAbilities().flying || player.isInWater()) return;
        Vec3 motion = player.getDeltaMovement();
        double y = motion.y;
        if (type == GearType.COPTER) {
            if (powered && jump) y = lift(player.getY(), y, 0.23, ceiling);
            else if (powered && descend && !player.onGround()) y = -0.22;
            else if (powered && hover && !player.onGround()) y = 0.08;
            else if (y < 0) y = Math.max(-0.3, y * 0.6 + 0.035);
        } else if (type == GearType.JETPACK && powered && jump) {
            y = thrust(player.getY(), y, ceiling);
        } else return;
        player.setDeltaMovement(motion.x, y, motion.z);
        player.fallDistance = 0;
    }
}
