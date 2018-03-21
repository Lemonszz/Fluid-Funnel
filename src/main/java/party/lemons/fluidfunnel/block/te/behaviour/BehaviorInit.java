package party.lemons.fluidfunnel.block.te.behaviour;

import party.lemons.fluidfunnel.block.te.TileEntityFunnel;

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
	}
}
