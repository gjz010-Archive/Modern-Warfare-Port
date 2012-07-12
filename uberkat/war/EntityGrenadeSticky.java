package uberkat.war;

import java.util.List;
import net.minecraft.server.*;

public class EntityGrenadeSticky extends EntityGrenade
{
    protected boolean stuckToBlock;
    protected Entity stuckToEntity;
    protected Point3d stuckToEntityOffset;

    public EntityGrenadeSticky(World world)
    {
        super(world);
        stuckToBlock = false;
        stuckToEntity = null;
        stuckToEntityOffset = null;
        itemStack = new ItemStack(mod_ModernWarfare.itemGrenadeSticky, 1, 0);
    }

    public EntityGrenadeSticky(World world, double d, double d1, double d2)
    {
        super(world, d, d1, d2);
        stuckToBlock = false;
        stuckToEntity = null;
        stuckToEntityOffset = null;
        itemStack = new ItemStack(mod_ModernWarfare.itemGrenadeSticky, 1, 0);
    }

    public EntityGrenadeSticky(World world, EntityLiving entityliving)
    {
        super(world, entityliving);
        stuckToBlock = false;
        stuckToEntity = null;
        stuckToEntityOffset = null;
        itemStack = new ItemStack(mod_ModernWarfare.itemGrenadeSticky, 1, 0);
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void F_()
    {
        if (stuckToEntity != null && stuckToEntity.dead)
        {
            stuckToEntity = null;
        }

        if (!stuckToBlock && stuckToEntity == null)
        {
            super.F_();
        }
        else
        {
            handleExplode();
        }

        if (stuckToEntity == null)
        {
            List list = world.getEntities(this, boundingBox);

            if (list.size() > 0)
            {
                Entity entity = (Entity)list.get(0);

                if (entity instanceof EntityLiving)
                {
                    stuckToBlock = false;
                    stuckToEntity = entity;
                    stuckToEntityOffset = new Point3d(Double.valueOf(locX - entity.locX), Double.valueOf(locY - entity.locY), Double.valueOf(locZ - entity.locZ));
                }
            }
        }
        else
        {
            lastX = locX;
            lastY = locY;
            lastZ = locZ;
            locX = stuckToEntity.locX + ((Double)stuckToEntityOffset.x).doubleValue();
            locY = stuckToEntity.locY + ((Double)stuckToEntityOffset.y).doubleValue();
            locZ = stuckToEntity.locZ + ((Double)stuckToEntityOffset.z).doubleValue();
        }
    }

    protected void handleBounce()
    {
        if (stuckToEntity == null)
        {
            stuckToBlock = true;
            motX = motY = motZ = 0.0D;
        }
    }
}
