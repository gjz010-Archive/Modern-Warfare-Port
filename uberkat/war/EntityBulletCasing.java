package uberkat.war;

import java.util.List;
import java.util.Random;
import net.minecraft.server.*;

public class EntityBulletCasing extends Entity
{
    public Item droppedItem;
    private int xTile;
    private int yTile;
    private int zTile;
    private int inTile;
    private boolean inGround;
    public Entity owner;
    private int timeInTile;
    private int timeInAir;
    private boolean createdByPlayer;

    public EntityBulletCasing(World world)
    {
        super(world);
        droppedItem = mod_ModernWarfare.itemBulletCasing;
        xTile = -1;
        yTile = -1;
        zTile = -1;
        inTile = 0;
        inGround = false;
        timeInAir = 0;
        b(0.0625F, 0.03125F);
    }

    public EntityBulletCasing(World world, double d, double d1, double d2)
    {
        super(world);
        droppedItem = mod_ModernWarfare.itemBulletCasing;
        xTile = -1;
        yTile = -1;
        zTile = -1;
        inTile = 0;
        inGround = false;
        timeInAir = 0;
        b(0.0625F, 0.03125F);
        setPosition(d, d1, d2);
        height = 0.0F;
    }

    public EntityBulletCasing(World world, Entity entity, float f)
    {
        super(world);
        droppedItem = mod_ModernWarfare.itemBulletCasing;
        xTile = -1;
        yTile = -1;
        zTile = -1;
        inTile = 0;
        inGround = false;
        timeInAir = 0;
        owner = entity;
        createdByPlayer = owner instanceof EntityHuman;
        b(0.0625F, 0.03125F);
        setPositionRotation(entity.locX, entity.locY + (double)entity.getHeadHeight() + (double)f, entity.locZ, entity.yaw, entity.pitch);

        if (entity instanceof EntityHuman)
        {
            locX -= MathHelper.cos((yaw / 180F) * (float)Math.PI) * 0.16F;
            locY -= 0.10000000000000001D;
            locZ -= MathHelper.sin((yaw / 180F) * (float)Math.PI) * 0.16F;
        }

        yaw += 90F;

        if (yaw > 360F)
        {
            yaw -= 360F;
        }

        pitch -= 30F;

        if (pitch < -90F)
        {
            pitch = (pitch + 180F) * -1F;
            yaw *= -1F;
        }

        locX -= MathHelper.cos((yaw / 180F) * (float)Math.PI) * 0.16F;
        locY -= 0.10000000000000001D;
        locZ -= MathHelper.sin((yaw / 180F) * (float)Math.PI) * 0.16F;
        setPosition(locX, locY, locZ);
        height = 0.0F;
        motX = -MathHelper.sin((yaw / 180F) * (float)Math.PI) * MathHelper.cos((pitch / 180F) * (float)Math.PI);
        motZ = MathHelper.cos((yaw / 180F) * (float)Math.PI) * MathHelper.cos((pitch / 180F) * (float)Math.PI);
        motY = -MathHelper.sin((pitch / 180F) * (float)Math.PI);
        setLocation(motX, motY, motZ, 0.25F, 1.0F);
    }

    protected void b()
    {
    }

    public boolean isInRangeToRenderDist(double d)
    {
        return true;
    }

