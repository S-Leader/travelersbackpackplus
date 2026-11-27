package keletu.travelersbackpackplus.gear;

import com.mojang.serialization.MapCodec;
import com.tiviacz.travelersbackpack.attachment.AttachmentUtils;
import keletu.travelersbackpackplus.GearRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;

public final class CompressorBlock extends BaseEntityBlock {
    public static final MapCodec<CompressorBlock> CODEC = simpleCodec(CompressorBlock::new);

    public CompressorBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(HorizontalDirectionalBlock.FACING, Direction.NORTH));
    }

    @Override
    protected MapCodec<CompressorBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HorizontalDirectionalBlock.FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(HorizontalDirectionalBlock.FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CompressorBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide() ? null : createTickerHelper(type, GearRegistry.COMPRESSOR_ENTITY.get(), CompressorBlockEntity::tick);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (level.isClientSide()) return InteractionResult.SUCCESS;
        if (!(level.getBlockEntity(pos) instanceof CompressorBlockEntity compressor)) return InteractionResult.PASS;
        ItemStack worn = AttachmentUtils.getWearingBackpack(player);
        if (player.isShiftKeyDown() && worn.is(GearRegistry.SCUBA.get())) {
            compressor.fillGear(worn, 6000);
            AttachmentUtils.synchronise(player);
        } else if (!player.isShiftKeyDown()) {
            compressor.pump(player);
        } else {
            player.openMenu(compressor, buffer -> buffer.writeBlockPos(pos));
        }
        return InteractionResult.SUCCESS_SERVER;
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide() && level.getBlockEntity(pos) instanceof CompressorBlockEntity compressor) {
            player.openMenu(compressor, buffer -> buffer.writeBlockPos(pos));
        }
        return InteractionResult.SUCCESS;
    }
}
