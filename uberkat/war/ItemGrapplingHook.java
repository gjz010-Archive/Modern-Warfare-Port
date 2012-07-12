package uberkat.war;

import java.util.Map;
import java.util.Random;
import net.minecraft.server.*;

public class ItemGrapplingHook extends ItemWar
{
    public ItemGrapplingHook(int i)
    {
        super(i);
        maxStackSize = 1;
    }

    public boolean isFull3D()
    {
        return true;
    }

    public boolean shouldRotateAroundWhenRendering()
    {
        return true;
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack a(ItemStack itemstack, World world, EntityHuman entityhuman)
    {
        if (mod_ModernWarfare.grapplingHooks.get(entityhuman) != null)
        {
            int i = ((EntityGrapplingHook)mod_ModernWarfare.grapplingHooks.get(entityhuman)).catchFish();
            entityhuman.C_();
        }
        else
        {
            world.makeSound(entityhuman, "war.grunt", 1.0F, 1.0F / (c.nextFloat() * 0.1F + 0.95F));
            world.addEntity(new EntityGrapplingHook(world, entityhuman));
            entityhuman.C_();
        }

        return itemstack;
    }
}
