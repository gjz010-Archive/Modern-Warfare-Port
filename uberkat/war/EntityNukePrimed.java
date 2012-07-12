package uberkat.war;

import net.minecraft.server.*;

public class EntityNukePrimed extends Entity
{
    public int fuse;

    public EntityNukePrimed(World world)
    {
        super(world);
        fuse = 0;
        bf = true;
        b(0.98F, 0.98F);
        height = length / 2.0F;
    }

    public EntityNukePrimed(World world, double d, double d1, double d2)
    {
        this(world);
        setPosition(d, d1, d2);
        float f = (float)(Math.random() * Math.PI * 2D);
        motX = -MathHelper.sin((f * (float)Math.PI) / 180F) * 0.02F;
        motY = 0.20000000000000001D;
        motZ = -MathHelper.cos((f * (float)Math.PI) / 180F) * 0.02F;
        fuse = mod_ModernWarfare.nukeFuse;
        lastX = d;
        lastY = d1;
        lastZ = d2;
    }

    protected void b()
    {
    }

    /**
     * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
     * prevent them from trampling crops
     */
    protected boolean g_()
    {
        return false;
    }

    public boolean isInRangeToRenderDist(double d)
    {
        return true;
    }

    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    public boolean o_()
    {
        return !dead;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void F_()
    {
        lastX = locX;
        lastY = locY;
        lastZ = locZ;
        motY -= 0.040000000000000001D;
        move(motX, motY, motZ);
        motX *= 0.97999999999999998D;
        motY *= 0.97999999999999998D;
        motZ *= 0.97999999999999998D;

        if (onGround)
        {
            motX *= 0.69999999999999996D;
            motZ *= 0.69999999999999996D;
            motY *= -0.5D;
        }

        if (fuse-- <= 0)
        {
            if (!world.isStatic)
            {
                die();
                explode();
            }
            else
            {
                die();
            }
        }
        else
        {
            world.a("smoke", locX, locY + 0.5D, locZ, 0.0D, 0.0D, 0.0D);
        }
    }

    private void explode()
    {
        ExplosionNuke explosionnuke = new ExplosionNuke(world, null, locX, locY, locZ, mod_ModernWarfare.nukeBlastDiameter, 0.0F, false);
        explosionnuke.doExplosionA();
        explosionnuke.doExplosionB();
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    protected void b(NBTTagCompound nbttagcompound)
    {
        nbttagcompound.setByte("Fuse", (byte)fuse);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    protected void a(NBTTagCompound nbttagcompound)
    {
        fuse = nbttagcompound.getByte("Fuse");
    }

    public float getShadowSize()
    {
        return 0.0F;
    }
}
