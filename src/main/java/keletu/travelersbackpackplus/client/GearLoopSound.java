package keletu.travelersbackpackplus.client;

import com.tiviacz.travelersbackpack.attachment.AttachmentUtils;
import keletu.travelersbackpackplus.GearRegistry;
import keletu.travelersbackpackplus.gear.GearItem;
import keletu.travelersbackpackplus.gear.GearState;
import keletu.travelersbackpackplus.gear.GearType;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public final class GearLoopSound extends AbstractTickableSoundInstance {
    private final Player player;
    private final GearType type;

    public GearLoopSound(Player player, GearType type) {
        super(type == GearType.COPTER ? GearRegistry.COPTER_SOUND.get() : GearRegistry.JET_SOUND.get(), SoundSource.PLAYERS, player.getRandom());
        this.player = player;
        this.type = type;
        looping = true;
        delay = 0;
        volume = type == GearType.COPTER ? 0.4F : 0.25F;
        tick();
    }

    @Override
    public void tick() {
        ItemStack stack = AttachmentUtils.getWearingBackpack(player);
        if (!player.isAlive() || player.isRemoved() || !(stack.getItem() instanceof GearItem item) || item.type() != type || !new GearState(stack).active()) {
            stop();
            return;
        }
        x = player.getX();
        y = player.getY() + 1;
        z = player.getZ();
        pitch = type == GearType.COPTER && player.getDeltaMovement().y > 0 ? 1.1F : 1.0F;
    }
}
