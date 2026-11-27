package keletu.travelersbackpackplus.client;

import com.tiviacz.travelersbackpack.attachment.AttachmentUtils;
import com.tiviacz.travelersbackpack.inventory.BackpackWrapper;
import keletu.travelersbackpackplus.gear.GearItem;
import keletu.travelersbackpackplus.gear.GearResources;
import keletu.travelersbackpackplus.gear.GearState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public final class GearHud {
    public static void render(GuiGraphicsExtractor graphics) {
        Minecraft client = Minecraft.getInstance();
        if (client.player == null || client.options.hideGui) return;
        ItemStack stack = AttachmentUtils.getWearingBackpack(client.player);
        if (!(stack.getItem() instanceof GearItem item)) return;
        GearState state = new GearState(stack);
        Component status = switch (item.type()) {
            case JETPACK ->
                    Component.translatable("status.travelersbackpackplus.boiler", state.temperature(), state.steam());
            case SCUBA -> Component.translatable("status.travelersbackpackplus.air", state.air(), 6000);
            case COPTER, FLAMETHROWER -> {
                BackpackWrapper wrapper = AttachmentUtils.getBackpackWrapper(client.player);
                int fuel = wrapper == null ? 0 : GearResources.available(wrapper, GearResources.FUEL);
                yield Component.translatable("status.travelersbackpackplus.fuel", fuel);
            }
        };
        graphics.text(client.font, stack.getHoverName(), 5, graphics.guiHeight() - 30, 0xFFFFFFFF, true);
        graphics.text(client.font, status, 5, graphics.guiHeight() - 20, 0xFFBCE6EF, true);
        if (state.enabled())
            graphics.text(client.font, Component.translatable(state.hover() ? "status.travelersbackpackplus.hover" : "status.travelersbackpackplus.on"), 5, graphics.guiHeight() - 40, 0xFFAAFFAA, true);
    }
}
