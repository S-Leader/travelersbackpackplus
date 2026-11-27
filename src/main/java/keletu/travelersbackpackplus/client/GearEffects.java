package keletu.travelersbackpackplus.client;

import com.tiviacz.travelersbackpack.attachment.AttachmentUtils;
import keletu.travelersbackpackplus.gear.GearItem;
import keletu.travelersbackpackplus.gear.GearState;
import keletu.travelersbackpackplus.gear.GearType;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Map;
import java.util.WeakHashMap;

public final class GearEffects {
    private static final Map<Player, GearLoopSound> SOUNDS = new WeakHashMap<>();

    public static void tick(Minecraft client) {
        if (client.level == null) {
            SOUNDS.clear();
            return;
        }
        SOUNDS.entrySet().removeIf(entry -> entry.getKey().isRemoved() || entry.getValue().isStopped());
        for (Player player : client.level.players()) {
            ItemStack stack = AttachmentUtils.getWearingBackpack(player);
            if (!(stack.getItem() instanceof GearItem item) || !new GearState(stack).active()) continue;
            if (item.type() == GearType.COPTER || item.type() == GearType.JETPACK) {
                if (!SOUNDS.containsKey(player)) {
                    GearLoopSound sound = new GearLoopSound(player, item.type());
                    SOUNDS.put(player, sound);
                    client.getSoundManager().play(sound);
                }
            }
            if (item.type() == GearType.JETPACK) {
                double angle = Math.toRadians(player.yBodyRot);
                double x = player.getX() + Math.sin(angle) * 0.3;
                double z = player.getZ() - Math.cos(angle) * 0.3;
                client.level.addParticle(ParticleTypes.CLOUD, x, player.getY() + 0.8, z, 0, -0.2, 0);
            }
        }
    }
}
