package uberkat.war;

import java.util.List;
import java.util.Random;
import net.minecraft.server.*;

public class EntityBulletRocket extends EntityBullet
{
    public EntityBulletRocket(World world)
    {
        super(world);
        b(0.25F, 0.25F);
    }

    public EntityBulletRocket(World world, double d, double d1, double d2)
    {
        super(world, d, d1, d2);
        b(0.25F, 0.25F);
    }

    public EntityBulletRocket(World world, Entity entity, ItemGun itemgun, float f, float f1, float f2, float f3, float f4)
    {
        super(world, entity, itemgun, f, f1, f2, f3, f4);
        b(0.25F, 0.25F);
    }

    public void playServerSound(World world)
    {
        world.makeSound(this, ((ItemGun)mod_ModernWarfare.itemGunRocketLauncher).firingSound, ((ItemGun)mod_ModernWarfare.itemGunRocketLauncher).soundRangeFactor, 1.0F / (random.nextFloat() * 0.1F + 0.95F));
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void F_()
    {
        aA();

        if (timeInAir == 200)
        {
            explode();
            return;
        }

        if (timeInAir % 2 == 0)
        {
            double d = 0.625D;
            double d1 = Math.sqrt(motX * motX + motZ * motZ + motY * motY);
            world.a("smoke", locX - (motX / d1) * d, locY - (motY / d1) * d, locZ - (motZ / d1) * d, 0.0D, 0.0D, 0.0D);
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
        double d2 = 0.0D;

        for (int j = 0; j < list.size(); j++)
        {
            Entity entity1 = (Entity)list.get(j);

            if (!entity1.o_() || (entity1 == owner || owner != null && entity1 == owner.vehicle) && timeInAir < 5 || serverSpawned)
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

            double d3 = vec3d.b(movingobjectposition1.pos);

            if (d3 < d2 || d2 == 0.0D)
            {
                entity = entity1;
                d2 = d3;
            }
        }

        if (entity != null)
        {
            movingobjectposition = new MovingObjectPosition(entity);
        }

        if (movingobjectposition != null)
        {
            int k = world.getTypeId(movingobjectposition.b, movingobjectposition.c, movingobjectposition.d);

            if (movingobjectposition.entity != null || k != Block.LONG_GRASS.id && k != Block.VINE.id)
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

                    if (movingobjectposition.entity instanceof EntityLiving)
                    {
                        WarTools.attackEntityIgnoreDelay((EntityLiving)movingobjectposition.entity, DamageSource.projectile(this, owner), l);
                    }
                    else
                    {
                        movingobjectposition.entity.damageEntity(DamageSource.projectile(this, owner), l);
                    }
                }

                explode();
            }
        }

        locX += motX;
        locY += motY;
        locZ += motZ;
        float f = MathHelper.sqrt(motX * motX + motZ * motZ);
        yaw = (float)((Math.atan2(motX, motZ) * 180D) / Math.PI);

        for (pitch = (float)((Math.atan2(motY, f) * 180D) / Math.PI); pitch - lastPitch < -180F; lastPitch -= 360F) { }

        for (; pitch - lastPitch >= 180F; lastPitch += 360F) { }

        for (; yaw - lastYaw < -180F; lastYaw -= 360F) { }

        for (; yaw - lastYaw >= 180F; lastYaw += 360F) { }

        pitch = lastPitch + (pitch - lastPitch) * 0.2F;
        yaw = lastYaw + (yaw - lastYaw) * 0.2F;
        float f1 = 1.002557F;
        float f3 = 0.0F;

        if (h_())
        {
            for (int i1 = 0; i1 < 4; i1++)
            {
                float f4 = 0.25F;
                world.a("bubble", locX - motX * (double)f4, locY - motY * (double)f4, locZ - motZ * (double)f4, motX, motY, motZ);
            }

            f1 = 0.95F;
            f3 = 0.03F;
        }

        motX *= f1;
        motY *= f1;
        motZ *= f1;
        motY -= f3;
        setPosition(locX, locY, locZ);
    }

    private void explode()
    {
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

        setEntityDead();
    }
}
