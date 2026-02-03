package com.d97.exo_mod.abilitys.exojump.system;

import com.d97.exo_mod.abilitys.exojump.ExoJumpComponent;
import com.hypixel.hytale.component.AddReason;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.component.Holder;
import com.hypixel.hytale.component.RemoveReason;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.HolderSystem;
import com.hypixel.hytale.server.core.modules.entity.AllLegacyLivingEntityTypesQuery;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;
// Applied Entity Verification
public final class ExoJumpSetupSystem extends HolderSystem<EntityStore> {

    private final ComponentType<EntityStore, ExoJumpComponent> componentType;

    public ExoJumpSetupSystem(ComponentType<EntityStore, ExoJumpComponent> componentType) {
        this.componentType = componentType;
    }
    @Nonnull
    @Override
    public Query<EntityStore> getQuery() {
        return AllLegacyLivingEntityTypesQuery.INSTANCE;
    }
    @Override
    public void onEntityAdd(@Nonnull Holder<EntityStore> holder,
                            @Nonnull AddReason reason,
                            @Nonnull Store<EntityStore> store) {
        if (holder.getComponent(componentType) == null) {
            holder.ensureAndGetComponent(componentType);
        }
    }
    @Override
    public void onEntityRemoved(@Nonnull Holder<EntityStore> holder,
                                @Nonnull RemoveReason reason,
                                @Nonnull Store<EntityStore> store) {
    }
}