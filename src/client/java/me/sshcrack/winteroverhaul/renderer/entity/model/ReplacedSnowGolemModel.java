package me.sshcrack.winteroverhaul.renderer.entity.model;

import me.sshcrack.winteroverhaul.entity.ReplacedSnowGolem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

import static me.sshcrack.winteroverhaul.WinterOverhaul.ref;

public class ReplacedSnowGolemModel<E extends ReplacedSnowGolem> extends GeoModel<E> {

    private static final ResourceLocation BASE_TEXTURE = ref("textures/entity/snow_golem.png");
    private static final ResourceLocation MODEL = ref("geo/snow_golem.geo.json");
    private static final ResourceLocation ANIMATION = ref("animations/snow_golem.animation.json");

    @Override
    public ResourceLocation getModelResource(E e) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(E e) {
        return BASE_TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(E e) {
        return ANIMATION;
    }

    @Override
    public void setCustomAnimations(E animatable, long instanceId, AnimationState<E> customPredicate) {
        super.setCustomAnimations(animatable, instanceId, customPredicate);


        if (customPredicate == null) return;
        EntityModelData extraData = customPredicate.getData(DataTickets.ENTITY_MODEL_DATA);

        GeoBone head = this.getAnimationProcessor().getBone("head");
        head.setRotY(extraData.netHeadYaw() * ((float) Math.PI / 180F));
        head.setRotX(extraData.headPitch() * ((float) Math.PI / 180F));
        GeoBone upperBody = this.getAnimationProcessor().getBone("body_2");
        upperBody.setRotY(extraData.netHeadYaw() * ((float) Math.PI / 180F) * 0.25F);

//        float sinRotY = Mth.sin(upperBody.getRotationY());
//        float cosRotY = Mth.cos(upperBody.getRotationY());
//        IBone leftArm = this.getAnimationProcessor().getBone("left_arm");
//        IBone rightArm = this.getAnimationProcessor().getBone("right_arm");
//        leftArm.setRotationY(upperBody.getRotationY());
//        rightArm.setRotationY(upperBody.getRotationY() + (float)Math.PI);
//        leftArm.setPositionX(cosRotY * 5.0F);
//        leftArm.setPositionZ(-sinRotY * 5.0F);
//        rightArm.setPositionX(-cosRotY * 5.0F);
//        rightArm.setPositionZ(sinRotY * 5.0F);
    }
}
