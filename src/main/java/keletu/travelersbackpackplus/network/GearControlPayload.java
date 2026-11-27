package keletu.travelersbackpackplus.network;

import keletu.travelersbackpackplus.TravelersBackpackPlus;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record GearControlPayload(int flags, int action) implements CustomPacketPayload {
    public static final Type<GearControlPayload> TYPE = new Type<>(TravelersBackpackPlus.id("control"));
    public static final StreamCodec<RegistryFriendlyByteBuf, GearControlPayload> CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, GearControlPayload::flags, ByteBufCodecs.VAR_INT, GearControlPayload::action, GearControlPayload::new);

    @Override
    public Type<GearControlPayload> type() {
        return TYPE;
    }
}
