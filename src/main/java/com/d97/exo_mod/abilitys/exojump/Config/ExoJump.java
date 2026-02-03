package com.d97.exo_mod.abilitys.exojump.Config;

public class ExoJump {

    private final float cooldownSec = 2f;// Cooldown for each jump
    private final float pushMult = 4.0f;// Camera steering boost
    private final float jumpMult = 11.0f;// Vertical boost
    private final boolean airJumpEnabled = true;
    private final float playerMinSpeed = 7.0f;
    private final float airMult = 1.85f;
    // public floats
    public float getCooldownSec() {
        return cooldownSec;
    }
    public float getPushMult() {
        return pushMult;
    }
    public float getJumpMult() {
        return jumpMult;
    }
    public boolean isAirJumpEnabled() {
        return airJumpEnabled;
    }
    public float getPlayerMinSpeed() {
        return playerMinSpeed;
    }
    public float getAirMult() {
        return airMult;
    }
}

