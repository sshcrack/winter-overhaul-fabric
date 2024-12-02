package me.sshcrack.winteroverhaul.mixin;

import me.sshcrack.winteroverhaul.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = {AbstractSkeleton.class, Zombie.class})
public abstract class MixinAbstractSkeleton {
    @Inject(
            method="finalizeSpawn",
            at= @At(
                    value = "INVOKE",
                    target = "Ljava/time/LocalDate;now()Ljava/time/LocalDate;"
            )
    )
    private void winterOverhaul$addScarfAndHat(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, SpawnGroupData spawnGroupData, CallbackInfoReturnable<SpawnGroupData> cir) {
        Entity eEntity = Entity.class.cast(this);
        if(!(eEntity instanceof AbstractSkeleton entity))
            return;
        BlockPos pos = entity.getOnPos();


        Biome biome = level.getBiome(pos).value();
        if(!Utils.isColdEnoughToSnow(level.getLevel(), biome, pos))
            return;

        if (entity.getRandom().nextFloat() > 0.90f && entity.getRandom().nextFloat() > 0.5f){
            Item item = Utils.getRandomHatAndScarf(level.getRandom());
            entity.setItemSlot(EquipmentSlot.HEAD, new ItemStack(item));
        }
    }
}
