package uberkat.war;

import net.minecraft.server.*;

public class ItemOil extends ItemWar
{
    public ItemOil(int i)
    {
        super(i);
        setMaxDurability(63);
        maxStackSize = 1;
    }

    /**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS !
     */
    public boolean interactWith(ItemStack itemstack, EntityHuman entityhuman, World world, int i, int j, int k, int l)
    {
        if (l == 0)
        {
            j--;
        }

        if (l == 1)
        {
            j++;
        }

        if (l == 2)
        {
            k--;
        }

        if (l == 3)
        {
            k++;
        }

        if (l == 4)
        {
            i--;
        }

        if (l == 5)
        {
            i++;
        }

        if (world.getTypeId(i, j - 1, k) == mod_ModernWarfare.blockOil.id && itemstack.getData() >= 4)
        {
            itemstack.damage(-4, entityhuman);
            world.setTypeId(i, j - 1, k, 0);
        }

        if (!world.isEmpty(i, j, k))
        {
            return false;
        }

        int i1 = (itemstack.getItem().getMaxDurability() + 1) - itemstack.getData();

        if (mod_ModernWarfare.blockOil.canPlace(world, i, j, k) && i1 >= 4)
        {
            if (i1 == 4)
            {
                entityhuman.inventory.items[entityhuman.inventory.itemInHandIndex] = new ItemStack(Item.BUCKET);
                itemstack.count = 1;
            }
            else
            {
                itemstack.damage(4, entityhuman);
            }

            world.setTypeId(i, j, k, mod_ModernWarfare.blockOil.id);
        }

        return true;
    }
}
