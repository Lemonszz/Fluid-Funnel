package party.lemons.fluidfunnel.block.te.render;


import party.lemons.fluidfunnel.block.te.TileEntityFluidHandlerBase;
import party.lemons.fluidfunnel.block.te.TileEntityFunnel;

/**
 * Created by Sam on 19/03/2018.
 */
public class FunnelLiquidRender extends LiquidRenderBase<TileEntityFunnel>
{
	@Override
	public double getY(TileEntityFluidHandlerBase te, double x, double y, double z)
	{
		double amt = ((float)te.getTank().getFluidAmount() / (float)te.getTank().getCapacity());
		double ymin = -0.18;
		double change = (amt * 0.3D);
		double yDraw = 0.85F + ymin + Math.min(change, 0.3);

		return yDraw;
	}
}
