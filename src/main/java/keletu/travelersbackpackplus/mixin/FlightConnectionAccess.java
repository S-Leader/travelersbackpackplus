package keletu.travelersbackpackplus.mixin;

import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ServerGamePacketListenerImpl.class)
public interface FlightConnectionAccess {
    @Accessor("clientIsFloating")
    void travelersbackpackplus$setFloating(boolean floating);

    @Accessor("aboveGroundTickCount")
    void travelersbackpackplus$setFloatingTicks(int ticks);
}
