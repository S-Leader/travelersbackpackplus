package keletu.travelersbackpackplus.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import keletu.travelersbackpackplus.gear.GearType;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import java.util.function.Consumer;

public final class GearItemRenderer implements SpecialModelRenderer<Void> {
    private final GearType type;

    public GearItemRenderer(GearType type) {
        this.type = type;
    }

    @Override
    public Void extractArgument(ItemStack stack) {
        return null;
    }

    @Override
    public void getExtents(Consumer<Vector3fc> output) {
        output.accept(new Vector3f(-0.5F, 0, 0));
        output.accept(new Vector3f(1.5F, 2.0F, 1.0F));
    }

    @Override
    public void submit(Void argument, PoseStack poses, SubmitNodeCollector collector, int light, int overlay, boolean foil, int outline) {
        poses.pushPose();
        poses.translate(0.5, 0.75, 0.75);
        poses.scale(1, -1, -1);
        GearRender.placed(type, poses, collector, light, overlay, outline);
        poses.popPose();
    }

    public record Unbaked(GearType gear) implements SpecialModelRenderer.Unbaked<Void> {
        public static final MapCodec<Unbaked> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Codec.STRING.xmap(GearType::valueOf, GearType::name).fieldOf("gear").forGetter(Unbaked::gear)).apply(instance, Unbaked::new));

        @Override
        public SpecialModelRenderer<Void> bake(BakingContext context) {
            return new GearItemRenderer(gear);
        }

        @Override
        public MapCodec<? extends SpecialModelRenderer.Unbaked<Void>> type() {
            return CODEC;
        }
    }
}
