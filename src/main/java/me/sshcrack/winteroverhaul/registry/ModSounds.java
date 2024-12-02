package me.sshcrack.winteroverhaul.registry;

import me.sshcrack.winteroverhaul.WinterOverhaul;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;

public class ModSounds {
    public static SoundEvent ROBIN_AMBIENT = Registry.register(
            BuiltInRegistries.SOUND_EVENT,
            WinterOverhaul.ref("entity.robin.ambient"),
            SoundEvent.createVariableRangeEvent(WinterOverhaul.ref("entity.robin.ambient"))
    );

    public static void initialize() {

    }
}
