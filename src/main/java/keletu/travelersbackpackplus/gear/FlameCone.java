package keletu.travelersbackpackplus.gear;

import net.minecraft.world.phys.Vec3;

public final class FlameCone {
    public static boolean contains(Vec3 direction, Vec3 delta, double range, double angle) {
        double distanceSquared = delta.lengthSqr();
        if (distanceSquared > range * range) return false;
        if (distanceSquared < 0.000001) return true;
        double dot = direction.dot(delta);
        double cosine = Math.cos(Math.toRadians(angle / 2));
        return dot > 0 && dot * dot >= cosine * cosine * direction.lengthSqr() * distanceSquared;
    }
}
