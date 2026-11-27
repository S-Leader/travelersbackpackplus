package keletu.travelersbackpackplus.gear;

import com.tiviacz.travelersbackpack.block.TravelersBackpackBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public final class GearBlock extends TravelersBackpackBlock {
    private static final VoxelShape NORTH_SOUTH = Block.box(2.4, 0, 4.8, 13.6, 12.8, 11.2);
    private static final VoxelShape EAST_WEST = Block.box(4.8, 0, 2.4, 11.2, 12.8, 13.6);

    public GearBlock(Properties properties) {
        super(properties);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(FACING).getAxis() == Direction.Axis.Z ? NORTH_SOUTH : EAST_WEST;
    }
}
