package uberkat.war;

import java.util.*;


import net.minecraft.server.*;

public abstract class EntitySentry extends EntityGuardians implements IMonster
{
    boolean restricted;

    /** How high this entity is considered to be */
    float length;
    private static final float MAX_TURN_SPEED = 10F;
    private static final int MAX_HEALTH = 20;
    protected ItemGun gun;
    protected ItemStack itemStack;
    protected int ATTACK_DELAY;
    private Map angerMap;

    public EntitySentry(World world)
    {
        super(world);
        length = 1.5F;
        angerMap = new HashMap();
        texture = "/war/mobSentry.png";
        health = 20;
    }

    public EntitySentry(World world, double d, double d1, double d2)
    {
        this(world);
        setPosition(d, d1, d2);
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean damageEntity(DamageSource damagesource, int i)
    {
        if ((damagesource.getEntity() instanceof EntityLiving) && okToAttack(damagesource.getEntity()))
        {
            List list = world.getEntities(this, boundingBox.grow(32D, 32D, 32D));

            for (int j = 0; j < list.size(); j++)
            {
                Entity entity = (Entity)list.get(j);

                if (!(entity instanceof EntitySentry))
                {
                    continue;
                }

                EntitySentry entitysentry = (EntitySentry)entity;

                if (entitysentry.getOwner() != null && !entitysentry.getOwner().equals("") && entitysentry.getOwner().equals(getOwner()))
                {
                    entitysentry.becomeAngryAt(damagesource.getEntity());
                }
            }

            becomeAngryAt(damagesource.getEntity());
        }

        return super.damageEntity(damagesource, i);
    }

    private void becomeAngryAt(Entity entity)
    {
        if (angerMap.containsKey(entity))
        {
            angerMap.remove(entity);
        }

        angerMap.put(entity, Integer.valueOf(400 + random.nextInt(400)));
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void F_()
    {
        aA();
        e();
    }

    /**
     * Basic mob attack. Default to touch of death in EntityCreature. Overridden by each mob to define their attack.
     */
    protected void a(Entity entity, float f)
    {
        if (okToAttack(entity))
        {
            if (attackTicks == 0 && world != null && gun != null && world.random != null)
            {
                if (itemStack == null)
                {
                    itemStack = new ItemStack(gun);
                }

                gun.use(itemStack, world, this, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
                attackTicks = ATTACK_DELAY;
            }

            e = true;
        }
    }

    protected void d_()
    {
        if (!dead)
        {
            if (world.getTime() % 20L == 0L)
            {
                target = findTarget();
            }

            if (target != null && h(target))
            {
                restricted = false;
                a(target, 10F, 10F);

                if (!restricted)
                {
                    a(target, range);
                }
            }
            else
            {
                yaw++;
                pitch = 0.0F;
            }
        }

        for (Iterator iterator = angerMap.entrySet().iterator(); iterator.hasNext();)
        {
            java.util.Map.Entry entry = (java.util.Map.Entry)iterator.next();
            int i = ((Integer)entry.getValue()).intValue() - 1;

            if (i <= 0)
            {
                iterator.remove();
            }
            else
            {
                entry.setValue(Integer.valueOf(i));
            }
        }
    }

    /**
     * Finds the closest player within 16 blocks to attack, or null if this Entity isn't interested in attacking
     * (Animals, Spiders at day, peaceful PigZombies).
     */
    protected Entity findTarget()
    {
        EntityLiving entityliving = getNearestAnger(this);

        if (entityliving != null)
        {
            return entityliving;
        }
        else
        {
            return super.findTarget();
        }
    }

    public EntityLiving getNearestAnger(Entity entity)
    {
        return getNearestAnger(entity.locX, entity.locY, entity.locZ);
    }

    public EntityLiving getNearestAnger(double d, double d1, double d2)
    {
        double d3 = -1D;
        EntityLiving entityliving = null;
        Iterator iterator = angerMap.entrySet().iterator();

        do
        {
            if (!iterator.hasNext())
            {
                break;
            }

            java.util.Map.Entry entry = (java.util.Map.Entry)iterator.next();
            Entity entity = (Entity)entry.getKey();

            if ((entity instanceof EntityLiving) && entity.isAlive())
            {
                double d4 = entity.e(d, d1, d2);

                if (d3 == -1D || d4 < d3 && h(entity) && okToAttack(entity))
                {
                    d3 = d4;
                    entityliving = (EntityLiving)entity;
                }
            }
        }
        while (true);

        return entityliving;
    }

    /**
     * Changes pitch and yaw so that the entity calling the function is facing the entity provided as an argument.
     */
    public void a(Entity entity, float f, float f1)
    {
        if (!okToAttack(entity))
        {
            return;
        }
        else
        {
            double d = entity.locX - locX;
            double d1 = entity.locZ - locZ;
            double d2 = (entity.boundingBox.b + entity.boundingBox.e) / 2D - (locY + (double)getHeadHeight());
            double d3 = MathHelper.sqrt(d * d + d1 * d1);
            float f2 = (float)((Math.atan2(d1, d) * 180D) / Math.PI) - 90F;
            float f3 = (float)((Math.atan2(d2, d3) * 180D) / Math.PI);
            pitch = -updateRotation(-pitch, f3, f1);
            yaw = updateRotation(yaw, f2, f);
            return;
        }
    }

    private float updateRotation(float f, float f1, float f2)
    {
        float f3;

        for (f3 = f1 - f; f3 < -180F; f3 += 360F) { }

        for (; f3 >= 180F; f3 -= 360F) { }

        if (f3 > f2)
        {
            restricted = true;
            f3 = f2;
        }

        if (f3 < -f2)
        {
            restricted = true;
            f3 = -f2;
        }

        return f + f3;
    }

    /**
     * Returns the item ID for the item the mob drops on death.
     */
    protected int getLootId()
    {
        return gun.requiredBullet.id;
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String j()
    {
        return "war.mechhurt";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String k()
    {
        return null;
    }

    /**
     * knocks back this entity
     */
    public void a(Entity entity, int i, double d, double d1)
    {
    }

    public void setEntityDead()
    {
        super.die();
        target = null;
        gun = null;
    }

    /**
     * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
     */
    public boolean b(EntityHuman entityhuman)
    {
        if (entityhuman.U() != null && entityhuman.U().id == mod_ModernWarfare.itemWrench.id)
        {
            if (health > 0 && health < 20)
            {
                world.makeSound(this, "war.wrench", 1.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);
                health = Math.min(health + 2, 20);
                entityhuman.C_();
                entityhuman.U().damage(1, entityhuman);

                if (entityhuman.U().getData() <= 0)
                {
                    entityhuman.inventory.items[entityhuman.inventory.itemInHandIndex] = null;
                }
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    public int getMaxHealth()
    {
        return 20;
    }
}
