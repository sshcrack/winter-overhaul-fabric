package me.sshcrack.winteroverhaul.renderer.entity.model;

import me.sshcrack.winteroverhaul.WinterOverhaul;
import me.sshcrack.winteroverhaul.entity.Robin;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class RobinModel extends GeoModel<Robin> {

    @Override
    public ResourceLocation getModelResource(Robin object) {
        return WinterOverhaul.ref("geo/robin.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Robin object) {
        return WinterOverhaul.ref("textures/entity/robin.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Robin animatable) {
        return WinterOverhaul.ref("animations/robin.animation.json");
    }

    @Override
    public void setCustomAnimations(Robin animatable, long instanceId, AnimationState<Robin> customPredicate) {
        super.setCustomAnimations(animatable, instanceId, customPredicate);

        if (customPredicate == null) return;

        EntityModelData modelData = customPredicate.getData(DataTickets.ENTITY_MODEL_DATA);

        var head = this.getAnimationProcessor().getBone("head");
        head.setRotX((modelData.headPitch() * ((float) Math.PI / 180F)) - 0.261799f);
        head.setRotY(modelData.netHeadYaw() * ((float) Math.PI / 180F));
    }
}
