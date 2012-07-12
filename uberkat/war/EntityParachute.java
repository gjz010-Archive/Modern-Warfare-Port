package uberkat.war;

import java.util.List;
import net.minecraft.server.*;

public class EntityParachute extends EntityLiving
{
    Entity entityWearing;
    boolean justServerSpawned;
    private static final int MAX_HEALTH = 4;
    private static final float HEIGHT = 0F;
    private static final float MAX_FALL_SPEED = 0.25F;

    public EntityParachute(World world)
    {
        super(world);
        justServerSpawned = false;
        texture = "/war/mobParachute.png";
        height = 0.0F;
        health = 4;
    }

    public EntityParachute(World world, double d, double d1, double d2)
    {
        this(world);
        setPosition(d, d1, d2);
        justServerSpawned = true;
    }

    public EntityParachute(World world, Entity entity)
    {
        this(world);
        entityWearing = entity;
        height = 0.0F;
        health = 4;
        lastX = locX;
        lastY = locY;
        lastZ = locZ;
        setMotionAndPosition();
    }

    public boolean isInRangeToRenderDist(double d)
    {
        return true;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void F_()
    {
        if (entityWearing == null)
        {
            if (world.isStatic && !justServerSpawned)
            {
                dead = true;
                return;
            }

            entityWearing = getNearestPlayer();
            justServerSpawned = false;

            if (entityWearing == null)
            {
                return;
            }
        }

        if (WarTools.onGroundOrInWater(world, entityWearing))
        {
            dead = true;
            return;
        }

        if (entityWearing.motY < -0.25D)
        {
            entityWearing.motY = -0.25D;
        }

        entityWearing.fallDistance = 0.0F;
        setMotionAndPosition();
    }

    public EntityHuman getNearestPlayer()
    {
        return getNearestPlayer(locX, locY, locZ);
    }

    public EntityHuman getNearestPlayer(double d, double d1, double d2)
    {
        double d3 = -1D;
        EntityHuman entityhuman = null;

        for (int i = 0; i < world.entityList.size(); i++)
        {
            Entity entity = (Entity)world.entityList.get(i);

            if (!(entity instanceof EntityHuman) || !entity.isAlive())
            {
                continue;
            }

            double d4 = entity.e(d, d1, d2);

            if (d3 == -1D || d4 < d3)
            {
                d3 = d4;
                entityhuman = (EntityHuman)entity;
            }
        }

        return entityhuman;
    }

    private void setMotionAndPosition()
    {
        setPosition(entityWearing.locX, entityWearing.locY + (double)(entityWearing.length / 2.0F) + 0.0D, entityWearing.locZ);
        motX = entityWearing.motX;
        motY = entityWearing.motY;
        motZ = entityWearing.motZ;
        yaw = entityWearing.yaw;
    }

    /**
     * Called by a player entity when they collide with an entity
     */
    public void a_(EntityHuman entityhuman)
    {
    }

    public int getMaxHealth()
    {
        return 4;
    }
}
