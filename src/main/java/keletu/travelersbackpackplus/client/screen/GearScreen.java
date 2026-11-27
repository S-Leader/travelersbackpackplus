package keletu.travelersbackpackplus.client.screen;

import com.tiviacz.travelersbackpack.client.screens.AbstractBackpackScreen;
import com.tiviacz.travelersbackpack.handlers.ModClientEventHandler;
import keletu.travelersbackpackplus.TravelersBackpackPlus;
import keletu.travelersbackpackplus.client.GearClient;
import keletu.travelersbackpackplus.client.screen.widget.BackgroundComposer;
import keletu.travelersbackpackplus.client.screen.widget.ClassicSkins;
import keletu.travelersbackpackplus.client.screen.widget.WidgetSkins;
import keletu.travelersbackpackplus.gear.GearType;
import keletu.travelersbackpackplus.menu.GearMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public final class GearScreen extends AbstractContainerScreen<GearMenu> {
    private static final Identifier WIDGETS = TravelersBackpackPlus.id("textures/gui/gui_backpack_widgets.png");
    private static final Identifier BUCKET = Identifier.withDefaultNamespace("textures/item/bucket.png");
    private static final int EQUIP_X = 157;
    private static final int EQUIP_Y = 71;
    private static final int EQUIP_SIZE = 12;
    private final int[] background;

    public GearScreen(GearMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, menu.layout.width(), menu.layout.height());
        ClassicSkins.install();
        background = BackgroundComposer.compose(menu.layout);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        for (int i = 0; i < background.length; i += BackgroundComposer.STRIDE) {
            int texture = background[i + BackgroundComposer.TEX];
            int size = WidgetSkins.textureSize(texture);
            graphics.blit(RenderPipelines.GUI_TEXTURED, WidgetSkins.textureAt(texture), leftPos + background[i], topPos + background[i + 1],
                    background[i + 2], background[i + 3], background[i + 4], background[i + 5], size, size, background[i + 7]);
        }
        var fluidInput = menu.getSlot(0);
        if (!fluidInput.hasItem()) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, BUCKET, leftPos + fluidInput.x, topPos + fluidInput.y,
                    0, 0, 16, 16, 16, 16, 0x66FFFFFF);
        }
        if (menu.showsEquipButton()) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, AbstractBackpackScreen.ICONS, leftPos + EQUIP_X + 1, topPos + EQUIP_Y + 1,
                    63, menu.equipped() ? 67 : 56, 10, 10, 256, 256);
            if (inside(mouseX, mouseY, EQUIP_X, EQUIP_Y, EQUIP_SIZE, EQUIP_SIZE)) {
                graphics.blit(RenderPipelines.GUI_TEXTURED, AbstractBackpackScreen.ICONS, leftPos + EQUIP_X, topPos + EQUIP_Y, 78, 82, EQUIP_SIZE, EQUIP_SIZE, 256, 256);
            }
        }
        if (menu.equipped() && menu.canControl() && menu.type != GearType.SCUBA) {
            face(graphics, 96, 61, 12, inside(mouseX, mouseY, 96, 61, 18, 12), menu.enabled());
            String label = text("toggle." + (menu.enabled() ? "on" : "off"));
            int shift = menu.enabled() ? 1 : 0;
            graphics.pose().pushMatrix();
            graphics.pose().translate(leftPos + 105 + shift, topPos + 64 + shift);
            graphics.pose().scale(0.75F, 0.75F);
            graphics.text(font, label, -font.width(label) / 2, 0, menu.enabled() ? 0xFF1E7A1E : 0xFF8A2020, false);
            graphics.pose().popMatrix();
        }
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        switch (menu.type) {
            case COPTER -> {
                tank(graphics, 27, menu.fluid(0), menu.fluid(0).getAmount(), menu.capacity(0), null);
                labels(graphics, fluidName(menu.fluid(0)), Integer.toString(menu.fluid(0).getAmount()), Integer.toString(menu.capacity(0)), "",
                        "Consumption: " + (menu.fluid(0).isEmpty() ? "0" : "1.0"));
            }
            case JETPACK -> {
                tank(graphics, 27, menu.fluid(0), menu.fluid(0).getAmount(), menu.capacity(0), null);
                tank(graphics, 151, FluidStack.EMPTY, menu.steam(), 12000, "steam");
                bar(graphics, 136, 7, 8, 64, menu.temperature() / 200F, 0xFFE0541E);
                bar(graphics, 72, 57, 6, 16, menu.burn() / (float) menu.burnTotal(), 0xFFFFA000);
                labels(graphics, fluidName(menu.fluid(0)) + "-" + menu.fluid(0).getAmount(),
                        (menu.steam() > 0 ? "Steam" : "None") + "-" + menu.steam(), menu.temperature() + " C");
            }
            case FLAMETHROWER -> {
                tank(graphics, 27, menu.fluid(0), menu.fluid(0).getAmount(), menu.capacity(0), null);
                tank(graphics, 151, menu.fluid(1), menu.fluid(1).getAmount(), menu.capacity(1), null);
                labels(graphics, text("flamethrower.fuel"), (menu.fluid(0).getAmount() + menu.fluid(1).getAmount()) + "/" + (menu.capacity(0) + menu.capacity(1)), "",
                        text("flamethrower." + (menu.enabled() ? "lit" : "out")));
            }
            case SCUBA -> {
                tank(graphics, 27, FluidStack.EMPTY, Math.min(menu.air(), 3000), 3000, "air");
                tank(graphics, 151, FluidStack.EMPTY, Math.max(0, menu.air() - 3000), 3000, "air");
                labels(graphics, text("scuba.tanks"), menu.air() + "/6000");
            }
        }
    }

    private static String fluidName(FluidStack fluid) {
        return fluid.isEmpty() ? "None" : fluid.getHoverName().getString();
    }

    private static String text(String key) {
        return Component.translatable("gui.travelersbackpackplus." + key).getString();
    }

    private void labels(GuiGraphicsExtractor graphics, String... labels) {
        int y = 10;
        for (String label : labels) {
            if (label.isEmpty()) {
                y += 10;
                continue;
            }
            for (var line : font.split(Component.literal(label), 64)) {
                graphics.text(font, line, 50, y, 0xFF373737, false);
                y += 10;
            }
        }
    }

    private void tank(GuiGraphicsExtractor graphics, int x, FluidStack fluid, int amount, int capacity, String gas) {
        int height = Math.clamp(amount / Math.max(1, capacity / 64), 0, 64);
        int top = 71 - height;
        if (height > 0) {
            if (gas != null) {
                int textureHeight = gas.equals("air") ? 128 : 16;
                int frame = gas.equals("air") && minecraft.level != null ? (int) (minecraft.level.getGameTime() / 4 % 8) : 0;
                graphics.blit(RenderPipelines.GUI_TEXTURED, TravelersBackpackPlus.id("textures/gui/" + gas + ".png"), x, top, 0, frame * 16, 16, height, 16, 16, 16, textureHeight);
            } else if (!fluid.isEmpty()) {
                var model = minecraft.getModelManager().getFluidStateModelSet().get(fluid.getFluid().defaultFluidState());
                var sprite = model.stillMaterial().sprite();
                int color = model.fluidTintSource() == null ? -1 : model.fluidTintSource().colorAsStack(fluid);
                int split = Math.min(top + 5, 71);
                graphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, x, top, 16, split - top, color);
                int shaded = color & 0xFF000000 | (int) ((color >> 16 & 255) * 0.9) << 16 | (int) ((color >> 8 & 255) * 0.9) << 8 | (int) ((color & 255) * 0.9);
                if (split < 71)
                    graphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, x, split, 16, 71 - split, shaded);
            }
            graphics.fill(x, top, x + 16, top + 1, 0x90FFFFFF);
        }
        graphics.fill(x + 13, 9, x + 14, 69, 0xD0FFFFFF);
    }

    private void bar(GuiGraphicsExtractor graphics, int x, int y, int width, int height, float fill, int color) {
        graphics.fill(x, y, x + width, y + height, 0xFF373737);
        graphics.fill(x + 1, y + 1, x + width - 1, y + height - 1, 0xFF555555);
        int filled = Math.round((height - 2) * Math.clamp(fill, 0, 1));
        if (filled == 0) return;
        int top = y + height - 1 - filled;
        graphics.fill(x + 1, top, x + width - 1, y + height - 1, color);
        graphics.fill(x + 1, top, x + width - 1, top + 1, 0xD0FFFFFF);
    }

    private void face(GuiGraphicsExtractor graphics, int x, int y, int height, boolean hover, boolean pressed) {
        int u = pressed ? 36 : hover ? 18 : 0;
        int upper = height / 2;
        int lower = height - upper;
        graphics.blit(RenderPipelines.GUI_TEXTURED, WIDGETS, leftPos + x, topPos + y, u, 0, 18, upper, 256, 256);
        graphics.blit(RenderPipelines.GUI_TEXTURED, WIDGETS, leftPos + x, topPos + y + upper, u, 18 - lower, 18, lower, 256, 256);
    }

    private boolean inside(double mouseX, double mouseY, int x, int y, int width, int height) {
        return mouseX >= leftPos + x && mouseX < leftPos + x + width && mouseY >= topPos + y && mouseY < topPos + y + height;
    }

    @Override
    protected void extractTooltip(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        super.extractTooltip(graphics, mouseX, mouseY);
        List<Component> tooltip = new ArrayList<>();
        if (menu.showsEquipButton() && inside(mouseX, mouseY, EQUIP_X, EQUIP_Y, EQUIP_SIZE, EQUIP_SIZE)) {
            tooltip.add(Component.translatable("gui.travelersbackpackplus.button." + (menu.equipped() ? "unequip" : "equip")));
        } else if (menu.equipped() && menu.canControl() && menu.type != GearType.SCUBA && inside(mouseX, mouseY, 96, 61, 18, 12)) {
            String kind = menu.type == GearType.COPTER ? "copter" : menu.type == GearType.JETPACK ? "jetpack" : "pilot";
            tooltip.add(Component.translatable("gui.travelersbackpackplus.toggle." + kind, text("toggle." + (menu.enabled() ? "stateOn" : "stateOff"))));
            tooltip.add(Component.translatable("gui.travelersbackpackplus.toggle.hint", GearClient.TOGGLE.getTranslatedKeyMessage()));
        } else {
            for (int i = 0; i < (menu.type == GearType.COPTER ? 1 : 2); i++) {
                if (!inside(mouseX, mouseY, i == 0 ? 27 : 151, 7, 16, 64)) continue;
                int amount;
                int capacity;
                String name;
                if (menu.type == GearType.SCUBA) {
                    amount = i == 0 ? Math.min(menu.air(), 3000) : Math.max(0, menu.air() - 3000);
                    capacity = 3000;
                    name = "Air";
                } else if (menu.type == GearType.JETPACK && i == 1) {
                    amount = menu.steam();
                    capacity = 12000;
                    name = "Steam";
                } else {
                    amount = menu.fluid(i).getAmount();
                    capacity = menu.capacity(i);
                    name = fluidName(menu.fluid(i));
                }
                tooltip.add(Component.literal(amount == 0 ? "None" : name));
                tooltip.add(Component.literal(amount == 0 ? "Empty" : amount + "/" + capacity));
            }
        }
        if (!tooltip.isEmpty()) graphics.setComponentTooltipForNextFrame(font, tooltip, mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        if (event.button() == 0 && minecraft.gameMode != null && menu.canControl()) {
            if (menu.showsEquipButton() && inside(event.x(), event.y(), EQUIP_X, EQUIP_Y, EQUIP_SIZE, EQUIP_SIZE)) {
                minecraft.gameMode.handleInventoryButtonClick(menu.containerId, 0);
                return true;
            }
            if (menu.equipped() && menu.type != GearType.SCUBA && inside(event.x(), event.y(), 96, 61, 18, 12)) {
                minecraft.gameMode.handleInventoryButtonClick(menu.containerId, 1);
                return true;
            }
        }
        return super.mouseClicked(event, doubleClick);
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        if (ModClientEventHandler.OPEN_BACKPACK.matches(event)) {
            onClose();
            return true;
        }
        if (menu.equipped() && menu.canControl() && menu.type != GearType.SCUBA && GearClient.TOGGLE.matches(event) && minecraft.gameMode != null) {
            minecraft.gameMode.handleInventoryButtonClick(menu.containerId, 1);
            return true;
        }
        return super.keyPressed(event);
    }
}
