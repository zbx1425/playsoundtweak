package net.zbx1425.playsoundtweak;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockNetherCrow extends BlockContainer {
	public static final PropertyDirection FACING = BlockHorizontal.FACING;

	public BlockNetherCrow() {
		super(Material.GLASS);
		this.setRegistryName("nethercrowblock");
		this.setUnlocalizedName("nethercrowblock");
        this.setHardness(1F);
        this.setCreativeTab(CreativeTabs.DECORATIONS);
        this.setSoundType(SoundType.STONE);
        this.setLightOpacity(0);
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (worldIn.isRemote)
        {
			worldIn.playSound(playerIn, pos, new SoundEvent(new ResourceLocation(
					"playsoundtweak:portablewife.nethercrow.pornmoansound")), SoundCategory.NEUTRAL, 3.0f, 1.0f);
        }
		return true;
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}
	
	@Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityNetherCrow();
    }
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
	
    /*public IBlockState getStateFromMeta(int meta)
    {
        EnumFacing enumfacing = EnumFacing.getFront(meta);

        if (enumfacing.getAxis() == EnumFacing.Axis.Y)
        {
            enumfacing = EnumFacing.NORTH;
        }

        return this.getDefaultState().withProperty(FACING, enumfacing);
    }

    public int getMetaFromState(IBlockState state)
    {
        return ((EnumFacing)state.getValue(FACING)).getIndex();
    }*/

}
