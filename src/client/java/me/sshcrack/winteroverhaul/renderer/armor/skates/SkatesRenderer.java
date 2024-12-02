package me.sshcrack.winteroverhaul.renderer.armor.skates;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import me.sshcrack.winteroverhaul.items.SkateItem;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.item.component.DyedItemColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.specialty.DyeableGeoArmorRenderer;
import software.bernie.geckolib.util.Color;

public class SkatesRenderer extends DyeableGeoArmorRenderer<SkateItem> {

    public SkatesRenderer() {
        super(new SkatesModel());
        addRenderLayer(new SkateOverlayLayer(this));
    }

    @Override
    public @Nullable GeoBone getRightLegBone(GeoModel<SkateItem> model) {
        return model.getBone("armorRightBoot").orElse(null);
    }


    @Override
    public @Nullable GeoBone getLeftLegBone(GeoModel<SkateItem> model) {
        return model.getBone("armorLeftBoot").orElse(null);
    }


    @Override
    protected @NotNull Color getColorForBone(GeoBone geoBone) {
        if (geoBone.getName().contains("Overlay"))
            return Color.WHITE;

        // From net/minecraft/client/renderer/entity/layers/HumanoidArmorLayer.class:69
        var color = FastColor.ARGB32.opaque(DyedItemColor.getOrDefault(currentStack, -6265536));
        return Color.ofOpaque(color);
    }

    @Override
    protected boolean isBoneDyeable(GeoBone geoBone) {
        String name = geoBone.getName().toLowerCase();
        return name.contains("boot") && name.contains("inner");
    }
}
