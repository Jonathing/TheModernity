/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   12 - 21 - 2019
 * Author: rgsw
 */

package modernity.common.biome;

import modernity.api.util.IBlockProvider;
import modernity.common.block.MDBlocks;
import modernity.common.environment.precipitation.IPrecipitationFunction;
import modernity.common.world.gen.feature.ClusterBushFeature;
import modernity.common.world.gen.feature.GroupedBushFeature;
import modernity.common.world.gen.feature.LakeFeature;
import modernity.common.world.gen.feature.MDFeatures;
import modernity.common.world.gen.surface.GrassSurfaceGenerator;
import modernity.common.world.gen.tree.MDTrees;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.FrequencyConfig;
import net.minecraft.world.gen.placement.LakeChanceConfig;
import net.minecraft.world.gen.placement.Placement;

import java.util.Random;
import java.util.function.Function;

/**
 * The 'Lush Meadow' or 'modernity:lush_meadow' biome.
 */
public class LushMeadowBiome extends ModernityBiome {
    private static final Function<Random, Integer> GRASS_HEIGHT_PROVIDER = rng -> Math.min( 4, rng.nextInt( 4 ) + rng.nextInt( 2 ) + 1 );

    protected LushMeadowBiome() {
        super(
            new Builder()
                .baseHeight( 2 ).heightVariation( 3 ).heightDifference( 2 )
                .surfaceGen( new GrassSurfaceGenerator() )
                .precipitation( IPrecipitationFunction.standard() )
        );


        addFeature( GenerationStage.Decoration.LOCAL_MODIFICATIONS, createDecoratedFeature( MDFeatures.LAKE, new LakeFeature.Config( MDBlocks.MURKY_WATER, null, null, MDBlocks.MURKY_GRASS_BLOCK ), Placement.WATER_LAKE, new LakeChanceConfig( 5 ) ) );

        addFeature( GenerationStage.Decoration.VEGETAL_DECORATION, createDecoratedFeature( MDFeatures.GROUPED_BUSH, new GroupedBushFeature.Config( 3, 5, 4, MDBlocks.REEDS ), Placement.CHANCE_TOP_SOLID_HEIGHTMAP, new ChanceConfig( 5 ) ) );
        addFeature( GenerationStage.Decoration.VEGETAL_DECORATION, createDecoratedFeature( MDFeatures.GROUPED_BUSH, new GroupedBushFeature.Config( 3, 5, 4, MDBlocks.REDWOLD ), Placement.CHANCE_TOP_SOLID_HEIGHTMAP, new ChanceConfig( 5 ) ) );

        addFeature( GenerationStage.Decoration.VEGETAL_DECORATION, createDecoratedFeature( MDFeatures.CLUSTER_BUSH, new ClusterBushFeature.Config( 120, 6, ( world, pos, rand ) -> MDBlocks.MURK_GRASS.provide( world, pos, rand, GRASS_HEIGHT_PROVIDER ) ), Placement.COUNT_HEIGHTMAP, new FrequencyConfig( 24 ) ) );
        addFeature( GenerationStage.Decoration.VEGETAL_DECORATION, createDecoratedFeature( MDFeatures.CLUSTER_BUSH, new ClusterBushFeature.Config( 120, 6, MDBlocks.NETTLES ), Placement.CHANCE_HEIGHTMAP, new ChanceConfig( 4 ) ) );
        addFeature( GenerationStage.Decoration.VEGETAL_DECORATION, createDecoratedFeature( MDFeatures.CLUSTER_BUSH, new ClusterBushFeature.Config( 81, 7, new IBlockProvider.ChooseRandom( MDBlocks.BLUE_MILLIUM, MDBlocks.CYAN_MILLIUM, MDBlocks.GREEN_MILLIUM, MDBlocks.YELLOW_MILLIUM, MDBlocks.MAGENTA_MILLIUM, MDBlocks.RED_MILLIUM, MDBlocks.WHITE_MILLIUM ) ), Placement.CHANCE_HEIGHTMAP, new ChanceConfig( 2 ) ) );
        addFeature( GenerationStage.Decoration.VEGETAL_DECORATION, createDecoratedFeature( MDFeatures.CLUSTER_BUSH, new ClusterBushFeature.Config( 81, 7, new IBlockProvider.ChooseRandom( MDBlocks.BLUE_MELION, MDBlocks.ORANGE_MELION, MDBlocks.INDIGO_MELION, MDBlocks.YELLOW_MELION, MDBlocks.MAGENTA_MELION, MDBlocks.RED_MELION, MDBlocks.WHITE_MELION ) ), Placement.COUNT_HEIGHTMAP, new FrequencyConfig( 1 ) ) );

        addFeature( GenerationStage.Decoration.VEGETAL_DECORATION, createDecoratedFeature( MDFeatures.TREE, MDTrees.BLACKWOOD, Placement.CHANCE_HEIGHTMAP, new ChanceConfig( 30 ) ) );
    }
}
