package com.github.dimadencep.realismpastelic.items;

import com.github.dimadencep.realismpastelic.utils.Utils;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class BandageClear extends Item {
    public BandageClear() {
        super(new Item.Settings().group(Utils.MOD_GROUP).setNoRepair());
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> text, TooltipContext tooltip) {

        // TODO text.add(new TranslatableText("ла"));

        super.appendTooltip(stack, world, text, tooltip);
    }

    public static class BandageBlood extends Item {
        public BandageBlood() {
            super(new Item.Settings().group(Utils.MOD_GROUP).setNoRepair());
        }

        @Override
        public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> text, TooltipContext tooltip) {

            // TODO text.add(new TranslatableText("ла"));

            super.appendTooltip(stack, world, text, tooltip);
        }


    }
}