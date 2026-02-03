package com.d97.exo_mod.abilitys.healing;


public class RegenerationMod {
    private final float regenSetComplete;
    private final float tickSeconds;
    private float accumulatedHeal = 0f;

    public RegenerationMod(float regenSetComplete, float tickSeconds) {
        this.regenSetComplete = regenSetComplete;
        this.tickSeconds = tickSeconds;
    }

    public float calculateHeal(int piecesEquipped,
                               float currentHealth,
                               float maxHealth) {
        // Check exo_Suit
        if (piecesEquipped < 2) {
            accumulatedHeal = 0f;
            return 0f;
        }
        if (currentHealth >= maxHealth) {
            accumulatedHeal = 0f;
            return 0f;
        }
        float healThisTick = regenSetComplete * tickSeconds;
        accumulatedHeal += healThisTick;

        if (accumulatedHeal >= 0.5f) {
            float heal = 0.5f;
            accumulatedHeal -= heal;
            return Math.min(heal, maxHealth - currentHealth);
        }
        return 0f;
    }
}