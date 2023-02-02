package com.github.dimadencep.realismpastelic.utils;

import com.github.dimadencep.realismpastelic.items.ModItems;
import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.stream.Collectors;

public class Utils {
    public static final String modid = "realismpastelic";

    public static final Identifier CAP_ID = new Identifier(Utils.modid, Utils.modid);

    public static final ItemGroup MOD_GROUP = new ItemGroup(Utils.modid) {

        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModItems.BANDAGE_CLEAR.orElse(ModItems.BANDAGE_BLOOD.get()));
        }

    };

    public static final DamageSource damageBlood = new DamageSource("lowBlood");
    public static final DamageSource damageWater = new DamageSource("lowWater");

    public static final Identifier gui = new Identifier(Utils.modid, "textures/gui.png");

    public static boolean isBad(Item item) {
        if (item.getTranslationKey().contains("raw") ||
                item.getTranslationKey().endsWith(".rabbit") ||
                item.getTranslationKey().endsWith(".cod") ||
                item.getTranslationKey().endsWith(".salmon") ||
                item.getTranslationKey().endsWith(".tropical_fish") ||
                item.getTranslationKey().endsWith(".beef")) return true;

        if (item.getFoodComponent() == null) return false;

        for (StatusEffectInstance effect : item.getFoodComponent().getStatusEffects().stream().map(Pair::getFirst).collect(Collectors.toList())) {
            if (effect.getEffectType().getType() == StatusEffectType.HARMFUL) {
                return true;
            }
        }

        return false;
    }
}
