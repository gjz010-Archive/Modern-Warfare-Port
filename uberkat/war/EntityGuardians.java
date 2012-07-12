package uberkat.war;

import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.List;

import net.minecraft.server.*;

public abstract class EntityGuardians extends EntityCreature
{
    protected int attackStrength;
    protected float range;
    private static boolean aiManagerIsPetNotFound = false;
    private static Method aiManagerIsPet = null;

    public EntityGuardians(World world)
    {
        super(world);
        range = 32F;
        attackStrength = 2;
        health = 20;
    }

    protected void b()
    {
        super.b();
        datawatcher.a(17, "");
        datawatcher.a(18, new Integer(health));
    }

    public String getOwner()
    {
        return datawatcher.getString(17);
    }

    public void setOwner(String s)
    {
        datawatcher.watch(17, s);
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean damageEntity(DamageSource damagesource, int i)
    {
        if (super.damageEntity(damagesource, i))
        {
            if (passenger == damagesource.getEntity() || vehicle == damagesource.getEntity())
            {
                return true;
            }

            if (damagesource.getEntity() != this)
            {
                target = damagesource.getEntity();
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Finds the closest player within 16 blocks to attack, or null if this Entity isn't interested in attacking
     * (Animals, Spiders at day, peaceful PigZombies).
     */
    protected Entity findTarget()
    {
        return getNearestEntityLivingInRange(this, range);
    }

    public EntityLiving getNearestEntityLivingInRange(Entity entity, double d)
    {
        return getNearestEntityLivingInRange(entity.locX, entity.locY, entity.locZ, d);
    }

    public EntityLiving getNearestEntityLivingInRange(double d, double d1, double d2, double d3)
    {
        double d4 = -1D;
        EntityLiving entityliving = null;

        for (int i = 0; i < world.entityList.size(); i++)
        {
            Entity entity = (Entity)world.entityList.get(i);

            if (!(entity instanceof EntityLiving) || !mod_ModernWarfare.sentriesKillAnimals && (!(entity instanceof IMonster) || (entity instanceof EntityPigZombie)) || (entity instanceof EntityHuman) || (entity instanceof EntityWolf) && ((EntityWolf)entity).isTamed() || !entity.isAlive())
            {
                continue;
            }

            double d5 = entity.e(d, d1, d2);

            if ((d3 < 0.0D || d5 < d3 * d3) && (d4 == -1D || d5 < d4) && h(entity) && okToAttack(entity))
            {
                d4 = d5;
                entityliving = (EntityLiving)entity;
            }
        }

        return entityliving;
    }

    protected boolean okToAttack(Entity entity)
    {
        boolean flag = false;

        if (!aiManagerIsPetNotFound && (entity instanceof EntityLiving))
        {
            try
            {
                if (aiManagerIsPet == null)
                {
                    Class class1;

                    try
                    {
                        class1 = Class.forName("mod_AIManager");
                    }
                    catch (Exception exception1)
                    {
                        class1 = Class.forName("net.minecraft.server.mod_AIManager");
                    }

                    aiManagerIsPet = class1.getDeclaredMethod("isPet", new Class[]
                            {
                                net.minecraft.server.EntityLiving.class
                            });
                }

                flag = ((Boolean)aiManagerIsPet.invoke(null, new Object[]
                        {
                            (EntityLiving)entity
                        })).booleanValue();
            }
            catch (Exception exception)
            {
                System.out.println("mod_AIManager not found in EntityGuardians. Ignore this exception if you do not have it installed.");
                aiManagerIsPetNotFound = true;
            }
        }

        return !flag && !(entity instanceof EntityGuardians) && (!(entity instanceof EntityHuman) || !((EntityHuman)entity).name.equals(getOwner())) && (!(entity instanceof EntityWolf) || !((EntityWolf)entity).isTamed() || !((EntityWolf)entity).getOwner().equals(getOwner()));
    }

    /**
     * Takes a coordinate in and returns a weight to determine how likely this creature will try to path to the block.
     * Args: x, y, z
     */
    public float a(int i, int j, int k)
    {
        return 1.0F;
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void b(NBTTagCompound nbttagcompound)
    {
        super.b(nbttagcompound);
        nbttagcompound.setString("Owner", getOwner() == null ? "" : getOwner());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void a(NBTTagCompound nbttagcompound)
    {
        super.a(nbttagcompound);
        setOwner(nbttagcompound.getString("Owner"));
    }

    protected void d_()
    {
        super.d_();

        if (!world.isStatic)
        {
            datawatcher.watch(18, Integer.valueOf(health));
        }
    }
}
