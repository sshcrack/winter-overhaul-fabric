package me.sshcrack.winteroverhaul.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import me.sshcrack.winteroverhaul.entity.ReplacedSnowGolem;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.SnowGolem;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.GeoReplacedEntityRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

import java.util.UUID;

import static me.sshcrack.winteroverhaul.WinterOverhaul.ref;

public class RandomArmLayer extends GeoRenderLayer<ReplacedSnowGolem> {

    private static final int SIZE = 10;

    private static final ResourceLocation[] TEXTURES = Util.make(() -> {
        ResourceLocation[] textures = new ResourceLocation[SIZE];
        for (int i = 0; i < SIZE; i++) {
            textures[i] = ref("textures/entity/arms/snow_golem_arms_" + (i + 1) + ".png");
        }
        return textures;
    });

    public RandomArmLayer(GeoRenderer<ReplacedSnowGolem> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack poseStack, ReplacedSnowGolem golem, BakedGeoModel bakedModel, @Nullable RenderType renderType, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        SnowGolem entity = ((GeoReplacedEntityRenderer<SnowGolem, ReplacedSnowGolem>) this.renderer).getCurrentEntity();
        if (entity.isInvisible()) return;
        BakedGeoModel normalModel = getDefaultBakedModel(null);
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucent(getRandomTexture(entity.getUUID())));
        getRenderer().reRender(normalModel, poseStack, bufferSource, golem, null, consumer, partialTick, packedLight, packedOverlay, 0xFFFFFFFF);
    }

    private ResourceLocation getRandomTexture(UUID uuid) {
        return TEXTURES[(int) (Math.abs(uuid.getLeastSignificantBits() ^ (uuid.getMostSignificantBits() | 0xFF00FFFFFFFFFFL)) % SIZE)];
    }
}
