package me.sshcrack.winteroverhaul.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import me.sshcrack.winteroverhaul.entity.ReplacedSnowGolem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.GeoReplacedEntityRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class PumpkinLayer extends GeoRenderLayer<ReplacedSnowGolem> {
    public PumpkinLayer(GeoRenderer<ReplacedSnowGolem> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void renderForBone(PoseStack stack, ReplacedSnowGolem animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        SnowGolem golem = ((GeoReplacedEntityRenderer<SnowGolem, ReplacedSnowGolem>) this.renderer).getCurrentEntity();
        if (!golem.hasPumpkin() || !bone.getName().equals("head"))
            return;

        Minecraft minecraft = Minecraft.getInstance();
        boolean flag = minecraft.shouldEntityAppearGlowing(golem) && golem.isInvisible();

        if (golem.isInvisible() && !minecraft.shouldEntityAppearGlowing(golem))
            return;

        stack.translate(0, 1.65, 0);
        stack.scale(0.625F, 0.625F, 0.625F);
        if (flag) {
            BlockState blockstate = Blocks.CARVED_PUMPKIN.defaultBlockState();
            BlockRenderDispatcher blockRenderer = minecraft.getBlockRenderer();
            BakedModel bakedmodel = blockRenderer.getBlockModel(blockstate);

            int i = LivingEntityRenderer.getOverlayCoords(golem, 0.0F);
            stack.translate(0.5D, 0.5D, 0.5D);

            blockRenderer
                    .getModelRenderer()
                    .renderModel(stack.last(), bufferSource.getBuffer(RenderType.outline(InventoryMenu.BLOCK_ATLAS)), blockstate, bakedmodel, 0.0F, 0.0F, 0.0F, packedLight, i);
        } else {
            minecraft.getItemRenderer().renderStatic(golem, new ItemStack(Blocks.CARVED_PUMPKIN), ItemDisplayContext.HEAD, false, stack, bufferSource, golem.level(), packedLight, LivingEntityRenderer.getOverlayCoords(golem, 0.0F), golem.getId());
        }
    }
}
