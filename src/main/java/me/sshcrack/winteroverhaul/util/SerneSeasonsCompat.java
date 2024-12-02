package me.sshcrack.winteroverhaul.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import sereneseasons.season.SeasonHooks;

public class SerneSeasonsCompat {
    public static boolean isColdEnoughToSnow(Level level, BlockPos pos) {
        return SeasonHooks.coldEnoughToSnowSeasonal(level, pos);
    }
}
