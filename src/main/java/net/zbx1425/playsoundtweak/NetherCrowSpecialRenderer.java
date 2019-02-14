package net.zbx1425.playsoundtweak;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBanner;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.FakePlayer;

public class NetherCrowSpecialRenderer extends TileEntitySpecialRenderer<TileEntityNetherCrow> {
	private final ModelPlayer playerModel = new ModelPlayer(0, false);
	
	@Override
	public void render (TileEntityNetherCrow te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		super.render(te,x,y,z,partialTicks,destroyStage,alpha);
		int k = te.getBlockMetadata();
        float f2 = 0.0F;

        if (k == 2)
        {
            f2 = 180.0F;
        }

        if (k == 4)
        {
            f2 = 90.0F;
        }

        if (k == 5)
        {
            f2 = -90.0F;
        }
        
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)x + 0.5F, (float)y + 1.5F, (float)z + 0.5F);
        GlStateManager.rotate(-f2, 0.0F, 1.0F, 0.0F);
        bindTexture(new ResourceLocation("playsoundtweak","textures/skin/nethercrow.png"));
        GlStateManager.enableRescaleNormal();
        GlStateManager.scale(-1.0F, -1.0F, 1.0F);
        GlStateManager.enableAlpha();
        GlStateManager.enableBlendProfile(GlStateManager.Profile.PLAYER_SKIN);
        playerModel.render(Minecraft.getMinecraft().player, 0, 0, -0.1f, 0, 0, 0.0625f);
        GlStateManager.popMatrix();
        //Minecraft.getMinecraft().getRenderManager().renderEntity(Minecraft.getMinecraft().player, 0.0D, 0.0D, 0.0D, 0.0F, partialTicks, false);
	}
}
