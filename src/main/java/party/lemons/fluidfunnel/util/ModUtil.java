package party.lemons.fluidfunnel.util;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;

/**
 * Created by Sam on 22/03/2018.
 */
public class ModUtil
{
	public static boolean isFluidBlock(Block block)
	{
		return block instanceof IFluidBlock || block == Blocks.WATER || block == Blocks.FLOWING_WATER || block == Blocks.LAVA || block == Blocks.FLOWING_LAVA;
	}

	public static FluidStack drainFluidBlock(World world, BlockPos pos, boolean doDrain)
	{
		Block b = world.getBlockState(pos).getBlock();
		Fluid f = FluidRegistry.lookupFluidForBlock(b);

		if(f!=null)
		{
			if(b instanceof IFluidBlock)
			{
				if(((IFluidBlock)b).canDrain(world, pos))
					return ((IFluidBlock) b).drain(world, pos, doDrain);
				else
					return null;
			}
			else
			{
				if(b.getMetaFromState(world.getBlockState(pos))==0)
				{
					if(doDrain)
						world.setBlockToAir(pos);
					return new FluidStack(f, 1000);
				}
				return null;
			}
		}
		return null;
	}
}
