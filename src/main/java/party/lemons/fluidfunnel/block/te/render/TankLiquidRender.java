package party.lemons.fluidfunnel.block.te.render;

import net.minecraft.client.Minecraft;
import party.lemons.fluidfunnel.block.te.TileEntityFluidHandlerBase;
import party.lemons.fluidfunnel.block.te.TileEntityTank;
import party.lemons.fluidfunnel.block.te.render.LiquidRenderBase;

/**
 * Created by Sam on 21/03/2018.
 */
public class TankLiquidRender extends LiquidRenderBase<TileEntityTank>
{
	private static final float THICC = 0.1F;
	@Override
	public double getY(TileEntityFluidHandlerBase te, double x, double y, double z)
	{
		float scale = Math.min(1.0F, 0.2F + (1.0f - THICC/2 - THICC) * te.getTank().getFluidAmount() / te.getTank().getCapacity());
		float shake = 0;
		if(scale != 1.0F)
		{
			Long t = Minecraft.getMinecraft().world.getTotalWorldTime();
			float d = t;
			shake = (float)Math.sin(d / 10) / 100;

			scale += shake;
		}

		return scale;
	}
}
