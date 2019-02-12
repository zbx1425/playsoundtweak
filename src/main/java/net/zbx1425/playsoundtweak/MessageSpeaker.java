package net.zbx1425.playsoundtweak;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessageSpeaker implements IMessage {
	public int x,y,z;
	public boolean constvol;
	public String audioname;
	
	@Override
    public void fromBytes(ByteBuf buf)
    {
		//System.out.println("Message Received!");
        String rawstr = ByteBufUtils.readUTF8String(buf);
        //System.out.println(rawstr);
        String[] str = rawstr.split("\\|");
        x = Integer.parseInt(str[0]);
        y = Integer.parseInt(str[1]);
        z = Integer.parseInt(str[2]);
        constvol = Boolean.parseBoolean(str[3]);
        if (str.length > 4) audioname = str[4];
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
    	//System.out.println("Message Sent!");
    	ByteBufUtils.writeUTF8String(buf, 
    			Integer.toString(x)+"|"+
    			Integer.toString(y)+"|"+
    			Integer.toString(z)+"|"+
    			Boolean.toString(constvol)+"|"+
    			audioname);
    }
    
    public static class Handler implements IMessageHandler<MessageSpeaker, IMessage>
    {
        @Override
        public IMessage onMessage(MessageSpeaker message, MessageContext ctx)
        {
        	//System.out.println("Message Handled!");
        	TileEntity te = ctx.getServerHandler().player.world.getTileEntity(new BlockPos(message.x,message.y,message.z));
        	if (te instanceof TileEntitySpeaker) {
        		((TileEntitySpeaker) te).soundname = message.audioname;
        		((TileEntitySpeaker) te).constvol = message.constvol;
        		te.markDirty();
        	}
            return null;
        }
    }

}
