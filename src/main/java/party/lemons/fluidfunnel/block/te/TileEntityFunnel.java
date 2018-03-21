package party.lemons.fluidfunnel.block.te;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import party.lemons.fluidfunnel.block.BlockFunnel;
import party.lemons.fluidfunnel.block.te.behaviour.*;
import party.lemons.fluidfunnel.block.te.tank.FluidTankTile;
import party.lemons.fluidfunnel.config.ModConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sam on 19/03/2018.
 */
public class TileEntityFunnel extends TileEntityFluidHandlerBase implements ITickable
{
	public static List<IFluidBehaviour> outputBehaviour = new ArrayList<>();
	public static List<IFluidBehaviour> inputBehaviour = new ArrayList<>();

	private int transferCooldown = -1;

	public TileEntityFunnel()
	{
		tank = new FluidTankTile(this, ModConfig.GameplayConfig.funnelCapacity);
	}

	public void update()
	{
		if (this.world != null && !this.world.isRemote && BlockFunnel.isEnabled(this.getBlockMetadata()))
		{
			--this.transferCooldown;

			if(!this.isOnTransferCooldown())
			{
				this.setTransferCooldown(0);
				if(!this.transferOut())
					this.takeIn();

			}
			this.markDirty();
		}

	}

	private void setTransferCooldown(int ticks)
	{
		this.transferCooldown = ticks;
	}

	private boolean isOnTransferCooldown()
	{
		return this.transferCooldown > 0;
	}


	public boolean takeIn()
	{
		if(isFull())
			return false;

		for(IFluidBehaviour b : inputBehaviour)
		{
			if(b.run(pos, world, this, EnumFacing.UP))
			{
				return true;
			}
		}

		return false;
	}

	public boolean transferOut()
	{
		if(isEmpty())
			return false;

		IBlockState state = world.getBlockState(getPos());
		EnumFacing facing = state.getValue(BlockFunnel.FACING);

		for(IFluidBehaviour b : outputBehaviour)
		{
			if(b.run(pos, world, this, facing))
			{
				return true;
			}
		}

		return false;
	}

	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
	{
		return false;
	}

	@Override
	public void setCooldown(int cooldown)
	{
		this.setTransferCooldown(cooldown);
	}

	@Override
	public int getMaxTransferRate()
	{
		return ModConfig.GameplayConfig.funnelTransferMax;
	}

	@Override
	public int getCooldownLength()
	{
		return ModConfig.GameplayConfig.funnelCooldown;
	}
}
