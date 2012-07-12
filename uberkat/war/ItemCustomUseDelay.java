package uberkat.war;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.server.*;

public abstract class ItemCustomUseDelay extends ItemWar
{
    public boolean stopArmSwing;
    public int useDelay;
    private Map lastUses;
    public static long doNotUseThisTick = 0L;

    public ItemCustomUseDelay(int i)
    {
        super(i);
        stopArmSwing = false;
        useDelay = 5;
        lastUses = new HashMap();
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack a(ItemStack itemstack, World world, EntityHuman entityhuman)
    {
        if (world.getTime() == doNotUseThisTick)
        {
            return itemstack;
        }
        else
        {
            return onItemRightClickEntity(itemstack, world, entityhuman, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        }
    }

    public ItemStack onItemRightClickEntity(ItemStack itemstack, World world, Entity entity, float f, float f1, float f2, float f3, float f4)
    {
        long l = 0L;
        int i = 0;

        if (lastUses.containsKey(itemstack))
        {
            CustomUseDelayEntry customusedelayentry = (CustomUseDelayEntry)lastUses.get(itemstack);
            l = customusedelayentry.lastUse;
            i = customusedelayentry.lastMinecraftUse;
        }

        if (world.getTime() - l < 0L)
        {
            l = world.getTime() - (long)useDelay;
            i = getMinecraftTicksRan() - useDelay;
        }

        if (world.getTime() - l >= (long)useDelay && use(itemstack, world, entity, f, f1, f2, f3, f4))
        {
            l = world.getTime();
            i = getMinecraftTicksRan();
        }

        lastUses.put(itemstack, new CustomUseDelayEntry(l, i));
        return itemstack;
    }

    public abstract boolean use(ItemStack itemstack, World world, Entity entity, float f, float f1, float f2, float f3, float f4);

    private int getMinecraftTicksRan()
    {
        return 0;
    }
}
