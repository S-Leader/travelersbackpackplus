package keletu.travelersbackpackplus.client;

import com.tiviacz.travelersbackpack.client.screens.AnimatedTank;
import com.tiviacz.travelersbackpack.client.screens.HudOverlay;
import com.tiviacz.travelersbackpack.component.RenderInfo;
import com.tiviacz.travelersbackpack.config.TravelersBackpackConfig;
import com.tiviacz.travelersbackpack.init.ModDataComponents;
import com.tiviacz.travelersbackpack.item.HoseItem;
import keletu.travelersbackpackplus.gear.GearItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

public final class GearTankHud {
    private static final AnimatedTank TANK = new AnimatedTank();

    public record Tank(FluidStack fluid, int capacity) {
    }

    public static Tank data(ItemStack stack) {
        if (!(stack.getItem() instanceof GearItem item) || item.type().tankCount() != 1) return null;
        RenderInfo info = stack.getOrDefault(ModDataComponents.RENDER_INFO, RenderInfo.EMPTY);
        return new Tank(info.getLeftFluidStack(), item.type().tankCapacity());
    }

    public static boolean render(ItemStack stack, Minecraft client, GuiGraphicsExtractor graphics) {
        if (stack.getItem() instanceof GearItem item && item.type().tankCount() == 0) return true;
        Tank tank = data(stack);
        if (tank == null) return false;
        if (client == null || client.getWindow() == null) return true;
        int x = client.getWindow().getGuiScaledWidth() - TravelersBackpackConfig.CLIENT.overlay.offsetX.get();
        int y = client.getWindow().getGuiScaledHeight() - TravelersBackpackConfig.CLIENT.overlay.offsetY.get();
        TANK.render(graphics, tank.fluid(), tank.fluid().getAmount(), tank.capacity(), x + 1, y, 21, 8);
        boolean selected = client.player != null && client.player.getMainHandItem().getItem() instanceof HoseItem;
        graphics.blit(RenderPipelines.GUI_TEXTURED, HudOverlay.OVERLAY, x, y, selected ? 0 : 10, 0, 10, 23, 256, 256);
        return true;
    }
}
