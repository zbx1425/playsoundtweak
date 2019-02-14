package net.zbx1425.playsoundtweak;
import net.zbx1425.playsoundtweak.CommonProxy;

import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.io.IOUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSound;
import net.minecraft.client.audio.Sound;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.IResource;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;


public class ClientProxy extends CommonProxy
{
	public static boolean validateResource(ResourceLocation resourcelocation) {
		return (new SoundConstVol(new SoundEvent(resourcelocation))
				.createAccessor(Minecraft.getMinecraft().getSoundHandler()) != null);
	}
	
    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        super.preInit(event);
    }

    @Override
    public void init(FMLInitializationEvent event)
    {
        super.init(event);
        new GuiElementLoader();
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityNetherCrow.class, new NetherCrowSpecialRenderer());
    }

    @Override
    public void postInit(FMLPostInitializationEvent event)
    {
        super.postInit(event);
    }
}