package me.sshcrack.winteroverhaul.entity;

import me.sshcrack.winteroverhaul.items.GolemUpgradeSlot;
import net.minecraft.world.item.ItemStack;

public interface IUpgradeAbleSnowGolem {

    /**
     * @param slot the slot in which to set the upgrade to.
     * @param stack the upgrade to put into the slot.
     * @return returns the upgrade that was in the slot already.
     */
    ItemStack winter_overhaul$setGolemUpgradeInSlot(GolemUpgradeSlot slot, ItemStack stack);

    /**
     * @param slot the slot in which to get the upgrade.
     * @return returns the upgrade that is in the slot.
     */
    ItemStack winter_overhaul$getGolemUpgradeInSlot(GolemUpgradeSlot slot);
}
