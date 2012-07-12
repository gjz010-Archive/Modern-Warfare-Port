package uberkat.war;

import java.util.ArrayList;
import java.util.Random;
import net.minecraft.server.*;

public class BlockNuke extends BlockWar
{
    public BlockNuke(int i, int j)
    {
        super(i, j, Material.TNT);
        a(true);
    }

    /**
     * Returns the block texture based on the side being looked at.  Args: side
     */
    public int a(int i)
    {
        if (i == 0)
        {
            return textureId + 5;
        }

        if (i == 1)
        {
            return textureId + 4;
        }
        else
        {
            return textureId;
        }
    }

    /**
     * How many world ticks before ticking
     */
    public int d()
    {
        return 40;
    }

    /**
     * Called whenever the block is added into the world. Args: world, x, y, z
     */
    public void onPlace(World world, int i, int j, int k)
    {
        world.c(i, j, k, id, d());
    }

    /**
     * Ticks the block if it's been scheduled
     */
    public void a(World world, int i, int j, int k, Random random)
    {
        if (!checkExplode(world, i, j, k))
        {
            world.c(i, j, k, id, d());
        }
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor blockID
     */
    public void doPhysics(World world, int i, int j, int k, int l)
    {
        if (l > 0 && Block.byId[l].isPowerSource() && world.isBlockIndirectlyPowered(i, j, k))
        {
            postBreak(world, i, j, k, 1);
            world.setTypeId(i, j, k, 0);
        }
    }

    public boolean checkExplode(World world, int i, int j, int k)
    {
        for (int l = i - 1; l <= i + 1; l++)
        {
            for (int i1 = j - 1; i1 <= j + 1; i1++)
            {
                for (int j1 = k - 1; j1 <= k + 1; j1++)
                {
                    if (Math.abs(i - l) + Math.abs(k - j1) + Math.abs(j - i1) != 0 && world.getTypeId(l, i1, j1) == Block.FIRE.id)
                    {
                        explode(world, i, j, k);
                        world.setTypeId(i, j, k, 0);
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    public int a(Random random)
    {
        return 0;
    }

    /**
     * Called upon the block being destroyed by an explosion
     */
    public void wasExploded(World world, int i, int j, int k)
    {
        EntityNukePrimed entitynukeprimed = new EntityNukePrimed(world, (float)i + 0.5F, (float)j + 0.5F, (float)k + 0.5F);
        entitynukeprimed.fuse = world.random.nextInt(entitynukeprimed.fuse / 4) + entitynukeprimed.fuse / 8;
        world.addEntity(entitynukeprimed);
    }

    /**
     * Called right before the block is destroyed by a player.  Args: world, x, y, z, metaData
     */
    public void postBreak(World world, int i, int j, int k, int l)
    {
        if (world.isStatic)
        {
            return;
        }

        if ((l & 1) == 0)
        {
            a(world, i, j, k, new ItemStack(mod_ModernWarfare.blockNuke.id, 1, 0));
        }
        else
        {
            explode(world, i, j, k);
        }
    }

    private void explode(World world, int i, int j, int k)
    {
        EntityNukePrimed entitynukeprimed = new EntityNukePrimed(world, (float)i + 0.5F, (float)j + 0.5F, (float)k + 0.5F);
        world.addEntity(entitynukeprimed);
        world.makeSound(entitynukeprimed, "random.fuse", 1.0F, 1.0F);
    }

    /**
     * Called when the block is clicked by a player. Args: x, y, z, entityPlayer
     */
    public void attack(World world, int i, int j, int k, EntityHuman entityhuman)
    {
        if (entityhuman.U() != null && entityhuman.U().id == Item.FLINT_AND_STEEL.id)
        {
            world.setRawData(i, j, k, 1);
        }

        super.attack(world, i, j, k, entityhuman);
    }

    public void addCreativeItems(ArrayList arraylist)
    {
        arraylist.add(new ItemStack(this));
    }
}
