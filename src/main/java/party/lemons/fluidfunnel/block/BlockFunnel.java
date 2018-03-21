package party.lemons.fluidfunnel.block;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import party.lemons.fluidfunnel.FluidFunnel;
import party.lemons.fluidfunnel.block.te.TileEntityFunnel;
import party.lemons.fluidfunnel.message.MessageRemoveTile;
import party.lemons.fluidfunnel.message.MessageUpdateFunnel;

import javax.annotation.Nullable;
import java.util.*;

/**
 * Created by Sam on 19/03/2018.
 */
public class BlockFunnel extends Block implements ITileEntityProvider
{
	public static final PropertyDirection FACING = PropertyDirection.create("facing", p_apply_1_ -> p_apply_1_ != EnumFacing.UP);
	public static final PropertyBool ENABLED = PropertyBool.create("enabled");
	protected static final AxisAlignedBB BASE_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.625D, 1.0D);
	protected static final AxisAlignedBB SOUTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.125D);
	protected static final AxisAlignedBB NORTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.875D, 1.0D, 1.0D, 1.0D);
	protected static final AxisAlignedBB WEST_AABB = new AxisAlignedBB(0.875D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
	protected static final AxisAlignedBB EAST_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.125D, 1.0D, 1.0D);
	private Map<EnumFacing, ArrayList<AxisAlignedBB>> blockBounds;

	public BlockFunnel()
	{
		super(Material.IRON, MapColor.GRAY);
		this.setCreativeTab(CreativeTabs.MISC);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.DOWN).withProperty(ENABLED, Boolean.valueOf(false)));

		this.setHardness(3.0F);
		this.setResistance(8.0F);
		this.setSoundType(SoundType.METAL);

		///bounds
		blockBounds = new HashMap<>();
		for(EnumFacing facing : EnumFacing.values())
		{
			if(facing != EnumFacing.UP)
			{
				ArrayList<AxisAlignedBB> bounds = new ArrayList<>();
				bounds.add(new AxisAlignedBB(0 /16F, 10 /16F, 0 /16F, 16 /16F, 16 /16F, 16 /16F));
				bounds.add(new AxisAlignedBB(4 /16F, 4 /16F, 4 /16F, 12 /16F, 10 /16F, 12 /16F));

				blockBounds.put(facing, bounds);
			}
		}
		blockBounds.get(EnumFacing.DOWN).add(new AxisAlignedBB(6 /16F, 0 /16F, 6 /16F, 10 /16F, 4 /16F, 10 /16F));
		blockBounds.get(EnumFacing.NORTH).add(new AxisAlignedBB(6 /16F, 4 /16F, 0 /16F, 10 /16F, 8 /16F, 4 /16F));
		blockBounds.get(EnumFacing.SOUTH).add(new AxisAlignedBB(6 /16F, 4 /16F, 12 /16F, 10 /16F, 8 /16F, 16 /16F));
		blockBounds.get(EnumFacing.WEST).add(new AxisAlignedBB(0 /16F, 4 /16F, 6 /16F, 4 /16F, 8 /16F, 10 /16F));
		blockBounds.get(EnumFacing.EAST).add(new AxisAlignedBB(12 /16F, 4 /16F, 6 /16F, 16 /16F, 8 /16F, 10 /16F));
		///end bounds
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		return !player.isSneaking() && FluidUtil.interactWithFluidHandler(player, hand, world, pos, side);
	}

	@Nullable
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileEntityFunnel();
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	/**
	 * Called when a neighboring block was changed and marks that this state should perform any checks during a neighbor
	 * change. Cases may include when redstone power is updated, cactus blocks popping off due to a neighboring solid
	 * block, etc.
	 */
	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
	{
		this.updateState(worldIn, pos, state);
	}

	private void updateState(World worldIn, BlockPos pos, IBlockState state)
	{
		boolean flag = !worldIn.isBlockPowered(pos);

		if (flag != state.getValue(ENABLED).booleanValue())
		{
			worldIn.setBlockState(pos, state.withProperty(ENABLED, Boolean.valueOf(flag)));
		}
	}

	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		TileEntity tile = worldIn.getTileEntity(pos);
		if(tile != null)
		{
			worldIn.removeTileEntity(pos);
			FluidFunnel.NETWORK.sendToAllAround(new MessageRemoveTile(tile.getPos()),
					new NetworkRegistry.TargetPoint(tile.getWorld().provider.getDimension(), (double)tile.getPos().getX(), (double)tile.getPos().getY(), (double)tile.getPos().getZ(), 128));
		}
		super.breakBlock(worldIn, pos, state);
	}

	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.MODEL;
	}

	public boolean isFullCube(IBlockState state)
	{
		return false;
	}

	public static EnumFacing getFacing(int meta)
	{
		return EnumFacing.getFront(meta & 7);
	}

	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
	{
		return true;
	}

	public static boolean isEnabled(int meta)
	{
		return (meta & 8) != 8;
	}

	public boolean hasComparatorInputOverride(IBlockState state)
	{
		return true;
	}

	public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos)
	{
		return Container.calcRedstone(worldIn.getTileEntity(pos));
	}

	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT_MIPPED;
	}

	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(FACING, getFacing(meta)).withProperty(ENABLED, Boolean.valueOf(isEnabled(meta)));
	}

	public int getMetaFromState(IBlockState state)
	{
		int i = 0;
		i = i | state.getValue(FACING).getIndex();

		if (!state.getValue(ENABLED).booleanValue())
		{
			i |= 8;
		}

		return i;
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
		return new BlockStateContainer(this, new IProperty[] {FACING, ENABLED});
	}

	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
	{
		return face == EnumFacing.UP ? BlockFaceShape.BOWL : BlockFaceShape.UNDEFINED;
	}

	public boolean isTopSolid(IBlockState state)
	{
		return true;
	}

	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
	{
		this.updateState(worldIn, pos, state);
	}

	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return FULL_BLOCK_AABB;
	}

	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState)
	{
		addCollisionBoxToList(pos, entityBox, collidingBoxes, BASE_AABB);
		addCollisionBoxToList(pos, entityBox, collidingBoxes, EAST_AABB);
		addCollisionBoxToList(pos, entityBox, collidingBoxes, WEST_AABB);
		addCollisionBoxToList(pos, entityBox, collidingBoxes, SOUTH_AABB);
		addCollisionBoxToList(pos, entityBox, collidingBoxes, NORTH_AABB);
	}

	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
	{
		EnumFacing enumfacing = facing.getOpposite();

		if (enumfacing == EnumFacing.UP)
		{
			enumfacing = EnumFacing.DOWN;
		}

		return this.getDefaultState().withProperty(FACING, enumfacing).withProperty(ENABLED, Boolean.valueOf(true));
	}

	@Override
	public RayTraceResult collisionRayTrace(IBlockState blockState, World worldIn, BlockPos pos, Vec3d start, Vec3d end)
	{
		EnumFacing facing = blockState.getValue(FACING);
		for(AxisAlignedBB bb : blockBounds.get(facing))
		{
			RayTraceResult res = rayTrace(pos, start, end, bb);
			if(res != null)
			{
				return super.collisionRayTrace(blockState, worldIn, pos, start, end);
			}
		}

		return null;
	}
}
