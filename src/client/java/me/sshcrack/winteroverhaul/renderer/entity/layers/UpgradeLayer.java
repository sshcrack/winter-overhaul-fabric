package me.sshcrack.winteroverhaul.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import me.sshcrack.winteroverhaul.WinterOverhaul;
import me.sshcrack.winteroverhaul.entity.IUpgradeAbleSnowGolem;
import me.sshcrack.winteroverhaul.entity.ReplacedSnowGolem;
import me.sshcrack.winteroverhaul.items.GolemUpgradeSlot;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.GeoReplacedEntityRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

import java.util.Optional;

public class UpgradeLayer extends GeoRenderLayer<ReplacedSnowGolem> {

    public UpgradeLayer(GeoRenderer<ReplacedSnowGolem> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack poseStack, ReplacedSnowGolem animatable, BakedGeoModel bakedModel, @Nullable RenderType renderType, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        SnowGolem entity = ((GeoReplacedEntityRenderer<SnowGolem, ReplacedSnowGolem>) this.renderer).getCurrentEntity();
        if (entity.isInvisible()) return;
        if (entity instanceof IUpgradeAbleSnowGolem upgradeAbleSnowGolem) {
            BakedGeoModel normalModel = getDefaultBakedModel(null);

            ItemStack scarf = upgradeAbleSnowGolem.winter_overhaul$getGolemUpgradeInSlot(GolemUpgradeSlot.SCARF);
            getTexture(scarf).ifPresent(texture ->
                    getRenderer().reRender(normalModel, poseStack, bufferSource, animatable, null, bufferSource.getBuffer(RenderType.entityTranslucent(texture)),
                            partialTick, packedLight,
                            OverlayTexture.NO_OVERLAY, 0xFFFFFFFF));

            ItemStack face = upgradeAbleSnowGolem.winter_overhaul$getGolemUpgradeInSlot(GolemUpgradeSlot.FACE);
            getTexture(face).ifPresent(texture ->
                    getRenderer().reRender(normalModel, poseStack, bufferSource, animatable, null, bufferSource.getBuffer(RenderType.entityTranslucent(texture)),
                            partialTick, packedLight,
                            OverlayTexture.NO_OVERLAY, 0xFFFFFFFF));

            ItemStack hat = upgradeAbleSnowGolem.winter_overhaul$getGolemUpgradeInSlot(GolemUpgradeSlot.HAT);
            getTexture(hat).ifPresent(texture ->
                    getRenderer().reRender(normalModel, poseStack, bufferSource, animatable, null, bufferSource.getBuffer(RenderType.entityTranslucent(texture)),
                            partialTick, packedLight,
                            OverlayTexture.NO_OVERLAY, 0xFFFFFFFF));
        }
    }


    private static Optional<ResourceLocation> getTexture(ItemStack item) {
        if (item.isEmpty()) return Optional.empty();
        ResourceLocation itemPath = BuiltInRegistries.ITEM.getKey(item.getItem());

        //noinspection ConstantValue
        if (itemPath == null) return Optional.empty();
        return Optional.of(WinterOverhaul.ref("textures/entity/upgrades/" + itemPath.getPath() + ".png"));
    }
}
