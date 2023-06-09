package me.afterdarkness.moloch.event.events.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;
import net.spartanb312.base.event.EventCenter;

public class RenderEntityEvent extends EventCenter {
    public ModelBase modelBase;
    public Entity entityIn;
    public float limbSwing;
    public float limbSwingAmount;
    public float ageInTicks;
    public float netHeadYaw;
    public float headPitch;
    public float scale;



    public RenderEntityEvent(ModelBase modelBase, Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        this.modelBase = modelBase;
        this.entityIn = entityIn;
        this.limbSwing = limbSwing;
        this.limbSwingAmount = limbSwingAmount;
        this.ageInTicks = ageInTicks;
        this.netHeadYaw = netHeadYaw;
        this.headPitch = headPitch;
        this.scale = scale;
    }
}
