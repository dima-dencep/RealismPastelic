package com.github.dimadencep.realismpastelic.packets;

import com.github.dimadencep.realismpastelic.capability.CapabilityRegistry;
import net.minecraft.network.PacketByteBuf;

public class UpdateData {
    public final int waterLevel;
    public final int bloodLevel;
    public final boolean bloodLeave;
    public final boolean brokenFoot;
    public final boolean brokenHand;
    public final boolean badStomach;
    public final boolean hasDisease;

    public final int energyLevel;


    public UpdateData(CapabilityRegistry.PlayerData playerData) {
        this.waterLevel = playerData.waterLevel;
        this.bloodLevel = playerData.bloodLevel;
        this.bloodLeave = playerData.bloodLeave;
        this.brokenHand = playerData.brokenHand;
        this.brokenFoot = playerData.brokenFoot;
        this.badStomach = playerData.badStomach;
        this.hasDisease = playerData.hasDisease;
        this.energyLevel = playerData.energyLevel;
    }

    public UpdateData(PacketByteBuf buf) {
        this.waterLevel = buf.readInt();
        this.bloodLevel = buf.readInt();
        this.energyLevel = buf.readInt();
        this.bloodLeave = buf.readBoolean();
        this.brokenHand = buf.readBoolean();
        this.brokenFoot = buf.readBoolean();
        this.badStomach = buf.readBoolean();
        this.hasDisease = buf.readBoolean();
    }

    public void toBytes(PacketByteBuf buf) {
        buf.writeInt(this.waterLevel);
        buf.writeInt(this.bloodLevel);
        buf.writeInt(this.energyLevel);
        buf.writeBoolean(this.bloodLeave);
        buf.writeBoolean(this.brokenHand);
        buf.writeBoolean(this.brokenFoot);
        buf.writeBoolean(this.badStomach);
        buf.writeBoolean(this.hasDisease);
    }
}
