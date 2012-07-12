package uberkat.war;

import net.minecraft.server.*;

public class ItemGunShotgun extends ItemGun
{
    public ItemGunShotgun(int i)
    {
        super(i);
        firingSound = "war.shotgun";
        requiredBullet = mod_ModernWarfare.itemBulletShell;
        numBullets = 12;
        damage = 2;
        muzzleVelocity = 3F;
        spread = 8F;
        useDelay = 16;
        recoil = 8F;
    }

    public EntityBullet getBulletEntity(World world, Entity entity, float f, float f1, float f2, float f3, float f4)
    {
        return new EntityBulletShot(world, entity, this, f, f1, f2, f3, f4);
    }

    public EntityBulletCasing getBulletCasingEntity(World world, Entity entity, float f)
    {
        return new EntityBulletCasingShell(world, entity, f);
    }
}
