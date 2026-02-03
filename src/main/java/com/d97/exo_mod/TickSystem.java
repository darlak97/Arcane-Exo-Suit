package com.d97.exo_mod;

import com.d97.exo_mod.abilitys.healing.state.RegenerationState;
import com.d97.exo_mod.abilitys.healing.system.HealthApplier;
import com.d97.exo_mod.abilitys.healing.system.OnlyHealth;
import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.tick.EntityTickingSystem;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatMap;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;

public final class TickSystem extends EntityTickingSystem<EntityStore> {

    private final RegenerationState state;
    private final OnlyHealth logic;
    private final HealthApplier applier;

    public TickSystem(@Nonnull RegenerationState state,
                                 @Nonnull OnlyHealth logic,
                                 @Nonnull HealthApplier applier) {
        this.state = state;
        this.logic = logic;
        this.applier = applier;
    }

    @Nonnull
    @Override
    public Query<EntityStore> getQuery() {
        return EntityStatMap.getComponentType();
    }
    @Override
    public boolean isParallel(int archetypeChunkSize, int taskCount) {
        return false;
    }

    @Override
    public void tick(float dt,
                     int index,
                     @Nonnull ArchetypeChunk<EntityStore> chunk,
                     @Nonnull Store<EntityStore> store,
                     @Nonnull CommandBuffer<EntityStore> commandBuffer) {

        Ref<EntityStore> ref = chunk.getReferenceTo(index);
        if (ref == null || !ref.isValid()) return;

        // Only if the boolean is active
        if (!state.isEnabled(ref)) return;

        EntityStatMap statMap = (EntityStatMap) chunk.getComponent(index, EntityStatMap.getComponentType());
        if (statMap == null) return;

        float healAmount = logic.computeHealAmount(statMap, dt);
        if (healAmount <= 0f) return;

        applier.heal(ref, statMap, commandBuffer, healAmount);
    }
}