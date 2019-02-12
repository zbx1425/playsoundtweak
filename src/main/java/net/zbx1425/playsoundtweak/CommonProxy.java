package net.zbx1425.playsoundtweak;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(modid="playsoundtweak")
public class CommonProxy
{
	public static Block speakerBlock = new BlockSpeaker();
	public static SimpleNetworkWrapper snwinstance = NetworkRegistry.INSTANCE.newSimpleChannel("playsoundtweak");

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		//System.out.println("Registering Blocks!");
		event.getRegistry().registerAll(speakerBlock);
		GameRegistry.registerTileEntity(TileEntitySpeaker.class, new ResourceLocation("playsoundtweak","tileentityspeaker"));
	}
	
	@SubscribeEvent
	public static void registerItemBlocks(RegistryEvent.Register<Item> event) {
		//System.out.println("Registering ItemBlocks!");
		event.getRegistry().registerAll(new ItemBlock(speakerBlock).setRegistryName(speakerBlock.getRegistryName()));
	}
	
	@SubscribeEvent
	public static void registerRenders(ModelRegistryEvent event) {
		//System.out.println("Registering Renders!");
		registerRender(Item.getItemFromBlock(speakerBlock));
	}
	
	public static void registerRender(Item item) {
		  ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
	}

    public void init(FMLInitializationEvent event)
    {
    	
    }

    public void postInit(FMLPostInitializationEvent event)
    {

    }

	public void preInit(FMLPreInitializationEvent event) {
		snwinstance.registerMessage(MessageSpeaker.Handler.class, MessageSpeaker.class, 0, Side.SERVER);
	}
}