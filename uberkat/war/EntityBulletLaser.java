package uberkat.war;

import java.util.List;
import java.util.Random;
import net.minecraft.server.*;

public class EntityBulletLaser extends EntityBullet
{
    public EntityBulletLaser(World world)
    {
        super(world);
        b(0.5F, 0.5F);
    }

    public EntityBulletLaser(World world, double d, double d1, double d2)
    {
        super(world, d, d1, d2);
        b(0.5F, 0.5F);
    }

    public EntityBulletLaser(World world, Entity entity, ItemGun itemgun, float f, float f1, float f2, float f3, float f4)
    {
        super(world, entity, itemgun, f, f1, f2, f3, f4);
        b(0.5F, 0.5F);
    }

    public void playServerSound(World world)
    {
        world.makeSound(this, ((ItemGun)mod_ModernWarfare.itemGunLaser).firingSound, ((ItemGun)mod_ModernWarfare.itemGunLaser).soundRangeFactor, 1.0F / (random.nextFloat() * 0.1F + 0.95F));
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void F_()
    {
        aA();

        if (timeInAir == 200)
        {
            setEntityDead();
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
        MovingObjectPosition movingobjectposition = rayTraceBlocks(vec3d, vec3d1);
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

            float f1 = 0.3F;
            AxisAlignedBB axisalignedbb = entity1.boundingBox.grow(f1, f1, f1);
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
                boolean flag = false;

                if (!flag)
                {
                    if (movingobjectposition.entity instanceof EntityCreature)
                    {
                        if (entity instanceof EntityPig)
                        {
                            int l = random.nextInt(3);

                            for (int k1 = 0; k1 < l; k1++)
                            {
                                entity.b(Item.GRILLED_PORK.id, 1);
                            }
                        }
                        else if (entity instanceof EntityCow)
                        {
                            int i1 = random.nextInt(3) + 1;

                            for (int l1 = 0; l1 < i1; l1++)
                            {
                                b(Item.COOKED_BEEF.id, 1);
                            }
                        }
                        else if (entity instanceof EntityChicken)
                        {
                            b(Item.COOKED_CHICKEN.id, 1);
                        }

                        movingobjectposition.entity.die();
                    }

                    if (!movingobjectposition.entity.dead)
                    {
                        damageEntity(movingobjectposition, vec3d2);
                    }

                    setEntityDead();
                }
            }
            else
            {
                int k = world.getTypeId(movingobjectposition.b, movingobjectposition.c, movingobjectposition.d);

                if (k != Block.GLASS.id && k != Block.THIN_GLASS.id)
                {
                    if (k == Block.DIAMOND_BLOCK.id || k == Block.GOLD_BLOCK.id || k == Block.IRON_BLOCK.id)
                    {
                        int j1 = movingobjectposition.face;

                        if (j1 == 0 || j1 == 1)
                        {
                            motY *= -1D;
                        }
                        else if (j1 == 2 || j1 == 3)
                        {
                            motZ *= -1D;
                        }
                        else if (j1 == 4 || j1 == 5)
                        {
                            motX *= -1D;
                        }
                    }
                    else
                    {
                        if (k != Block.BEDROCK.id && k != Block.OBSIDIAN.id && Block.byId[k].m() < 1000000F)
                        {
                            if (k == Block.SAND.id)
                            {
                                world.setTypeId(movingobjectposition.b, movingobjectposition.c, movingobjectposition.d, Block.GLASS.id);
                            }
                            else if (k == Block.ICE.id)
                            {
                                world.setTypeId(movingobjectposition.b, movingobjectposition.c, movingobjectposition.d, Block.STATIONARY_WATER.id);
                            }
                            else if (mod_ModernWarfare.laserSetsFireToBlocks && WarTools.isFlammable(k))
                            {
                                world.setTypeId(movingobjectposition.b, movingobjectposition.c, movingobjectposition.d, Block.FIRE.id);
                            }
                            else
                            {
                                world.setTypeId(movingobjectposition.b, movingobjectposition.c, movingobjectposition.d, 0);
                            }
                        }

                        setEntityDead();
                    }
                }
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

        pitch = lastPitch + (pitch - lastPitch) * 1.0F;
        yaw = lastYaw + (yaw - lastYaw) * 1.0F;
        setPosition(locX, locY, locZ);
    }

    public void damageEntity(MovingObjectPosition movingobjectposition, Vec3D vec3d)
    {
        int i = damage;

        if ((owner instanceof IMonster) && (movingobjectposition.entity instanceof EntityHuman))
        {
            if (world.difficulty == 0)
            {
                i = 0;
            }

            if (world.difficulty == 1)
            {
                i = i / 3 + 1;
            }

            if (world.difficulty == 3)
            {
                i = (i * 3) / 2;
            }
        }

        i = checkHeadshot(movingobjectposition, vec3d, i);

        if (movingobjectposition.entity instanceof EntityLiving)
        {
            WarTools.attackEntityIgnoreDelay((EntityLiving)movingobjectposition.entity, DamageSource.projectile(this, owner), i);
        }
        else
        {
            movingobjectposition.entity.damageEntity(DamageSource.projectile(this, owner), i);
        }
    }

    public float getEntityBrightness(float f)
    {
        return 2.0F;
    }

    public MovingObjectPosition rayTraceBlocks(Vec3D vec3d, Vec3D vec3d1)
    {
        return rayTraceBlocks_do(vec3d, vec3d1, false);
    }

    public MovingObjectPosition rayTraceBlocks_do(Vec3D vec3d, Vec3D vec3d1, boolean flag)
    {
        if (Double.isNaN(vec3d.a) || Double.isNaN(vec3d.b) || Double.isNaN(vec3d.c))
        {
            return null;
        }

        if (Double.isNaN(vec3d1.a) || Double.isNaN(vec3d1.b) || Double.isNaN(vec3d1.c))
        {
            return null;
        }

        int i = MathHelper.floor(vec3d1.a);
        int j = MathHelper.floor(vec3d1.b);
        int k = MathHelper.floor(vec3d1.c);
        int l = MathHelper.floor(vec3d.a);
        int i1 = MathHelper.floor(vec3d.b);
        int j1 = MathHelper.floor(vec3d.c);

        for (int k1 = 200; k1-- >= 0;)
        {
            if (Double.isNaN(vec3d.a) || Double.isNaN(vec3d.b) || Double.isNaN(vec3d.c))
            {
                return null;
            }

            if (l == i && i1 == j && j1 == k)
            {
                return null;
            }

            double d = 999D;
            double d1 = 999D;
            double d2 = 999D;

            if (i > l)
            {
                d = (double)l + 1.0D;
            }

            if (i < l)
            {
                d = (double)l + 0.0D;
            }

            if (j > i1)
            {
                d1 = (double)i1 + 1.0D;
            }

            if (j < i1)
            {
                d1 = (double)i1 + 0.0D;
            }

            if (k > j1)
            {
                d2 = (double)j1 + 1.0D;
            }

            if (k < j1)
            {
                d2 = (double)j1 + 0.0D;
            }

            double d3 = 999D;
            double d4 = 999D;
            double d5 = 999D;
            double d6 = vec3d1.a - vec3d.a;
            double d7 = vec3d1.b - vec3d.b;
            double d8 = vec3d1.c - vec3d.c;

            if (d != 999D)
            {
                d3 = (d - vec3d.a) / d6;
            }

            if (d1 != 999D)
            {
                d4 = (d1 - vec3d.b) / d7;
            }

            if (d2 != 999D)
            {
                d5 = (d2 - vec3d.c) / d8;
            }

            byte byte0 = 0;

            if (d3 < d4 && d3 < d5)
            {
                if (i > l)
                {
                    byte0 = 4;
                }
                else
                {
                    byte0 = 5;
                }

                vec3d.a = d;
                vec3d.b += d7 * d3;
                vec3d.c += d8 * d3;
            }
            else if (d4 < d5)
            {
                if (j > i1)
                {
                    byte0 = 0;
                }
                else
                {
                    byte0 = 1;
                }

                vec3d.a += d6 * d4;
                vec3d.b = d1;
                vec3d.c += d8 * d4;
            }
            else
            {
                if (k > j1)
                {
                    byte0 = 2;
                }
                else
                {
                    byte0 = 3;
                }

                vec3d.a += d6 * d5;
                vec3d.b += d7 * d5;
                vec3d.c = d2;
            }

            Vec3D vec3d2 = Vec3D.create(vec3d.a, vec3d.b, vec3d.c);
            l = (int)(vec3d2.a = MathHelper.floor(vec3d.a));

            if (byte0 == 5)
            {
                l--;
                vec3d2.a++;
            }

            i1 = (int)(vec3d2.b = MathHelper.floor(vec3d.b));

            if (byte0 == 1)
            {
                i1--;
                vec3d2.b++;
            }

            j1 = (int)(vec3d2.c = MathHelper.floor(vec3d.c));

            if (byte0 == 3)
            {
                j1--;
                vec3d2.c++;
            }

            int l1 = world.getTypeId(l, i1, j1);
            int i2 = world.getData(l, i1, j1);
            Block block = Block.byId[l1];

            if (l1 > 0 && block.a(i2, flag) && l1 != Block.GLASS.id && l1 != Block.THIN_GLASS.id)
            {
                MovingObjectPosition movingobjectposition = block.a(world, l, i1, j1, vec3d, vec3d1);

                if (movingobjectposition != null)
                {
                    return movingobjectposition;
                }
            }
        }

        return null;
    }
}
