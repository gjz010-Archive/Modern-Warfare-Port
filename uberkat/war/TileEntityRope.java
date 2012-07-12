package uberkat.war;

import net.minecraft.server.*;

public class TileEntityRope extends TileEntity
{
    public int delay;

    public TileEntityRope()
    {
        delay = 5;
    }

    /**
     * Allows the entity to update its state. Overridden in most subclasses, e.g. the mob spawner uses this to count
     * ticks and creates a new spawn inside its implementation.
     */
    public void q_()
    {
        if (delay == 0)
        {
            if (world.getTypeId(x, y - 1, z) == 0 || world.getTypeId(x, y - 1, z) == Block.SNOW.id)
            {
                world.setTypeId(x, y - 1, z, mod_ModernWarfare.blockRope.id);
                world.setData(x, y - 1, z, world.getData(x, y, z));
                delay--;
            }
        }
        else if (delay > 0)
        {
            delay--;
        }

        super.q_();
    }

    /**
     * Reads a tile entity from NBT.
     */
    public void a(NBTTagCompound nbttagcompound)
    {
        super.a(nbttagcompound);
        delay = nbttagcompound.getShort("Delay");
    }

    /**
     * Writes a tile entity to NBT.
     */
    public void b(NBTTagCompound nbttagcompound)
    {
        super.b(nbttagcompound);
        nbttagcompound.setShort("Delay", (short)delay);
    }
}
