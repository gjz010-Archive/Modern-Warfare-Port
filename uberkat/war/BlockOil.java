package uberkat.war;

import java.util.Random;
import net.minecraft.server.*;

public class BlockOil extends BlockWar
{
    private int renderType;

    public BlockOil(int i, int j, int k)
    {
        super(i, j, Material.ORIENTABLE);
        renderType = k;
        a(0.0F, 0.0F, 0.0F, 1.0F, 0.0625F, 1.0F);
        a(true);
        r[i] = true;
    }

    /**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
    public AxisAlignedBB e(World world, int i, int j, int k)
    {
        return null;
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    public boolean a()
    {
        return false;
    }

    /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
    public boolean b()
    {
        return false;
    }

    /**
     * The type of render function that is called for this block
     */
    public int c()
    {
        return renderType;
    }

    /**
     * How many world ticks before ticking
     */
    public int d()
    {
        return 100;
    }

    /**
     * Ticks the block if it's been scheduled
     */
    public void a(World world, int i, int j, int k, Random random)
    {
        int l = world.getData(i, j, k);
        int i1 = world.getTypeId(i, j + 1, k);

        if (!setFireAbove(world, i, j, k, l) && l > 0)
        {
            if (--l == 0)
            {
                if (i1 == Block.FIRE.id)
                {
                    world.setRawTypeId(i, j + 1, k, 0);
                }

                world.setTypeId(i, j, k, 0);
            }
            else
            {
                world.setData(i, j, k, l);
            }
        }

        if (l >= 13)
        {
            world.c(i, j, k, id, 1);
        }
        else
        {
            world.c(i, j, k, id, d());
        }
    }

    private boolean setFireAbove(World world, int i, int j, int k, int l)
    {
        int i1 = world.getTypeId(i, j + 1, k);

        if (l > 0 && i1 == 0)
        {
            world.setTypeId(i, j + 1, k, Block.FIRE.id);
            return true;
        }

        if (i1 == Block.FIRE.id)
        {
            world.setData(i, j + 1, k, 15);
        }

        return false;
    }

    /**
     * Checks to see if its valid to put this block at the specified coordinates. Args: world, x, y, z
     */
    public boolean canPlace(World world, int i, int j, int k)
    {
        return world.r(i, j - 1, k);
    }

    private void notifyOilNeighborsOfNeighborChange(World world, int i, int j, int k)
    {
        if (world.getTypeId(i, j, k) != id)
        {
            return;
        }
        else
        {
            world.applyPhysics(i, j, k, id);
            world.applyPhysics(i - 1, j, k, id);
            world.applyPhysics(i + 1, j, k, id);
            world.applyPhysics(i, j, k - 1, id);
            world.applyPhysics(i, j, k + 1, id);
            world.applyPhysics(i, j - 1, k, id);
            world.applyPhysics(i, j + 1, k, id);
            return;
        }
    }

    /**
     * Called whenever the block is added into the world. Args: world, x, y, z
     */
    public void onPlace(World world, int i, int j, int k)
    {
        super.onPlace(world, i, j, k);

        if (world.isStatic)
        {
            return;
        }

        world.applyPhysics(i, j + 1, k, id);
        world.applyPhysics(i, j - 1, k, id);
        notifyOilNeighborsOfNeighborChange(world, i - 1, j, k);
        notifyOilNeighborsOfNeighborChange(world, i + 1, j, k);
        notifyOilNeighborsOfNeighborChange(world, i, j, k - 1);
        notifyOilNeighborsOfNeighborChange(world, i, j, k + 1);

        if (world.r(i - 1, j, k))
        {
            notifyOilNeighborsOfNeighborChange(world, i - 1, j + 1, k);
        }
        else
        {
            notifyOilNeighborsOfNeighborChange(world, i - 1, j - 1, k);
        }

        if (world.r(i + 1, j, k))
        {
            notifyOilNeighborsOfNeighborChange(world, i + 1, j + 1, k);
        }
        else
        {
            notifyOilNeighborsOfNeighborChange(world, i + 1, j - 1, k);
        }

        if (world.r(i, j, k - 1))
        {
            notifyOilNeighborsOfNeighborChange(world, i, j + 1, k - 1);
        }
        else
        {
            notifyOilNeighborsOfNeighborChange(world, i, j - 1, k - 1);
        }

        if (world.r(i, j, k + 1))
        {
            notifyOilNeighborsOfNeighborChange(world, i, j + 1, k + 1);
        }
        else
        {
            notifyOilNeighborsOfNeighborChange(world, i, j - 1, k + 1);
        }

        checkForFire(world, i, j, k);
    }

