package party.lemons.fluidfunnel.block.te;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.TileFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

/**
 * Created by Sam on 22/03/2018.
 */
public abstract class TileEntityFluidHandlerBase extends TileFluidHandler
{
	public FluidTank getTank()
	{
		return tank;
	}

	public abstract void setCooldown(int cooldown);
	public abstract int getMaxTransferRate();
	public abstract int getCooldownLength();

	public boolean isEmpty()
	{
		return tank.getFluidAmount() == 0;
	}

	public boolean isFull()
	{
		return tank.getFluidAmount() == tank.getCapacity();
	}

	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
	}

	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		return compound;
	}

	@Override
	public NBTTagCompound getUpdateTag()
	{
		return writeToNBT(new NBTTagCompound());
	}

	@Nullable
	@Override
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		return new SPacketUpdateTileEntity(getPos(), 0, getUpdateTag());
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onDataPacket(final NetworkManager net, final SPacketUpdateTileEntity pkt) {
		readFromNBT(pkt.getNbtCompound());
	}
}
