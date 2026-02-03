package com.d97.exo_mod.abilitys.healing.system;

import com.hypixel.hytale.server.core.modules.entitystats.EntityStatMap;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatValue;
import com.hypixel.hytale.server.core.modules.entitystats.asset.DefaultEntityStatTypes;

import javax.annotation.Nonnull;

public final class OnlyHealth {

    private final float healPerSecond;

    public OnlyHealth(float healPerSecond) {
        this.healPerSecond = healPerSecond;
    }

    public float computeHealAmount(@Nonnull EntityStatMap statMap, float dt) {
        int healthIndex = DefaultEntityStatTypes.getHealth();
        EntityStatValue health = statMap.get(healthIndex);

        if (health == null) return 0f;
        if (health.get() >= health.getMax()) return 0f;

        float amount = healPerSecond * dt;
        return Math.max(amount, 0f);
    }
}
