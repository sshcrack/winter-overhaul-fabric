package me.sshcrack.winteroverhaul.util;

import me.sshcrack.winteroverhaul.registry.ModItems;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

public class Utils {
    public static boolean SERENE_INSTALLED = FabricLoader.getInstance().isModLoaded("serneseasons");

    public static boolean isColdEnoughToSnow(Level level, Biome biome, BlockPos pos) {
        if(SERENE_INSTALLED)
            return SerneSeasonsCompat.isColdEnoughToSnow(level, pos);
        return biome.coldEnoughToSnow(pos);
    }

    public static Item getRandomHatAndScarf(RandomSource random) {
        int randomInt = random.nextInt(8);
        return switch (randomInt) {
            case 0 -> ModItems.GREEN_HAT;
            case 1 -> ModItems.GREEN_SCARF;
            case 2 -> ModItems.YELLOW_HAT;
            case 3 -> ModItems.YELLOW_SCARF;
            case 4 -> ModItems.RED_HAT;
            case 5 -> ModItems.RED_SCARF;
            case 6 -> ModItems.CYAN_HAT;
            default -> ModItems.CYAN_SCARF;
        };
    }
}
