package keletu.travelersbackpackplus.gear;

import com.tiviacz.travelersbackpack.attachment.AttachmentUtils;
import com.tiviacz.travelersbackpack.inventory.BackpackWrapper;
import com.tiviacz.travelersbackpack.inventory.upgrades.tanks.TanksUpgrade;
import com.tiviacz.travelersbackpack.util.StacksHandlerUtils;
import keletu.travelersbackpackplus.GearConfig;
import keletu.travelersbackpackplus.GearRegistry;
import keletu.travelersbackpackplus.mixin.FlightConnectionAccess;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingFallEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.Map;
import java.util.WeakHashMap;

public final class GearEvents {
    private static final Map<ServerPlayer, Controls> INPUTS = new WeakHashMap<>();

    public static void control(ServerPlayer player, int flags, int action) {
        ItemStack stack = AttachmentUtils.getWearingBackpack(player);
        if (!(stack.getItem() instanceof GearItem item) || !player.isAlive()) return;
        long now = player.level().getGameTime();
        Controls previous = INPUTS.get(player);
        long lastAction = previous == null ? now - 10 : previous.lastAction();
        INPUTS.put(player, new Controls(stack, flags & 3, now, lastAction));
        if (action == 0 || now - lastAction < 5) return;
        if (action == 1 && item.type() == GearType.SCUBA || action == 2 && item.type() != GearType.COPTER) return;
        GearState state = new GearState(stack);
        if (action == 1 && item.type() != GearType.SCUBA) {
            if (!state.enabled() && item.type() == GearType.COPTER) {
                BackpackWrapper wrapper = AttachmentUtils.getBackpackWrapper(player);
                if (wrapper == null || !GearResources.consume(wrapper, 100, GearResources.FUEL)) {
                    player.sendOverlayMessage(Component.translatable("message.travelersbackpackplus.no_fuel"));
                    return;
                }
            }
            state.enabled(!state.enabled());
            state.active(false);
        } else if (action == 2 && item.type() == GearType.COPTER) state.hover(!state.hover());
        state.save(stack);
        INPUTS.put(player, new Controls(stack, flags & 3, now, now));
        AttachmentUtils.synchronise(player);
        String message = action == 2 ? (state.hover() ? "status.travelersbackpackplus.hover" : "status.travelersbackpackplus.normal") : (state.enabled() ? "message.travelersbackpackplus.on" : "message.travelersbackpackplus.off");
        player.sendOverlayMessage(Component.translatable(message));
    }

