package me.sshcrack.winteroverhaul.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import me.sshcrack.winteroverhaul.WinterOverhaul;
import me.sshcrack.winteroverhaul.entity.ReplacedSnowGolem;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.SnowGolem;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.GeoReplacedEntityRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

import java.util.UUID;

public class RandomButtonLayer extends GeoRenderLayer<ReplacedSnowGolem> {

    private static final int SIZE = 12;

    private static final ResourceLocation[] TEXTURES = Util.make(() -> {
        ResourceLocation[] textures = new ResourceLocation[SIZE];
        for (int i = 0; i < SIZE; i++) {
            textures[i] = WinterOverhaul.ref("textures/entity/buttons/snow_golem_buttons_" + (i + 1) + ".png");
        }
        return textures;
    });

    public RandomButtonLayer(GeoRenderer<ReplacedSnowGolem> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack poseStack, ReplacedSnowGolem animatable, BakedGeoModel bakedModel, @Nullable RenderType renderType, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        SnowGolem golem = ((GeoReplacedEntityRenderer<SnowGolem, ReplacedSnowGolem>) this.renderer).getCurrentEntity();
        if (golem.isInvisible()) return;

        BakedGeoModel normalModel = getDefaultBakedModel(null);
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucent(getRandomTexture(golem.getUUID())));

        getRenderer().reRender(normalModel, poseStack, bufferSource, animatable,
                null, consumer, partialTick,
                packedLight, renderer.getPackedOverlay(animatable, 0, partialTick),
                0xFFFFFFFF);
    }


    private ResourceLocation getRandomTexture(UUID uuid) {
        return TEXTURES[(int) (Math.abs(uuid.getLeastSignificantBits() ^ (uuid.getMostSignificantBits() | 0xFF00FFFFFFFFFFL)) % SIZE)];
    }
}
