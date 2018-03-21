package party.lemons.fluidfunnel.block.te.behaviour;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import party.lemons.fluidfunnel.block.te.TileEntityFluidHandlerBase;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by Sam on 22/03/2018.
 */
public class FluidPushContainer implements IFluidBehaviour
{
	@Override
	public boolean run(@Nonnull BlockPos pos, @Nonnull World world, @Nonnull TileEntityFluidHandlerBase tileEntity, @Nullable EnumFacing direction)
	{
		if(direction == null)
			return false;

		EnumFacing targetSide = direction.getOpposite();
		TileEntity targetTe = world.getTileEntity(pos.offset(direction));

		if(targetTe == null)
			return false;
		if(!targetTe.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, targetSide))
			return false;

		IFluidHandler targetHandler = targetTe.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, targetSide);
		IFluidHandler handler = tileEntity.getTank();

		if(FluidUtil.tryFluidTransfer(targetHandler, handler, tileEntity.getMaxTransferRate(),  true) != null)
		{
			tileEntity.setCooldown(tileEntity.getCooldownLength());
			return true;
		}

		return false;
	}
}
