package com.github.dimadencep.realismpastelic.items;

import com.github.dimadencep.realismpastelic.utils.Utils;
import net.minecraft.item.Item;
import net.minecraft.util.Rarity;

public class Healer extends Item {
    public final TypeHealer typeHealer;

    public Healer(TypeHealer typeHealer) {
        super(new Item.Settings().group(Utils.MOD_GROUP).setNoRepair().rarity(typeHealer.rarity));

        this.typeHealer = typeHealer;
    }

    public enum TypeHealer {
        RED("red_healer", Rarity.EPIC),
        BLUE("blue_healer", Rarity.RARE),
        YELLOW("yellow_healer", Rarity.UNCOMMON);

        public final String name;
        public final Rarity rarity;

        TypeHealer(String name, Rarity rarity) {
            this.name = name;
            this.rarity = rarity;
        }
    }
}