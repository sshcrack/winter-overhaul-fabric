package me.sshcrack.winteroverhaul.renderer.armor.cosmetics;

import me.sshcrack.winteroverhaul.items.GolemUpgradeItem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

import static me.sshcrack.winteroverhaul.WinterOverhaul.ref;

public class CosmeticModel extends GeoModel<GolemUpgradeItem> {

    @Override
    public ResourceLocation getModelResource(GolemUpgradeItem item) {
        return ref("geo/cosmetics.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(GolemUpgradeItem item) {
        ResourceLocation location = BuiltInRegistries.ITEM.getKey(item);
        String path = location.getPath();

        return ref("textures/entity/upgrades/" + path + ".png");
    }

    @Override
    public ResourceLocation getAnimationResource(GolemUpgradeItem item) {
        return ref("animations/empty.animation.json");
    }
}
