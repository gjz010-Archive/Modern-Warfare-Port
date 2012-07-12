package uberkat.war;

import net.minecraft.server.*;

public class ContainerAtv extends Container
{
    private EntityAtv atv;

    public ContainerAtv(IInventory iinventory, EntityAtv entityatv)
    {
        atv = entityatv;
        a(new SlotGun(entityatv, 0, 56, 17));
        a(new SlotGun(entityatv, 1, 104, 17));

        for (int i = 0; i < 3; i++)
        {
            for (int k = 0; k < 9; k++)
            {
                a(new Slot(iinventory, k + i * 9 + 9, 8 + k * 18, 48 + i * 18));
            }
        }

        for (int j = 0; j < 9; j++)
        {
            a(new Slot(iinventory, j, 8 + j * 18, 106));
        }
    }

    public boolean b(EntityHuman entityhuman)
    {
        return atv.a(entityhuman);
    }
    //Hook by cpw
}
