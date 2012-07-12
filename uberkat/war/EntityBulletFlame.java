package uberkat.war;

import java.util.List;
import java.util.Random;
import net.minecraft.server.*;

public class EntityBulletFlame extends EntityBullet
{
    public EntityBulletFlame(World world)
    {
        super(world);
        b(0.5F, 0.5F);
    }

    public EntityBulletFlame(World world, double d, double d1, double d2)
    {
        super(world, d, d1, d2);
        b(0.5F, 0.5F);
    }

    public EntityBulletFlame(World world, Entity entity, ItemGun itemgun, float f, float f1, float f2, float f3, float f4)
    {
        super(world, entity, itemgun, f, f1, f2, f3, f4);
        b(0.5F, 0.5F);
    }

    public void playServerSound(World world)
    {
        world.makeSound(this, ((ItemGun)mod_ModernWarfare.itemGunFlamethrower).firingSound, ((ItemGun)mod_ModernWarfare.itemGunFlamethrower).soundRangeFactor, 1.0F / (random.nextFloat() * 0.1F + 0.95F));
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void F_()
    {
        aA();

        if (timeInAir == 30)
        {
            setEntityDead();
        }

        world.a("smoke", locX, locY, locZ, 0.0D, 0.0D, 0.0D);

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

            if (!entity1.o_() || (entity1 == owner || owner != null && entity1 == owner.vehicle) && timeInAir < 5 || serverSpawned)
            {
                continue;
            }

            float f = 0.3F;
            AxisAlignedBB axisalignedbb = entity1.boundingBox.grow(f, f, f);
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
            if (movingobjectposition.entity != null)
            {
                int k = damage;

                if ((owner instanceof IMonster) && (movingobjectposition.entity instanceof EntityHuman))
                {
                    if (world.difficulty == 0)
                    {
                        k = 0;
                    }

                    if (world.difficulty == 1)
                    {
                        k = k / 3 + 1;
                    }

                    if (world.difficulty == 3)
                    {
                        k = (k * 3) / 2;
                    }
                }

                k = checkHeadshot(movingobjectposition, vec3d2, k);

                if (movingobjectposition.entity instanceof EntityLiving)
                {
                    WarTools.attackEntityIgnoreDelay((EntityLiving)movingobjectposition.entity, DamageSource.projectile(this, owner), k);
                }
                else
                {
                    movingobjectposition.entity.damageEntity(DamageSource.projectile(this, owner), k);
                }

                movingobjectposition.entity.setOnFire(300);
            }
            else
            {
                xTile = movingobjectposition.b;
                yTile = movingobjectposition.c;
                zTile = movingobjectposition.d;

                if (world.getTypeId(xTile, yTile, zTile) == Block.ICE.id && Block.ICE.m() < 1000000F)
                {
                    Block.ICE.remove(world, xTile, yTile, zTile);
                }
                else
                {
                    byte byte0 = (byte)(motX > 0.0D ? 1 : -1);
                    byte byte1 = (byte)(motY > 0.0D ? 1 : -1);
                    byte byte2 = (byte)(motZ > 0.0D ? 1 : -1);
                    boolean flag = world.getTypeId(xTile - byte0, yTile, zTile) == 0 || world.getTypeId(xTile - byte0, yTile, zTile) == Block.SNOW.id;
                    boolean flag1 = world.getTypeId(xTile, yTile - byte1, zTile) == 0 || world.getTypeId(xTile, yTile - byte1, zTile) == Block.SNOW.id;
                    boolean flag2 = world.getTypeId(xTile, yTile, zTile - byte2) == 0 || world.getTypeId(xTile, yTile, zTile - byte2) == Block.SNOW.id;

                    if (flag)
                    {
                        world.setTypeId(xTile - byte0, yTile, zTile, Block.FIRE.id);
                    }

                    if (flag1)
                    {
                        world.setTypeId(xTile, yTile - byte1, zTile, Block.FIRE.id);
                    }

                    if (flag2)
                    {
                        world.setTypeId(xTile, yTile, zTile - byte2, Block.FIRE.id);
                    }
                }
            }

            setEntityDead();
        }

        locX += motX;
        locY += motY;
        locZ += motZ;
        setRotationToVelocity();

        if (world.a(boundingBox, Material.WATER, this))
        {
            setEntityDead();
        }

        setPosition(locX, locY, locZ);
    }

    public void setRotationToVelocity()
    {
        float f = MathHelper.sqrt(motX * motX + motZ * motZ);
        yaw = (float)((Math.atan2(motX, motZ) * 180D) / Math.PI);

        for (pitch = (float)((Math.atan2(motY, f) * 180D) / Math.PI); pitch - lastPitch < -180F; lastPitch -= 360F) { }

        for (; pitch - lastPitch >= 180F; lastPitch += 360F) { }

        for (; yaw - lastYaw < -180F; lastYaw -= 360F) { }

        for (; yaw - lastYaw >= 180F; lastYaw += 360F) { }
    }

    public float getEntityBrightness(float f)
    {
        return 2.0F;
    }
}
