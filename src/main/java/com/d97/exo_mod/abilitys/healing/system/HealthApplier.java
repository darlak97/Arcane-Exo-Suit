package com.d97.exo_mod.abilitys.healing.system;

import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatMap;
import com.hypixel.hytale.server.core.modules.entitystats.asset.DefaultEntityStatTypes;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;

//Class that APPLIES the actual healing to the stat.
public final class HealthApplier {

    public void heal(
            @Nonnull Ref<EntityStore> ref,
            @Nonnull EntityStatMap statMap,
            @Nonnull CommandBuffer<EntityStore> commandBuffer,
            float amount
    ) {
        if (amount <= 0f) return;

        int healthIndex = DefaultEntityStatTypes.getHealth();
        statMap.addStatValue(healthIndex, amount);
    }
}