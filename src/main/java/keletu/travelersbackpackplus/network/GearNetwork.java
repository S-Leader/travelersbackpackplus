package keletu.travelersbackpackplus.network;

import keletu.travelersbackpackplus.gear.GearEvents;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

public final class GearNetwork {
    public static void register(RegisterPayloadHandlersEvent event) {
        event.registrar("1").playToServer(GearControlPayload.TYPE, GearControlPayload.CODEC, (payload, context) -> {
            if (context.player() instanceof ServerPlayer player && payload.action() >= 0 && payload.action() <= 2) {
                GearEvents.control(player, payload.flags(), payload.action());
            }
        });
    }
}
