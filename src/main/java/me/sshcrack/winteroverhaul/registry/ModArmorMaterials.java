package me.sshcrack.winteroverhaul.registry;

import me.sshcrack.winteroverhaul.WinterOverhaul;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class ModArmorMaterials {
    public static Holder<ArmorMaterial> registerMaterial(String id, Map<ArmorItem.Type, Integer> defense, int enchantmentValue, Holder<SoundEvent> equipSound, Supplier<Ingredient> repairIngredientSupplier, float toughness, float knockbackResistance, boolean dyeable) {
        // Get the supported layers for the armor material
        List<ArmorMaterial.Layer> layers = List.of(
                new ArmorMaterial.Layer(WinterOverhaul.ref(id), "", dyeable)
        );

        ArmorMaterial material = new ArmorMaterial(defense, enchantmentValue, equipSound, repairIngredientSupplier, layers, toughness, knockbackResistance);
        // Register the material within the ArmorMaterials registry.
        material = Registry.register(BuiltInRegistries.ARMOR_MATERIAL, WinterOverhaul.ref(id), material);

        // The majority of the time, you'll want the RegistryEntry of the material - especially for the ArmorItem constructor.
        return Holder.direct(material);
    }

    public static Holder<ArmorMaterial> registerMaterial(String id, Map<ArmorItem.Type, Integer> defense, int enchantmentValue, Holder<SoundEvent> equipSound, Supplier<Ingredient> repairIngredientSupplier, float toughness, float knockbackResistance, List<ArmorMaterial.Layer> layers, boolean dyeable) {
        ArmorMaterial material = new ArmorMaterial(defense, enchantmentValue, equipSound, repairIngredientSupplier, layers, toughness, knockbackResistance);
        material = Registry.register(BuiltInRegistries.ARMOR_MATERIAL, WinterOverhaul.ref(id), material);

        return Holder.direct(material);
    }

    public static final Holder<ArmorMaterial> GOLEM_UPGRADE = registerMaterial(
            "golemupgrade",
            Util.make(new EnumMap<>(ArmorItem.Type.class), enumMap -> {
                enumMap.put(ArmorItem.Type.BOOTS, 0);
                enumMap.put(ArmorItem.Type.LEGGINGS, 0);
                enumMap.put(ArmorItem.Type.CHESTPLATE, 0);
                enumMap.put(ArmorItem.Type.HELMET, 0);
                enumMap.put(ArmorItem.Type.BODY, 0);
            }), 0,
            SoundEvents.ARMOR_EQUIP_LEATHER,
            () -> Ingredient.EMPTY,
            0, 0, false
    );


    public static final Holder<ArmorMaterial> SKATES_LEATHER = registerMaterial(
            "skates_leather",
            Util.make(new EnumMap<>(ArmorItem.Type.class), enumMap -> {
                enumMap.put(ArmorItem.Type.BOOTS, 1);
                enumMap.put(ArmorItem.Type.LEGGINGS, 2);
                enumMap.put(ArmorItem.Type.CHESTPLATE, 3);
                enumMap.put(ArmorItem.Type.HELMET, 1);
                enumMap.put(ArmorItem.Type.BODY, 3);
            }),
            15,
            SoundEvents.ARMOR_EQUIP_LEATHER,
            () -> Ingredient.of(Items.LEATHER),
            0.0F,
            0.0F,
            List.of(
                    new ArmorMaterial.Layer(WinterOverhaul.ref("skates/base"), "", true),
                    new ArmorMaterial.Layer(WinterOverhaul.ref("skates/overlay"), "", false)
            ),
            true
    );

    public static void initialize() {
    }
}
