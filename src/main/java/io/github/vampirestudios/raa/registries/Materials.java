package io.github.vampirestudios.raa.registries;

import io.github.vampirestudios.raa.RandomlyAddingAnything;
import io.github.vampirestudios.raa.api.enums.GeneratesIn;
import io.github.vampirestudios.raa.api.enums.OreTypes;
import io.github.vampirestudios.raa.blocks.LayeredOreBlock;
import io.github.vampirestudios.raa.client.Color;
import io.github.vampirestudios.raa.items.*;
import io.github.vampirestudios.raa.materials.Material;
import io.github.vampirestudios.raa.materials.MaterialBuilder;
import io.github.vampirestudios.raa.utils.Rands;
import io.github.vampirestudios.raa.utils.RegistryUtils;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.FoodComponents;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.DefaultedRegistry;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Materials {
    public static final List<Material> MATERIAL_LIST = new ArrayList<>();
    public static final Registry<Material> MATERIALS = new DefaultedRegistry<>("materials");

    public static boolean isReady = false;
    public static final int[] BASE_DURABILITY = new int[]{13, 15, 16, 11};

    public static void init() {
        for (int a = 0; a < RandomlyAddingAnything.CONFIG.materialNumber; a++) {
            Color RGB = new Color(Rands.randIntRange(0, 255),Rands.randIntRange(0, 255),Rands.randIntRange(0, 255));
            Random random = new Random();
            Material material = MaterialBuilder.create()
                    .oreType(Rands.values(OreTypes.values())).name(RandomlyAddingAnything.CONFIG.namingLanguage.generate()).color(RGB.getColor())
                    .generatesIn(Rands.values(GeneratesIn.values())).overlayTexture()
                    .resourceItemTexture().storageBlockTexture().armor((random.nextBoolean() && random.nextBoolean()))
                    .tools((random.nextBoolean() && random.nextBoolean())).oreFlower(random.nextBoolean() && random.nextBoolean())
                    .weapons((random.nextBoolean() && random.nextBoolean())).glowing((random.nextBoolean() && random.nextBoolean()))
                    .minXPAmount(0).maxXPAmount(Rands.randIntRange(0, 100)).oreClusterSize(Rands.randIntRange(4, 20))
                    .build();
            String id = material.getName().toLowerCase();
            for (Map.Entry<String, String> entry : RandomlyAddingAnything.CONFIG.namingLanguage.getCharMap().entrySet()) {
                id = id.replace(entry.getKey(), entry.getValue());
            }
            if (!MATERIALS.containsId(new Identifier(RandomlyAddingAnything.MOD_ID, id)))
                Registry.register(MATERIALS, new Identifier(RandomlyAddingAnything.MOD_ID, id), material);
            // Debug Only
            System.out.println("\nname : " + material.getName() +
                    "\noreType : " + material.getOreInformation().getOreType().name().toLowerCase() +
                    "\nRGB color : " + RGB.getRed() + "," + RGB.getGreen() + "," + RGB.getBlue() +
                    "\nGenerate in : " + material.getOreInformation().getGenerateIn().name().toLowerCase() +
                    "\nOverlay Texture : " + material.getOreInformation().getOverlayTexture().toString() +
                    "\nResource Item Texture : " + material.getResourceItemTexture().toString() +
                    "\nHas Armor : " + material.hasArmor() +
                    "\nHas Weapons : " + material.hasWeapons() +
                    "\nHas Tools : " + material.hasTools() +
                    "\nIs Glowing : " + material.isGlowing() +
                    "\nHas Ore Flower : " + material.hasOreFlower()
            );
        }
        isReady = true;
    }

    public static boolean isIsReady() {
        return isReady;
    }

    public static void createMaterialResources() {
        if (RandomlyAddingAnything.CONFIG.debug) RegistryUtils.registerItem(new RAADebugItem(), new Identifier(RandomlyAddingAnything.MOD_ID, "debug_stick"));
        MATERIALS.forEach(material -> {
            String id = material.getName().toLowerCase();
            for (Map.Entry<String, String> entry : RandomlyAddingAnything.CONFIG.namingLanguage.getCharMap().entrySet()) {
                id = id.replace(entry.getKey(), entry.getValue());
            }
            Item repairItem;
            RegistryUtils.register(new Block(Block.Settings.copy(Blocks.IRON_BLOCK)),
                    new Identifier(RandomlyAddingAnything.MOD_ID, id + "_block"), RandomlyAddingAnything.RAA_RESOURCES, material.getName(),
                    RAABlockItem.BlockType.BLOCK);
            RegistryUtils.register(new LayeredOreBlock(material),
                    new Identifier(RandomlyAddingAnything.MOD_ID, id + "_ore"), RandomlyAddingAnything.RAA_ORES, material.getName(),
                    RAABlockItem.BlockType.ORE);
            if (material.getOreInformation().getOreType() == OreTypes.METAL) {
                RegistryUtils.registerItem(repairItem = new RAASimpleItem(material.getName(), new Item.Settings().group(RandomlyAddingAnything.RAA_RESOURCES),
                        RAASimpleItem.SimpleItemType.INGOT), new Identifier(RandomlyAddingAnything.MOD_ID, id + "_ingot"));
                RegistryUtils.registerItem(new RAASimpleItem(material.getName(), new Item.Settings().group(RandomlyAddingAnything.RAA_RESOURCES),
                        RAASimpleItem.SimpleItemType.NUGGET), new Identifier(RandomlyAddingAnything.MOD_ID, id + "_nugget"));
            } else if (material.getOreInformation().getOreType() == OreTypes.GEM) {
                RegistryUtils.registerItem(repairItem = new RAASimpleItem(material.getName(), new Item.Settings().group(RandomlyAddingAnything.RAA_RESOURCES),
                        RAASimpleItem.SimpleItemType.GEM), new Identifier(RandomlyAddingAnything.MOD_ID, id + "_gem"));
            } else {
                RegistryUtils.registerItem(repairItem = new RAASimpleItem(material.getName(), new Item.Settings().group(RandomlyAddingAnything.RAA_RESOURCES),
                        RAASimpleItem.SimpleItemType.CRYSTAL), new Identifier(RandomlyAddingAnything.MOD_ID, id + "_crystal"));
            }
            if (material.hasArmor()) {
                RegistryUtils.registerItem(
                        new RAAArmorItem(material,
                                EquipmentSlot.HEAD, (new Item.Settings()).group(RandomlyAddingAnything.RAA_ARMOR).recipeRemainder(repairItem)),
                        new Identifier(RandomlyAddingAnything.MOD_ID, id + "_helmet")
                );
                RegistryUtils.registerItem(
                        new RAAArmorItem(material,
                                EquipmentSlot.CHEST, (new Item.Settings()).group(RandomlyAddingAnything.RAA_ARMOR).recipeRemainder(repairItem)),
                        new Identifier(RandomlyAddingAnything.MOD_ID, id + "_chestplate")
                );
                RegistryUtils.registerItem(
                        new RAAArmorItem(material,
                                EquipmentSlot.LEGS, (new Item.Settings()).group(RandomlyAddingAnything.RAA_ARMOR).recipeRemainder(repairItem)),
                        new Identifier(RandomlyAddingAnything.MOD_ID, id + "_leggings")
                );
                RegistryUtils.registerItem(
                        new RAAArmorItem(material,
                                EquipmentSlot.FEET, (new Item.Settings()).group(RandomlyAddingAnything.RAA_ARMOR).recipeRemainder(repairItem)),
                        new Identifier(RandomlyAddingAnything.MOD_ID, id + "_boots")
                );
            }
            if (material.hasTools()) {
                RegistryUtils.registerItem(
                        new RAAPickaxeItem(material,
                                material.getToolMaterial(),
                                1, -2.8F, new Item.Settings().group(RandomlyAddingAnything.RAA_TOOLS).recipeRemainder(repairItem)
                        ),
                        new Identifier(RandomlyAddingAnything.MOD_ID, id + "_pickaxe")
                );
                RegistryUtils.registerItem(
                        new RAAAxeItem(material,
                                material.getToolMaterial(),
                                (5.0F + material.getToolMaterial().getAxeAttackDamage()), (-3.2F + material.getToolMaterial().getAxeAttackSpeed()),
                                new Item.Settings().group(RandomlyAddingAnything.RAA_TOOLS).recipeRemainder(repairItem)
                        ),
                        new Identifier(RandomlyAddingAnything.MOD_ID, id + "_axe")
                );
                RegistryUtils.registerItem(
                        new RAAShovelItem(material,
                                material.getToolMaterial(),
                                1.5F, -3.0F, new Item.Settings().group(RandomlyAddingAnything.RAA_TOOLS).recipeRemainder(repairItem)
                        ),
                        new Identifier(RandomlyAddingAnything.MOD_ID, id + "_shovel")
                );
                RegistryUtils.registerItem(
                        new RAAHoeItem(material,
                                material.getToolMaterial(),
                                (-3.0F + material.getToolMaterial().getHoeAttackSpeed()), new Item.Settings().group(RandomlyAddingAnything.RAA_TOOLS).recipeRemainder(repairItem)
                        ),
                        new Identifier(RandomlyAddingAnything.MOD_ID, id + "_hoe")
                );
            }
            if (material.hasWeapons()) {
                RegistryUtils.registerItem(
                        new RAASwordItem(
                                material,
                                new Item.Settings().group(RandomlyAddingAnything.RAA_WEAPONS).recipeRemainder(repairItem)
                        ),
                        new Identifier(RandomlyAddingAnything.MOD_ID, id + "_sword")
                );
            }
            RegistryUtils.registerItem(
                    new RAAFoodItem(
                            material.getName(),
                            new Item.Settings().group(RandomlyAddingAnything.RAA_RESOURCES).food(FoodComponents.GOLDEN_APPLE),
                            RAAFoodItem.SimpleItemType.APPLE
                    ),
                    new Identifier(RandomlyAddingAnything.MOD_ID, id + "_apple")
            );
            RegistryUtils.registerItem(
                    new RAAFoodItem(
                            material.getName(),
                            new Item.Settings().group(RandomlyAddingAnything.RAA_RESOURCES).food(FoodComponents.GOLDEN_CARROT),
                            RAAFoodItem.SimpleItemType.CARROT
                    ),
                    new Identifier(RandomlyAddingAnything.MOD_ID, id + "_carrot")
            );
            RegistryUtils.registerItem(new RAAHorseArmorItem(material), new Identifier(RandomlyAddingAnything.MOD_ID, id + "_horse_armor"));
        });
    }

}
