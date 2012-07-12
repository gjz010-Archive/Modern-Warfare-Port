package uberkat.war;

import java.util.List;
import net.minecraft.server.*;

public class ContainerCraftingPack extends Container
{
    public InventoryCrafting craftMatrix;
    public IInventory craftResult;
    private World worldObj;

    public ContainerCraftingPack(PlayerInventory playerinventory, World world)
    {
        craftMatrix = new InventoryCrafting(this, 3, 3);
        craftResult = new InventoryCraftResult();
        worldObj = world;
        craftMatrix = new InventoryCrafting(this, 3, 3);
        craftResult = new InventoryCraftResult();
        a(new SlotResult(playerinventory.player, craftMatrix, craftResult, 0, 124, 35));

        for (int i = 0; i < 3; i++)
        {
            for (int l = 0; l < 3; l++)
            {
                a(new Slot(craftMatrix, l + i * 3, 30 + l * 18, 17 + i * 18));
            }
        }

        for (int j = 0; j < 3; j++)
        {
            for (int i1 = 0; i1 < 9; i1++)
            {
                a(new Slot(playerinventory, i1 + j * 9 + 9, 8 + i1 * 18, 84 + j * 18));
            }
        }

        for (int k = 0; k < 9; k++)
        {
            a(new Slot(playerinventory, k, 8 + k * 18, 142));
        }

        a(craftMatrix);
    }

    /**
     * Callback for when the crafting matrix is changed.
     */
    public void a(IInventory iinventory)
    {
        craftResult.setItem(0, CraftingManager.getInstance().craft(craftMatrix));
    }

    /**
     * Callback for when the crafting gui is closed.
     */
    public void a(EntityHuman entityhuman)
    {
        super.a(entityhuman);

        if (worldObj.isStatic)
        {
            return;
        }

        for (int i = 0; i < 9; i++)
        {
            ItemStack itemstack = craftMatrix.splitWithoutUpdate(i);

            if (itemstack != null)
            {
                entityhuman.drop(itemstack);
            }
        }
    }

    public boolean b(EntityHuman entityhuman)
    {
        return true;
    }

    /**
     * Called to transfer a stack from one inventory to the other eg. when shift clicking.
     */
    public ItemStack a(int i)
    {
        ItemStack itemstack = null;
        Slot slot = (Slot)e.get(i);

        if (slot != null && slot.c())
        {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.cloneItemStack();

            if (i == 0)
            {
                if (!a(itemstack1, 10, 46, true))
                {
                    return null;
                }

                slot.a(itemstack1, itemstack);
            }
            else if (i >= 10 && i < 37)
            {
                if (!a(itemstack1, 37, 46, false))
                {
                    return null;
                }
            }
            else if (i >= 37 && i < 46)
            {
                if (!a(itemstack1, 10, 37, false))
                {
                    return null;
                }
            }
            else if (!a(itemstack1, 10, 46, false))
            {
                return null;
            }

            if (itemstack1.count == 0)
            {
                slot.set(null);
            }
            else
            {
                slot.d();
            }

            if (itemstack1.count != itemstack.count)
            {
                slot.c(itemstack1);
            }
            else
            {
                return null;
            }
        }

        return itemstack;
    }
}
