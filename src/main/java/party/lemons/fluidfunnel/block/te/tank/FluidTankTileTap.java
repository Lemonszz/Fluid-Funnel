package party.lemons.fluidfunnel.block.te.tank;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import party.lemons.fluidfunnel.FluidFunnel;
import party.lemons.fluidfunnel.block.te.TileEntityFunnel;
import party.lemons.fluidfunnel.block.te.TileEntityTap;
import party.lemons.fluidfunnel.message.MessageUpdateFunnel;

/**
 * Created by Sam on 19/03/2018.
 */

public class FluidTankTileTap extends FluidTank
{

	//refactor this shit, 3 classes for no reason
	public FluidTankTileTap(final TileEntityTap tileEntity, final int capacity) {
		super(capacity);
		tile = tileEntity;
	}

	public FluidTankTileTap(final TileEntityTap tileEntity, final FluidStack stack, final int capacity) {
		super(stack, capacity);
		tile = tileEntity;
	}

	public FluidTankTileTap(final TileEntityTap tileEntity, final Fluid fluid, final int amount, final int capacity) {
		super(fluid, amount, capacity);
		tile = tileEntity;
	}

	@Override
	protected void onContentsChanged()
	{

	}
}