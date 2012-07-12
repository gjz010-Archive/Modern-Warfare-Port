package uberkat.war;

import net.minecraft.server.*;

class SlotGun extends Slot
{
    SlotGun(IInventory iinventory, int i, int j, int k)
    {
        super(iinventory, i, j, k);
    }

    /**
     * Returns the maximum stack size for a given slot (usually the same as getInventoryStackLimit(), but 1 in the case
     * of armor slots)
     */
    public int a()
    {
        return 1;
    }

    /**
     * Check if the stack is a valid item for this slot. Always true beside for the armor slots.
     */
    public boolean isAllowed(ItemStack itemstack)
    {
        return itemstack.getItem() instanceof ItemGun;
    }
}