    /**
     * Called whenever the block is removed.
     */
    public void remove(World world, int i, int j, int k)
    {
        super.remove(world, i, j, k);

        if (world.isStatic)
        {
            return;
        }

        world.applyPhysics(i, j + 1, k, id);
        world.applyPhysics(i, j - 1, k, id);
        notifyOilNeighborsOfNeighborChange(world, i - 1, j, k);
        notifyOilNeighborsOfNeighborChange(world, i + 1, j, k);
        notifyOilNeighborsOfNeighborChange(world, i, j, k - 1);
        notifyOilNeighborsOfNeighborChange(world, i, j, k + 1);

        if (world.r(i - 1, j, k))
        {
            notifyOilNeighborsOfNeighborChange(world, i - 1, j + 1, k);
        }
        else
        {
            notifyOilNeighborsOfNeighborChange(world, i - 1, j - 1, k);
        }

        if (world.r(i + 1, j, k))
        {
            notifyOilNeighborsOfNeighborChange(world, i + 1, j + 1, k);
        }
        else
        {
            notifyOilNeighborsOfNeighborChange(world, i + 1, j - 1, k);
        }

        if (world.r(i, j, k - 1))
        {
            notifyOilNeighborsOfNeighborChange(world, i, j + 1, k - 1);
        }
        else
        {
            notifyOilNeighborsOfNeighborChange(world, i, j - 1, k - 1);
        }

        if (world.r(i, j, k + 1))
        {
            notifyOilNeighborsOfNeighborChange(world, i, j + 1, k + 1);
        }
        else
        {
            notifyOilNeighborsOfNeighborChange(world, i, j - 1, k + 1);
        }
    }

    /**
     * Called when the block is clicked by a player. Args: x, y, z, entityPlayer
     */
    public void attack(World world, int i, int j, int k, EntityHuman entityhuman)
    {
        setFireAbove(world, i, j, k, world.getData(i, j, k));
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor blockID
     */
    public void doPhysics(World world, int i, int j, int k, int l)
    {
        if (world.isStatic)
        {
            return;
        }

        int i1 = world.getData(i, j, k);
        boolean flag = canPlace(world, i, j, k);

        if (!flag)
        {
            b(world, i, j, k, i1, 0);
            world.setTypeId(i, j, k, 0);
        }
        else
        {
            checkForFire(world, i, j, k);
        }

        super.doPhysics(world, i, j, k, l);
    }

    public boolean checkForFire(World world, int i, int j, int k)
    {
        if (world.getData(i, j, k) == 0)
        {
            for (int l = i - 1; l <= i + 1; l++)
            {
                for (int i1 = j - 1; i1 <= j + 1; i1++)
                {
                    for (int j1 = k - 1; j1 <= k + 1; j1++)
                    {
                        int k1 = Math.abs(i - l) + Math.abs(k - j1);

                        if ((k1 != 0 || i1 == 0) && (k1 != 1 || i1 < 0))
                        {
                            continue;
                        }

                        int l1 = world.getTypeId(l, i1, j1);

                        if (l1 != Block.FIRE.id && (l1 != id || world.getData(l, i1, j1) <= 0))
                        {
                            continue;
                        }

                        world.setData(i, j, k, 15);
                        setFireAbove(world, i, j, k, 15);
                        world.c(i, j, k, id, 1);

                        for (int i2 = i - 1; i2 <= i + 1; i2++)
                        {
                            for (int j2 = k - 1; j2 <= k + 1; j2++)
                            {
                                int k2 = Math.abs(i - l) + Math.abs(k - j1);

                                if (k2 == 1 && world.getTypeId(i2, i1 - 1, j2) == id)
                                {
                                    doPhysics(world, i2, i1 - 1, j2, id);
                                }
                            }
                        }

                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Returns the ID of the items to drop on destruction.
     */
    public int getDropType(int i, Random random, int j)
    {
        return 0;
    }

    /**
     * Called upon block activation (left or right click on the block.). The three integers represent x,y,z of the
     * block.
     */
    public boolean interact(World world, int i, int j, int k, EntityHuman entityhuman)
    {
        if (entityhuman.inventory.getItemInHand() != null && entityhuman.inventory.getItemInHand().id == Item.BUCKET.id)
        {
            entityhuman.inventory.items[entityhuman.inventory.itemInHandIndex] = new ItemStack(mod_ModernWarfare.itemOil);
            entityhuman.inventory.getItemInHand().setData((mod_ModernWarfare.itemOil.getMaxDurability() + 1) - 4);
            world.setTypeId(i, j, k, 0);
            return true;
        }
        else
        {
            return false;
        }
    }
}
