package uberkat.war;

import java.util.List;
import java.util.Random;
import net.minecraft.server.*;

public abstract class EntityBullet extends Entity
{
    protected int xTile;
    protected int yTile;
    protected int zTile;
    protected int inTile;
    protected boolean inGround;
    public Entity owner;
    protected int timeInTile;
    protected int timeInAir;
    protected int damage;
    protected float headshotMultiplier;
    protected boolean serverSpawned;
    protected String firingSound;
    protected float soundRangeFactor;
    protected boolean serverSoundPlayed;

    public EntityBullet(World world)
    {
        super(world);
        soundRangeFactor = 8F;
        serverSoundPlayed = false;
        xTile = -1;
        yTile = -1;
        zTile = -1;
        inTile = 0;
        inGround = false;
        timeInAir = 0;
        b(0.0625F, 0.03125F);
    }

    public EntityBullet(World world, double d, double d1, double d2)
    {
        this(world);
        setPosition(d, d1, d2);
        height = 0.0F;
        serverSpawned = true;
    }

    public abstract void playServerSound(World world);

    public EntityBullet(World world, Entity entity, ItemGun itemgun, float f, float f1, float f2, float f3, float f4)
    {
        this(world);
        owner = entity;
        damage = itemgun.damage;
        headshotMultiplier = itemgun.headshotMultiplier;
        float f5 = entity.yaw;
        float f6 = f5 * 0.01745329F;
        double d = f * MathHelper.cos(f6) - f2 * MathHelper.sin(f6);
        double d1 = f * MathHelper.sin(f6) + f2 * MathHelper.cos(f6);
        setPositionRotation(entity.locX + d, entity.locY + (double)entity.getHeadHeight() + (double)f1, entity.locZ + d1, entity.yaw + f3, entity.pitch + f4);
        locX -= MathHelper.cos((yaw / 180F) * (float)Math.PI) * 0.16F;
        locY -= 0.10000000000000001D;
        locZ -= MathHelper.sin((yaw / 180F) * (float)Math.PI) * 0.16F;
        setPosition(locX, locY, locZ);
        height = 0.0F;
        float f7 = itemgun.spread;

        if (entity instanceof EntityLiving)
        {
            if (entity instanceof EntityHuman)
            {
                float f8 = itemgun.recoil / (float)itemgun.useDelay;
                float f9 = f8 / 0.1F;

                if (f9 > 0.0F)
                {
                    f7 = (float)((double)f7 * (1.0D + mod_ModernWarfare.currentRecoilV / (double)f9));
                }
            }

            boolean flag = Math.abs(entity.motX) > 0.10000000000000001D || Math.abs(entity.motY) > 0.10000000000000001D || Math.abs(entity.motZ) > 0.10000000000000001D;

            if (flag)
            {
                f7 *= 2.0F;

                if (itemgun instanceof ItemGunMinigun)
                {
                    f7 *= 2.0F;
                }
            }

            if (!entity.onGround)
            {
                f7 *= 2.0F;

                if (itemgun instanceof ItemGunMinigun)
                {
                    f7 *= 2.0F;
                }
            }

            if ((entity instanceof EntityHuman) && (itemgun instanceof ItemGunSniper))
            {
                EntityHuman entityhuman = (EntityHuman)entity;

                if (flag)
                {
                    f7 = (float)((double)f7 + 0.25D);
                }

                if (!entity.onGround)
                {
                    f7 = (float)((double)f7 + 0.25D);
                }

                if (!entityhuman.isSneaking())
                {
                    f7 = (float)((double)f7 + 0.25D);
                }

                if (!mod_ModernWarfare.getSniperZoomedIn(entityhuman))
                {
                    f7 = 8F;
                }
            }
        }

        if (entity.passenger != null && (entity instanceof EntityHuman))
        {
            owner = entity.passenger;
        }

        motX = -MathHelper.sin((yaw / 180F) * (float)Math.PI) * MathHelper.cos((pitch / 180F) * (float)Math.PI);
        motZ = MathHelper.cos((yaw / 180F) * (float)Math.PI) * MathHelper.cos((pitch / 180F) * (float)Math.PI);
        motY = -MathHelper.sin((pitch / 180F) * (float)Math.PI);
        setBulletHeading(motX, motY, motZ, itemgun.muzzleVelocity, f7 / 2.0F);
        double d2 = 0.0D;
        double d3 = 0.0D;
        double d4 = 0.0D;

        if (entity.vehicle != null)
        {
            d2 = entity.vehicle.motX;
            d3 = entity.vehicle.onGround ? 0.0D : entity.vehicle.motY;
            d4 = entity.vehicle.motZ;
        }
        else if (entity.passenger != null)
        {
            d2 = entity.motX;
            d3 = entity.onGround ? 0.0D : entity.motY;
            d4 = entity.motZ;
        }

        motX += d2;
        motY += d3;
        motZ += d4;
    }

    protected void b()
    {
    }

    public void setBulletHeading(double d, double d1, double d2, float f, float f1)
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

