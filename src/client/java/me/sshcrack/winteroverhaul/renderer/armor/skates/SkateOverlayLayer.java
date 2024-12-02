package me.sshcrack.winteroverhaul.renderer.armor.skates;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import me.sshcrack.winteroverhaul.items.SkateItem;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;
import software.bernie.geckolib.util.Color;

import static me.sshcrack.winteroverhaul.WinterOverhaul.ref;

public class SkateOverlayLayer extends GeoRenderLayer<SkateItem> {
    public static final ResourceLocation TEXTURE = ref("textures/entity/skates/overlay.png");

    public SkateOverlayLayer(GeoRenderer<SkateItem> entityRendererIn) {
        super(entityRendererIn);
    }


    @Override
    public void render(PoseStack poseStack, SkateItem animatable, BakedGeoModel bakedModel, @Nullable RenderType renderType, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        int color = ((SkatesRenderer) getRenderer()).getColorForBone(bakedModel.getBone("armorRightBoot").orElseThrow()).argbInt();
        getRenderer().reRender(getDefaultBakedModel(animatable), poseStack, bufferSource, animatable, renderType,
                buffer, partialTick, packedLight, packedOverlay, color);

        RenderType armorRenderType = RenderType.armorCutoutNoCull(TEXTURE);
        getRenderer().reRender(getDefaultBakedModel(animatable), poseStack, bufferSource, animatable, armorRenderType,
                bufferSource.getBuffer(armorRenderType), partialTick, packedLight, packedOverlay, Color.WHITE.argbInt());
    }
}
