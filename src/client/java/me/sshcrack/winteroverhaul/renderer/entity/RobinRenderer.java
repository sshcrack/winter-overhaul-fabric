package me.sshcrack.winteroverhaul.renderer.entity;

import me.sshcrack.winteroverhaul.entity.Robin;
import me.sshcrack.winteroverhaul.renderer.entity.model.RobinModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class RobinRenderer extends GeoEntityRenderer<Robin> {
    public RobinRenderer(EntityRendererProvider.Context context) {
        super(context, new RobinModel());
    }
}
