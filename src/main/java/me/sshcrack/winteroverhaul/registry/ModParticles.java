package me.sshcrack.winteroverhaul.registry;

import me.sshcrack.winteroverhaul.WinterOverhaul;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;

public class ModParticles {
    public static final SimpleParticleType SNOWFLAKE_1 = FabricParticleTypes.simple(true);
    public static final SimpleParticleType SNOWFLAKE_2 = FabricParticleTypes.simple(true);
    public static final SimpleParticleType SNOWFLAKE_3 = FabricParticleTypes.simple(true);

    public static void initialize() {
        Registry.register(BuiltInRegistries.PARTICLE_TYPE, WinterOverhaul.ref("snowflake_1"), SNOWFLAKE_1);
        Registry.register(BuiltInRegistries.PARTICLE_TYPE, WinterOverhaul.ref("snowflake_2"), SNOWFLAKE_2);
        Registry.register(BuiltInRegistries.PARTICLE_TYPE, WinterOverhaul.ref("snowflake_3"), SNOWFLAKE_3);
    }
}
