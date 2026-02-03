package com.d97.exo_mod.abilitys.healing;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatMap;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatValue;
import com.hypixel.hytale.server.core.modules.entitystats.asset.DefaultEntityStatTypes;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.util.EventTitleUtil;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class UpdateHealthComand extends AbstractPlayerCommand {

    public UpdateHealthComand(@NonNullDecl String name, @NonNullDecl String description, boolean requiresConfirmation) {
        super(name, description, requiresConfirmation);
    }

    @Override
    protected void execute(
            @NonNullDecl CommandContext commandContext,
            @NonNullDecl Store<EntityStore> store,
            @NonNullDecl Ref<EntityStore> ref,
            @NonNullDecl PlayerRef playerRef,
            @NonNullDecl World world
    ) {
        // Player stats
        EntityStatMap statMap = store.getComponent(ref, EntityStatMap.getComponentType());
        if (statMap == null) {
            playerRef.sendMessage(Message.raw("No EntityStatMap was found on the player."));
            return;
        }

        // Health index
        int healthIndex = DefaultEntityStatTypes.getHealth();
        if (healthIndex == Integer.MIN_VALUE) {
            playerRef.sendMessage(Message.raw("Invalid health index (DefaultEntityStatTypes has not been initialized)."));
            return;
        }

        EntityStatValue health = statMap.get(healthIndex);
        if (health == null) {
            playerRef.sendMessage(Message.raw("The Health stat could not be found for this player."));
            return;
        }
        // before stat
        float before = health.get();
        float maxBefore = health.getMax();
        // health
        statMap.maximizeStatValue(healthIndex);
        // after stat
        EntityStatValue healthAfter = statMap.get(healthIndex);
        float after = healthAfter != null ? healthAfter.get() : before;
        float maxAfter = healthAfter != null ? healthAfter.getMax() : maxBefore;
        // Show event
        EventTitleUtil.showEventTitleToPlayer(
                playerRef,
                Message.raw(String.format("Before: %.1f / %.1f  ->  After: %.1f / %.1f", before, maxBefore, after, maxAfter)),
                Message.raw("Health"),
                true
        );
    }
}