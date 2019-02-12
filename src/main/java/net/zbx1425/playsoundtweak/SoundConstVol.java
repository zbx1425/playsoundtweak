package net.zbx1425.playsoundtweak;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.MovingSound;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SoundConstVol extends MovingSound
{

	private final EntityPlayerSP clientplayer;
    private float distance = 0.0F;

    public SoundConstVol(SoundEvent soundIn)
    {
        super(soundIn, SoundCategory.RECORDS);
        this.clientplayer = Minecraft.getMinecraft().player;
        this.volume=3.0f;
        //this.repeat = true;
        //this.repeatDelay = 0;
    }

    /**
     * Like the old updateEntity(), except more generic.
     */
    public void update()
    {
        if (this.clientplayer.isDead)
        {
            this.donePlaying = true;
        }
        else
        {
            this.xPosF = (float)this.clientplayer.posX;
            this.yPosF = (float)this.clientplayer.posY;
            this.zPosF = (float)this.clientplayer.posZ;
            /*float f = MathHelper.sqrt(this.clientplayer.motionX * this.clientplayer.motionX + this.clientplayer.motionZ * this.clientplayer.motionZ);

            if ((double)f >= 0.01D)
            {
                this.distance = MathHelper.clamp(this.distance + 0.0025F, 0.0F, 1.0F);
                this.volume = 0.0F + MathHelper.clamp(f, 0.0F, 0.5F) * 0.7F;
            }
            else
            {
                this.distance = 0.0F;
                this.volume = 0.0F;
            }*/
        }
    }
}