package party.lemons.fluidfunnel.block.te.behaviour;

import net.minecraft.block.BlockCauldron;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import party.lemons.fluidfunnel.block.te.TileEntityFluidHandlerBase;
import party.lemons.fluidfunnel.config.ModConfig;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by Sam on 22/03/2018.
 */
public class FluidPushCauldron implements IFluidBehaviour
{
	@Override
	public boolean run(@Nonnull BlockPos pos, @Nonnull World world, @Nonnull TileEntityFluidHandlerBase tileEntity, @Nullable EnumFacing direction)
	{
		if(!ModConfig.GameplayConfig.fillCauldron)
			return false;

		if(direction == null)
			return false;

		if(!(world.getBlockState(pos.offset(direction)).getBlock() instanceof BlockCauldron))
			return false;

		if(tileEntity.getTank().getFluidAmount() < Fluid.BUCKET_VOLUME)
			return false;

		IBlockState offsetState = world.getBlockState(pos.offset(direction));
		int cauldLevel = offsetState.getValue(BlockCauldron.LEVEL).intValue();
		if(cauldLevel < 3)
		{
			tileEntity.getTank().drain(Fluid.BUCKET_VOLUME, true);
			Blocks.CAULDRON.setWaterLevel(world, pos.offset(direction), offsetState, cauldLevel + 1);
			tileEntity.setCooldown(tileEntity.getCooldownLength());
			return true;
		}

		return false;
	}
}
