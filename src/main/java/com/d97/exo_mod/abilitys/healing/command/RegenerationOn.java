package com.d97.exo_mod.abilitys.healing.command;

import com.d97.exo_mod.abilitys.healing.state.RegenerationState;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.util.EventTitleUtil;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public final class RegenerationOn extends AbstractPlayerCommand {

    private final RegenerationState state;

    public RegenerationOn(
            @NonNullDecl String name,
            @NonNullDecl String description,
            boolean requiresConfirmation,
            RegenerationState state
    ) {
        super(name, description, requiresConfirmation);
        this.state = state;
    }

    @Override
    protected void execute(
            @NonNullDecl CommandContext ctx,
            @NonNullDecl Store<EntityStore> store,
            @NonNullDecl Ref<EntityStore> ref,
            @NonNullDecl PlayerRef player,
            @NonNullDecl World world
    ) {
        boolean ok = state.enableFromPlayerRef(player);

        EventTitleUtil.showEventTitleToPlayer(
                player,
                Message.raw("Exo_Suit: Regeneration"),
                Message.raw(ok ? "Activated" : "Error"),
                true
        );
    }
}