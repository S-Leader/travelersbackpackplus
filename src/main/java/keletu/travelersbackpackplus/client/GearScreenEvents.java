package keletu.travelersbackpackplus.client;

import com.tiviacz.travelersbackpack.attachment.AttachmentUtils;
import com.tiviacz.travelersbackpack.client.screens.ToolsScreen;
import com.tiviacz.travelersbackpack.item.HoseItem;
import keletu.travelersbackpackplus.TravelersBackpackPlus;
import keletu.travelersbackpackplus.gear.GearItem;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;

@EventBusSubscriber(modid = TravelersBackpackPlus.ID, value = Dist.CLIENT)
public final class GearScreenEvents {
    @SubscribeEvent
    public static void opening(ScreenEvent.Opening event) {
        var player = Minecraft.getInstance().player;
        if (player != null && event.getNewScreen() instanceof ToolsScreen && blocksTools(player))
            event.setCanceled(true);
    }

    @SubscribeEvent
    public static void tick(ClientTickEvent.Pre event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player != null && minecraft.screen instanceof ToolsScreen && blocksTools(minecraft.player))
            minecraft.setScreen(null);
    }

    private static boolean blocksTools(Player player) {
        var held = player.getMainHandItem().isEmpty() ? player.getOffhandItem() : player.getMainHandItem();
        return AttachmentUtils.getWearingBackpack(player).getItem() instanceof GearItem && !(held.getItem() instanceof HoseItem);
    }
}
