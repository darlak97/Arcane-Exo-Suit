package com.d97.exo_mod;

import com.d97.exo_mod.abilitys.exojump.ExoJumpComponent;
import com.d97.exo_mod.abilitys.exojump.Config.ExoJump;
import com.d97.exo_mod.abilitys.exojump.system.ExoJumpSetupSystem;
import com.d97.exo_mod.abilitys.exojump.system.ExoJumpSystem;
import com.d97.exo_mod.abilitys.healing.command.RegenerationOff;
import com.d97.exo_mod.abilitys.healing.command.RegenerationOn;
import com.d97.exo_mod.abilitys.healing.state.RegenerationState;
import com.d97.exo_mod.abilitys.healing.system.HealthApplier;
import com.d97.exo_mod.abilitys.healing.system.HealthTickSystem;
import com.d97.exo_mod.abilitys.healing.system.OnlyHealth;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;

import javax.annotation.Nonnull;

public class D97_Plugin extends JavaPlugin {

    private final RegenerationState regenState = new RegenerationState();

    public D97_Plugin(@Nonnull JavaPluginInit init) {
        super(init);
    }

    @Override
    protected void setup() {
        super.setup();

        OnlyHealth logic = new OnlyHealth(0.4f);
        HealthApplier applier = new HealthApplier();
        this.getEntityStoreRegistry().registerSystem(new HealthTickSystem(regenState, logic, applier));

        this.getCommandRegistry().registerCommand(
                new RegenerationOn("exo_rege_true", "Active gradual regeneration", false, regenState)
        );
        this.getCommandRegistry().registerCommand(
                new RegenerationOff("exo_rege_false", "Deactivate gradual regeneration", false, regenState)
        );
        var jumpComponentType = this.getEntityStoreRegistry()
                .registerComponent(ExoJumpComponent.class, "ExoJump", ExoJumpComponent.CODEC);
        ExoJumpComponent.setComponentType(jumpComponentType);

        this.getEntityStoreRegistry().registerSystem(new ExoJumpSetupSystem(jumpComponentType));

        ExoJump jumpConfig = new ExoJump();
        this.getEntityStoreRegistry().registerSystem(new ExoJumpSystem(jumpConfig));
    }
}