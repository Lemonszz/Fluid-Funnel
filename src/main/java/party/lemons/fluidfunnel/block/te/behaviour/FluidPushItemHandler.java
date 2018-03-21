package party.lemons.fluidfunnel.block.te.behaviour;

import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemGlassBottle;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import party.lemons.fluidfunnel.block.te.TileEntityFluidHandlerBase;
import party.lemons.fluidfunnel.config.ModConfig;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by Sam on 22/03/2018.
 */
public class FluidPushItemHandler implements IFluidBehaviour
{
	@Override
	public boolean run(@Nonnull BlockPos pos, @Nonnull World world, @Nonnull TileEntityFluidHandlerBase tileEntity, @Nullable EnumFacing direction)
	{
		if(direction == null)
			return false;

		if(!ModConfig.GameplayConfig.fillContainers)
			return false;

		EnumFacing targetSide = direction.getOpposite();
		TileEntity targetTe = world.getTileEntity(pos.offset(direction));

		if(targetTe == null)
			return false;
		if(!targetTe.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, targetSide))
			return false;

		IItemHandler targetHandler = targetTe.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, targetSide);
		IFluidHandler handler = tileEntity.getTank();

		for(int i = 0; i < targetHandler.getSlots(); i++)
		{
			ItemStack stack = targetHandler.getStackInSlot(i);
			if(!stack.isEmpty())
			{
				if(stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null) || stack.getItem() instanceof ItemGlassBottle)
				{
					if(stack.getItem() instanceof ItemGlassBottle)
					{
						if(tileEntity.getTank().getFluid().getFluid() == FluidRegistry.WATER)
						{
							if(tileEntity.getTank().drain(ModConfig.GameplayConfig.glassBottleSize, false) != null)
							{
								ItemStack newBottle = PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.WATER);
								if(!targetHandler.extractItem(i, 1, true).isEmpty() && ItemHandlerHelper.insertItem(targetHandler, newBottle, true).isEmpty())
								{
									tileEntity.getTank().drain(ModConfig.GameplayConfig.glassBottleSize, true);
									targetHandler.extractItem(i, 1, false);
									ItemHandlerHelper.insertItem(targetHandler, newBottle, false);
								}
							}
						}
					}
					else
					{

						FluidActionResult result = FluidUtil.tryFillContainer(stack, handler, Fluid.BUCKET_VOLUME, null, false);
						if(result.isSuccess())
						{
							if(!targetHandler.extractItem(i, 1, true).isEmpty() && ItemHandlerHelper.insertItem(targetHandler, result.getResult(), true).isEmpty())
							{
								result = FluidUtil.tryFillContainer(stack, handler, Fluid.BUCKET_VOLUME, null, true);
								targetHandler.extractItem(i, 1, false);
								ItemHandlerHelper.insertItem(targetHandler, result.getResult(), false);
								tileEntity.setCooldown(tileEntity.getCooldownLength());
								return true;
							}
						}
					}
				}
			}
		}

		return false;
	}
}
