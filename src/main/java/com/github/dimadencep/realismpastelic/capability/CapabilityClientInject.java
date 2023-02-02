package com.github.dimadencep.realismpastelic.capability;

import com.github.dimadencep.realismpastelic.packets.UpdateData;
import net.minecraft.client.MinecraftClient;

public class CapabilityClientInject { // Костыль, по сути

    public static void inject(UpdateData updateData) {
        CapabilityRegistry.PlayerData.get(MinecraftClient.getInstance().player).ifPresent(cap -> {
            cap.waterLevel = updateData.waterLevel;
            cap.bloodLevel = updateData.bloodLevel;
            cap.bloodLeave = updateData.bloodLeave;
            cap.brokenHand = updateData.brokenHand;
            cap.brokenFoot = updateData.brokenFoot;
            cap.badStomach = updateData.badStomach;
            cap.hasDisease = updateData.hasDisease;
            cap.energyLevel = updateData.energyLevel;
        });
    }
}