    public boolean isInRangeToRenderDist(double d)
    {
        return true;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void F_()
    {
        super.F_();

        if (serverSpawned && !serverSoundPlayed)
        {
            playServerSound(world);
            serverSoundPlayed = true;
        }

        if (timeInAir == 200)
        {
            setEntityDead();
        }

        if (lastPitch == 0.0F && lastYaw == 0.0F)
        {
            float f = MathHelper.sqrt(motX * motX + motZ * motZ);
            lastYaw = yaw = (float)((Math.atan2(motX, motZ) * 180D) / Math.PI);
            lastPitch = pitch = (float)((Math.atan2(motY, f) * 180D) / Math.PI);
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
                timeInTile = 0;
                timeInAir = 0;
            }
            else
            {
                timeInTile++;

                if (timeInTile == 200)
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
        Vec3D vec3d2 = null;

        for (int j = 0; j < list.size(); j++)
        {
            Entity entity1 = (Entity)list.get(j);

            if (!entity1.o_() || (entity1 == owner || owner != null && entity1 == owner.vehicle || owner != null && entity1 == owner.passenger) && timeInAir < 5 || serverSpawned)
            {
                continue;
            }

            float f4 = 0.3F;
            AxisAlignedBB axisalignedbb = entity1.boundingBox.grow(f4, f4, f4);
            MovingObjectPosition movingobjectposition1 = axisalignedbb.a(vec3d, vec3d1);

            if (movingobjectposition1 == null)
            {
                continue;
            }

            double d1 = vec3d.b(movingobjectposition1.pos);

            if (d1 < d || d == 0.0D)
            {
                vec3d2 = movingobjectposition1.pos;
                entity = entity1;
                d = d1;
            }
        }

        if (entity != null)
        {
            movingobjectposition = new MovingObjectPosition(entity);
        }

        if (movingobjectposition != null)
        {
            int k = world.getTypeId(movingobjectposition.b, movingobjectposition.c, movingobjectposition.d);

            if (movingobjectposition.entity != null || k != Block.LONG_GRASS.id)
            {
                if (movingobjectposition.entity != null)
                {
                    int l = damage;

                    if ((owner instanceof IMonster) && (movingobjectposition.entity instanceof EntityHuman))
                    {
                        if (world.difficulty == 0)
                        {
                            l = 0;
                        }

                        if (world.difficulty == 1)
                        {
                            l = l / 3 + 1;
                        }

                        if (world.difficulty == 3)
                        {
                            l = (l * 3) / 2;
                        }
                    }

                    l = checkHeadshot(movingobjectposition, vec3d2, l);

                    if (movingobjectposition.entity instanceof EntityLiving)
                    {
                        WarTools.attackEntityIgnoreDelay((EntityLiving)movingobjectposition.entity, DamageSource.projectile(this, owner), l);
                    }
                    else
                    {
                        movingobjectposition.entity.damageEntity(DamageSource.projectile(this, owner), l);
                    }
                }
                else
                {
                    xTile = movingobjectposition.b;
                    yTile = movingobjectposition.c;
                    zTile = movingobjectposition.d;
                    inTile = k;
                    motX = (float)(movingobjectposition.pos.a - locX);
                    motY = (float)(movingobjectposition.pos.b - locY);
                    motZ = (float)(movingobjectposition.pos.c - locZ);
                    float f2 = MathHelper.sqrt(motX * motX + motY * motY + motZ * motZ);
                    locX -= (motX / (double)f2) * 0.050000000000000003D;
                    locY -= (motY / (double)f2) * 0.050000000000000003D;
                    locZ -= (motZ / (double)f2) * 0.050000000000000003D;
                    inGround = true;

                    if (mod_ModernWarfare.bulletsDestroyGlass && (inTile == Block.GLASS.id || inTile == Block.THIN_GLASS.id))
                    {
                        Block block;

                        if (inTile == Block.GLASS.id)
                        {
                            block = Block.GLASS;
                        }
                        else
                        {
                            block = Block.THIN_GLASS;
                        }

                        world.setTypeId(xTile, yTile, zTile, 0);
                        block.postBreak(world, xTile, yTile, zTile, world.getData(xTile, yTile, zTile));
                    }
                }

                world.makeSound(this, "war.impact", 0.2F, 1.0F / (random.nextFloat() * 0.1F + 0.95F));
                setEntityDead();
            }
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
        float f3 = 1.0F;
        float f5 = 0.0F;

        if (h_())
        {
            for (int i1 = 0; i1 < 4; i1++)
            {
                float f6 = 0.25F;
                world.a("bubble", locX - motX * (double)f6, locY - motY * (double)f6, locZ - motZ * (double)f6, motX, motY, motZ);
            }

            f3 = 0.8F;
            f5 = 0.03F;
        }

        motX *= f3;
        motY *= f3;
        motZ *= f3;
        motY -= f5;
        setPosition(locX, locY, locZ);
    }

    protected int checkHeadshot(MovingObjectPosition movingobjectposition, Vec3D vec3d, int i)
    {
        float f = 0.0F;

        if ((movingobjectposition.entity instanceof EntityHuman) || (movingobjectposition.entity instanceof EntityZombie) || (movingobjectposition.entity instanceof EntitySkeleton))
        {
            f = 0.25F;
        }
        else if (movingobjectposition.entity instanceof EntityCreeper)
        {
            f = 0.3076923F;
        }
        else if (movingobjectposition.entity instanceof EntityEnderman)
        {
            f = 0.173913F;
        }

        if (f > 0.0F)
        {
            double d = movingobjectposition.entity.boundingBox.e;
            double d1 = movingobjectposition.entity.boundingBox.b;
            double d2 = d - d1;

            if (vec3d.b > d - d2 * (double)f)
            {
                i = Math.round((float)i * headshotMultiplier);
            }
        }

        return i;
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

    public void setEntityDead()
    {
        super.die();
        owner = null;
    }
}
