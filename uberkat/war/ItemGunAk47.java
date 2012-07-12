package uberkat.war;

import net.minecraft.server.*;

public class ItemGunAk47 extends ItemGun
{
    public ItemGunAk47(int i)
    {
        super(i);
        firingSound = "war.ak";
        requiredBullet = mod_ModernWarfare.itemBulletLight;
        numBullets = 1;
        damage = 5;
        muzzleVelocity = 4F;
        spread = 0.5F;
        useDelay = 2;
        recoil = 2.0F;
    }

    public EntityBullet getBulletEntity(World world, Entity entity, float f, float f1, float f2, float f3, float f4)
    {
        return new EntityBulletAk47(world, entity, this, f, f1, f2, f3, f4);
    }

    public EntityBulletCasing getBulletCasingEntity(World world, Entity entity, float f)
    {
        return new EntityBulletCasing(world, entity, f);
    }
}
