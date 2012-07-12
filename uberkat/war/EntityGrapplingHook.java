package uberkat.war;

import java.util.*;
import net.minecraft.server.*;

public class EntityGrapplingHook extends Entity
{
    private int xTile;
    private int yTile;
    private int zTile;
    private int inTile;
    private boolean inGround;
    public EntityHuman owner;
    private int ticksInGround;
    private int ticksInAir;
    private int ticksCatchable;
    public Entity bobber;
    private int fishPosRotationIncrements;
    private double fishX;
    private double fishY;
    private double fishZ;
    private double fishYaw;
    private double fishPitch;
    private double velocityX;
    private double velocityY;
    private double velocityZ;
    private double startPosX;
    private double startPosZ;

    public EntityGrapplingHook(World world)
    {
        super(world);
        xTile = -1;
        yTile = -1;
        zTile = -1;
        inTile = 0;
        inGround = false;
        ticksInAir = 0;
        ticksCatchable = 0;
        bobber = null;
        b(0.25F, 0.25F);
        cd = true;
    }

    public EntityGrapplingHook(World world, double d, double d1, double d2)
    {
        this(world);
        setPosition(d, d1, d2);
    }

    public EntityGrapplingHook(World world, EntityHuman entityhuman)
    {
        this(world);
        owner = entityhuman;
        mod_ModernWarfare.grapplingHooks.put(entityhuman, this);
        setPositionRotation(entityhuman.locX, (entityhuman.locY + 1.6200000000000001D) - (double)entityhuman.height, entityhuman.locZ, entityhuman.yaw, entityhuman.pitch);
        locX -= MathHelper.cos((yaw / 180F) * (float)Math.PI) * 0.16F;
        locY -= 0.10000000000000001D;
        locZ -= MathHelper.sin((yaw / 180F) * (float)Math.PI) * 0.16F;
        setPosition(locX, locY, locZ);
        height = 0.0F;
        float f = 0.4F;
        motX = -MathHelper.sin((yaw / 180F) * (float)Math.PI) * MathHelper.cos((pitch / 180F) * (float)Math.PI) * f;
        motZ = MathHelper.cos((yaw / 180F) * (float)Math.PI) * MathHelper.cos((pitch / 180F) * (float)Math.PI) * f;
        motY = -MathHelper.sin((pitch / 180F) * (float)Math.PI) * f;
        calculateVelocity(motX, motY, motZ, 1.5F, 1.0F);
        startPosX = owner.locX;
        startPosZ = owner.locZ;
    }

    protected void b()
    {
    }

    public boolean isInRangeToRenderDist(double d)
    {
        return true;
    }

