package me.sshcrack.winteroverhaul;

import me.sshcrack.winteroverhaul.registry.*;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WinterOverhaul implements ModInitializer {
    public static final String MOD_ID = "winteroverhaul";

    public static ResourceLocation ref(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        ModParticles.initialize();
        ModArmorMaterials.initialize();
        ModSounds.initialize();
        ModEntities.initialize();
        ModItems.initialize();
        ModEvents.initialize();
    }
}