    /**
     * Sets the entity's position and rotation. Args: posX, posY, posZ, yaw, pitch
     */
    public void setLocation(double d, double d1, double d2, float f, float f1)
    {
        float f2 = MathHelper.sqrt(d * d + d1 * d1 + d2 * d2);
        d /= f2;
        d1 /= f2;
        d2 /= f2;
        d += random.nextGaussian() * 0.0074999999999999997D * (double)f1;
        d1 += random.nextGaussian() * 0.0074999999999999997D * (double)f1;
        d2 += random.nextGaussian() * 0.0074999999999999997D * (double)f1;
        d *= f;
        d1 *= f;
        d2 *= f;
        motX = d;
        motY = d1;
        motZ = d2;
        float f3 = MathHelper.sqrt(d * d + d2 * d2);
        lastYaw = yaw = (float)((Math.atan2(d, d2) * 180D) / Math.PI);
        lastPitch = pitch = (float)((Math.atan2(d1, f3) * 180D) / Math.PI);
        timeInTile = 0;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void F_()
    {
        super.F_();

        if (inGround)
        {
            if (!(owner instanceof EntityHuman) && !world.isStatic)
            {
                setEntityDead();
            }

            int i = world.getTypeId(xTile, yTile, zTile);

            if (i != inTile)
            {
                inGround = false;
                motX *= random.nextFloat() * 0.2F;
                motY *= random.nextFloat() * 0.2F;
                motZ *= random.nextFloat() * 0.2F;
                timeInTile = 0;
                timeInAir = 0;
            }
            else
            {
                timeInTile++;

                if (timeInTile == 1200)
                {
                    setEntityDead();
                }

                return;
            }
        }
        else
        {
            timeInAir++;
        }

        Vec3D vec3d = Vec3D.create(locX, locY, locZ);
        Vec3D vec3d1 = Vec3D.create(locX + motX, locY + motY, locZ + motZ);
        MovingObjectPosition movingobjectposition = world.a(vec3d, vec3d1);
        vec3d = Vec3D.create(locX, locY, locZ);
        vec3d1 = Vec3D.create(locX + motX, locY + motY, locZ + motZ);

        if (movingobjectposition != null)
        {
            vec3d1 = Vec3D.create(movingobjectposition.pos.a, movingobjectposition.pos.b, movingobjectposition.pos.c);
        }

        Entity entity = null;
        List list = world.getEntities(this, boundingBox.a(motX, motY, motZ).grow(1.0D, 1.0D, 1.0D));
        double d = 0.0D;

        for (int j = 0; j < list.size(); j++)
        {
            Entity entity1 = (Entity)list.get(j);

            if (!entity1.o_() || entity1 == owner && timeInAir < 5)
            {
                continue;
            }

            float f3 = 0.3F;
            AxisAlignedBB axisalignedbb = entity1.boundingBox.grow(f3, f3, f3);
            MovingObjectPosition movingobjectposition1 = axisalignedbb.a(vec3d, vec3d1);

            if (movingobjectposition1 == null)
            {
                continue;
            }

            double d2 = vec3d.b(movingobjectposition1.pos);

            if (d2 < d || d == 0.0D)
            {
                entity = entity1;
                d = d2;
            }
        }

        if (entity != null)
        {
            movingobjectposition = new MovingObjectPosition(entity);
        }

        if (movingobjectposition != null && movingobjectposition.entity == null)
        {
            xTile = movingobjectposition.b;
            yTile = movingobjectposition.c;
            zTile = movingobjectposition.d;
            inTile = world.getTypeId(xTile, yTile, zTile);
            motX = (float)(movingobjectposition.pos.a - locX);
            motY = (float)(movingobjectposition.pos.b - locY);
            motZ = (float)(movingobjectposition.pos.c - locZ);
            float f = MathHelper.sqrt(motX * motX + motY * motY + motZ * motZ);
            double d1 = 0.025000000000000001D;
            locX -= (motX / (double)f) * d1;
            locY -= (motY / (double)f) * d1;
            locZ -= (motZ / (double)f) * d1;
            inGround = true;
        }

        locX += motX;
        locY += motY;
        locZ += motZ;
        float f1 = MathHelper.sqrt(motX * motX + motZ * motZ);
        yaw = (float)((Math.atan2(motX, motZ) * 180D) / Math.PI);

        for (pitch = (float)((Math.atan2(motY, f1) * 180D) / Math.PI); pitch - lastPitch < -180F; lastPitch -= 360F) { }

        for (; pitch - lastPitch >= 180F; lastPitch += 360F) { }

        for (; yaw - lastYaw < -180F; lastYaw -= 360F) { }

        for (; yaw - lastYaw >= 180F; lastYaw += 360F) { }

        pitch = lastPitch + (pitch - lastPitch) * 0.2F;
        yaw = lastYaw + (yaw - lastYaw) * 0.2F;
        float f2 = 0.99F;
        float f4 = 0.1F;

        if (h_())
        {
            for (int k = 0; k < 4; k++)
            {
                float f5 = 0.25F;
                world.a("bubble", locX - motX * (double)f5, locY - motY * (double)f5, locZ - motZ * (double)f5, motX, motY, motZ);
            }

            f2 = 0.8F;
        }

        motX *= f2;
        motY *= f2;
        motZ *= f2;
        motY -= f4;
        setPosition(locX, locY, locZ);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void b(NBTTagCompound nbttagcompound)
    {
        nbttagcompound.setShort("xTile", (short)xTile);
        nbttagcompound.setShort("yTile", (short)yTile);
        nbttagcompound.setShort("zTile", (short)zTile);
        nbttagcompound.setByte("inTile", (byte)inTile);
        nbttagcompound.setByte("inGround", (byte)(inGround ? 1 : 0));
        nbttagcompound.setByte("createdByPlayer", (byte)(createdByPlayer ? 1 : 0));
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void a(NBTTagCompound nbttagcompound)
    {
        xTile = nbttagcompound.getShort("xTile");
        yTile = nbttagcompound.getShort("yTile");
        zTile = nbttagcompound.getShort("zTile");
        inTile = nbttagcompound.getByte("inTile") & 0xff;
        inGround = nbttagcompound.getByte("inGround") == 1;
        createdByPlayer = nbttagcompound.getByte("createdByPlayer") == 1;
    }

    /**
     * Called by a player entity when they collide with an entity
     */
    public void a_(EntityHuman entityhuman)
    {
        if (world.isStatic)
        {
            return;
        }

        if (mod_ModernWarfare.ammoRestrictions && droppedItem != null && createdByPlayer && timeInTile > 5 && inGround && entityhuman.inventory.pickup(new ItemStack(droppedItem.id, 1, 0)))
        {
            world.makeSound(this, "random.pop", 0.2F, ((random.nextFloat() - random.nextFloat()) * 0.7F + 1.0F) * 2.0F);
            entityhuman.receive(this, 1);
            setEntityDead();
        }
    }

    public float getShadowSize()
    {
        return 0.0F;
    }

    public void setEntityDead()
    {
        super.die();
        owner = null;
    }
}
