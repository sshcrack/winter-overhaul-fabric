package me.sshcrack.winteroverhaul.registry;

import me.sshcrack.winteroverhaul.entity.IUpgradeAbleSnowGolem;
import me.sshcrack.winteroverhaul.items.GolemUpgradeSlot;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.biome.Biome;

public class ModEvents {
    public static void initialize() {
        UseEntityCallback.EVENT.register((player, level, hand, entity, res) -> {
            if (!(entity instanceof SnowGolem golem) || golem.hasPumpkin())
                return InteractionResult.PASS;

            if (!(golem instanceof IUpgradeAbleSnowGolem upgradable))
                return InteractionResult.PASS;

            ItemStack currStack = player.getItemInHand(hand);
            if (currStack.is(Items.GOLDEN_CARROT) || currStack.is(Items.CARROT)) {
                var oldItem = upgradable.winter_overhaul$getGolemUpgradeInSlot(GolemUpgradeSlot.FACE);
                if (!oldItem.isEmpty()) player.drop(oldItem, true);

                ItemStack newStack = currStack.copy();
                upgradable.winter_overhaul$setGolemUpgradeInSlot(GolemUpgradeSlot.FACE, newStack);

                if (!player.isCreative()) currStack.shrink(1);
                return InteractionResult.sidedSuccess(level.isClientSide());
            }

            return InteractionResult.PASS;
        });


        BiomeModifications.addSpawn(
                biome -> {
                    if(!BiomeSelectors.tag(BiomeTags.IS_TAIGA).test(biome))
                        return false;
                    // Was previously can snow but this is dynamic now so too bad
                    // Constants from getBiome
                    return biome.getBiome().getBaseTemperature() < 0.15F;
                },
                MobCategory.CREATURE,
                ModEntities.ROBIN,
                25,
                1,
                2
        );
    }
}
