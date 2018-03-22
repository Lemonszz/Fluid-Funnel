package party.lemons.fluidfunnel.block.te.behaviour;

import party.lemons.fluidfunnel.block.te.TileEntityFunnel;
import party.lemons.fluidfunnel.block.te.TileEntityTap;

/**
 * Created by Sam on 22/03/2018.
 */
public class BehaviorInit
{
	public static void init()
	{
		TileEntityFunnel.outputBehaviour.add(new FluidPushContainer());
		TileEntityFunnel.outputBehaviour.add(new FluidPushWorld());
		TileEntityFunnel.outputBehaviour.add(new FluidPushCauldron());
		TileEntityFunnel.outputBehaviour.add(new FluidPushItemHandler());

		TileEntityFunnel.inputBehaviour.add(new FluidPullContainer());
		TileEntityFunnel.inputBehaviour.add(new FluidPullWorld());
		TileEntityFunnel.inputBehaviour.add(new FluidPullCauldron());

		TileEntityTap.outputBehaviour.add(new FluidPushContainer());

		TileEntityTap.inputBehaviour.add(new FluidPullContainer());
		TileEntityTap.inputBehaviour.add(new FluidPullWorld());
		TileEntityTap.inputBehaviour.add(new FluidPullCauldron());
	}
}
