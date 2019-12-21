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
import modernity.common.world.gen.feature.MDFeatures;
import modernity.common.world.gen.surface.SwampSurfaceGenerator;
import modernity.common.world.gen.tree.MDTrees;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.FrequencyConfig;
import net.minecraft.world.gen.placement.Placement;

/**
 * The 'Swamp' or 'modernity:swamp' biome.
 */
public class SwampBiome extends ModernityBiome {
    protected SwampBiome() {
        super(
            new Builder()
                .baseHeight( - 1 ).heightVariation( 4 ).heightDifference( 1 )
                .surfaceGen( new SwampSurfaceGenerator() )
                .precipitation( IPrecipitationFunction.swampy() )
        );

        addFeature( GenerationStage.Decoration.VEGETAL_DECORATION, createDecoratedFeature( MDFeatures.GROUPED_BUSH, new GroupedBushFeature.Config( 3, 5, 4, MDBlocks.REEDS ), Placement.COUNT_TOP_SOLID, new FrequencyConfig( 3 ) ) );
        addFeature( GenerationStage.Decoration.VEGETAL_DECORATION, createDecoratedFeature( MDFeatures.GROUPED_BUSH, new GroupedBushFeature.Config( 3, 5, 4, MDBlocks.REDWOLD ), Placement.CHANCE_HEIGHTMAP, new ChanceConfig( 3 ) ) );

        addFeature( GenerationStage.Decoration.VEGETAL_DECORATION, createDecoratedFeature( MDFeatures.CLUSTER_BUSH, new ClusterBushFeature.Config( 100, 6, MDBlocks.MURK_GRASS ), Placement.COUNT_HEIGHTMAP, new FrequencyConfig( 5 ) ) );
        addFeature( GenerationStage.Decoration.VEGETAL_DECORATION, createDecoratedFeature( MDFeatures.CLUSTER_BUSH, new ClusterBushFeature.Config( 81, 7, new IBlockProvider.ChooseRandom( MDBlocks.BLUE_MILLIUM, MDBlocks.CYAN_MILLIUM, MDBlocks.GREEN_MILLIUM, MDBlocks.YELLOW_MILLIUM, MDBlocks.MAGENTA_MILLIUM, MDBlocks.RED_MILLIUM, MDBlocks.WHITE_MILLIUM ) ), Placement.CHANCE_HEIGHTMAP, new ChanceConfig( 6 ) ) );

        addFeature( GenerationStage.Decoration.VEGETAL_DECORATION, createDecoratedFeature( MDFeatures.TREE, MDTrees.BLACKWOOD, Placement.CHANCE_HEIGHTMAP, new ChanceConfig( 2 ) ) );
    }

}
