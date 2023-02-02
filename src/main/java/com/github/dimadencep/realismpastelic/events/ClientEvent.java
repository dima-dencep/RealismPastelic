package com.github.dimadencep.realismpastelic.events;

import com.github.dimadencep.realismpastelic.MainMod;
import com.github.dimadencep.realismpastelic.packets.AltPressed;
import com.github.dimadencep.realismpastelic.utils.Utils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = Utils.modid, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvent {
    public static final KeyBinding ALT = new KeyBinding("key." + Utils.modid + ".energyuse", GLFW.GLFW_KEY_LEFT_ALT, Utils.modid);

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent e) {
        if (e.phase == TickEvent.Phase.END && ALT.isPressed() && MinecraftClient.getInstance().getNetworkHandler() != null) {
            MainMod.getInstance().simpleChannel.sendToServer(new AltPressed());
        }
    }

    @SubscribeEvent
    public static void register(final FMLClientSetupEvent event) {
        ClientRegistry.registerKeyBinding(ALT);
    }
}
