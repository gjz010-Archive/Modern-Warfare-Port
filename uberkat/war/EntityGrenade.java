package uberkat.war;

import java.util.Random;
import net.minecraft.server.*;

public class EntityGrenade extends EntityItem
{
    protected String bounceSound;
    protected double bounceFactor;
    protected double bounceSlowFactor;
    protected int fuse;
    protected boolean exploded;
    protected double initialVelocity;
    protected static final int FUSE_LENGTH = 50;
    protected static final double MIN_BOUNCE_SOUND_VELOCITY = 0.10000000000000001D;

    public EntityGrenade(World world)
    {
        super(world);
        bounceSound = "war.grenadebounce";
        bounceFactor = 0.14999999999999999D;
        bounceSlowFactor = 0.80000000000000004D;
        initialVelocity = 1.0D;
        b(0.25F, 0.25F);
        exploded = false;
        fuse = 50;
        height = 0.0F;
        itemStack = new ItemStack(mod_ModernWarfare.itemGrenade, 1, 0);
    }

    public EntityGrenade(World world, double d, double d1, double d2)
    {
        this(world);
        setPosition(d, d1, d2);
    }

    public EntityGrenade(World world, EntityLiving entityliving)
    {
        this(world);
        double d = -MathHelper.sin((entityliving.yaw * (float)Math.PI) / 180F);
        double d1 = MathHelper.cos((entityliving.yaw * (float)Math.PI) / 180F);
        motX = initialVelocity * d * (double)MathHelper.cos((entityliving.pitch / 180F) * (float)Math.PI);
        motY = -initialVelocity * (double)MathHelper.sin((entityliving.pitch / 180F) * (float)Math.PI);
        motZ = initialVelocity * d1 * (double)MathHelper.cos((entityliving.pitch / 180F) * (float)Math.PI);

        if (entityliving.vehicle != null && (entityliving.vehicle instanceof EntityLiving))
        {
            entityliving = (EntityLiving)entityliving.vehicle;
        }

        motX += entityliving.motX;
        motY += entityliving.onGround ? 0.0D : entityliving.motY;
        motZ += entityliving.motZ;
        setPosition(entityliving.locX + d * 0.80000000000000004D, entityliving.locY + (double)entityliving.getHeadHeight(), entityliving.locZ + d1 * 0.80000000000000004D);
        lastX = locX;
        lastY = locY;
        lastZ = locZ;
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
        double d = motX;
        double d1 = motY;
        double d2 = motZ;
        lastX = locX;
        lastY = locY;
        lastZ = locZ;
        move(motX, motY, motZ);
        boolean flag = false;

        if (motX == 0.0D && d != 0.0D)
        {
            motX = -bounceFactor * d;
            motY = bounceSlowFactor * d1;
            motZ = bounceSlowFactor * d2;

            if (Math.abs(d) > 0.10000000000000001D)
            {
                flag = true;
            }
        }

        if (motY == 0.0D && d1 != 0.0D)
        {
            motX = bounceSlowFactor * d;
            motY = -bounceFactor * d1;
            motZ = bounceSlowFactor * d2;

            if (Math.abs(d1) > 0.10000000000000001D)
            {
                flag = true;
            }
        }

        if (motZ == 0.0D && d2 != 0.0D)
        {
            motX = bounceSlowFactor * d;
            motY = bounceSlowFactor * d1;
            motZ = -bounceFactor * d2;

            if (Math.abs(d2) > 0.10000000000000001D)
            {
                flag = true;
            }
        }

        if (flag)
        {
            handleBounce();
        }

        motY -= 0.040000000000000001D;
        motX *= 0.98999999999999999D;
        motY *= 0.98999999999999999D;
        motZ *= 0.98999999999999999D;
        handleExplode();
    }

    protected void handleBounce()
    {
        world.makeSound(this, bounceSound, 0.25F, 1.0F / (random.nextFloat() * 0.1F + 0.95F));
    }

    protected void handleExplode()
    {
        if (fuse-- <= 0)
        {
            explode();
        }
    }

    protected void explode()
    {
        if (!exploded)
        {
            exploded = true;
            Explosion explosion = new Explosion(world, null, locX, (float)locY, (float)locZ, 3F);
            explosion.a();

            if (mod_ModernWarfare.explosionsDestroyBlocks)
            {
                explosion.a(true);
            }
            else
            {
                world.makeSound(locX, locY, locZ, "random.explode", 4F, (1.0F + (world.random.nextFloat() - world.random.nextFloat()) * 0.2F) * 0.7F);
            }

            for (int i = 0; i < 32; i++)
            {
                world.a("explode", locX, locY, locZ, world.random.nextDouble() - 0.5D, world.random.nextDouble() - 0.5D, world.random.nextDouble() - 0.5D);
                world.a("smoke", locX, locY, locZ, world.random.nextDouble() - 0.5D, world.random.nextDouble() - 0.5D, world.random.nextDouble() - 0.5D);
            }

            dead = true;
        }
    }

    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    public boolean o_()
    {
        return true;
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean damageEntity(DamageSource damagesource, int i)
    {
        return false;
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void b(NBTTagCompound nbttagcompound)
    {
        super.b(nbttagcompound);
        nbttagcompound.setByte("Fuse", (byte)fuse);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void a(NBTTagCompound nbttagcompound)
    {
        super.a(nbttagcompound);
        fuse = nbttagcompound.getByte("Fuse");
    }

    /**
     * Called by a player entity when they collide with an entity
     */
    public void a_(EntityHuman entityhuman)
    {
    }

    public float getHeadHeight()
    {
        return length;
    }
}
