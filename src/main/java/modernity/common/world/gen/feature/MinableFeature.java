package modernity.common.world.gen.feature;

import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;
import modernity.api.util.BlockPredicates;
import modernity.api.util.BlockUpdates;
import modernity.api.util.MovingBlockPos;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.OreFeature;

import java.util.BitSet;
import java.util.Random;
import java.util.function.Predicate;

/**
 * A feature that generates an underground minable (usually an ore) like vanilla's ores ({@link OreFeature}).
 */
public class MinableFeature extends Feature<MinableFeature.Config> {
    public MinableFeature() {
        super( dynamic -> new Config( BlockPredicates.FALSE, Blocks.AIR.getDefaultState(), 0 ) );
    }

    @Override
    public boolean place( IWorld world, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, Config config ) {
        float sinCosOffset = rand.nextFloat() * (float) Math.PI;
        float scale = config.size / 8F;
        int radius = MathHelper.ceil( ( config.size / 8F + 1 ) / 2 );

        // Determine endpoints of the shape of the minable
        double x1 = pos.getX() + MathHelper.sin( sinCosOffset ) * scale;
        double x2 = pos.getX() - MathHelper.sin( sinCosOffset ) * scale;
        double z1 = pos.getZ() + MathHelper.cos( sinCosOffset ) * scale;
        double z2 = pos.getZ() - MathHelper.cos( sinCosOffset ) * scale;

        double y1 = pos.getY() + rand.nextInt( 3 ) - 2;
        double y2 = pos.getY() + rand.nextInt( 3 ) - 2;

        // Integer scale
        int scaleI = MathHelper.ceil( scale );

        // Determine lower bounds
        int minX = pos.getX() - scaleI - radius;
        int minY = pos.getY() - 2 - radius;
        int minZ = pos.getZ() - scaleI - radius;

        // Determine bound size
        int xzSize = 2 * ( scaleI + radius );
        int ySize = 2 * ( 2 + radius );

        // Don't generate when lower y-bound lies completely above heightmap
        for( int x = minX; x <= minX + xzSize; ++ x ) {
            for( int z = minZ; z <= minZ + xzSize; ++ z ) {
                if( minY <= world.getHeight( Heightmap.Type.OCEAN_FLOOR_WG, x, z ) ) {
                    return generate( world, rand, config, x1, x2, z1, z2, y1, y2, minX, minY, minZ, xzSize, ySize );
                }
            }
        }

        return false;
    }

