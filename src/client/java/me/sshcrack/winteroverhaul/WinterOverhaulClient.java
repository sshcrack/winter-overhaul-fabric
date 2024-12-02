package me.sshcrack.winteroverhaul;

import me.sshcrack.winteroverhaul.items.GolemUpgradeItem;
import me.sshcrack.winteroverhaul.items.SkateItem;
import me.sshcrack.winteroverhaul.particles.SnowflakeParticleProvider;
import me.sshcrack.winteroverhaul.registry.ModEntities;
import me.sshcrack.winteroverhaul.registry.ModItems;
import me.sshcrack.winteroverhaul.registry.ModParticles;
import me.sshcrack.winteroverhaul.renderer.armor.cosmetics.CosmeticsRenderer;
import me.sshcrack.winteroverhaul.renderer.armor.skates.SkatesRenderer;
import me.sshcrack.winteroverhaul.renderer.entity.ReplacedSnowGolemRenderer;
import me.sshcrack.winteroverhaul.renderer.entity.RobinRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.util.Color;

public class WinterOverhaulClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        SkateItem.renderProvider.setValue(new GeoRenderProvider() {
            private GeoArmorRenderer<?> renderer;

            @Override
            public <T extends LivingEntity> @NotNull HumanoidModel<?> getGeoArmorRenderer(@Nullable T livingEntity, ItemStack itemStack, @Nullable EquipmentSlot equipmentSlot, @Nullable HumanoidModel<T> original) {
                if (this.renderer == null)
                    this.renderer = new SkatesRenderer();

                return this.renderer;
            }
        });

        GolemUpgradeItem.renderProvider.setValue(new GeoRenderProvider() {
            private GeoArmorRenderer<?> renderer;

            @Override
            public <T extends LivingEntity> @NotNull HumanoidModel<?> getGeoArmorRenderer(@Nullable T livingEntity, ItemStack itemStack, @Nullable EquipmentSlot equipmentSlot, @Nullable HumanoidModel<T> original) {
                if (renderer == null)
                    renderer = new CosmeticsRenderer();

                return renderer;
            }
        });


        EntityRendererRegistry.register(ModEntities.ROBIN, RobinRenderer::new);
        EntityRendererRegistry.register(EntityType.SNOW_GOLEM, ReplacedSnowGolemRenderer::new);

        ParticleFactoryRegistry.getInstance().register(ModParticles.SNOWFLAKE_1, SnowflakeParticleProvider::new);
        ParticleFactoryRegistry.getInstance().register(ModParticles.SNOWFLAKE_2, SnowflakeParticleProvider::new);
        ParticleFactoryRegistry.getInstance().register(ModParticles.SNOWFLAKE_3, SnowflakeParticleProvider::new);
        ColorProviderRegistry.ITEM.register((itemStack, i) -> i == 1 ? Color.WHITE.argbInt() : DyedItemColor.getOrDefault(itemStack, -6265536), ModItems.SKATES);
    }
}