    public void calculateVelocity(double d, double d1, double d2, float f, float f1)
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
        ticksInGround = 0;
    }

    public void setPositionAndRotation2(double d, double d1, double d2, float f, float f1, int i)
    {
        fishX = d;
        fishY = d1;
        fishZ = d2;
        fishYaw = f;
        fishPitch = f1;
        fishPosRotationIncrements = i;
        motX = velocityX;
        motY = velocityY;
        motZ = velocityZ;
    }

    public void setVelocity(double d, double d1, double d2)
    {
        velocityX = motX = d;
        velocityY = motY = d1;
        velocityZ = motZ = d2;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void F_()
    {
        super.F_();

        if (fishPosRotationIncrements > 0)
        {
            double d = locX + (fishX - locX) / (double)fishPosRotationIncrements;
            double d1 = locY + (fishY - locY) / (double)fishPosRotationIncrements;
            double d2 = locZ + (fishZ - locZ) / (double)fishPosRotationIncrements;
            double d4;

            for (d4 = fishYaw - (double)yaw; d4 < -180D; d4 += 360D) { }

            for (; d4 >= 180D; d4 -= 360D) { }

            yaw += d4 / (double)fishPosRotationIncrements;
            pitch += (fishPitch - (double)pitch) / (double)fishPosRotationIncrements;
            fishPosRotationIncrements--;
            setPosition(d, d1, d2);
            c(yaw, pitch);
            return;
        }

        if (!world.isStatic)
        {
            if (owner == null)
            {
                setEntityDead();
                return;
            }

            ItemStack itemstack = owner.U();

            if (owner.dead || !owner.isAlive() || itemstack == null || itemstack.getItem() != mod_ModernWarfare.itemGrapplingHook || j(owner) > 1024D)
            {
                setEntityDead();
                return;
            }

            if (bobber != null)
            {
                if (bobber.dead)
                {
                    bobber = null;
                }
                else
                {
                    locX = bobber.locX;
                    locY = bobber.boundingBox.b + (double)bobber.length * 0.80000000000000004D;
                    locZ = bobber.locZ;
                    return;
                }
            }
        }

        if (inGround)
        {
            int i = world.getTypeId(xTile, yTile, zTile);

            if (i != inTile)
            {
                inGround = false;
                motX *= random.nextFloat() * 0.2F;
                motY *= random.nextFloat() * 0.2F;
                motZ *= random.nextFloat() * 0.2F;
                ticksInGround = 0;
                ticksInAir = 0;
            }
            else
            {
                ticksInGround++;

                if (ticksInGround == 1200)
                {
                    setEntityDead();
                }

                return;
            }
        }
        else
        {
            ticksInAir++;
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
        double d3 = 0.0D;

        for (int j = 0; j < list.size(); j++)
        {
            Entity entity1 = (Entity)list.get(j);

            if (!entity1.o_() || entity1 == owner && ticksInAir < 5)
            {
                continue;
            }

            float f2 = 0.3F;
            AxisAlignedBB axisalignedbb = entity1.boundingBox.grow(f2, f2, f2);
            MovingObjectPosition movingobjectposition1 = axisalignedbb.a(vec3d, vec3d1);

            if (movingobjectposition1 == null)
            {
                continue;
            }

            double d8 = vec3d.b(movingobjectposition1.pos);

            if (d8 < d3 || d3 == 0.0D)
            {
                entity = entity1;
                d3 = d8;
            }
        }

        if (entity != null)
        {
            movingobjectposition = new MovingObjectPosition(entity);
        }

        if (movingobjectposition != null)
        {
            if (movingobjectposition.entity != null)
            {
                if (movingobjectposition.entity.damageEntity(DamageSource.projectile(this, owner), 0))
                {
                    bobber = movingobjectposition.entity;
                }
            }
            else
            {
                double d5 = motX;
                double d6 = motZ;
                xTile = movingobjectposition.b;
                yTile = movingobjectposition.c;
                zTile = movingobjectposition.d;
                inTile = world.getTypeId(xTile, yTile, zTile);
                motX = (float)(movingobjectposition.pos.a - locX);
                motY = (float)(movingobjectposition.pos.b - locY);
                motZ = (float)(movingobjectposition.pos.c - locZ);
                float f3 = MathHelper.sqrt(motX * motX + motY * motY + motZ * motZ);
                locX -= (motX / (double)f3) * 0.050000000000000003D;
                locY -= (motY / (double)f3) * 0.050000000000000003D;
                locZ -= (motZ / (double)f3) * 0.050000000000000003D;
                int l = yTile;

                if (movingobjectposition.pos.b - (double)yTile == 0.125D && world.getTypeId(xTile, yTile, zTile) == Block.SNOW.id)
                {
                    l--;
                }

                if ((movingobjectposition.pos.b - (double)l == 1.0D && world.getTypeId(xTile, l + 1, zTile) == 0 || movingobjectposition.pos.b - (double)l == 1.125D && world.getTypeId(xTile, l + 1, zTile) == Block.SNOW.id) && l + 1 < 128)
                {
                    if (d5 == 0.0D || d6 == 0.0D)
                    {
                        d5 = locX - startPosX;
                        d6 = locZ - startPosZ;
                    }

                    byte byte0 = (byte)(d5 > 0.0D ? 1 : -1);
                    byte byte1 = (byte)(d6 > 0.0D ? 1 : -1);
                    boolean flag = (world.getTypeId(xTile - byte0, l, zTile) == 0 || world.getTypeId(xTile - byte0, l, zTile) == Block.SNOW.id) && world.getTypeId(xTile - byte0, l + 1, zTile) == 0;
                    boolean flag1 = (world.getTypeId(xTile, l, zTile - byte1) == 0 || world.getTypeId(xTile, l, zTile - byte1) == Block.SNOW.id) && world.getTypeId(xTile, l + 1, zTile - byte1) == 0;
                    int l1 = xTile;
                    int i2 = l;
                    int j2 = zTile;
                    byte byte2 = 0;
                    boolean flag2 = false;

                    if (flag && !flag1 || flag && flag1 && Math.abs(d5) > Math.abs(d6))
                    {
                        l1 -= byte0;
                        flag2 = true;

                        if (byte0 > 0)
                        {
                            byte2 = 4;
                        }
                        else
                        {
                            byte2 = 5;
                        }
                    }
                    else if (!flag && flag1 || flag && flag1 && Math.abs(d5) <= Math.abs(d6))
                    {
                        j2 -= byte1;
                        flag2 = true;

                        if (byte1 > 0)
                        {
                            byte2 = 2;
                        }
                        else
                        {
                            byte2 = 3;
                        }
                    }

                    if (flag2)
                    {
                        world.setTypeId(xTile, l + 1, zTile, mod_ModernWarfare.blockGrapplingHook.id);
                        world.setData(xTile, l + 1, zTile, byte2);
                        world.setTypeId(l1, i2, j2, mod_ModernWarfare.blockRope.id);
                        world.setData(l1, i2, j2, byte2);

                        if (owner != null)
                        {
                            owner.V();
                        }

                        setEntityDead();
                    }
                    else
                    {
                        inGround = true;
                    }
                }
            }
        }

        if (inGround)
        {
            return;
        }

        move(motX, motY, motZ);
        float f = MathHelper.sqrt(motX * motX + motZ * motZ);
        yaw = (float)((Math.atan2(motX, motZ) * 180D) / Math.PI);

        for (pitch = (float)((Math.atan2(motY, f) * 180D) / Math.PI); pitch - lastPitch < -180F; lastPitch -= 360F) { }

        for (; pitch - lastPitch >= 180F; lastPitch += 360F) { }

        for (; yaw - lastYaw < -180F; lastYaw -= 360F) { }

        for (; yaw - lastYaw >= 180F; lastYaw += 360F) { }

        pitch = lastPitch + (pitch - lastPitch) * 0.2F;
        yaw = lastYaw + (yaw - lastYaw) * 0.2F;
        float f1 = 0.92F;

        if (onGround || positionChanged)
        {
            f1 = 0.5F;
        }

        int k = 5;
        double d7 = 0.0D;

        for (int i1 = 0; i1 < k; i1++)
        {
            double d10 = ((boundingBox.b + ((boundingBox.e - boundingBox.b) * (double)(i1 + 0)) / (double)k) - 0.125D) + 0.125D;
            double d11 = ((boundingBox.b + ((boundingBox.e - boundingBox.b) * (double)(i1 + 1)) / (double)k) - 0.125D) + 0.125D;
            AxisAlignedBB axisalignedbb1 = AxisAlignedBB.b(boundingBox.a, d10, boundingBox.c, boundingBox.d, d11, boundingBox.f);
        }

        if (d7 > 0.0D)
        {
            if (ticksCatchable > 0)
            {
                ticksCatchable--;
            }
            else if (random.nextInt(500) == 0)
            {
                ticksCatchable = random.nextInt(30) + 10;
                motY -= 0.20000000000000001D;
                world.makeSound(this, "random.splash", 0.25F, 1.0F + (random.nextFloat() - random.nextFloat()) * 0.4F);
                float f4 = MathHelper.floor(boundingBox.b);

                for (int j1 = 0; (float)j1 < 1.0F + width * 20F; j1++)
                {
                    float f5 = (random.nextFloat() * 2.0F - 1.0F) * width;
                    float f7 = (random.nextFloat() * 2.0F - 1.0F) * width;
                    world.a("bubble", locX + (double)f5, f4 + 1.0F, locZ + (double)f7, motX, motY - (double)(random.nextFloat() * 0.2F), motZ);
                }

                for (int k1 = 0; (float)k1 < 1.0F + width * 20F; k1++)
                {
                    float f6 = (random.nextFloat() * 2.0F - 1.0F) * width;
                    float f8 = (random.nextFloat() * 2.0F - 1.0F) * width;
                    world.a("splash", locX + (double)f6, f4 + 1.0F, locZ + (double)f8, motX, motY, motZ);
                }
            }
        }

        if (ticksCatchable > 0)
        {
            motY -= (double)(random.nextFloat() * random.nextFloat() * random.nextFloat()) * 0.20000000000000001D;
        }

        double d9 = d7 * 2D - 1.0D;
        motY += 0.040000000000000001D * d9;

        if (d7 > 0.0D)
        {
            f1 = (float)((double)f1 * 0.90000000000000002D);
            motY *= 0.80000000000000004D;
        }

        motX *= f1;
        motY *= f1;
        motZ *= f1;
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
    }

    public float getShadowSize()
    {
        return 0.0F;
    }

    public int catchFish()
    {
        byte byte0 = 0;

        if (bobber != null)
        {
            double d = owner.locX - locX;
            double d1 = owner.locY - locY;
            double d2 = owner.locZ - locZ;
            double d3 = MathHelper.sqrt(d * d + d1 * d1 + d2 * d2);
            double d4 = 0.10000000000000001D;
            bobber.motX += d * d4;
            bobber.motY += d1 * d4 + (double)MathHelper.sqrt(d3) * 0.080000000000000002D;
            bobber.motZ += d2 * d4;
            byte0 = 3;
        }

        if (inGround)
        {
            byte0 = 2;
        }

        setEntityDead();
        return byte0;
    }

    public void setEntityDead()
    {
        super.die();
        mod_ModernWarfare.grapplingHooks.remove(owner);
        owner = null;
    }
}
