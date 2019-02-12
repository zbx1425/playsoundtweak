package net.zbx1425.playsoundtweak;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.MovingSoundMinecartRiding;
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
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        boolean flag = worldIn.isBlockPowered(pos);
        TileEntity tileentity = worldIn.getTileEntity(pos);

        if (tileentity instanceof TileEntitySpeaker)
        {
        	TileEntitySpeaker tileentitynote = (TileEntitySpeaker)tileentity;

            if (tileentitynote.previousRedstoneState != flag)
            {
                if (flag)
                {
                    tileentitynote.triggerNote(worldIn, pos);
                }

                tileentitynote.previousRedstoneState = flag;
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
	
	@Override
	public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int id, int param)
    {
        //net.minecraftforge.event.world.NoteBlockEvent.Play e = new net.minecraftforge.event.world.NoteBlockEvent.Play(worldIn, pos, state, param, id);
        TileEntitySpeaker te = (TileEntitySpeaker) worldIn.getTileEntity(pos);
        //if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(e)) return false;
        //if (!worldIn.isRemote) return false;
        //System.out.println(te.soundname.trim());
        if (te.constvol) {
        	if (!worldIn.isRemote) {
            	return true;
            } else {
	        	Minecraft.getMinecraft().getSoundHandler().playSound(new SoundConstVol(new SoundEvent(new ResourceLocation(te.soundname.trim()))));
            }
        } else {
        	if (!worldIn.isRemote) {
            	worldIn.playSound((EntityPlayer)null, pos, new SoundEvent(new ResourceLocation(te.soundname.trim())), SoundCategory.RECORDS, 3.0F, 1.0F);
            	return false;
            } else {
            	//worldIn.playSound((EntityPlayer)null, pos, new SoundEvent(new ResourceLocation(te.soundname.trim())), SoundCategory.RECORDS, 3.0F, 1.0F);
            }
        }
        worldIn.spawnParticle(EnumParticleTypes.NOTE, (double)pos.getX() + 0.5D, (double)pos.getY() + 1.2D, (double)pos.getZ() + 0.5D, (double)param / 24.0D, 0.0D, 0.0D);
        return false;
    }
	
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
				boolean constvol = false;
				if (txstr.startsWith("!")) {
					txstr = txstr.replace("!", "");
					constvol = true;
				}
				TileEntity te = worldIn.getTileEntity(pos);
				if (!(te instanceof TileEntitySpeaker)) {
					return false;
				}
				((TileEntitySpeaker) te).soundname = txstr;
				((TileEntitySpeaker) te).constvol = constvol;
				MessageSpeaker msg = new MessageSpeaker();
				msg.x = te.getPos().getX();
		        msg.y = te.getPos().getY();
		        msg.z = te.getPos().getZ();
		        msg.audioname = ((TileEntitySpeaker) te).soundname;
		        msg.constvol = ((TileEntitySpeaker) te).constvol;
		        CommonProxy.snwinstance.sendToServer(msg);
			} else {
	            int id = GuiElementLoader.Speaker;
	            playerIn.openGui(Core.instance, id, worldIn, pos.getX(), pos.getY(), pos.getZ());
			}
        }
		return true;
	}

}
