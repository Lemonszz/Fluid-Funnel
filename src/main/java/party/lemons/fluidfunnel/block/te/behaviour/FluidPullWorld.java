package party.lemons.fluidfunnel.block.te.behaviour;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import party.lemons.fluidfunnel.block.te.TileEntityFluidHandlerBase;
import party.lemons.fluidfunnel.config.ModConfig;
import party.lemons.fluidfunnel.util.ModUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by Sam on 22/03/2018.
 */
public class FluidPullWorld implements IFluidBehaviour
{
	@Override
	public boolean run(@Nonnull BlockPos pos, @Nonnull World world, @Nonnull TileEntityFluidHandlerBase tileEntity, @Nullable EnumFacing direction)
	{
		if(!ModConfig.GameplayConfig.takeFromWorld)
			return false;

		BlockPos takePos = pos.offset(direction);
		IBlockState takeState = world.getBlockState(takePos);

		if(!ModUtil.isFluidBlock(takeState.getBlock()))
			return false;

		FluidStack drain = ModUtil.drainFluidBlock(world, takePos, false);
		if (drain != null && drain.amount <= tileEntity.getTank().getCapacity() - tileEntity.getTank().getFluidAmount())
		{
			tileEntity.getTank().fill(ModUtil.drainFluidBlock(world, pos.up(), true), true);
			tileEntity.setCooldown(tileEntity.getCooldownLength());
			return true;
		}
		return false;
	}
}
