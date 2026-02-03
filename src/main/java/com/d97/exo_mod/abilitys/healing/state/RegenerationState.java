package com.d97.exo_mod.abilitys.healing.state;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class RegenerationState {

    private final Set<Ref<EntityStore>> enabledPlayers =
            ConcurrentHashMap.newKeySet();

    public void enable(@Nonnull Ref<EntityStore> ref) {
        enabledPlayers.add(ref);
    }
    public void disable(@Nonnull Ref<EntityStore> ref) {
        enabledPlayers.remove(ref);
    }
    public boolean isEnabled(@Nonnull Ref<EntityStore> ref) {
        return enabledPlayers.contains(ref);
    }
    public boolean enableFromPlayerRef(@Nonnull PlayerRef playerRef) {
        Ref<EntityStore> ref = playerRef.getReference();
        if (ref == null) return false;
        enable(ref);
        return true;
    }

    public boolean disableFromPlayerRef(@Nonnull PlayerRef playerRef) {
        Ref<EntityStore> ref = playerRef.getReference();
        if (ref == null) return false;
        disable(ref);
        return true;
    }
}
