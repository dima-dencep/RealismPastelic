package com.github.dimadencep.realismpastelic.events;

import com.github.dimadencep.realismpastelic.MainMod;
import com.github.dimadencep.realismpastelic.capability.CapabilityRegistry;
import com.github.dimadencep.realismpastelic.items.*;
import com.github.dimadencep.realismpastelic.packets.UpdateData;
import com.github.dimadencep.realismpastelic.utils.Utils;
import net.minecraft.block.BlockState;
import net.minecraft.block.CauldronBlock;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.EntityDamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.property.Properties;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.PacketDistributor;

// Очень крутой класс от dima_dencep:
//		К сожелению, я понял что надо разделять каждую функцию по классам слишком поздно, поэтому все тут
// Разработчики, приятного вам чтения...
public class EventHandler {
    @SubscribeEvent
    public void onItemUseFinish(LivingEntityUseItemEvent.Finish event) {
        if (event.getEntityLiving() instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) event.getEntityLiving();

            if (event.getItem().getItem() instanceof PotionItem) {
                CapabilityRegistry.PlayerData.get(player).ifPresent(data -> {
                    if (data.waterLevel < 0) data.waterLevel = 0;

                    data.waterLevel += 1.5;

                    if (data.waterLevel > 20) data.waterLevel = 20;
                });
            }

            if (event.getItem().isFood()) {
                if (Utils.isBad(event.getItem().getItem())) {
                    CapabilityRegistry.PlayerData.get(player).ifPresent(data -> {
                        int rand = player.getRandom().nextInt(10);

                        if (rand >= 4) {
                            // 60% chance
                        } else if (rand >= 1) {
                            if (!data.badStomach) {
                                data.badStomach = true;

                                player.sendMessage(new TranslatableText("messages.badStomach").formatted(Formatting.RED), true);
                            }
                        }
                    });
                }
            }

            sync(player);
        }
    }

    @SubscribeEvent
    public void onItemClick(PlayerInteractEvent.RightClickItem event) {
        if (event.getItemStack().getItem() == ModItems.SPIKE.get() || event.getItemStack().getItem() == ModItems.SPLINT.get()) {
            CapabilityRegistry.PlayerData.get(event.getPlayer()).ifPresent(data -> {
                if (data.brokenHand || data.brokenFoot) {
                    event.getPlayer().sendMessage(new LiteralText(MathHelper.ceil(((double) data.ticksFb / 20) * 100.0D) + "%").formatted(Formatting.YELLOW), true);

                    data.ticksFb++;
                } else {
                    data.ticksFb = 0;

                    event.getPlayer().sendMessage(new TranslatableText("messages.nothingbroke").formatted(Formatting.YELLOW), true);
                }

                if (data.ticksFb >= 20) {

                    if (data.brokenHand || data.brokenFoot) {
                        if (event.getItemStack().getItem() == ModItems.SPLINT.get()) {
                            data.brokenHand = false;
                        } else {
                            data.brokenFoot = false;
                        }

                        event.getItemStack().decrement(1);
                    }

                    data.ticksFb = 0;

                    event.getPlayer().sendMessage(new LiteralText(""), true);
                }

                sync(event.getPlayer());
            });
        }

        if (event.getItemStack().getItem() == ModItems.ACC.get()) {
            CapabilityRegistry.PlayerData.get(event.getPlayer()).ifPresent(data -> {

                if (data.hasDisease) {
                    data.hasDisease = false;

                    event.getItemStack().decrement(1);
                    event.getPlayer().sendMessage(new TranslatableText("messages.acc.used").formatted(Formatting.GREEN), true);

                    sync(event.getPlayer());
                }
            });
        }

        if (event.getItemStack().getItem() == ModItems.CARBON.get()) {
            CapabilityRegistry.PlayerData.get(event.getPlayer()).ifPresent(data -> {

                if (data.badStomach) {
                    data.badStomach = false;

                    event.getItemStack().decrement(1);
                    event.getPlayer().sendMessage(new TranslatableText("messages.carbon.used").formatted(Formatting.GREEN), true);

                    sync(event.getPlayer());
                }
            });
        }

        if (event.getItemStack().getItem() == ModItems.BLOOD_PACKET.get()) {
            CapabilityRegistry.PlayerData.get(event.getPlayer()).ifPresent(data -> {
                if (data.bloodLevel < 0) data.bloodLevel = 0;

                if (data.bloodLevel < 20) {
                    data.bloodLevel += 6;

                    event.getItemStack().decrement(1);
                    event.getPlayer().sendMessage(new TranslatableText("messages.bloodpack.used").formatted(Formatting.GREEN), true);
                }

                if (data.bloodLevel > 20) data.bloodLevel = 20;

                sync(event.getPlayer());
            });
        }

        if (event.getItemStack().getItem() instanceof BandageClear || event.getItemStack().getItem() == ModItems.SALINE.get()) {
            CapabilityRegistry.PlayerData.get(event.getPlayer()).ifPresent(data -> {
                if (data.bloodLeave) {
                    data.bloodLeave = false;

                    if (event.getItemStack().getItem() == ModItems.BANDAGE_CLEAR.get()) {
                        if (data.bloodLevel < 0) data.bloodLevel = 0;

                        data.bloodLevel += 8;
                        event.getPlayer().giveItemStack(ModItems.BANDAGE_BLOOD.get().getDefaultStack());

                        if (data.bloodLevel > 20) data.bloodLevel = 20;
                    }

                    event.getItemStack().decrement(1);
                }

                sync(event.getPlayer());
            });
        }

        if (event.getItemStack().getItem() instanceof Healer) {
            Healer healer = (Healer) event.getItemStack().getItem();

            CapabilityRegistry.PlayerData.get(event.getPlayer()).ifPresent(data -> {
                boolean isUsed = false;

                switch (healer.typeHealer) {

                    case RED: {
                        // if (event.getPlayer().getHealth() == event.getPlayer().getMaxHealth()) break;

                        isUsed = true;
                    }

                    case BLUE: {
                        // if (!data.brokenFoot && !data.brokenHand) break;

                        isUsed = true;
                        data.brokenFoot = data.brokenHand = false;
                    }

                    case YELLOW: {
                        // if (!data.badStomach && !data.hasDisease) break;

                        isUsed = true;
                        data.brokenFoot = data.brokenHand = data.badStomach = data.hasDisease = false;
                    }
                }

                if (isUsed) {
                    event.getPlayer().heal(10);
                    event.getPlayer().sendMessage(new TranslatableText("messages.healer.used").formatted(Formatting.GREEN), true);

                    event.getItemStack().decrement(1);
                }

                sync(event.getPlayer());
            });
        }
    }

    @SubscribeEvent
    public void onBlockClick(PlayerInteractEvent.RightClickBlock event) {
        BlockState clickedBlock = event.getWorld().getBlockState(event.getPos());

        if (event.getItemStack().getItem() instanceof BandageClear.BandageBlood && clickedBlock.getBlock() instanceof CauldronBlock) {
            int cauldronLevel = clickedBlock.get(Properties.LEVEL_3);

            if (cauldronLevel >= 1) {
                event.getItemStack().decrement(1);
                event.getPlayer().giveItemStack(ModItems.BANDAGE_CLEAR.get().getDefaultStack());

                ((CauldronBlock)clickedBlock.getBlock()).setLevel(event.getWorld(), event.getPos(), clickedBlock, cauldronLevel - 1);
            }
        }
    }

    @SubscribeEvent
    public void onEntityDamage(LivingDamageEvent event) {
        if (event.getSource().getAttacker() instanceof PlayerEntity) {
            CapabilityRegistry.PlayerData.get(event.getSource().getAttacker()).ifPresent(data -> {
                if (data.brokenHand) {
                    event.setCanceled(true);
                }
            });
        }

        if (event.getEntityLiving() instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) event.getEntityLiving();

            CapabilityRegistry.PlayerData.get(player).ifPresent(data -> {
                if (event.getSource() != DamageSource.STARVE && event.getSource() != DamageSource.DROWN && !event.getSource().name.equals("lowBlood") && !event.getSource().name.equals("lowWater")) {
                    int a = player.getRandom().nextInt(10);

                    if (a < 5) {
                        // 50% chance
                    } else if (a < 9) {
                        if (!data.bloodLeave) {
                            data.bloodLeave = true;

                            player.sendMessage(new TranslatableText("messages.bloodleave").formatted(Formatting.RED), true);
                        }
                    }
                }

                if (event.getSource() == DamageSource.FALL) {
                    int rand = player.getRandom().nextInt(10);

                    if (rand >= 4) {
                        // 60% chance
                    } else if (rand >= 1) {
                        if (!data.brokenFoot) {
                            data.brokenFoot = true;

                            player.sendMessage(new TranslatableText("messages.brokenFoot").formatted(Formatting.RED), true);
                        }
                    }
                }

                if (event.getSource().isProjectile() || event.getSource() instanceof EntityDamageSource) {
                    int rand = player.getRandom().nextInt(10);

                    if (rand >= 4) {
                        // 60% chance
                    } else if (rand >= 1) {
                        if (!data.brokenHand) {
                            data.brokenHand = true;

                            player.sendMessage(new TranslatableText("messages.brokenHand").formatted(Formatting.RED), true);
                        }
                    }

                }

                sync(player);
            });
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            if (event.player.isSpectator() || event.player.isCreative() || !event.player.isAlive()) return;

            CapabilityRegistry.PlayerData.get(event.player).ifPresent(data -> {
                if (!(event.player.getMainHandStack().getItem() == ModItems.SPIKE.get()) && !(event.player.getMainHandStack().getItem() == ModItems.SPLINT.get())) data.ticksFb = 0;

                data.ticksW++;
                data.ticksB++;
                data.ticksC++;

                if (data.brokenFoot || data.brokenHand) {
                    data.ticksF++;
                } else {
                    data.ticksF = 0;
                }

                if (data.bloodLevel <= 0) {
                    event.player.damage(Utils.damageBlood, 0.5F);
                }

                if (data.brokenFoot) {
                    if (event.player.isSprinting()) {
                        event.player.setSprinting(false);
                    }
                    event.player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 800, 3, false, false));
                }

                if ((data.hasDisease || data.badStomach) && event.player.isSprinting()) {
                    event.player.setSprinting(false);
                }

                if (data.ticksF >= 3000) {
                    if (data.brokenFoot || data.brokenHand) {
                        data.brokenHand = false;
                        data.brokenFoot = false;

                        event.player.sendMessage(new TranslatableText("messages.brokenfix"), true);
                    }

                    data.ticksF = 0;
                }

                if (data.ticksC >= 6000) {
                    int a = event.player.getRandom().nextInt(10);

                    if (a < 6) {
                        // 60% chance
                    } else if (a < 9) {
                        data.hasDisease = true;
                    }

                    data.ticksC = 0;
                }

                if (data.waterLevel <= 0) {
                    event.player.damage(Utils.damageWater, 0.2F);
                }

                if (data.ticksW >= 500) {
                    if (data.waterLevel >= 0 && data.waterLevel <= 20) {
                        data.waterLevel -= event.player.isSprinting() ? 1 : 0.5;
                    }

                    data.ticksW = 0;
                }

                if (data.ticksB >= 100) {
                    if (data.bloodLeave && data.bloodLevel >= 0 && data.bloodLevel <= 20) {
                        data.bloodLevel -= 0.5;
                    }

                    data.ticksB = 0;
                }

                if (data.altPressed || data.energyLevel < 20) data.ticksL++;

                if (data.ticksL >= 5) {
                    if (data.altPressed && data.energyLevel >= 1) {
                        data.energyLevel -= 1;

                        event.player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 6, event.player.getHungerManager().getFoodLevel() < 4 ? 4 : 1, false, false));
                    } else if (!data.altPressed && data.energyLevel < 20) {

                        data.energyLevel += 2;
                    }

                    data.ticksL = 0;
                }

                data.altPressed = false;

                sync(event.player);
            });
        }
    }

    @SubscribeEvent
    public void onPlayerClone(PlayerEvent.Clone event) {
        PlayerEntity old = event.getOriginal();
        PlayerEntity newP = event.getPlayer();

        CapabilityRegistry.PlayerData.get(old).ifPresent(oldPm -> {
            CapabilityRegistry.PlayerData.get(newP).ifPresent(cap -> {
                cap.waterLevel = oldPm.waterLevel;
                cap.bloodLevel = oldPm.bloodLevel;
                cap.bloodLeave = oldPm.bloodLeave;
                cap.brokenHand = oldPm.brokenHand;
                cap.brokenFoot = oldPm.brokenFoot;
                cap.badStomach = oldPm.badStomach;
                cap.hasDisease = oldPm.hasDisease;
                cap.energyLevel = oldPm.energyLevel;

                cap.ticksB = cap.ticksFb = cap.ticksF = cap.ticksW = cap.ticksC = 0;
            });
        });
    }

    @SubscribeEvent
    public void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        CapabilityRegistry.PlayerData.get(event.getPlayer()).ifPresent(newPm -> {
            newPm.waterLevel = newPm.bloodLevel = newPm.energyLevel = 20;
            newPm.bloodLeave = newPm.badStomach = newPm.brokenFoot = newPm.hasDisease = newPm.brokenHand = false;
            newPm.ticksB = newPm.ticksFb = newPm.ticksF = newPm.ticksW = newPm.ticksC = 0;
        });

        sync(event.getPlayer());
    }

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        sync(event.getPlayer());
    }

    @SubscribeEvent
    public void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        sync(event.getPlayer());
    }

    public static void sync(PlayerEntity player) {
        if (player instanceof ServerPlayerEntity) {
            CapabilityRegistry.PlayerData.get(player).ifPresent(data -> MainMod.getInstance().simpleChannel.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), new UpdateData(data)));
        } else {
            throw new IllegalStateException("player not server");
        }
    }
}
