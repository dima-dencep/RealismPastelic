package com.github.dimadencep.realismpastelic;

import com.github.dimadencep.realismpastelic.capability.CapabilityClientInject;
import com.github.dimadencep.realismpastelic.capability.CapabilityDataProvider;
import com.github.dimadencep.realismpastelic.capability.CapabilityRegistry;
import com.github.dimadencep.realismpastelic.events.ClientEvent;
import com.github.dimadencep.realismpastelic.items.ModItems;
import com.github.dimadencep.realismpastelic.utils.Utils;
import com.github.dimadencep.realismpastelic.packets.AltPressed;
import com.github.dimadencep.realismpastelic.packets.UpdateData;
import com.github.dimadencep.realismpastelic.events.EventHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;
import java.util.function.Supplier;

@Mod(Utils.modid)
public class MainMod {
    public final SimpleChannel simpleChannel = NetworkRegistry.newSimpleChannel(new Identifier(Utils.modid, "messages"), () -> "1", s -> true, s -> true);
    public static final Logger logger = LogManager.getFormatterLogger(Utils.modid);
    private static MainMod instance;

    public MainMod() {
        MainMod.logger.info("Developed by dima_dencep");
        MainMod.logger.info("Repo: https://github.com/TurboMC/RealismPastelic");

        if (instance == null) {
            MainMod.logger.info("Initialize mod...");

            instance = this;
        }

        this.simpleChannel.registerMessage(0, UpdateData.class, UpdateData::toBytes, UpdateData::new, this::onMessage, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        this.simpleChannel.registerMessage(1, AltPressed.class, AltPressed::toBytes, AltPressed::new, this::onMessage, Optional.of(NetworkDirection.PLAY_TO_SERVER));

        ModItems.register(FMLJavaModLoadingContext.get().getModEventBus());

        MinecraftForge.EVENT_BUS.register(this);
        if (FMLEnvironment.dist == Dist.DEDICATED_SERVER) {
            MinecraftForge.EVENT_BUS.register(new EventHandler());
        } else {
            MinecraftForge.EVENT_BUS.register(new ClientEvent());
        }
    }

    @SubscribeEvent
    public void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof PlayerEntity) event.addCapability(Utils.CAP_ID, new CapabilityDataProvider());
    }

    @SubscribeEvent
    public void setup(FMLCommonSetupEvent event) {
        CapabilityRegistry.register();
    }

    public void onMessage(Object packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();

        context.enqueueWork(() -> {
            if (packet instanceof AltPressed) {
                CapabilityRegistry.PlayerData.get(context.getSender()).ifPresent(data -> data.altPressed = true);
            } else if (packet instanceof UpdateData) {
                CapabilityClientInject.inject((UpdateData) packet);
            }

            context.setPacketHandled(true);
        });
    }

    public static MainMod getInstance() {
        return instance;
    }
}
