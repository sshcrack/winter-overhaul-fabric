package me.sshcrack.winteroverhaul.renderer.armor.cosmetics;

import me.sshcrack.winteroverhaul.items.GolemUpgradeItem;
import software.bernie.geckolib.renderer.GeoArmorRenderer;


public class CosmeticsRenderer extends GeoArmorRenderer<GolemUpgradeItem> {
    public CosmeticsRenderer() {
        super(new CosmeticModel());
    }
}
