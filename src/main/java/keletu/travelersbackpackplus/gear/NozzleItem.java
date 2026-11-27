package keletu.travelersbackpackplus.gear;

import com.tiviacz.travelersbackpack.attachment.AttachmentUtils;
import com.tiviacz.travelersbackpack.inventory.BackpackWrapper;
import keletu.travelersbackpackplus.GearConfig;
import keletu.travelersbackpackplus.GearRegistry;
import keletu.travelersbackpackplus.TravelersBackpackPlus;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public final class NozzleItem extends Item {
    public static final ResourceKey<DamageType> DAMAGE = ResourceKey.create(Registries.DAMAGE_TYPE, TravelersBackpackPlus.id("flamethrower"));

    public NozzleItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack pack = AttachmentUtils.getWearingBackpack(player);
        if (!pack.is(GearRegistry.FLAMETHROWER.get()) || !new GearState(pack).enabled()) return InteractionResult.FAIL;
        player.startUsingItem(hand);
        return InteractionResult.CONSUME;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity user) {
        return 72000;
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack stack) {
        return ItemUseAnimation.BOW;
    }

    @Override
    public void onUseTick(Level level, LivingEntity user, ItemStack stack, int remaining) {
        if (!(user instanceof ServerPlayer player) || remaining % 5 != 0) return;
        ItemStack pack = AttachmentUtils.getWearingBackpack(player);
        BackpackWrapper wrapper = AttachmentUtils.getBackpackWrapper(player);
        if (!pack.is(GearRegistry.FLAMETHROWER.get()) || !new GearState(pack).enabled() || wrapper == null
                || player.isInWater() || !GearResources.consume(wrapper, 25, GearResources.FUEL)) {
            player.stopUsingItem();
            return;
        }
        Vec3 eye = player.getEyePosition();
        Vec3 direction = player.getLookAngle();
        for (LivingEntity target : level.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(8))) {
            if (target == player || target.fireImmune() || target.isInWater() || target instanceof TamableAnimal pet && pet.isOwnedBy(player))
                continue;
            if (target instanceof Player other && (!GearConfig.FLAME_PVP.get() || !player.level().isPvpAllowed() || !player.canHarmPlayer(other)))
                continue;
            Vec3 center = target.getBoundingBox().getCenter();
            Vec3 delta = center.subtract(eye);
            if (!FlameCone.contains(direction, delta, 8, 30)) continue;
            HitResult obstruction = level.clip(new ClipContext(eye, center, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));
            if (obstruction.getType() != HitResult.Type.MISS) continue;
            target.hurtServer(player.level(), player.damageSources().source(DAMAGE, player), GearConfig.FLAME_DAMAGE.get().floatValue());
            target.igniteForSeconds(10);
        }
        for (int i = 1; i <= 16; i++) {
            Vec3 point = eye.add(direction.scale(i * 0.5));
            HitResult hit = level.clip(new ClipContext(eye, point, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));
            if (hit.getType() != HitResult.Type.MISS) break;
            player.level().sendParticles(ParticleTypes.FLAME, point.x, point.y, point.z, 3, i * 0.025, i * 0.025, i * 0.025, 0.02);
        }
        if (remaining % 20 == 0)
            level.playSound(null, player.blockPosition(), GearRegistry.FLAME_SOUND.get(), SoundSource.PLAYERS, 0.6F, 1.0F);
    }
}
