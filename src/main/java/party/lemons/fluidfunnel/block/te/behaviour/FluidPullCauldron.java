package party.lemons.fluidfunnel.block.te.behaviour;

import net.minecraft.block.BlockCauldron;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import party.lemons.fluidfunnel.block.te.TileEntityFluidHandlerBase;
import party.lemons.fluidfunnel.config.ModConfig;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by Sam on 22/03/2018.
 */
public class FluidPullCauldron implements IFluidBehaviour
{
	@Override
	public boolean run(@Nonnull BlockPos pos, @Nonnull World world, @Nonnull TileEntityFluidHandlerBase tileEntity, @Nullable EnumFacing direction)
	{
		if(!ModConfig.GameplayConfig.fillCauldron)
			return false;

		BlockPos takePos = pos.offset(direction);
		IBlockState takeState = world.getBlockState(takePos);

		if(!(takeState.getBlock() instanceof BlockCauldron))
			return false;

		int level = takeState.getValue(BlockCauldron.LEVEL);
		if(level > 0 && (tileEntity.getTank().getCapacity() - (tileEntity.getTank().getFluidAmount()) >= Fluid.BUCKET_VOLUME && (tileEntity.isEmpty() || tileEntity.getTank().getFluid().getFluid() == FluidRegistry.WATER)))
		{
			Blocks.CAULDRON.setWaterLevel(world, takePos, takeState, level -1);
			tileEntity.getTank().fill(new FluidStack(FluidRegistry.WATER, Fluid.BUCKET_VOLUME), true);

			tileEntity.setCooldown(tileEntity.getCooldownLength());

			return true;
		}

		return false;
	}
}
