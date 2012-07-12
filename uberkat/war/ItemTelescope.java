package uberkat.war;

import net.minecraft.server.*;

public class ItemTelescope extends ItemWar
{
    public ItemTelescope(int i)
    {
        super(i);
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack a(ItemStack itemstack, World world, EntityHuman entityhuman)
    {
        mod_ModernWarfare.useZoom();
        return itemstack;
    }
}
