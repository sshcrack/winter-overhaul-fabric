package me.sshcrack.winteroverhaul.renderer.armor.skates;

import me.sshcrack.winteroverhaul.items.SkateItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

import static me.sshcrack.winteroverhaul.WinterOverhaul.ref;

public class SkatesModel extends GeoModel<SkateItem> {
    @Override
    public ResourceLocation getModelResource(SkateItem skateItem) {
        return ref("geo/skates.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SkateItem skateItem) {
        return ref("textures/entity/skates/base.png");
    }

    @Override
    public ResourceLocation getAnimationResource(SkateItem skateItem) {
        return ref("animations/empty.animation.json");
    }
}
