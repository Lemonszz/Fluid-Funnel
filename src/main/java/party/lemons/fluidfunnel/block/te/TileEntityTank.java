package party.lemons.fluidfunnel.block.te;

import party.lemons.fluidfunnel.block.te.tank.FluidTankTileBase;
import party.lemons.fluidfunnel.config.ModConfig;

/**
 * Created by Sam on 21/03/2018.
 */
public class TileEntityTank extends TileEntityFluidHandlerBase
{
	public TileEntityTank()
	{
		tank = new FluidTankTileBase(this, ModConfig.GameplayConfig.tankCapacity);
	}


	@Override
	public void setCooldown(int cooldown)
	{

	}

	@Override
	public int getMaxTransferRate()
	{
		return 0;
	}

	@Override
	public int getCooldownLength()
	{
		return 0;
	}
}
