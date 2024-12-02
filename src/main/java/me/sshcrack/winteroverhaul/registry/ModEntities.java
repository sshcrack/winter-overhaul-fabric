package me.sshcrack.winteroverhaul.registry;

import me.sshcrack.winteroverhaul.entity.Robin;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import static me.sshcrack.winteroverhaul.WinterOverhaul.ref;

public class ModEntities {
    public static final EntityType<Robin> ROBIN = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            ref("robin"),
            EntityType.Builder.of(Robin::new, MobCategory.CREATURE)
                    .sized(0.5f, 0.9f)
                    .build()
    );

    public static void initialize() {
        //noinspection DataFlowIssue
        FabricDefaultAttributeRegistry.register(ROBIN, Robin.createAttributes());
    }
}
