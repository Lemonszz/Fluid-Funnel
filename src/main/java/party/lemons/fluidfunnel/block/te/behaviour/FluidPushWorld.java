package party.lemons.fluidfunnel.block.te.behaviour;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import party.lemons.fluidfunnel.block.te.TileEntityFluidHandlerBase;
import party.lemons.fluidfunnel.config.ModConfig;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by Sam on 22/03/2018.
 */
public class FluidPushWorld implements IFluidBehaviour
{

	@Override
	public boolean run(@Nonnull BlockPos pos, @Nonnull World world, @Nonnull TileEntityFluidHandlerBase tileEntity, @Nullable EnumFacing direction)
	{
		if(!ModConfig.GameplayConfig.placeInWorld)
			return false;

		if(direction == null)
			return false;

		FluidTank handler = tileEntity.getTank();

		if(handler.getFluidAmount() < Fluid.BUCKET_VOLUME)
			return false;

		if(!world.isAirBlock(pos.offset(direction)))
			return false;

		if(!handler.getFluid().getFluid().canBePlacedInWorld())
			return false;

		Block placeBlock = handler.getFluid().getFluid().getBlock();
		if(placeBlock instanceof BlockLiquid)
			placeBlock = BlockLiquid.getFlowingBlock(placeBlock.getDefaultState().getMaterial());

		if(!placeBlock.canPlaceBlockAt(world, pos.offset(direction)))
			return false;

		handler.drain(Fluid.BUCKET_VOLUME, true);
		world.setBlockState(pos.offset(direction), placeBlock.getDefaultState());
		tileEntity.setCooldown(tileEntity.getCooldownLength());

		return true;
	}
}
