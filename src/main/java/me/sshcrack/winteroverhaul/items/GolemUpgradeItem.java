package me.sshcrack.winteroverhaul.items;

import me.sshcrack.winteroverhaul.WinterOverhaul;
import me.sshcrack.winteroverhaul.entity.IUpgradeAbleSnowGolem;
import me.sshcrack.winteroverhaul.registry.ModArmorMaterials;
import me.sshcrack.winteroverhaul.registry.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.mutable.MutableObject;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.DeferredGeoRenderProvider;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.function.Consumer;

public class GolemUpgradeItem extends ArmorItem implements GeoItem {
    public static MutableObject<GeoRenderProvider> renderProvider = new MutableObject<>(null);
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final GolemUpgradeSlot slot;

    public GolemUpgradeItem(GolemUpgradeSlot slot, Item.Properties pProperties) {
        super(ModArmorMaterials.GOLEM_UPGRADE, Type.HELMET, pProperties.stacksTo(1));
        this.slot = slot;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, Player pPlayer, @NotNull InteractionHand pHand) {
        ItemStack handItem = pPlayer.getItemInHand(pHand);
        EquipmentSlot equipmentSlot = pPlayer.getEquipmentSlotForItem(handItem);
        ItemStack itemInSlot = pPlayer.getItemBySlot(equipmentSlot);

        if (itemInSlot.isEmpty()) {
            pPlayer.setItemSlot(equipmentSlot, handItem.copy());
            if (!pLevel.isClientSide()) {
                pPlayer.awardStat(Stats.ITEM_USED.get(this));
            }

            handItem.setCount(0);
            return InteractionResultHolder.sidedSuccess(handItem, pLevel.isClientSide());
        } else {
            return InteractionResultHolder.pass(handItem);
        }
    }

    @SuppressWarnings("resource")
    @Override
    public @NotNull InteractionResult interactLivingEntity(@NotNull ItemStack stack, @NotNull Player player, @NotNull LivingEntity target, @NotNull InteractionHand hand) {
        if (!(target instanceof SnowGolem snowGolem) || snowGolem.hasPumpkin())
            return super.interactLivingEntity(stack, player, target, hand);
        if (!(snowGolem instanceof IUpgradeAbleSnowGolem upgradeAbleSnowGolem)) {
            WinterOverhaul.LOGGER.error("Snow Golem was not an instance of IUpgradeAbleSnowGolem so mixin error occurred!");
            return super.interactLivingEntity(stack, player, target, hand);
        }
        var oldHat = upgradeAbleSnowGolem.winter_overhaul$getGolemUpgradeInSlot(slot);
        if (!oldHat.isEmpty()) player.drop(oldHat, true);
        upgradeAbleSnowGolem.winter_overhaul$setGolemUpgradeInSlot(slot, stack.copy());
        stack.shrink(1);
        return InteractionResult.sidedSuccess(player.level().isClientSide);
    }

    @Override
    public @NotNull EquipmentSlot getEquipmentSlot() {
        return EquipmentSlot.HEAD;
    }

    public void tick(ItemStack stack, SnowGolem golem) {
        Item item = stack.getItem();
        if (!golem.hasEffect(MobEffects.FIRE_RESISTANCE)) {
            if (item.equals(ModItems.YELLOW_SCARF) || item.equals(ModItems.YELLOW_HAT)) {
                golem.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 20, 0, false, false));
            }
        }
        if (item.equals(ModItems.TOP_HAT) && golem.tickCount % 20 == 0) {
            golem.heal(0.5f);
        }
        if (item.equals(ModItems.TOP_HAT)) {
            MobEffectInstance effectInstance = golem.getEffect(MobEffects.HEALTH_BOOST);
            if (effectInstance == null || golem.tickCount % 5 == 0) {
                MobEffectInstance healthBoost = new MobEffectInstance(MobEffects.HEALTH_BOOST, 20, 0, false, false);
                if (effectInstance == null) golem.addEffect(healthBoost);
                else effectInstance.update(healthBoost);
            }
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept((DeferredGeoRenderProvider) () -> renderProvider);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
        tooltip.add(CommonComponents.EMPTY);
        tooltip.add(Component.translatable("item.winteroverhaul.upgrade.header").withStyle(ChatFormatting.GRAY));
        tooltip.add(getDesc().withStyle(ChatFormatting.BLUE));
    }

    public MutableComponent getDesc() {
        return Component.translatable(this.getDescriptionId() + ".desc");
    }

    @Override
    public @NotNull ItemAttributeModifiers getDefaultAttributeModifiers() {
        return ItemAttributeModifiers.EMPTY;
    }
}
