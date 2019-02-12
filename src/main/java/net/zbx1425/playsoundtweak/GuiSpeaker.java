package net.zbx1425.playsoundtweak;

import java.io.IOException;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.network.play.client.CPacketUpdateSign;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiSpeaker extends GuiScreen {
	
	TileEntitySpeaker te;
	
	public GuiSpeaker(TileEntitySpeaker te) {
		super();
		this.te = te;
		//System.out.println("Gui Loaded!");
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.drawWorldBackground(0);
		GlStateManager.scale(2, 2, 2);
		this.fontRenderer.drawStringWithShadow(I18n.format("gui.speakerblock.title"), 20, 25, -1);
		GlStateManager.scale(.5, .5, .5);
		this.fontRenderer.drawStringWithShadow(I18n.format("gui.speakerblock.credit"), 40, 70, -1);
		this.fontRenderer.drawStringWithShadow(I18n.format("gui.speakerblock.hint"), 40, 100, -1);
        this.fontRenderer.drawStringWithShadow(te.soundname, 40, 110, -1);
        if (te.constvol) {
        	this.fontRenderer.drawStringWithShadow(I18n.format("gui.speakerblock.constvol.yes"), 60, 130, -1);
        } else {
        	this.fontRenderer.drawStringWithShadow(I18n.format("gui.speakerblock.constvol.nop"), 60, 130, -1);
        }
        this.drawRect(40, 120, this.width-80, 121, -1);
    }
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        super.keyTyped(typedChar, keyCode);
        if (keyCode == 14) {
        	te.soundname = (te.soundname.substring(0, te.soundname.length() - 1)).trim();
        }
        if (keyCode == 211) {
        	te.soundname = "";
        }
        if (2<= keyCode && keyCode <=57) {
        	if (typedChar=='/'||typedChar=='\\') {
        		te.constvol = !te.constvol;
        	} else {
        		te.soundname = (te.soundname + typedChar).trim();
        	}
        }
        this.updateScreen();
    }
	
	public void onGuiClosed()
    {
        Keyboard.enableRepeatEvents(false);
        /*NetHandlerPlayClient nethandlerplayclient = this.mc.getConnection();

        if (nethandlerplayclient != null)
        {
            nethandlerplayclient.sendPacket(new CPacketUpdateSign(this.tileSign.getPos(), this.tileSign.signText));
        }

        this.tileSign.setEditable(true);*/
        
        //te.markDirty();
       // te.getWorld().scheduleBlockUpdate(te.getPos(),te.getBlockType(),0,0);
       // te.markDirty();
        MessageSpeaker msg = new MessageSpeaker();
        msg.x = te.getPos().getX();
        msg.y = te.getPos().getY();
        msg.z = te.getPos().getZ();
        msg.audioname = te.soundname;
        msg.constvol = te.constvol;
        CommonProxy.snwinstance.sendToServer(msg);
    }
}
