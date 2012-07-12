package uberkat.war;

import java.util.Map;
import java.util.Random;
import net.minecraft.server.*;

public abstract class ItemGun extends ItemCustomUseDelay
{
    public String firingSound;
    public Item requiredBullet;
    public int numBullets;
    public int burstShots;
    public int damage;
    public float headshotMultiplier;
    public float muzzleVelocity;
    public float spread;
    public float recoil;
    public int soundDelay;
    public float soundRangeFactor;
    protected long lastSound;
    protected long lastEmptySound;

    public ItemGun(int i)
    {
        super(i);
        numBullets = 1;
        burstShots = 0;
        damage = 0;
        headshotMultiplier = 2.0F;
        muzzleVelocity = 1.5F;
        spread = 1.0F;
        recoil = 1.0F;
        soundDelay = -1;
        soundRangeFactor = 4F;
        lastSound = 0L;
        lastEmptySound = 0L;
        maxStackSize = 1;
    }

    /**
     * Returns the damage against a given entity.
     */
    public int a(Entity entity)
    {
        return 4;
    }

    public boolean use(ItemStack itemstack, World world, Entity entity, float f, float f1, float f2, float f3, float f4)
    {
        return fireBullet(world, entity, itemstack, false, f, f1, f2, f3, f4);
    }

    public boolean fireBullet(World world, Entity entity, ItemStack itemstack, boolean flag, float f, float f1, float f2, float f3, float f4)
    {
        if (!mod_ModernWarfare.reloadTimes.containsKey(entity))
        {
            int i;

            if (entity instanceof EntityHuman)
            {
                i = WarTools.useItemInInventory((EntityHuman)entity, requiredBullet.id);
            }
            else if (entity.passenger != null && (entity.passenger instanceof EntityHuman))
            {
                i = WarTools.useItemInInventory((EntityHuman)entity.passenger, requiredBullet.id);
            }
            else
            {
                i = 1;
            }

            if (i > 0)
            {
                if (world.getTime() - lastSound < 0L)
                {
                    lastSound = world.getTime() - (long)soundDelay;
                }

                if (soundDelay == 0 || lastSound == 0L || world.getTime() - lastSound > (long)soundDelay)
                {
                    world.makeSound(entity, firingSound, 1.0F, 1.0F / (c.nextFloat() * 0.1F + 0.95F));
                    lastSound = world.getTime();
                }

                if (!world.isStatic)
                {
                    for (int j = 0; j < numBullets; j++)
                    {
                        EntityBullet entitybullet = getBulletEntity(world, entity, f, f1, f2, f3, f4);

                        if (entitybullet != null)
                        {
                            world.addEntity(entitybullet);
                        }
                    }

                    EntityBulletCasing entitybulletcasing = getBulletCasingEntity(world, entity, f1);

                    if ((entity instanceof EntityHuman) && mod_ModernWarfare.ammoCasings && entitybulletcasing != null)
                    {
                        world.addEntity(entitybulletcasing);
                    }

                    if (!flag && burstShots > 0)
                    {
                        mod_ModernWarfare.burstShots.put(entity, new BurstShotEntry(burstShots, itemstack));
                    }
                }

                if (entity instanceof EntityHuman)
                {
                    double d = Math.min(recoil, entity.pitch + 90F);
                    double d1 = world.random.nextFloat() * recoil * 0.5F - recoil * 0.25F;

                    if (!entity.isSneaking())
                    {
                        d *= 2D;
                        d1 *= 2D;

                        if (this instanceof ItemGunMinigun)
                        {
                            d *= 2D;
                            d1 *= 2D;
                        }
                    }

                    if (!entity.onGround)
                    {
                        d *= 2D;
                        d1 *= 2D;
                    }

                    mod_ModernWarfare.currentRecoilV += d;
                    mod_ModernWarfare.currentRecoilH += d1;
                    entity.pitch -= d;
                    entity.yaw += d1;

                    if (i == 2 && !(this instanceof ItemGunMinigun) && !(this instanceof ItemGunLaser))
                    {
                        mod_ModernWarfare.handleReload(world, (EntityHuman)entity, true);
                    }
                }

                return true;
            }

            if (lastEmptySound == 0L || world.getTime() - lastEmptySound > 20L)
            {
                world.makeSound(entity, "sdk.gunempty", 1.0F, 1.0F / (c.nextFloat() * 0.1F + 0.95F));
                lastEmptySound = world.getTime();
            }
        }

        return false;
    }

    public boolean isFull3D()
    {
        return true;
    }

    public abstract EntityBullet getBulletEntity(World world, Entity entity, float f, float f1, float f2, float f3, float f4);

    public abstract EntityBulletCasing getBulletCasingEntity(World world, Entity entity, float f);
}
