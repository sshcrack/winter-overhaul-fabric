package me.sshcrack.winteroverhaul.items;

import me.sshcrack.winteroverhaul.registry.ModArmorMaterials;
import net.minecraft.world.item.ArmorItem;
import org.apache.commons.lang3.mutable.MutableObject;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.DeferredGeoRenderProvider;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public class SkateItem extends ArmorItem implements GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public static MutableObject<GeoRenderProvider> renderProvider = new MutableObject<>(null);

    public SkateItem(Properties builder) {
        super(ModArmorMaterials.SKATES_LEATHER, Type.BOOTS, builder);
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept((DeferredGeoRenderProvider) () -> renderProvider);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
