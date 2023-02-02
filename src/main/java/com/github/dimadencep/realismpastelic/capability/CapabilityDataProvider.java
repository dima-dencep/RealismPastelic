package com.github.dimadencep.realismpastelic.capability;

import com.github.dimadencep.realismpastelic.utils.Utils;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.math.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

// Спизжено у лололошки 
public class CapabilityDataProvider implements ICapabilitySerializable<NbtElement> {
    private final CapabilityRegistry.PlayerData playerData = new CapabilityRegistry.PlayerData();


    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityRegistry.INSTANCE) return LazyOptional.of(() -> playerData).cast();

        return LazyOptional.empty();
    }

    @Override
    public NbtElement serializeNBT() {
        NbtCompound nbt = new NbtCompound();

        nbt.put(Utils.modid, CapabilityRegistry.STORAGE.writeNBT(null, playerData, null));

        return nbt;
    }

    @Override
    public void deserializeNBT(NbtElement nbt) {
        if (!(nbt instanceof NbtCompound)) return;

        NbtCompound compound = (NbtCompound) nbt;

        CapabilityRegistry.STORAGE.readNBT(null, playerData, null, compound.get(Utils.modid));
    }
}