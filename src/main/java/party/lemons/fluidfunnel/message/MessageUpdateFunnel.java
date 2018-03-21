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
import party.lemons.fluidfunnel.block.te.TileEntityFunnel;

/**
 * Created by Sam on 19/03/2018.
 */
public class MessageUpdateFunnel implements IMessage
{
	BlockPos pos;
	NBTTagCompound tags;

	public MessageUpdateFunnel(BlockPos pos, NBTTagCompound tags)
	{
		this.pos = pos;
		this.tags = tags;
	}

	public MessageUpdateFunnel()
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

	public static class Handler implements IMessageHandler<MessageUpdateFunnel, IMessage>
	{
		@Override
		@SideOnly(Side.CLIENT)
		public IMessage onMessage(MessageUpdateFunnel message, MessageContext ctx)
		{
			Minecraft.getMinecraft().addScheduledTask(new Runnable()
			{
				@Override
				@SideOnly(Side.CLIENT)
				public void run()
				{
					TileEntity te = Minecraft.getMinecraft().world.getTileEntity(message.pos);
					if(te != null && te instanceof TileEntityFunnel)
					{
						TileEntityFunnel ff = (TileEntityFunnel) te;
						ff.readFromNBT(message.tags);
					}
				}
			});

			return null;
		}
	}
}
