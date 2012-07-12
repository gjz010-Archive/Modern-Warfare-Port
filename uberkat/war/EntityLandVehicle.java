package uberkat.war;

import java.util.List;
import net.minecraft.server.*;

public abstract class EntityLandVehicle extends Entity
{
    private double lastTurnSpeed;
    public boolean lastOnGround;
    public int health;
    public double prevMotionX;
    public double prevMotionY;
    public double prevMotionZ;
    public Entity lastCollidedEntity;
    public double ACCEL_FORWARD_STOPPED;
    public double ACCEL_FORWARD_FULL;
    public double ACCEL_BACKWARD_STOPPED;
    public double ACCEL_BACKWARD_FULL;
    public double ACCEL_BRAKE;
    public double TURN_SPEED_STOPPED;
    public double TURN_SPEED_FULL;
    public double MAX_SPEED;
    public double FALL_SPEED;
    public double ROTATION_PITCH_DELTA_MAX;
    public double SPEED_MULT_WATER;
    public double SPEED_MULT_UNMOUNTED;
    public double SPEED_MULT_DECEL;
    public double STOP_SPEED;
    public double TURN_SPEED_RENDER_MULT;
    public double COLLISION_SPEED_MIN;
    public int COLLISION_DAMAGE_ENTITY;
    public int COLLISION_DAMAGE_SELF;
    public int MAX_HEALTH;
    public boolean COLLISION_DAMAGE;
    public boolean COLLISION_FLIGHT_PLAYER;
    public boolean COLLISION_FLIGHT_ENTITY;

    public EntityLandVehicle(World world)
    {
        super(world);
        lastTurnSpeed = 0.0D;
        lastOnGround = true;
        prevMotionX = 0.0D;
        prevMotionY = 0.0D;
        prevMotionZ = 0.0D;
        lastCollidedEntity = null;
        ACCEL_FORWARD_STOPPED = 0.02D;
        ACCEL_FORWARD_FULL = 0.0050000000000000001D;
        ACCEL_BACKWARD_STOPPED = 0.01D;
        ACCEL_BACKWARD_FULL = 0.0025000000000000001D;
        ACCEL_BRAKE = 0.040000000000000001D;
        TURN_SPEED_STOPPED = 10D;
        TURN_SPEED_FULL = 2D;
        MAX_SPEED = 0.75D;
        FALL_SPEED = 0.059999999999999998D;
        ROTATION_PITCH_DELTA_MAX = 10D;
        SPEED_MULT_WATER = 0.90000000000000002D;
        SPEED_MULT_UNMOUNTED = 0.94999999999999996D;
        SPEED_MULT_DECEL = 0.94999999999999996D;
        STOP_SPEED = 0.01D;
        TURN_SPEED_RENDER_MULT = 2D;
        COLLISION_SPEED_MIN = 0.5D;
        COLLISION_DAMAGE_ENTITY = 10;
        COLLISION_DAMAGE_SELF = 10;
        MAX_HEALTH = 100;
        COLLISION_DAMAGE = true;
        COLLISION_FLIGHT_PLAYER = true;
        COLLISION_FLIGHT_ENTITY = true;
        bf = true;
        height = length / 2.0F;
        health = MAX_HEALTH;
    }

    public EntityLandVehicle(World world, double d, double d1, double d2)
    {
        this(world);
        setPosition(d, d1 + (double)height, d2);
        motX = 0.0D;
        motY = 0.0D;
        motZ = 0.0D;
        lastX = d;
        lastY = d1;
        lastZ = d2;
    }

    protected void b()
    {
    }

    /**
     * Returns a boundingBox used to collide the entity with other entities and blocks. This enables the entity to be
     * pushable on contact, like boats or minecarts.
     */
    public AxisAlignedBB b_(Entity entity)
    {
        return entity.boundingBox;
    }

