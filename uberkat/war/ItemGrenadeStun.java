package uberkat.war;

import java.util.Random;
import net.minecraft.server.*;

public class ItemGrenadeStun extends ItemWar
{
    public ItemGrenadeStun(int i)
    {
        super(i);
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack a(ItemStack itemstack, World world, EntityHuman entityhuman)
    {
        itemstack.count--;
        world.makeSound(entityhuman, "war.grunt", 1.0F, 1.0F / (c.nextFloat() * 0.1F + 0.95F));

        if (!world.isStatic)
        {
            world.addEntity(new EntityGrenadeStun(world, entityhuman));
        }

        return itemstack;
    }
}
