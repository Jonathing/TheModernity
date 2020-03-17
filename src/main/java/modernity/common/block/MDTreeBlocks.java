/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 17 - 2020
 * Author: rgsw
 */

package modernity.common.block;

import modernity.common.block.base.AxisBlock;
import modernity.common.block.loot.LeavesBlockDrops;
import modernity.common.block.loot.MDBlockDrops;
import modernity.common.block.tree.*;
import modernity.common.generator.tree.MDTrees;
import modernity.common.item.MDItemGroup;
import modernity.common.item.MDItems;
import net.minecraft.block.material.MaterialColor;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder( "modernity" )
public interface MDTreeBlocks {
    static void init() {
    }

    // Blackwood
    AxisBlock STRIPPED_BLACKWOOD_LOG
        = MDBlocks.function( "stripped_blackwood_log", AxisBlock::new )
                  .wood( MaterialColor.BLACK_TERRACOTTA )
                  .item( MDItemGroup.BLOCKS )
                  .drops( MDBlockDrops.SIMPLE )
                  .create();

    AxisBlock BLACKWOOD_LOG
        = MDBlocks.function( "blackwood_log", props -> new StripableLogBlock( () -> STRIPPED_BLACKWOOD_LOG, props ) )
                  .wood( MaterialColor.BLACK_TERRACOTTA )
                  .item( MDItemGroup.BLOCKS )
                  .drops( MDBlockDrops.SIMPLE )
                  .create();

    AxisBlock STRIPPED_BLACKWOOD
        = MDBlocks.function( "stripped_blackwood", AxisBlock::new )
                  .wood( MaterialColor.BLACK_TERRACOTTA )
                  .item( MDItemGroup.BLOCKS )
                  .drops( MDBlockDrops.SIMPLE )
                  .create();

    AxisBlock BLACKWOOD
        = MDBlocks.function( "blackwood", props -> new StripableLogBlock( () -> STRIPPED_BLACKWOOD, props ) )
                  .wood( MaterialColor.BLACK_TERRACOTTA )
                  .item( MDItemGroup.BLOCKS )
                  .drops( MDBlockDrops.SIMPLE )
                  .create();

    BlackwoodSaplingBlock BLACKWOOD_SAPLING
        = MDBlocks.function( "blackwood_sapling", BlackwoodSaplingBlock::new )
                  .strongPlant( MaterialColor.GRASS, 0 )
                  .item( MDItemGroup.PLANTS )
                  .drops( MDBlockDrops.SIMPLE )
                  .create();

    HangLeavesBlock BLACKWOOD_LEAVES
        = MDBlocks.function( "blackwood_leaves", props -> new BlackwoodLeavesBlock( MDBlockTags.BLACKWOOD_LOG, props ) )
                  .leaves( MaterialColor.FOLIAGE, 0.2 )
                  .item( MDItemGroup.PLANTS )
                  .drops( new LeavesBlockDrops( () -> MDTreeBlocks.BLACKWOOD_SAPLING, () -> MDItems.BLACKWOOD_STICK, 0.05F, 0.0625F, 0.083333336F, 0.1F ) )
                  .create();


    // Inver
    AxisBlock STRIPPED_INVER_LOG
        = MDBlocks.function( "stripped_inver_log", AxisBlock::new )
                  .wood( MaterialColor.WOOD )
                  .item( MDItemGroup.BLOCKS )
                  .dropSelf()
                  .create();

    AxisBlock INVER_LOG
        = MDBlocks.function( "inver_log", props -> new StripableLogBlock( () -> STRIPPED_INVER_LOG, props ) )
                  .wood( MaterialColor.WOOD )
                  .item( MDItemGroup.BLOCKS )
                  .dropSelf()
                  .create();

    AxisBlock STRIPPED_INVER
        = MDBlocks.function( "stripped_inver_wood", AxisBlock::new )
                  .wood( MaterialColor.WOOD )
                  .item( MDItemGroup.BLOCKS )
                  .dropSelf()
                  .create();

    AxisBlock INVER_WOOD
        = MDBlocks.function( "inver_wood", props -> new StripableLogBlock( () -> STRIPPED_INVER, props ) )
                  .wood( MaterialColor.WOOD )
                  .item( MDItemGroup.BLOCKS )
                  .dropSelf()
                  .create();

    SimpleSaplingBlock INVER_SAPLING
        = MDBlocks.function( "inver_sapling", props -> new SimpleSaplingBlock( () -> MDTrees.INVER, props ) )
                  .strongPlant( MaterialColor.GRASS, 0 )
                  .item( MDItemGroup.PLANTS )
                  .dropSelf()
                  .create();

    SimpleSaplingBlock RED_INVER_SAPLING
        = MDBlocks.function( "red_inver_sapling", props -> new SimpleSaplingBlock( () -> MDTrees.RED_INVER, props ) )
                  .strongPlant( MaterialColor.GRASS, 0 )
                  .item( MDItemGroup.PLANTS )
                  .dropSelf()
                  .create();

    DecayLeavesBlock INVER_LEAVES
        = MDBlocks.function( "inver_leaves", props -> new InverLeavesBlock( MDBlockTags.INVER_LOG, props, false ) )
                  .leaves( MaterialColor.FOLIAGE, 0.2 )
                  .item( MDItemGroup.PLANTS )
                  .drops( new LeavesBlockDrops( () -> MDTreeBlocks.INVER_SAPLING, () -> MDItems.INVER_STICK, 0.05F, 0.0625F, 0.083333336F, 0.1F ) )
                  .create();

    DecayLeavesBlock RED_INVER_LEAVES
        = MDBlocks.function( "red_inver_leaves", props -> new InverLeavesBlock( MDBlockTags.INVER_LOG, props, true ) )
                  .leaves( MaterialColor.RED, 0.2 )
                  .item( MDItemGroup.PLANTS )
                  .drops( new LeavesBlockDrops( () -> MDTreeBlocks.RED_INVER_SAPLING, () -> MDItems.INVER_STICK, 0.05F, 0.0625F, 0.083333336F, 0.1F ) )
                  .create();
}
