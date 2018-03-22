package party.lemons.fluidfunnel.block.te;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import party.lemons.fluidfunnel.block.BlockTap;
import party.lemons.fluidfunnel.block.ModBlocks;
import party.lemons.fluidfunnel.block.te.behaviour.IFluidBehaviour;
import party.lemons.fluidfunnel.block.te.tank.FluidTankTileTap;
import party.lemons.fluidfunnel.config.ModConfig;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sam on 23/03/2018.
 */
public class TileEntityTap extends TileEntityFluidHandlerBase implements ITickable
{
	private int transferCooldown = -1;

	public static List<IFluidBehaviour> outputBehaviour = new ArrayList<>();
	public static List<IFluidBehaviour> inputBehaviour = new ArrayList<>();

	public TileEntityTap()
	{
		tank = new FluidTankTileTap(this, ModConfig.GameplayConfig.tapCapacity);
	}

	@Override
	public void update()
	{
		if (this.world != null && !this.world.isRemote)
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

	public boolean takeIn()
	{
		if(isFull())
			return false;


		IBlockState state = world.getBlockState(getPos());
		EnumFacing facing = state.getValue(BlockTap.FACING);

		if(!world.isAirBlock(pos.offset(facing)))
		{
			IBlockState offsetState = world.getBlockState(pos.offset(facing));
			if(offsetState.getBlock() == ModBlocks.tap || offsetState.getBlock() == ModBlocks.funnel)
			{
				return false;
			}
		}

		for(IFluidBehaviour b : inputBehaviour)
		{
			if(b.run(pos, world, this, facing))
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

		for(IFluidBehaviour b : outputBehaviour)
		{
			if(b.run(pos, world, this, EnumFacing.DOWN))
			{
				return true;
			}
		}

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
		return ModConfig.GameplayConfig.tapTransferMax;
	}

	@Override
	public int getCooldownLength()
	{
		return ModConfig.GameplayConfig.tapCooldown;
	}

	private void setTransferCooldown(int ticks)
	{
		this.transferCooldown = ticks;
	}

	private boolean isOnTransferCooldown()
	{
		return this.transferCooldown > 0;
	}

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing)
	{
		IBlockState state = world.getBlockState(pos);
		if(state.getValue(BlockTap.FACING) == facing)
		{
			return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY;
		}

		if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
			return false;

		return super.hasCapability(capability, facing);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Nullable
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing)
	{
		IBlockState state = world.getBlockState(pos);
		if(state.getValue(BlockTap.FACING) == facing & capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
		{
			return (T)tank;
		}

		if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
			return null;

		return super.getCapability(capability, facing);
	}
}
