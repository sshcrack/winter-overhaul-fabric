package me.sshcrack.winteroverhaul.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import me.sshcrack.winteroverhaul.entity.ReplacedSnowGolem;
import me.sshcrack.winteroverhaul.renderer.entity.layers.*;
import me.sshcrack.winteroverhaul.renderer.entity.model.ReplacedSnowGolemModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.SnowGolem;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoReplacedEntityRenderer;

public class ReplacedSnowGolemRenderer extends GeoReplacedEntityRenderer<SnowGolem, ReplacedSnowGolem> {


    public ReplacedSnowGolemRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ReplacedSnowGolemModel<>(), new ReplacedSnowGolem());

        addRenderLayer(new RandomButtonLayer(this));
        addRenderLayer(new RandomFaceLayer(this));
        addRenderLayer(new RandomArmLayer(this));
        addRenderLayer(new UpgradeLayer(this));
        addRenderLayer(new PumpkinLayer(this));
    }

    @Override
    public @Nullable RenderType getRenderType(ReplacedSnowGolem animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityTranslucent(texture);
    }

    @Override
    public void defaultRender(PoseStack poseStack, ReplacedSnowGolem animatable, MultiBufferSource bufferSource, @Nullable RenderType renderType, @Nullable VertexConsumer buffer, float yaw, float partialTick, int packedLight) {
        if (this.getCurrentEntity().isInvisible())
            return;

        super.defaultRender(poseStack, animatable, bufferSource, renderType, buffer, yaw, partialTick, packedLight);
    }
}
