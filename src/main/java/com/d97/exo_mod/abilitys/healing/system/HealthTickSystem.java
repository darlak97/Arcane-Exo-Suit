package com.d97.exo_mod.abilitys.healing.system;

import com.d97.exo_mod.exosuit.ExoSuitDetector;
import com.d97.exo_mod.abilitys.healing.state.RegenerationState;
import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.tick.EntityTickingSystem;
import com.hypixel.hytale.server.core.entity.EntityUtils;
import com.hypixel.hytale.server.core.entity.LivingEntity;
import com.hypixel.hytale.server.core.modules.entity.AllLegacyLivingEntityTypesQuery;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatMap;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;

//Gradual regeneration
public final class HealthTickSystem extends EntityTickingSystem<EntityStore> {

    private final RegenerationState state;
    private final OnlyHealth logic;
    private final HealthApplier applier;

    private final ExoSuitDetector detector = new ExoSuitDetector();

    public HealthTickSystem(@Nonnull RegenerationState state,
                            @Nonnull OnlyHealth logic,
                            @Nonnull HealthApplier applier) {
        this.state = state;
        this.logic = logic;
        this.applier = applier;
    }

    @Nonnull
    @Override
    public Query<EntityStore> getQuery() {
        //Living Entity + stats (to heal) (inventory comes from LivingEntity)
        return Query.and(
                AllLegacyLivingEntityTypesQuery.INSTANCE,
                EntityStatMap.getComponentType()
        );
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

        // Get LivingEntity to read inventory
        LivingEntity living = (LivingEntity) EntityUtils.getEntity(index, chunk);
        if (living == null) return;

        boolean equippedNow = detector.isExoSuitChestEquipped(living.getInventory(), "Exo_Suit_Chest_Wind");
        boolean enabledByCommand = state.isEnabled(ref);

        // Healing if: it has the chest plate OR it is activated by command
        if (!equippedNow && !enabledByCommand) return;

        EntityStatMap statMap = (EntityStatMap) chunk.getComponent(index, EntityStatMap.getComponentType());
        if (statMap == null) return;

        float amount = logic.computeHealAmount(statMap, dt);
        if (amount <= 0f) return;

        applier.heal(ref, statMap, commandBuffer, amount);
    }
}