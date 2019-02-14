package net.zbx1425.playsoundtweak;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageSpeaker implements IMessage {
	public int type;
	public int x,y,z;
	public boolean constvol;
	public boolean longtrig;
	public String audioname = "";
	public String rawstr;
	
	@Override
    public void fromBytes(ByteBuf buf)
    {
		//System.out.println("Message Received!");
        this.rawstr = ByteBufUtils.readUTF8String(buf);
        //System.out.println(rawstr);
        String[] str = rawstr.split("\\|");
        type = Integer.parseInt(str[0]);
        x = Integer.parseInt(str[1]);
        y = Integer.parseInt(str[2]);
        z = Integer.parseInt(str[3]);
        constvol = Boolean.parseBoolean(str[4]);
        longtrig = Boolean.parseBoolean(str[5]);
        if (str.length > 6) audioname = str[6];
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
    	//System.out.println("Message Sent!");
    	this.rawstr= 
    			Integer.toString(type)+"|"+
    			Integer.toString(x)+"|"+
    			Integer.toString(y)+"|"+
    			Integer.toString(z)+"|"+
    			Boolean.toString(constvol)+"|"+
    			Boolean.toString(longtrig)+"|"+
    			audioname;
    	ByteBufUtils.writeUTF8String(buf, rawstr);
    }
    
    public static class HandlerServer implements IMessageHandler<MessageSpeaker, IMessage>
    {
        @Override
        public IMessage onMessage(MessageSpeaker message, MessageContext ctx)
        {
        	if (ctx.side != Side.SERVER) return null;
        	System.out.println("Message Handled Server! " + message.rawstr);
        	TileEntity teraw = ctx.getServerHandler().player.world.getTileEntity(new BlockPos(message.x,message.y,message.z));
        	if (!(teraw instanceof TileEntitySpeaker)) return null;
        	TileEntitySpeaker te = (TileEntitySpeaker) teraw;
        	switch (message.type) {
        	case 0:
        		te.soundname = message.audioname;
        		te.constvol = message.constvol;
        		te.longtrig = message.longtrig;
        		te.markDirty();
        		CommonProxy.snwinstance.sendToAll(message);
	            break;
        	case 1:
        	case 2:
        		CommonProxy.snwinstance.sendToAllAround(message,new TargetPoint(
        				ctx.getServerHandler().player.world.provider.getDimension(),message.x,message.y,message.z,128));
        		break;
        	}
        	return null;
        }
    }
    
    public static class HandlerClient implements IMessageHandler<MessageSpeaker, IMessage>
    {
        @Override
        public IMessage onMessage(MessageSpeaker message, MessageContext ctx)
        {
        	if (ctx.side != Side.CLIENT) return null;
        	System.out.println("Message Handled Client! " + message.rawstr);
        	TileEntity teraw = FMLClientHandler.instance().getClient().world.getTileEntity(new BlockPos(message.x, message.y, message.z));
        	if (!(teraw instanceof TileEntitySpeaker)) return null;
        	TileEntitySpeaker te = (TileEntitySpeaker) teraw;
        	switch (message.type) {
        	case 0:
        		te.soundname = message.audioname;
        		te.constvol = message.constvol;
        		te.longtrig = message.longtrig;
        		te.markDirty();
	        	break;
        	case 1:
        		if (ClientProxy.validateResource(new ResourceLocation(te.soundname.trim()))) {
	        		if (te.constvol) {
	        			te.soundcache = new SoundConstVol(new SoundEvent(new ResourceLocation(te.soundname.trim())));
	                } else {
	                	te.soundcache = new PositionedSoundRecord(new SoundEvent(new ResourceLocation(te.soundname.trim())), 
	        					SoundCategory.RECORDS, 3.0f, 1.0f, message.x, message.y, message.z);
	                }
	        		Minecraft.getMinecraft().addScheduledTask(()->{
	        			Minecraft.getMinecraft().getSoundHandler().playSound((ISound) te.soundcache);
	            		FMLClientHandler.instance().getClient().world.spawnParticle(EnumParticleTypes.NOTE, 
	            				(double)message.x + 0.5D, (double)message.y + 1.2D, (double)message.z + 0.5D, 
	            				0.0D, 0.0D, 0.0D);
	        		});
        		}
        		break;
        	case 2:
        		Minecraft.getMinecraft().addScheduledTask(()->{
        			Minecraft.getMinecraft().getSoundHandler().stopSound((ISound) te.soundcache);
        		});
        		break;
        	}
            return null;
        }
    }

}
