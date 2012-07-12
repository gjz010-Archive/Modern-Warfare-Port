package uberkat.war;

import java.util.Random;
import net.minecraft.server.*;

public class EntityBulletShot extends EntityBullet
{
    public EntityBulletShot(World world)
    {
        super(world);
        b(0.03125F, 0.03125F);
    }

    public EntityBulletShot(World world, double d, double d1, double d2)
    {
        super(world, d, d1, d2);
        b(0.03125F, 0.03125F);
    }

    public EntityBulletShot(World world, Entity entity, ItemGun itemgun, float f, float f1, float f2, float f3, float f4)
    {
        super(world, entity, itemgun, f, f1, f2, f3, f4);
        b(0.03125F, 0.03125F);
    }

    public void playServerSound(World world)
    {
        world.makeSound(this, ((ItemGun)mod_ModernWarfare.itemGunShotgun).firingSound, ((ItemGun)mod_ModernWarfare.itemGunShotgun).soundRangeFactor, 1.0F / (random.nextFloat() * 0.1F + 0.95F));
    }
}