    /**
     * returns the bounding box for this entity
     */
    public AxisAlignedBB h()
    {
        return null;
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean damageEntity(DamageSource damagesource, int i)
    {
        if (MAX_HEALTH != -1)
        {
            onHurt();
            health -= i;

            if (health <= 0)
            {
                onDeath();
            }
        }

        return true;
    }

    public void onHurt()
    {
    }

    public void onDeath()
    {
        die();
    }

    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    public boolean o_()
    {
        return !dead;
    }

    /**
     * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
     */
    public boolean b(EntityHuman entityhuman)
    {
        if (passenger != null && (passenger instanceof EntityHuman) && passenger != entityhuman)
        {
            return true;
        }

        if (!world.isStatic)
        {
            entityhuman.mount(this);
        }

        return true;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void F_()
    {
        super.F_();
        lastX = locX;
        lastY = locY;
        lastZ = locZ;

        if (getSpeed() > 0.0D)
        {
            double d = getMotionYaw();
            double d1 = (double)yaw - d;
            projectMotion(d1);
        }

        boolean flag = false;
        boolean flag1 = true;

        if (getSpeed() != 0.0D)
        {
            double d2 = ((double)yaw * Math.PI) / 180D;
            double d6 = Math.cos(d2);
            flag1 = -d6 > 0.0D && motX > 0.0D || -d6 < 0.0D && motX < 0.0D;
        }

        if (onGround)
        {
            if (passenger != null)
            {
                if (getSpeed() != 0.0D)
                {
                    double d3 = 0.0D;

                    if (d3 != 0.0D)
                    {
                        yaw += d3;
                        projectMotion(d3);
                    }

                    lastTurnSpeed = d3 * (double)(flag1 ? 1 : -1);
                }

                double d4 = 0.0D;

                if (d4 != 0.0D)
                {
                    double d7 = ((double)yaw * Math.PI) / 180D;
                    double d8 = Math.cos(d7);
                    double d9 = Math.sin(d7);
                    motX += d4 * d8;
                    motZ += d4 * d9;
                }
            }

            if (!flag)
            {
                multiplySpeed(SPEED_MULT_DECEL);
            }

            if (passenger == null)
            {
                multiplySpeed(SPEED_MULT_UNMOUNTED);
            }

            double d5 = getSpeed();

            if (d5 > MAX_SPEED)
            {
                multiplySpeed(MAX_SPEED / d5);
            }
        }

        if (h_())
        {
            multiplySpeed(SPEED_MULT_WATER);
        }

        if (!flag && getSpeed() < STOP_SPEED)
        {
            multiplySpeed(0.0D);
        }

        move(motX, motY, motZ);
        int i = flag1 ? 1 : -1;

        if (onGround && lastOnGround)
        {
            if (lastY - locY > 0.01D)
            {
                pitch = 45 * i;
            }
            else if (lastY - locY < -0.01D)
            {
                pitch = -45 * i;
            }
            else
            {
                pitch = 0.0F;
            }

            motY -= 0.001D;
        }
        else
        {
            setRotationPitch(Math.max(Math.min((float)((-90D * motY) / getSpeed()) * (float)i, 90F), -90F) / 2.0F);
            motY = locY - lastY - FALL_SPEED;
        }

        lastOnGround = onGround;
        List list = world.getEntities(this, boundingBox.grow(0.20000000000000001D, 0.0D, 0.20000000000000001D));

        if (list != null && list.size() > 0)
        {
            for (int j = 0; j < list.size(); j++)
            {
                Entity entity = (Entity)list.get(j);

                if (entity != passenger && entity.e_())
                {
                    handleCollision(entity);
                }
            }
        }

        if (passenger != null && getPrevSpeed() - getSpeed() > COLLISION_SPEED_MIN)
        {
            if (lastCollidedEntity != null)
            {
                if (COLLISION_FLIGHT_ENTITY)
                {
                    lastCollidedEntity.b_(prevMotionX, prevMotionY + 1.0D, prevMotionZ);
                }

                if (COLLISION_DAMAGE)
                {
                    lastCollidedEntity.damageEntity(DamageSource.projectile(this, passenger), COLLISION_DAMAGE_ENTITY);
                }
            }

            if (COLLISION_DAMAGE)
            {
                damageEntity(DamageSource.projectile(this, lastCollidedEntity), COLLISION_DAMAGE_SELF);
            }

            if (COLLISION_FLIGHT_PLAYER)
            {
                passenger.b_(prevMotionX, prevMotionY + 1.0D, prevMotionZ);
                passenger.mount(null);
            }
        }

        lastCollidedEntity = null;
        prevMotionX = motX;
        prevMotionY = motY;
        prevMotionZ = motZ;

        if (passenger != null && passenger.dead)
        {
            passenger = null;
        }
    }

    public double getMotionYaw()
    {
        double d;

        if (motX >= 0.0D && motZ >= 0.0D)
        {
            d = Math.atan(Math.abs(motZ / motX)) * (180D / Math.PI) + 180D;
        }
        else if (motX >= 0.0D && motZ <= 0.0D)
        {
            d = Math.atan(Math.abs(motX / motZ)) * (180D / Math.PI) + 90D;
        }
        else if (motX <= 0.0D && motZ >= 0.0D)
        {
            d = Math.atan(Math.abs(motX / motZ)) * (180D / Math.PI) + 270D;
        }
        else
        {
            d = Math.atan(Math.abs(motZ / motX)) * (180D / Math.PI);
        }

        return d;
    }

    public void projectMotion(double d)
    {
        double d1 = (d * Math.PI) / 180D;
        double d2 = Math.cos(d1) * motX - Math.sin(d1) * motZ;
        double d3 = Math.sin(d1) * motX + Math.cos(d1) * motZ;
        double d4 = getSpeed();
        double d5 = d4 * Math.cos(d1);
        d2 *= d5 / d4;
        d3 *= d5 / d4;
        motX = d2;
        motZ = d3;
    }

    public double getSpeed()
    {
        return Math.sqrt(motX * motX + motZ * motZ);
    }

    public void multiplySpeed(double d)
    {
        motX *= d;
        motZ *= d;
    }

    public double getTurnSpeed()
    {
        return scaleOnSpeed(TURN_SPEED_STOPPED, TURN_SPEED_FULL);
    }

    public double getAccelForward()
    {
        return scaleOnSpeed(ACCEL_FORWARD_STOPPED, ACCEL_FORWARD_FULL);
    }

    public double getAccelBackward()
    {
        return scaleOnSpeed(ACCEL_BACKWARD_STOPPED, ACCEL_BACKWARD_FULL);
    }

    public double scaleOnSpeed(double d, double d1)
    {
        return d - (d - d1) * (getSpeed() / MAX_SPEED);
    }

    public void handleCollision(Entity entity)
    {
        entity.collide(this);

        if (entity.passenger != this && entity.vehicle != this)
        {
            lastCollidedEntity = entity;
        }
    }

    public void setRotationPitch(float f)
    {
        if ((double)(f - pitch) > ROTATION_PITCH_DELTA_MAX)
        {
            pitch += ROTATION_PITCH_DELTA_MAX;
        }
        else if ((double)(pitch - f) > ROTATION_PITCH_DELTA_MAX)
        {
            pitch -= ROTATION_PITCH_DELTA_MAX;
        }
        else
        {
            pitch = f;
        }
    }

    public double getPrevSpeed()
    {
        return Math.sqrt(prevMotionX * prevMotionX + prevMotionZ * prevMotionZ);
    }

    public float getTurnSpeedForRender()
    {
        return (float)(lastTurnSpeed * TURN_SPEED_RENDER_MULT);
    }
}
