package uberkat.war;

import net.minecraft.server.*;

public class ItemCraftingPack extends Item
{
    public ItemCraftingPack(int i)
    {
        super(i);
        e(1);
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack a(ItemStack itemstack, World world, EntityHuman entityhuman)
    {
        entityhuman.openGui(mod_ModernWarfare.instance, 14, entityhuman.world, (int)entityhuman.locX, (int)entityhuman.locY, (int)entityhuman.locZ);
        return itemstack;
    }
}
