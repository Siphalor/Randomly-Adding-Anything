package fr.arthurbambou.randomlyaddinganything.api.enums;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;

import java.util.ArrayList;
import java.util.List;

public enum AppearsIn {
    STONE(Blocks.STONE),
    DIRT_SURFACE(Blocks.DIRT),
    GRAVEL(Blocks.GRAVEL),
    SAND_DESERT(Blocks.SAND),
    DIORITE(Blocks.DIORITE),
    ANDESITE(Blocks.ANDESITE),
    GRANITE(Blocks.GRANITE),
    SAND_BEACH(Blocks.SAND),
    DIRT_ANY(Blocks.DIRT),
    SAND_ANY(Blocks.SAND),
    DIRT_UNDERGROUND(Blocks.DIRT),
    SAND_ANY2(Blocks.SAND),
    RED_SAND(Blocks.RED_SAND),
    END_STONE(Blocks.END_STONE),
    NETHERRACK(Blocks.NETHERRACK),
    DOES_NOT_APPEAR(null),
    BIOME_SPECIFIC(Blocks.STONE);

    private Block block;

    AppearsIn(Block block) {
        this.block = block;
    }

    public Block getBlock() {
        return block;
    }
}
