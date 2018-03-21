package party.lemons.fluidfunnel.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import party.lemons.fluidfunnel.block.te.TileEntityTank;

/**
 * Created by Sam on 19/03/2018.
 */
public class MessageUpdateTank implements IMessage
{
	BlockPos pos;
	NBTTagCompound tags;

	public MessageUpdateTank(BlockPos pos, NBTTagCompound tags)
	{
		this.pos = pos;
		this.tags = tags;
	}

	public MessageUpdateTank()
	{

	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		tags = ByteBufUtils.readTag(buf);
		NBTTagCompound posTag = ByteBufUtils.readTag(buf);
		pos = NBTUtil.getPosFromTag(posTag);
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		NBTTagCompound tagCompound = NBTUtil.createPosTag(pos);
		ByteBufUtils.writeTag(buf, tags);
		ByteBufUtils.writeTag(buf, tagCompound);
	}

	public static class Handler implements IMessageHandler<MessageUpdateTank, IMessage>
	{
		@Override
		@SideOnly(Side.CLIENT)
		public IMessage onMessage(MessageUpdateTank message, MessageContext ctx)
		{
			Minecraft.getMinecraft().addScheduledTask(new Runnable()
			{
				@Override
				@SideOnly(Side.CLIENT)
				public void run()
				{
					TileEntity te = Minecraft.getMinecraft().world.getTileEntity(message.pos);
					if(te != null && te instanceof TileEntityTank)
					{
						TileEntityTank ff = (TileEntityTank) te;
						ff.readFromNBT(message.tags);
					}
				}
			});

			return null;
		}
	}
}
