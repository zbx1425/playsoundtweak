package net.zbx1425.playsoundtweak;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.MovingSoundMinecartRiding;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityNote;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

public class BlockSpeaker extends BlockContainer {

	public BlockSpeaker() {
		super(Material.WOOD);
		this.setRegistryName("speakerblock");
		this.setUnlocalizedName("speakerblock");
        this.setHardness(1F);
        this.setCreativeTab(CreativeTabs.REDSTONE);
        this.setSoundType(SoundType.STONE);
	}
	
	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos){
        boolean flag = worldIn.isBlockPowered(pos);
        TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof TileEntitySpeaker){
        	TileEntitySpeaker te = (TileEntitySpeaker)tileentity;
            if (te.previousRedstoneState != flag){
            	MessageSpeaker msg = new MessageSpeaker();
				msg.x = te.getPos().getX();
		        msg.y = te.getPos().getY();
		        msg.z = te.getPos().getZ();
		        msg.audioname = ((TileEntitySpeaker) te).soundname;
		        msg.constvol = ((TileEntitySpeaker) te).constvol;
		        msg.longtrig = ((TileEntitySpeaker) te).longtrig;
                if (flag){
                    //tileentitynote.triggerNote(worldIn, pos);
                	msg.type = 1;
                	CommonProxy.snwinstance.sendToAllAround(msg,new TargetPoint(
            				worldIn.provider.getDimension(),msg.x,msg.y,msg.z,128));
                } else if (te.longtrig) {
                	msg.type = 2;
                	CommonProxy.snwinstance.sendToAllAround(msg,new TargetPoint(
            				worldIn.provider.getDimension(),msg.x,msg.y,msg.z,128));
                }
                te.previousRedstoneState = flag;
            }
        }
    }
	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}
	
	@Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntitySpeaker();
    }
	
	/*@Override
	public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int id, int param)
    {
		if (!worldIn.isRemote) return false;
        TileEntitySpeaker te = (TileEntitySpeaker) worldIn.getTileEntity(pos);
        if (id == 1) {
	        if (te.constvol) {
				te.soundcache = new SoundConstVol(new SoundEvent(new ResourceLocation(te.soundname.trim())));
	        } else {
	        	te.soundcache = new PositionedSoundRecord(new SoundEvent(new ResourceLocation(te.soundname.trim())), 
						SoundCategory.RECORDS, 3.0f, 1.0f, pos.getX(),pos.getY(),pos.getZ());
	        }
			Minecraft.getMinecraft().getSoundHandler().playSound((ISound) te.soundcache);
			worldIn.spawnParticle(EnumParticleTypes.NOTE, 
					(double)pos.getX() + 0.5D, (double)pos.getY() + 1.2D, (double)pos.getZ() + 0.5D, 
					0.0D, 0.0D, 0.0D);
        } else {
        	Minecraft.getMinecraft().getSoundHandler().stopSound((ISound) te.soundcache);
        }
        return false;
    }*/
	
	@Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		//System.out.println("Block Clicked!");
		if (worldIn.isRemote)
        {
			if (playerIn.getHeldItem(EnumHand.MAIN_HAND).getItem().getRegistryName().getResourcePath().equals("paper")) {
				NBTTagCompound nbt = playerIn.getHeldItem(EnumHand.MAIN_HAND).getTagCompound();
				String txstr = nbt.getCompoundTag("display").getString("Name").trim();
				boolean constvol = false, longtrig = false;
				if (txstr.contains("!")) {
					txstr = txstr.replace("!", "");
					constvol = true;
				}
				if (txstr.contains("#")) {
					txstr = txstr.replace("#", "");
					longtrig = true;
				}
				TileEntity te = worldIn.getTileEntity(pos);
				if (!(te instanceof TileEntitySpeaker)) {
					return false;
				}
				((TileEntitySpeaker) te).soundname = txstr;
				((TileEntitySpeaker) te).constvol = constvol;
				((TileEntitySpeaker) te).longtrig = longtrig;
				MessageSpeaker msg = new MessageSpeaker();
				msg.x = te.getPos().getX();
		        msg.y = te.getPos().getY();
		        msg.z = te.getPos().getZ();
		        msg.type = 0;
		        msg.audioname = ((TileEntitySpeaker) te).soundname;
		        msg.constvol = ((TileEntitySpeaker) te).constvol;
		        msg.longtrig = ((TileEntitySpeaker) te).longtrig;
		        CommonProxy.snwinstance.sendToServer(msg);
			} else {
	            int id = GuiElementLoader.Speaker;
	            playerIn.openGui(Core.instance, id, worldIn, pos.getX(), pos.getY(), pos.getZ());
			}
        }
		return true;
	}

}
