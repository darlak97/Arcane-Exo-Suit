package com.d97.exo_mod.abilitys.exojump.system;

import com.d97.exo_mod.abilitys.exojump.ExoJumpComponent;
import com.d97.exo_mod.abilitys.exojump.Config.ExoJump;
import com.d97.exo_mod.exosuit.ExoSuitDetector;
import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.tick.EntityTickingSystem;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.protocol.ChangeVelocityType;
import com.hypixel.hytale.protocol.MovementStates;
import com.hypixel.hytale.protocol.SoundCategory;
import com.hypixel.hytale.server.core.asset.type.soundevent.config.SoundEvent;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.entity.movement.MovementStatesComponent;
import com.hypixel.hytale.server.core.modules.entity.EntityModule;
import com.hypixel.hytale.server.core.modules.entity.component.HeadRotation;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.modules.physics.component.Velocity;
import com.hypixel.hytale.server.core.modules.splitvelocity.VelocityConfig;
import com.hypixel.hytale.server.core.universe.world.SoundUtil;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;

public final class ExoJumpSystem extends EntityTickingSystem<EntityStore> {

    private final ExoJump config;
    private final ExoSuitDetector detector = new ExoSuitDetector();

    public ExoJumpSystem(@Nonnull ExoJump config) {
        this.config = config;
    }
    @Nonnull
    @Override
    public Query<EntityStore> getQuery() {
        return Query.and(
                Player.getComponentType(),
                MovementStatesComponent.getComponentType(),
                Velocity.getComponentType(),
                HeadRotation.getComponentType(),
                ExoJumpComponent.getComponentType()
        );
    }

    @Override
    public boolean isParallel(int archetypeChunkSize, int taskCount) {
        return false;
    }
    @Override
    public void tick(
            float dt,
            int index,
            @Nonnull ArchetypeChunk<EntityStore> chunk,
            @Nonnull Store<EntityStore> store,
            @Nonnull CommandBuffer<EntityStore> commandBuffer
    ) {
        Player player = chunk.getComponent(index, Player.getComponentType());
        if (player == null) return;

        // Detect exo-suit
        if (!detector.isExoSuitChestEquipped(player.getInventory(), "Exo_Suit_Chest_Lighting")) {
            // Check if it has it
            ExoJumpComponent comp = chunk.getComponent(index, ExoJumpComponent.getComponentType());
            if (comp != null) {
                comp.setCharging(false);
                comp.setUsed(false);
            }
            return;
        }

        ExoJumpComponent comp = chunk.getComponent(index, ExoJumpComponent.getComponentType());
        if (comp == null) return;

        // cooldown tick-down
        if (comp.getCooldown() > 0f) {
            comp.setCooldown(Math.max(0f, comp.getCooldown() - dt));
        }

        MovementStatesComponent msc = chunk.getComponent(index, MovementStatesComponent.getComponentType());
        Velocity velocity = chunk.getComponent(index, Velocity.getComponentType());
        HeadRotation headRotation = chunk.getComponent(index, HeadRotation.getComponentType());
        if (msc == null || velocity == null || headRotation == null) return;

        MovementStates states = msc.getMovementStates();

        boolean crouchingOrSliding = states.crouching || states.sliding;
        boolean wantsJump = states.jumping;

        // Perform exo jump
        if (crouchingOrSliding && comp.getCooldown() <= 0f && !comp.isUsed()) {
            comp.setCharging(true);
        }

        // Perform a double jump
        if (comp.isCharging() && wantsJump && comp.getCooldown() <= 0f && !comp.isUsed()) {
            playSound(player);
            applyJumpVelocity(velocity, headRotation);
            comp.setCooldown(config.getCooldownSec());
            comp.setCharging(false);
            comp.setUsed(true);
            return;
        }

        // Check crouching
        if (!crouchingOrSliding) {
            comp.setUsed(false);
            comp.setCharging(false);
        }

        //Air Boost
        if (config.isAirJumpEnabled()) {
            applyAirSlideBoost(states, velocity, headRotation);
        }
    }
    private void playSound( Player player) {
        int index = SoundEvent.getAssetMap().getIndex("SFX_Exo_Suit_Jump");
        World world = player.getWorld();
        EntityStore store = world.getEntityStore();
        Ref<EntityStore> playerRef = player.getReference();
        world.execute(() -> {
            TransformComponent transform = store.getStore().getComponent(playerRef, EntityModule.get().getTransformComponentType());
            SoundUtil.playSoundEvent3dToPlayer(playerRef, index, SoundCategory.UI, transform.getPosition(), store.getStore());
            SoundUtil.playSoundEvent2d(index, SoundCategory.SFX, store.getStore());
        });
    }
    private void applyJumpVelocity(@Nonnull Velocity velocity, @Nonnull HeadRotation headRotation) {
        Vector3d dir = headRotation.getDirection();

        Vector3d horizontal = new Vector3d(dir.x, 0.0, dir.z);
        if (horizontal.length() > 0.0001) {
            horizontal.normalize();
        }

        Vector3d impulse = horizontal.scale(config.getPushMult());
        impulse.setY(impulse.y + config.getJumpMult());

        VelocityConfig velocityConfig = new VelocityConfig();
        velocity.addInstruction(impulse, velocityConfig, ChangeVelocityType.Add);
    }

    private void applyAirSlideBoost(@Nonnull MovementStates states,
                                    @Nonnull Velocity velocity,
                                    @Nonnull HeadRotation headRotation) {
        if (!states.sliding) return;

        Vector3d dir = headRotation.getDirection();
        Vector3d push = new Vector3d(dir.x, 0.0, dir.z);
        if (push.length() > 0.0001) {
            push.normalize();
        }
        Vector3d boost = push.scale(
                config.getPlayerMinSpeed() * (config.getAirMult() - 1.0f)
        );
        VelocityConfig velocityConfig = new VelocityConfig();
        velocity.addInstruction(boost, velocityConfig, ChangeVelocityType.Add);
    }
}