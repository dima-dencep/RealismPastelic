package com.github.dimadencep.realismpastelic.items;

import com.github.dimadencep.realismpastelic.utils.Utils;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

// Спасибо, LexManos, за `DeferredRegister`
public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Utils.modid);

    public static final RegistryObject<Item> BANDAGE_CLEAR = ITEMS.register("bandage_clear", BandageClear::new);
    public static final RegistryObject<Item> BANDAGE_BLOOD = ITEMS.register("bandage_blood", BandageClear.BandageBlood::new);

    public static final RegistryObject<Item> SALINE = ITEMS.register("saline", () -> new Item(new Item.Settings().group(Utils.MOD_GROUP).setNoRepair()));

    public static final RegistryObject<Item> RED_HEALER = ITEMS.register("red_healer", () -> new Healer(Healer.TypeHealer.RED));
    public static final RegistryObject<Item> BLUE_HEALER = ITEMS.register("blue_healer", () -> new Healer(Healer.TypeHealer.BLUE));
    public static final RegistryObject<Item> YELLOW_HEALER = ITEMS.register("yellow_healer", () -> new Healer(Healer.TypeHealer.YELLOW));

    public static final RegistryObject<Item> BEANS_CONSERV = ITEMS.register("beans", () -> new Item(new Item.Settings().group(Utils.MOD_GROUP).setNoRepair().food(new FoodComponent.Builder().hunger(6).saturationModifier(0.6F).build())));
    public static final RegistryObject<Item> BECON_CONSERV = ITEMS.register("becon", () -> new Item(new Item.Settings().group(Utils.MOD_GROUP).setNoRepair().food(new FoodComponent.Builder().hunger(6).saturationModifier(0.6F).build())));
    public static final RegistryObject<Item> CHEES_BURGER = ITEMS.register("cheesburger", () -> new Item(new Item.Settings().group(Utils.MOD_GROUP).setNoRepair().food(new FoodComponent.Builder().hunger(6).saturationModifier(0.6F).build())));
    public static final RegistryObject<Item> DOUBLE_BURGER = ITEMS.register("double_burger", () -> new Item(new Item.Settings().group(Utils.MOD_GROUP).setNoRepair().food(new FoodComponent.Builder().hunger(6).saturationModifier(0.6F).build())));
    public static final RegistryObject<Item> HAM_BURGER = ITEMS.register("hamburger", () -> new Item(new Item.Settings().group(Utils.MOD_GROUP).setNoRepair().food(new FoodComponent.Builder().hunger(6).saturationModifier(0.6F).build())));
    public static final RegistryObject<Item> PERSIKI_CONSERV = ITEMS.register("persiki", () -> new Item(new Item.Settings().group(Utils.MOD_GROUP).setNoRepair().food(new FoodComponent.Builder().hunger(6).saturationModifier(0.6F).build())));
    public static final RegistryObject<Item> PORRIDGE = ITEMS.register("porridge", () -> new Item(new Item.Settings().group(Utils.MOD_GROUP).setNoRepair().food(new FoodComponent.Builder().hunger(6).saturationModifier(0.6F).build())));
    public static final RegistryObject<Item> POTATO_FREE = ITEMS.register("potato_free", () -> new Item(new Item.Settings().group(Utils.MOD_GROUP).setNoRepair().food(new FoodComponent.Builder().hunger(4).saturationModifier(0.6F).build())));
    public static final RegistryObject<Item> RISE_UNKNOWN = ITEMS.register("rise", () -> new Item(new Item.Settings().group(Utils.MOD_GROUP).setNoRepair().food(new FoodComponent.Builder().hunger(4).saturationModifier(0.6F).build())));
    public static final RegistryObject<Item> SPAGHETI_CONSERV = ITEMS.register("spagheti", () -> new Item(new Item.Settings().group(Utils.MOD_GROUP).setNoRepair().food(new FoodComponent.Builder().hunger(6).saturationModifier(0.6F).build())));
    public static final RegistryObject<Item> TUNEC_CONSERV = ITEMS.register("tunec", () -> new Item(new Item.Settings().group(Utils.MOD_GROUP).setNoRepair().food(new FoodComponent.Builder().hunger(6).saturationModifier(0.6F).build())));

    public static final RegistryObject<Item> COLA = ITEMS.register("cola", Water::new);
    public static final RegistryObject<Item> COLA_TWO = ITEMS.register("colatwo", Water::new);
    public static final RegistryObject<Item> ENERGY_DRINK = ITEMS.register("energy_drink", () -> new Water(true));
    public static final RegistryObject<Item> FANTA = ITEMS.register("fanta", Water::new);
    public static final RegistryObject<Item> KVASS = ITEMS.register("kvass", Water::new);
    public static final RegistryObject<Item> PEPSI = ITEMS.register("pepsi", Water::new);
    public static final RegistryObject<Item> SPRITE2 = ITEMS.register("sprite2", Water::new);
    public static final RegistryObject<Item> VODKA = ITEMS.register("vodka", Water::new);
    public static final RegistryObject<Item> WATER_BOTTLE = ITEMS.register("water_bottle", Water::new);

    public static final RegistryObject<Item> SPLINT = ITEMS.register("splint", () -> new Item(new Item.Settings().group(Utils.MOD_GROUP).setNoRepair()));
    public static final RegistryObject<Item> SPIKE = ITEMS.register("spike_new", () -> new Item(new Item.Settings().group(Utils.MOD_GROUP).setNoRepair()));

    public static final RegistryObject<Item> ACC = ITEMS.register("acc", () -> new Item(new Item.Settings().group(Utils.MOD_GROUP).setNoRepair()));
    public static final RegistryObject<Item> CARBON = ITEMS.register("activated_carbon", () -> new Item(new Item.Settings().group(Utils.MOD_GROUP).setNoRepair()));

    public static final RegistryObject<Item> BLOOD_PACKET = ITEMS.register("blood_packet", () -> new Item(new Item.Settings().group(Utils.MOD_GROUP).setNoRepair()));

    public static void register(IEventBus eventbus) {
        ITEMS.register(eventbus);
    }
}
