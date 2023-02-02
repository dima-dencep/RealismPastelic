package com.github.dimadencep.realismpastelic.capability;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.math.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;

public class CapabilityRegistry {
    public static final CapabilityRegistry.Storage STORAGE = new CapabilityRegistry.Storage();

    @CapabilityInject(PlayerData.class)
    public static Capability<PlayerData> INSTANCE;

    public static void register() {
        CapabilityManager.INSTANCE.register(PlayerData.class, STORAGE, PlayerData::new);
    }

    public static final class Storage implements Capability.IStorage<PlayerData> {
        @Nonnull
        @Override
        public NbtElement writeNBT(Capability<PlayerData> capability, PlayerData instance, Direction side) {
            NbtCompound nbt = new NbtCompound();

            nbt.putInt("waterLevel",instance.waterLevel);
            nbt.putInt("bloodLevel", instance.bloodLevel);
            nbt.putInt("energyLevel", instance.energyLevel);

            nbt.putBoolean("bloodLeave", instance.bloodLeave);

            nbt.putBoolean("brokenFoot", instance.brokenFoot);
            nbt.putBoolean("brokenHand", instance.brokenHand);
            nbt.putBoolean("badStomach", instance.badStomach);
            nbt.putBoolean("hasDisease", instance.hasDisease);

            nbt.putInt("ticksL",instance.ticksL);
            nbt.putInt("ticksW", instance.ticksW);
            nbt.putInt("ticksB", instance.ticksB);
            nbt.putInt("ticksC",instance.ticksC);
            nbt.putInt("ticksF", instance.ticksF);
            nbt.putInt("ticksFb", instance.ticksFb);

            return nbt;
        }

        @Override
        public void readNBT(Capability<PlayerData> capability, PlayerData instance, Direction side, NbtElement nbt) {
            if (!(nbt instanceof NbtCompound)) return;


            NbtCompound compound = (NbtCompound) nbt;

            instance.waterLevel = compound.getInt("waterLevel");
            instance.bloodLevel = compound.getInt("bloodLevel");
            instance.energyLevel = compound.getInt("energyLevel");

            instance.bloodLeave = compound.getBoolean("bloodLeave");

            instance.brokenFoot = compound.getBoolean("brokenFoot");
            instance.brokenHand = compound.getBoolean("brokenHand");
            instance.badStomach = compound.getBoolean("badStomach");
            instance.hasDisease = compound.getBoolean("hasDisease");

            instance.ticksL = compound.getInt("ticksL");
            instance.ticksW = compound.getInt("ticksW");
            instance.ticksB = compound.getInt("ticksB");
            instance.ticksC = compound.getInt("ticksC");
            instance.ticksF = compound.getInt("ticksF");
            instance.ticksFb = compound.getInt("ticksFb");
        }
    }

    public static class PlayerData {
        public int waterLevel = 20;
        public int bloodLevel = 20;
        public int energyLevel = 20;

        public boolean bloodLeave = false;

        public boolean brokenFoot = false;
        public boolean brokenHand = false;
        public boolean badStomach = false;
        public boolean hasDisease = false;

        public int ticksL = 0;
        public int ticksW = 0;
        public int ticksB = 0;
        public int ticksC = 0;
        public int ticksF = 0;
        public int ticksFb = 0;

        public boolean altPressed = false;

        public static LazyOptional<PlayerData> get(Entity entity) {
            return entity.getCapability(CapabilityRegistry.INSTANCE);
        }
    }
}