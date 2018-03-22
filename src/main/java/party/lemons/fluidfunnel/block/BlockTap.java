package party.lemons.fluidfunnel.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import party.lemons.fluidfunnel.block.te.TileEntityTap;

import javax.annotation.Nullable;

/**
 * Created by Sam on 23/03/2018.
 */
public class BlockTap extends Block implements ITileEntityProvider
{
	public static final PropertyDirection FACING = BlockHorizontal.FACING;

	AxisAlignedBB NORTH = new AxisAlignedBB(5F/16F, 0F, 0F, 11F/16F, 1F, 7F/16F);
	AxisAlignedBB SOUTH = new AxisAlignedBB(5F/16F, 0F, 1F, 11F/16F, 1F, 1F - (7F/16F));
	AxisAlignedBB EAST = new AxisAlignedBB(1F, 0F, 5F/16F, 1F - (7F/16F), 1F, 11F/16F);
	AxisAlignedBB WEST = new AxisAlignedBB(0F, 0F, 5F/16F, 7F/16F, 1F, 11F/16F);


	public BlockTap()
	{
		super(Material.IRON, MapColor.GRAY);
		this.setCreativeTab(CreativeTabs.MISC);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));

		this.setHardness(2.0F);
		this.setResistance(8.0F);
		this.setSoundType(SoundType.METAL);
	}

	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
	{
		if(facing.getAxis().isVertical())
			facing = placer.getHorizontalFacing();

		return this.getDefaultState().withProperty(FACING, facing.getOpposite());
	}

	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(FACING).getIndex();
	}

	public IBlockState getStateFromMeta(int meta)
	{
		EnumFacing enumfacing = EnumFacing.getFront(meta);

		if (enumfacing.getAxis() == EnumFacing.Axis.Y)
		{
			enumfacing = EnumFacing.NORTH;
		}

		return this.getDefaultState().withProperty(FACING, enumfacing);
	}


	public IBlockState withRotation(IBlockState state, Rotation rot)
	{
		return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
	}

	public IBlockState withMirror(IBlockState state, Mirror mirrorIn)
	{
		return state.withRotation(mirrorIn.toRotation(state.getValue(FACING)));
	}

	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[] {FACING});
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}

	@Nullable
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileEntityTap();
	}

	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{

		switch (state.getValue(FACING))
		{
			case NORTH:
				return NORTH;
			case SOUTH:
				return SOUTH;
			case WEST:
				return WEST;
			case EAST:
				return EAST;
		}

		return NORTH;
	}

}
