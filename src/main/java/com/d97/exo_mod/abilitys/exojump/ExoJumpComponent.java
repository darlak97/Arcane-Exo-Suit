package com.d97.exo_mod.abilitys.exojump;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;

public final class ExoJumpComponent implements Component<EntityStore> {

    public static final BuilderCodec<ExoJumpComponent> CODEC =
            BuilderCodec.builder(ExoJumpComponent.class, ExoJumpComponent::new)
                    .addField(new KeyedCodec<>("Cooldown", Codec.FLOAT),
                            (c, v) -> c.cooldown = v,
                            (c) -> c.cooldown)
                    .addField(new KeyedCodec<>("Charging", Codec.BOOLEAN),
                            (c, v) -> c.charging = v,
                            (c) -> c.charging)
                    .addField(new KeyedCodec<>("Used", Codec.BOOLEAN),
                            (c, v) -> c.used = v,
                            (c) -> c.used)
                    .build();

    private static ComponentType<EntityStore, ExoJumpComponent> COMPONENT_TYPE;

    public static void setComponentType(ComponentType<EntityStore, ExoJumpComponent> type) {
        COMPONENT_TYPE = type;
    }
    public static ComponentType<EntityStore, ExoJumpComponent> getComponentType() {
        return COMPONENT_TYPE;
    }

    private float cooldown = 0f;
    private boolean charging = false;
    private boolean used = false;

    public float getCooldown() {
        return cooldown;
    }
    public void setCooldown(float cooldown) {
        this.cooldown = cooldown;
    }
    public boolean isCharging() {
        return charging;
    }
    public void setCharging(boolean charging) {
        this.charging = charging;
    }
    public boolean isUsed() {
        return used;
    }
    public void setUsed(boolean used) {
        this.used = used;
    }
    @Nonnull
    @Override
    public Component<EntityStore> clone() {
        ExoJumpComponent c = new ExoJumpComponent();
        c.cooldown = this.cooldown;
        c.charging = this.charging;
        c.used = this.used;
        return c;
    }
}