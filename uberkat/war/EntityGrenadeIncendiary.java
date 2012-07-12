package uberkat.war;

import java.util.List;
import net.minecraft.server.*;

public class EntityGrenadeIncendiary extends EntityGrenade
{
    private static final int RADIUS = 2;

    public EntityGrenadeIncendiary(World world)
    {
        super(world);
        itemStack = new ItemStack(mod_ModernWarfare.itemGrenadeIncendiaryLit, 1, 0);
    }

    public EntityGrenadeIncendiary(World world, double d, double d1, double d2)
    {
        super(world, d, d1, d2);
        itemStack = new ItemStack(mod_ModernWarfare.itemGrenadeIncendiaryLit, 1, 0);
    }

    public EntityGrenadeIncendiary(World world, EntityLiving entityliving)
    {
        super(world, entityliving);
        itemStack = new ItemStack(mod_ModernWarfare.itemGrenadeIncendiaryLit, 1, 0);
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void F_()
    {
        super.F_();
        List list = world.getEntities(this, boundingBox);

        if (list.size() > 0)
        {
            Entity entity = (Entity)list.get(0);

            if (entity instanceof EntityLiving)
            {
                entity.setOnFire(300);
                explode();
            }
        }
    }

    protected void handleBounce()
    {
        explode();
    }

    protected void explode()
    {
        if (!exploded)
        {
            exploded = true;
            int i = (int)Math.floor(locX);
            int j = (int)Math.floor(locY);
            int k = (int)Math.floor(locZ);

            for (int l = -2; l <= 2; l++)
            {
                for (int i1 = -2; i1 <= 2; i1++)
                {
                    for (int j1 = -2; j1 <= 2; j1++)
                    {
                        int k1 = Math.abs(l) + Math.abs(i1) + Math.abs(j1);

                        if (k1 <= 2 && world.isEmpty(i + l, j + i1, k + j1))
                        {
                            world.setTypeId(i + l, j + i1, k + j1, Block.FIRE.id);
                        }
                    }
                }
            }

            die();
        }
    }
}
