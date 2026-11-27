package keletu.travelersbackpackplus.compat;

import com.tiviacz.travelersbackpack.compat.curios.TravelersBackpackCurioClient;
import keletu.travelersbackpackplus.GearRegistry;
import top.theillusivec4.curios.api.client.ICurioRenderer;

public final class CurioClientCompat {
    public static void register() {
        GearRegistry.PACKS.forEach(pack -> ICurioRenderer.register(pack.get(), TravelersBackpackCurioClient.Renderer::new));
    }
}
