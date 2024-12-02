package me.sshcrack.winteroverhaul.registry;

import me.sshcrack.winteroverhaul.WinterOverhaul;
import me.sshcrack.winteroverhaul.items.GolemUpgradeItem;
import me.sshcrack.winteroverhaul.items.GolemUpgradeSlot;
import me.sshcrack.winteroverhaul.items.SkateItem;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;

import static me.sshcrack.winteroverhaul.WinterOverhaul.ref;

public class ModItems {
    public static GolemUpgradeItem YELLOW_SCARF = register(new GolemUpgradeItem(GolemUpgradeSlot.SCARF, new Item.Properties()), "yellow_scarf");
    public static GolemUpgradeItem RED_SCARF = register(new GolemUpgradeItem(GolemUpgradeSlot.SCARF, new Item.Properties()), "red_scarf");
    public static GolemUpgradeItem CYAN_SCARF = register(new GolemUpgradeItem(GolemUpgradeSlot.SCARF, new Item.Properties()), "cyan_scarf");
    public static GolemUpgradeItem GREEN_SCARF = register(new GolemUpgradeItem(GolemUpgradeSlot.SCARF, new Item.Properties()), "green_scarf");

    public static GolemUpgradeItem YELLOW_HAT = register(new GolemUpgradeItem(GolemUpgradeSlot.HAT, new Item.Properties()), "yellow_winter_hat");
    public static GolemUpgradeItem RED_HAT = register(new GolemUpgradeItem(GolemUpgradeSlot.HAT, new Item.Properties()), "red_winter_hat");
    public static GolemUpgradeItem CYAN_HAT = register(new GolemUpgradeItem(GolemUpgradeSlot.HAT, new Item.Properties()), "cyan_winter_hat");
    public static GolemUpgradeItem GREEN_HAT = register(new GolemUpgradeItem(GolemUpgradeSlot.HAT, new Item.Properties()), "green_winter_hat");

    public static GolemUpgradeItem TOP_HAT = register(new GolemUpgradeItem(GolemUpgradeSlot.HAT, new Item.Properties()), "top_hat");

    public static SkateItem SKATES = register(new SkateItem(new Item.Properties().stacksTo(1)), "skates");

    public static final SpawnEggItem ROBIN_SPAWN_EGG = register(new SpawnEggItem(ModEntities.ROBIN, 0x57372F, 0xC96125, new Item.Properties()), "robin_spawn_egg");


    public static final ResourceKey<CreativeModeTab> TAB_KEY = ResourceKey.create(BuiltInRegistries.CREATIVE_MODE_TAB.key(), ref(WinterOverhaul.MOD_ID));
    public static final CreativeModeTab TAB_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(TOP_HAT))
            .title(Component.translatable("itemGroup.winteroverhaul"))
            .build();


    public static void initialize() {
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, TAB_KEY, TAB_GROUP);
        ItemGroupEvents.modifyEntriesEvent(TAB_KEY).register(itemGroup -> {
            itemGroup.accept(YELLOW_SCARF);
            itemGroup.accept(RED_SCARF);
            itemGroup.accept(CYAN_SCARF);
            itemGroup.accept(GREEN_SCARF);

            itemGroup.accept(YELLOW_HAT);
            itemGroup.accept(RED_HAT);
            itemGroup.accept(CYAN_HAT);
            itemGroup.accept(GREEN_HAT);
            itemGroup.accept(TOP_HAT);

            itemGroup.accept(ROBIN_SPAWN_EGG);
            itemGroup.accept(SKATES);
        });
    }

    public static <T extends Item> T register(T item, String id) {
        ResourceLocation itemID = WinterOverhaul.ref(id);
        return Registry.register(BuiltInRegistries.ITEM, itemID, item);
    }
}
