package keletu.travelersbackpackplus.client.screen;

import keletu.travelersbackpackplus.TravelersBackpackPlus;
import keletu.travelersbackpackplus.gear.CompressorBlockEntity;
import keletu.travelersbackpackplus.menu.CompressorMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public final class CompressorScreen extends AbstractContainerScreen<CompressorMenu> {
    private static final Identifier TEXTURE = TravelersBackpackPlus.id("textures/gui/air_compressor.png");
    private static final Identifier AIR = TravelersBackpackPlus.id("textures/gui/air.png");

    public CompressorScreen(CompressorMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, 176, 166);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, leftPos, topPos, 0, 0, 176, 166, 256, 256);
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        if (menu.burn() > 0 && menu.fuel() > 0) {
            int flame = Math.clamp(menu.burn() * 13L / menu.fuel(), 0, 13);
            graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, 78, 60 - flame, 176, 12 - flame, 14, flame + 1, 256, 256);
        }
        int height = Math.clamp(menu.air() / Math.max(1, CompressorBlockEntity.CAPACITY / 72), 0, 72);
        if (height > 0) {
            int frame = minecraft.level == null ? 0 : (int) (minecraft.level.getGameTime() / 4 % 8);
            graphics.blit(RenderPipelines.GUI_TEXTURED, AIR, 8, 80 - height, 0, frame * 16, 16, height, 16, 16, 16, 128);
        }
        Component air = Component.translatable("gui.travelersbackpackplus.scuba.air", menu.air(), CompressorBlockEntity.CAPACITY);
        graphics.text(font, title, labelX(title), 6, 0xFF373737, false);
        graphics.text(font, air, labelX(air), 16, 0xFF373737, false);
    }

    private int labelX(Component text) {
        return Math.max(52, Math.min(65, 169 - font.width(text)));
    }
}
