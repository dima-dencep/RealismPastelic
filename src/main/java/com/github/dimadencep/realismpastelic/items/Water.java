package com.github.dimadencep.realismpastelic.items;

import com.github.dimadencep.realismpastelic.capability.CapabilityRegistry;
import com.github.dimadencep.realismpastelic.utils.Utils;
import com.github.dimadencep.realismpastelic.events.EventHandler;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

public class Water extends Item {
    public final boolean energy;

    public Water(boolean energy) {
        super(new Item.Settings().group(Utils.MOD_GROUP).setNoRepair());

        this.energy = energy;
    }

    public Water() {
        this(false);
    }

    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        PlayerEntity lv = user instanceof PlayerEntity ? (PlayerEntity) user : null;

        if (lv instanceof ServerPlayerEntity) {
            Criteria.CONSUME_ITEM.trigger((ServerPlayerEntity) lv, stack);

            CapabilityRegistry.PlayerData.get(lv).ifPresent(data -> {
                if (data.waterLevel < 0) data.waterLevel = 0;

                data.waterLevel += 6;

                if (data.waterLevel > 20) data.waterLevel = 20;
            });

            if (energy) {
                lv.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 300, 0, false, false, true));
            }

            EventHandler.sync(lv);
        }

        stack.decrement(1);

        return stack;
    }

    public int getMaxUseTime(ItemStack stack) {
        return 32;
    }

    public UseAction getUseAction(ItemStack stack) {
        return UseAction.DRINK;
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return ItemUsage.consumeHeldItem(world, user, hand);
    }
}