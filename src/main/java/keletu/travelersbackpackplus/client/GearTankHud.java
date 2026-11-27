package keletu.travelersbackpackplus.client;

import com.tiviacz.travelersbackpack.client.screens.AnimatedTank;
import com.tiviacz.travelersbackpack.client.screens.HudOverlay;
import com.tiviacz.travelersbackpack.component.RenderInfo;
import com.tiviacz.travelersbackpack.config.TravelersBackpackConfig;
import com.tiviacz.travelersbackpack.init.ModDataComponents;
import com.tiviacz.travelersbackpack.item.HoseItem;
import keletu.travelersbackpackplus.GearRegistry;
import keletu.travelersbackpackplus.gear.GearItem;
import keletu.travelersbackpackplus.gear.GearState;
import keletu.travelersbackpackplus.gear.GearType;
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
        if (!(stack.getItem() instanceof GearItem item) || item.type() == GearType.FLAMETHROWER) return null;
        if (item.type() == GearType.SCUBA)
            return new Tank(new FluidStack(GearRegistry.AIR.get(), new GearState(stack).air()), 6000);
        RenderInfo info = stack.getOrDefault(ModDataComponents.RENDER_INFO, RenderInfo.EMPTY);
        return new Tank(info.getLeftFluidStack(), item.type().tankCapacity());
    }

    public static boolean render(ItemStack stack, Minecraft client, GuiGraphicsExtractor graphics) {
        Tank tank = data(stack);
        if (tank == null) return false;
        if (client == null || client.getWindow() == null) return true;
        int x = client.getWindow().getGuiScaledWidth() - TravelersBackpackConfig.CLIENT.overlay.offsetX.get();
        int y = client.getWindow().getGuiScaledHeight() - TravelersBackpackConfig.CLIENT.overlay.offsetY.get();
        TANK.render(graphics, tank.fluid(), tank.fluid().getAmount(), tank.capacity(), x + 1, y, 21, 8);
        boolean selected = client.player != null && client.player.getMainHandItem().getItem() instanceof HoseItem && HoseItem.getHoseTank(client.player.getMainHandItem()) == 1;
        graphics.blit(RenderPipelines.GUI_TEXTURED, HudOverlay.OVERLAY, x, y, selected ? 0 : 10, 0, 10, 23, 256, 256);
        return true;
    }
}
