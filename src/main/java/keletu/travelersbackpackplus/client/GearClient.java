package keletu.travelersbackpackplus.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.tiviacz.travelersbackpack.attachment.AttachmentUtils;
import com.tiviacz.travelersbackpack.init.ModBlockEntityTypes;
import keletu.travelersbackpackplus.GearConfig;
import keletu.travelersbackpackplus.GearRegistry;
import keletu.travelersbackpackplus.TravelersBackpackPlus;
import keletu.travelersbackpackplus.client.screen.CompressorScreen;
import keletu.travelersbackpackplus.client.screen.GearScreen;
import keletu.travelersbackpackplus.compat.CurioClientCompat;
import keletu.travelersbackpackplus.gear.FlightPhysics;
import keletu.travelersbackpackplus.gear.GearItem;
import keletu.travelersbackpackplus.gear.GearState;
import keletu.travelersbackpackplus.network.GearControlPayload;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(modid = TravelersBackpackPlus.ID, value = Dist.CLIENT)
public final class GearClient {
    private static final KeyMapping.Category CATEGORY = new KeyMapping.Category(TravelersBackpackPlus.id("gear"));
    public static final KeyMapping TOGGLE = new KeyMapping("key.travelersbackpackplus.toggle", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_N, CATEGORY);
    public static final KeyMapping MODE = new KeyMapping("key.travelersbackpackplus.mode", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_G, CATEGORY);
    private static int lastInput = -1;

    @SubscribeEvent
    public static void setup(FMLClientSetupEvent event) {
        if (ModList.get().isLoaded("curios")) event.enqueueWork(CurioClientCompat::register);
    }

    @SubscribeEvent
    public static void keys(RegisterKeyMappingsEvent event) {
        event.registerCategory(CATEGORY);
        event.register(TOGGLE);
        event.register(MODE);
    }

    @SubscribeEvent
    public static void screens(RegisterMenuScreensEvent event) {
        event.register(GearRegistry.GEAR_ITEM_MENU.get(), GearScreen::new);
        event.register(GearRegistry.GEAR_BLOCK_MENU.get(), GearScreen::new);
        event.register(GearRegistry.COMPRESSOR_MENU.get(), CompressorScreen::new);
    }

    @SubscribeEvent
    public static void fluids(RegisterFluidModelsEvent event) {
        Material material = new Material(TravelersBackpackPlus.id("block/air"));
        event.register(new FluidModel.Unbaked(material, material, null, null), GearRegistry.AIR);
    }

    @SubscribeEvent
    public static void models(RegisterSpecialModelRendererEvent event) {
        event.register(TravelersBackpackPlus.id("gear"), GearItemRenderer.Unbaked.CODEC);
        GearRender.reload();
    }

    @SubscribeEvent
    public static void renderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntityTypes.BACKPACK.get(), GearPlacedRenderer::new);
    }

    @SubscribeEvent
    public static void hud(RegisterGuiLayersEvent event) {
        event.registerAboveAll(TravelersBackpackPlus.id("gear"), (graphics, delta) -> GearHud.render(graphics));
    }

    @SubscribeEvent
    public static void tick(ClientTickEvent.Post event) {
        Minecraft client = Minecraft.getInstance();
        GearEffects.tick(client);
        if (client.player == null || client.level == null) {
            lastInput = -1;
            return;
        }
        ItemStack stack = AttachmentUtils.getWearingBackpack(client.player);
        if (!(stack.getItem() instanceof GearItem item)) {
            lastInput = -1;
            while (TOGGLE.consumeClick()) {
            }
            while (MODE.consumeClick()) {
            }
            return;
        }
        boolean jump = client.screen == null && client.options.keyJump.isDown();
        boolean descend = client.screen == null && client.options.keyShift.isDown();
        int flags = (jump ? 1 : 0) | (descend ? 2 : 0);
        if (flags != lastInput || client.player.tickCount % 10 == 0) send(flags, 0);
        lastInput = flags;
        while (TOGGLE.consumeClick()) if (client.screen == null) send(flags, 1);
        while (MODE.consumeClick()) if (client.screen == null) send(flags, 2);
        GearState state = new GearState(stack);
        if (state.enabled())
            FlightPhysics.apply(client.player, item.type(), state.active(), jump, descend, state.hover(), GearConfig.FLIGHT_CEILING.get());
    }

    private static void send(int flags, int action) {
        ClientPacketDistributor.sendToServer(new GearControlPayload(flags, action));
    }
}
