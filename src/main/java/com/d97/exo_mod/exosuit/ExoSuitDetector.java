package com.d97.exo_mod.exosuit;

import com.hypixel.hytale.server.core.asset.type.item.config.Item;
import com.hypixel.hytale.server.core.inventory.Inventory;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.inventory.container.ItemContainer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Method;

public final class ExoSuitDetector {

    // IDs
    public static final String EXO_CHEST_ID = "Exo_Suit_Chest";
    public static final String EXO_CHEST_Wind_ID = "Exo_Suit_Chest_Wind";
    public static final String EXO_CHEST_Lighting_ID = "Exo_Suit_Chest_Lighting";

    public boolean isExoSuitChestEquipped(@Nullable Inventory inventory, @Nullable String itemVerify) {
        if (inventory == null) return false;

        ItemContainer armor = inventory.getArmor();
        if (armor == null) return false;

        short cap = armor.getCapacity();
        for (short i = 0; i < cap; i++) {
            ItemStack stack = armor.getItemStack(i);
            if (ItemStack.isEmpty(stack)) continue;

            Item item = stack.getItem();
            if (item == null) continue;

            String id = safeGetItemId(item);
            if (EXO_CHEST_ID.equals(id) && itemVerify == EXO_CHEST_ID) {
                return true;
            }
            if (EXO_CHEST_Wind_ID.equals(id) && itemVerify == EXO_CHEST_Wind_ID) {
                return true;
            }
            if (EXO_CHEST_Lighting_ID.equals(id) && itemVerify == EXO_CHEST_Lighting_ID) {
                return true;
            }
        }
        return false;
    }

    @Nullable
    private static String safeGetItemId(@Nonnull Item item) {
        String[] odds = { "getId", "getItemId", "getAssetId" };
        for (String name : odds) {
            try {
                Method m = item.getClass().getMethod(name);
                Object v = m.invoke(item);
                if (v instanceof String s && !s.isEmpty()) return s;
            } catch (Throwable ignored) {
            }
        }
        return null;
    }
}