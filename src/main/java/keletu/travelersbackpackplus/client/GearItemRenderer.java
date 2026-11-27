package keletu.travelersbackpackplus.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.tiviacz.travelersbackpack.component.Fluids;
import keletu.travelersbackpackplus.gear.GearType;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import java.util.function.Consumer;

public final class GearItemRenderer implements SpecialModelRenderer<Fluids> {
    private final GearType type;

    public GearItemRenderer(GearType type) {
        this.type = type;
    }

    @Override
    public Fluids extractArgument(ItemStack stack) {
        return new Fluids(GearTankRender.fluid(stack), FluidStack.EMPTY);
    }

    @Override
    public void getExtents(Consumer<Vector3fc> output) {
        output.accept(new Vector3f(-0.5F, 0, 0));
        output.accept(new Vector3f(1.5F, 2.0F, 1.0F));
    }

    @Override
    public void submit(Fluids argument, PoseStack poses, SubmitNodeCollector collector, int light, int overlay, boolean foil, int outline) {
        poses.pushPose();
        poses.translate(0.5, 0.75, 0.75);
        poses.scale(1, -1, -1);
        GearRender.placed(type, argument == null ? FluidStack.EMPTY : argument.leftFluidStack(), poses, collector, light, overlay, outline);
        poses.popPose();
    }

    public record Unbaked(GearType gear) implements SpecialModelRenderer.Unbaked<Fluids> {
        public static final MapCodec<Unbaked> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Codec.STRING.xmap(GearType::valueOf, GearType::name).fieldOf("gear").forGetter(Unbaked::gear)).apply(instance, Unbaked::new));

        @Override
        public SpecialModelRenderer<Fluids> bake(BakingContext context) {
            return new GearItemRenderer(gear);
        }

        @Override
        public MapCodec<? extends SpecialModelRenderer.Unbaked<Fluids>> type() {
            return CODEC;
        }
    }
}
