package com.github.dimadencep.realismpastelic.patch;

import com.github.dimadencep.realismpastelic.capability.CapabilityRegistry;
import com.github.dimadencep.realismpastelic.utils.Utils;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.gui.ForgeIngameGui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// Forge иди нахуй, я не знаю что такое эвенты
@Mixin(ForgeIngameGui.class)
public abstract class ForgeIngameMixin extends InGameHud {
    @Shadow protected abstract boolean pre(RenderGameOverlayEvent.ElementType type, MatrixStack mStack);
    @Shadow protected abstract void post(RenderGameOverlayEvent.ElementType type, MatrixStack mStack);
    @Shadow public static int left_height;

    @Shadow protected abstract void bind(Identifier res);

    @Shadow public static int right_height;

    public ForgeIngameMixin(MinecraftClient client) {
        super(client);
    }

    @Inject(
            method = "renderArmor",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/systems/RenderSystem;enableBlend()V"
            )
    )
    public void renderArmor(MatrixStack mStack, int width, int height, CallbackInfo callbackInfo) {
        CapabilityRegistry.PlayerData.get(client.player).ifPresent(data -> {
            this.bind(Utils.gui);

            RenderSystem.enableBlend();

            int left = width / 2 - 91;
            int top = height - left_height;

            for (int i = 1; i < 20; i += 2) {
                int y = top;

                if (data.bloodLevel <= 4 && this.ticks % (data.bloodLevel * 3 + 1) == 0) {
                    y += this.random.nextInt(2);
                }

                if (i < data.bloodLevel) {
                    this.drawTexture(mStack, left, y, 0, 10, 9, 8);
                } else if (i == data.bloodLevel) {
                    this.drawTexture(mStack, left, y, 10, 10, 9, 8);
                } else if (i > data.bloodLevel) {
                    this.drawTexture(mStack, left, y, 18, 10, 9, 8);
                }

                left += 8;
            }

            left_height += 10;

            RenderSystem.disableBlend();

            this.bind(GUI_ICONS_TEXTURE);
        });
    }

    @Inject(
            method = "renderFood",
            at = @At(
                value = "INVOKE",
                target = "Lcom/mojang/blaze3d/systems/RenderSystem;disableBlend()V"
            )
    )
    public void renderFood(int width, int height, MatrixStack mStack, CallbackInfo callbackInfo) {
        CapabilityRegistry.PlayerData.get(client.player).ifPresent(data -> {
            this.bind(Utils.gui);

            {
                int left = width / 2 + 89;
                int top = (height - right_height) - 1;
                right_height += 10;

                for(int i = 0; i < 10; ++i) {
                    int idx = i * 2 + 1;
                    int x = left - i * 8 - 8;
                    int y = top;

                    if (data.waterLevel <= 4 && this.ticks % (data.waterLevel * 3 + 1) == 0) {
                        y += this.random.nextInt(2);
                    }

                    if (idx < data.waterLevel) {
                        this.drawTexture(mStack, x, y, 0, 0, 9, 9);
                    } else if (idx == data.waterLevel) {
                        this.drawTexture(mStack, x, y, 10, 0, 9, 9);
                    } else if (idx > data.waterLevel) {
                        this.drawTexture(mStack, x, y, 18, 0, 9, 9);
                    }
                }
            }

            {
                int left = width / 2 + 89;
                int top = (height - right_height) - 1;
                right_height += 10;

                for(int i = 0; i < 10; ++i) {
                    int idx = i * 2 + 1;
                    int x = left - i * 8 - 8;
                    int y = top;

                    if (data.energyLevel <= 4 && this.ticks % (data.energyLevel * 3 + 1) == 0) {
                        y += this.random.nextInt(2);
                    }

                    if (idx < data.energyLevel) {
                        this.drawTexture(mStack, x, y, 0, 20, 9, 9);
                    } else if (idx == data.energyLevel) {
                        this.drawTexture(mStack, x, y, 10, 20, 9, 9);
                    } else if (idx > data.energyLevel) {
                        this.drawTexture(mStack, x, y, 18, 20, 9, 9);
                    }
                }
            }

            {
                int left = width - 35;

                int top = height - 35;

                if (data.bloodLeave) {
                    mStack.push();

                    this.drawTexture(mStack, left, top, 0, 30, 32, 35);

                    mStack.pop();
                    top -= 34;
                }

                if (data.hasDisease) {
                    mStack.push();

                    this.drawTexture(mStack, left, top, 32, 30, 32, 35);

                    mStack.pop();
                    top -= 34;
                }

                if (data.badStomach) {
                    mStack.push();

                    this.drawTexture(mStack, left, top, 64, 30, 32, 35);

                    mStack.pop();
                    top -= 34;
                }

                if (data.brokenFoot) {
                    mStack.push();

                    this.drawTexture(mStack, left, top, 96, 30, 32, 35);

                    mStack.pop();
                    top -= 34;
                }

                if (data.brokenHand) {
                    mStack.push();

                    this.drawTexture(mStack, left, top, 128, 30, 32, 35);

                    mStack.pop();
                    top -= 34;
                }
            }


            this.bind(GUI_ICONS_TEXTURE);
        });
    }
}