    /**
     * Generates the actual minable, which is some kind of a curve between two points.
     * @param world  The world to generate in.
     * @param random A random number generator.
     * @param config The {@link Config} object to use.
     * @param x1     X of the first curve endpoint
     * @param y1     Y of the first curve endpoint
     * @param z1     Z of the first curve endpoint
     * @param x2     X of the second curve endpoint
     * @param y2     Y of the second curve endpoint
     * @param z2     Z of the second curve endpoint
     * @param minX   The lower X limit, it doesn't generate blocks beyond this limit
     * @param minY   The lower Y limit, it doesn't generate blocks beyond this limit
     * @param minZ   The lower Z limit, it doesn't generate blocks beyond this limit
     * @param xzSize The size over X and Z axis, it doesn't generate block outside this size.
     * @param ySize  The size over Y axis, it doesn't generate blocks outside this size.
     * @return True if something generated.
     */
    protected boolean generate( IWorld world, Random random, Config config, double x1, double y1, double z1, double x2, double y2, double z2, int minX, int minY, int minZ, int xzSize, int ySize ) {
        int amountPlaced = 0;

        // Tracks placed blocks so that we don't place any block twice
        BitSet placedBlocks = new BitSet( xzSize * ySize * xzSize );

        MovingBlockPos mpos = new MovingBlockPos();

        // An array of spheres forming a curvy shape, four entries for each sphere (x, y, z and radius)
        double[] curve = new double[ config.size * 4 ];


        // Generate the curvy shape of this minable deposit
        for( int i = 0; i < config.size; ++ i ) {
            float progress = (float) i / config.size;

            double x = MathHelper.lerp( progress, x1, x2 );
            double y = MathHelper.lerp( progress, y1, y2 );
            double z = MathHelper.lerp( progress, z1, z2 );

            // Sine function gives a thin-thick-thin curve
            double radiusScale = random.nextDouble() * config.size / 16;
            double radius = ( ( MathHelper.sin( (float) Math.PI * progress ) + 1 ) * radiusScale + 1 ) / 2;

            curve[ i * 4 ] = x;
            curve[ i * 4 + 1 ] = y;
            curve[ i * 4 + 2 ] = z;
            curve[ i * 4 + 3 ] = radius;
        }

        // Make sure the radius of each sphere does not exceed the distance from the local origin
        for( int i = 0; i < config.size - 1; ++ i ) {
            double ix = curve[ i * 4 ];
            double iy = curve[ i * 4 + 1 ];
            double iz = curve[ i * 4 + 2 ];
            double ir = curve[ i * 4 + 3 ];

            if( ir > 0 ) {
                for( int j = i + 1; j < config.size; ++ j ) {
                    double jx = curve[ j * 4 ];
                    double jy = curve[ j * 4 + 1 ];
                    double jz = curve[ j * 4 + 2 ];
                    double jr = curve[ j * 4 + 3 ];

                    if( jr > 0 ) {
                        double dx = ix - jx;
                        double dy = iy - jy;
                        double dz = iz - jz;
                        double dr = ir - jr;

                        if( dr * dr > dx * dx + dy * dy + dz * dz ) {
                            if( dr > 0 ) {
                                curve[ j * 4 + 3 ] = - 1;
                            } else {
                                curve[ i * 4 + 3 ] = - 1;
                            }
                        }
                    }
                }
            }
        }

        // Generate the actual minable
        for( int i = 0; i < config.size; ++ i ) {
            double radius = curve[ i * 4 + 3 ];
            if( radius >= 0 ) {
                double x = curve[ i * 4 ];
                double y = curve[ i * 4 + 1 ];
                double z = curve[ i * 4 + 2 ];

                int nx = Math.max( MathHelper.floor( x - radius ), minX );
                int ny = Math.max( MathHelper.floor( y - radius ), minY );
                int nz = Math.max( MathHelper.floor( z - radius ), minZ );
                int px = Math.max( MathHelper.floor( x + radius ), nx );
                int py = Math.max( MathHelper.floor( y + radius ), ny );
                int pz = Math.max( MathHelper.floor( z + radius ), nz );

                for( int lx = nx; lx <= px; ++ lx ) {
                    double dx = ( lx + 0.5 - x ) / radius;

                    if( dx * dx < 1 ) {
                        for( int ly = ny; ly <= py; ++ ly ) {
                            double dy = ( ly + 0.5 - y ) / radius;

                            if( dx * dx + dy * dy < 1 ) {
                                for( int lz = nz; lz <= pz; ++ lz ) {
                                    double dz = ( lz + 0.5 - z ) / radius;

                                    if( dx * dx + dy * dy + dz * dz < 1 ) {

                                        int idx = lx - minX + ( ly - minY ) * xzSize + ( lz - minZ ) * xzSize * ySize;

                                        // Only place a block when not already placed one here
                                        if( ! placedBlocks.get( idx ) ) {
                                            placedBlocks.set( idx );
                                            mpos.setPos( lx, ly, lz );
                                            if( config.target.test( world.getBlockState( mpos ) ) ) {
                                                setBlockState( world, mpos, config.state, BlockUpdates.NOTIFY_CLIENTS );
                                                amountPlaced++;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return amountPlaced > 0;
    }

    private void setBlockState( IWorld world, BlockPos pos, BlockState state, int flags ) {
        int cx = pos.getX() >>> 4;
        int cz = pos.getZ() >>> 4;
        if( world.chunkExists( cx, cz ) ) { // Prevent out of bounds crash
            world.setBlockState( pos, state, flags );
        }
    }

    /**
     * A configuration for the {@link MinableFeature}.
     */
    public static class Config implements IFeatureConfig {
        public final Predicate<BlockState> target;
        public final int size;
        public final BlockState state;

        /**
         * Creates a minable config.
         * @param target A predicate that matches blocks that may be replaced.
         * @param state  The minable block state
         * @param size   The size of the minable.
         */
        public Config( Predicate<BlockState> target, BlockState state, int size ) {
            this.size = size;
            this.state = state;
            this.target = target;
        }

        @Override
        public <T> Dynamic<T> serialize( DynamicOps<T> ops ) {
            return new Dynamic<>( ops, ops.emptyMap() );
        }
    }
}