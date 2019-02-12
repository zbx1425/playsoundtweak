package net.zbx1425.playsoundtweak;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class GuiElementLoader implements IGuiHandler {
	public static final int Speaker = 1;
	
	public GuiElementLoader()
    {
        NetworkRegistry.INSTANCE.registerGuiHandler(Core.instance, this);
    }

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		//System.out.println("ElementLoader Received!");
		switch (ID)
        {
        case Speaker:
        	//System.out.println("Gui Decided!");
        	TileEntity te = world.getTileEntity(new BlockPos(x,y,z));
        	if (te instanceof TileEntitySpeaker) {
        		return new GuiSpeaker((TileEntitySpeaker) te);
        	} else {
        		return null;
        	}
        default:
            return null;
        }
	}

}