    @SubscribeEvent
    public static void tick(PlayerTickEvent.Post event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        ItemStack stack = AttachmentUtils.getWearingBackpack(player);
        if (!(stack.getItem() instanceof GearItem item) || !player.isAlive() || player.isSpectator()) {
            Controls removed = INPUTS.remove(player);
            if (removed != null) {
                GearState old = new GearState(removed.stack());
                old.enabled(false);
                old.active(false);
                old.save(removed.stack());
            }
            return;
        }
        BackpackWrapper wrapper = AttachmentUtils.getBackpackWrapper(player);
        if (wrapper == null) return;
        GearState state = new GearState(stack);
        boolean wasActive = state.active();
        boolean wasEnabled = state.enabled();
        Controls controls = INPUTS.get(player);
        long tick = player.level().getGameTime();
        int input = controls != null && controls.stack() == stack && tick - controls.tick() < 30 ? controls.flags() : 0;
        boolean jump = (input & 1) != 0;
        boolean descend = (input & 2) != 0;
        boolean flight = false;
        state.active(false);
        switch (item.type()) {
            case COPTER -> {
                if (player.isInWater()) state.enabled(false);
                if (state.enabled()) {
                    int multiplier = 1 + (state.hover() ? 1 : 0) + (jump ? 1 : 0);
                    boolean powered = GearResources.available(wrapper, GearResources.FUEL) >= GearConfig.COPTER_COST.get() * multiplier;
                    if (powered && tick % 3 == 0)
                        powered = GearResources.consume(wrapper, GearConfig.COPTER_COST.get() * multiplier, GearResources.FUEL);
                    if (!powered && player.onGround()) state.enabled(false);
                    if (!powered) state.hover(false);
                    state.active(powered);
                    FlightPhysics.apply(player, item.type(), powered, jump, descend, state.hover(), GearConfig.FLIGHT_CEILING.get());
                    flight = !player.onGround();
                    if (tick % 2 == 0 && powered) {
                        for (ItemEntity entity : player.level().getEntitiesOfClass(ItemEntity.class, player.getBoundingBox().inflate(4))) {
                            Vec3 push = entity.position().subtract(player.position()).normalize().scale(0.08);
                            entity.push(push.x, 0.02, push.z);
                        }
                    }
                }
            }
            case JETPACK -> {
                if (player.isInWater() && tick % 5 == 0) wrapper.getUpgrade(TanksUpgrade.class).ifPresent(tanks -> {
                    if (tanks.getLeftTank().getResource(0).isEmpty() || GearResources.WATER.test(tanks.getLeftTank().getResource(0))) {
                        StacksHandlerUtils.fill(tanks.getLeftTank(), new FluidStack(Fluids.WATER, 2), false);
                    }
                });
                if (state.enabled()) {
                    if (state.burn() <= 0 && state.temperature() < 200) {
                        state.burn(GearResources.burnFuel(wrapper, player));
                        state.burnTotal(state.burn());
                    }
                    if (state.burn() > 0) {
                        state.burn(state.burn() - (jump ? 3 : 1));
                        int heatInterval = state.temperature() < 50 ? 20 : state.temperature() < 100 ? 15 : state.temperature() < 150 ? 10 : 5;
                        if (tick % heatInterval == 0) state.temperature(state.temperature() + 1);
                    }
                    if (state.temperature() >= 100 && state.steam() <= 11992 && GearResources.consume(wrapper, 2, GearResources.WATER))
                        state.steam(state.steam() + 8);
                    int cost = GearConfig.JET_COST.get() + (wasActive ? 0 : 150);
                    if (jump && !player.isInWater() && state.steam() >= cost) {
                        state.steam(state.steam() - cost);
                        state.active(true);
                        if (tick % 10 == 0) state.temperature(state.temperature() - 1);
                        FlightPhysics.apply(player, item.type(), true, true, descend, false, GearConfig.FLIGHT_CEILING.get());
                        flight = true;
                    }
                }
                if (state.burn() == 0 && tick % 40 == 0) state.temperature(state.temperature() - 1);
            }
            case SCUBA -> {
                if (tick % 20 == 0 && player.isUnderWater() && player.getAirSupply() < player.getMaxAirSupply() && state.air() > 0) {
                    int before = state.air();
                    state.air(state.air() - GearConfig.AIR_COST.get());
                    player.setAirSupply(player.getMaxAirSupply());
                    state.active(true);
                    player.level().sendParticles(ParticleTypes.BUBBLE, player.getX(), player.getEyeY(), player.getZ(), 4, 0.1, 0.1, 0.1, 0.02);
                    if (before >= 600 && state.air() < 600)
                        player.sendOverlayMessage(Component.translatable("message.travelersbackpackplus.low_air"));
                    if (state.air() == 0)
                        player.sendOverlayMessage(Component.translatable("message.travelersbackpackplus.empty_air"));
                }
            }
            case FLAMETHROWER -> {
                if (GearResources.available(wrapper, GearResources.FUEL) == 0 || player.isInWater())
                    state.enabled(false);
                state.active(state.enabled() && player.isUsingItem() && player.getUseItem().is(GearRegistry.NOZZLE.get()));
            }
        }
        state.save(stack);
        if (flight) {
            player.fallDistance = 0;
            FlightConnectionAccess connection = (FlightConnectionAccess) player.connection;
            connection.travelersbackpackplus$setFloating(false);
            connection.travelersbackpackplus$setFloatingTicks(0);
        }
        if (tick % 10 == 0 || wasActive != state.active() || wasEnabled != state.enabled())
            AttachmentUtils.synchronise(player);
    }

    @SubscribeEvent
    public static void fall(LivingFallEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        ItemStack stack = AttachmentUtils.getWearingBackpack(player);
        if (!(stack.getItem() instanceof GearItem item)) return;
        GearState state = new GearState(stack);
        if (item.type() == GearType.COPTER && state.enabled() || item.type() == GearType.JETPACK && state.active())
            event.setCanceled(true);
    }

    private record Controls(ItemStack stack, int flags, long tick, long lastAction) {
    }
}
