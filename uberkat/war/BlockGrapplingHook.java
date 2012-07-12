package uberkat.war;

import java.util.Random;
import net.minecraft.server.*;

public class BlockGrapplingHook extends BlockWar
{
    private int renderType;

    public BlockGrapplingHook(int i, int j, int k)
    {
        super(i, j, Material.WOOD);
        renderType = k;
        a(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
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
     * Checks to see if its valid to put this block at the specified coordinates. Args: world, x, y, z
     */
    public boolean canPlace(World world, int i, int j, int k)
    {
        int l = world.getTypeId(i, j - 1, k);

        if (l == 0 || !Block.byId[l].a())
        {
            return false;
        }
        else
        {
            return world.getMaterial(i, j - 1, k).isBuildable();
        }
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor blockID
     */
    public void doPhysics(World world, int i, int j, int k, int l)
    {
        canSnowStay(world, i, j, k);
    }

    private boolean canSnowStay(World world, int i, int j, int k)
    {
        if (!canPlace(world, i, j, k))
        {
            b(world, i, j, k, world.getData(i, j, k), 0);
            world.setTypeId(i, j, k, 0);
            onBlockDestroyed(world, i, j, k);
            return false;
        }
        else
        {
            return true;
        }
    }

    /**
     * Returns the ID of the items to drop on destruction.
     */
    public int getDropType(int i, Random random, int j)
    {
        return mod_ModernWarfare.itemGrapplingHook.id;
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    public int a(Random random)
    {
        return 1;
    }

    /**
     * Called right before the block is destroyed by a player.  Args: world, x, y, z, metaData
     */
    public void postBreak(World world, int i, int j, int k, int l)
    {
        onBlockDestroyed(world, i, j, k);
    }

    /**
     * Called upon the block being destroyed by an explosion
     */
    public void wasExploded(World world, int i, int j, int k)
    {
        onBlockDestroyed(world, i, j, k);
    }

    private void onBlockDestroyed(World world, int i, int j, int k)
    {
        int ai[][] =
        {
            {
                i - 1, j - 1, k
            }, {
                i + 1, j - 1, k
            }, {
                i, j - 1, k - 1
            }, {
                i, j - 1, k + 1
            }
        };

        for (int l = 0; l < ai.length; l++)
        {
            if (world.getTypeId(ai[l][0], ai[l][1], ai[l][2]) != mod_ModernWarfare.blockRope.id)
            {
                continue;
            }

            for (int i1 = ai[l][1]; world.getTypeId(ai[l][0], i1, ai[l][2]) == mod_ModernWarfare.blockRope.id; i1--)
            {
                world.setTypeId(ai[l][0], i1, ai[l][2], 0);
            }
        }
    }
}
