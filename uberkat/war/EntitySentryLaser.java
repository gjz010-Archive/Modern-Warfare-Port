package uberkat.war;

import net.minecraft.server.World;
import net.minecraft.server.mod_ModernWarfare;

public class EntitySentryLaser extends EntitySentry
{
    public EntitySentryLaser(World world)
    {
        super(world);
        setParameters();
    }

    public EntitySentryLaser(World world, double d, double d1, double d2)
    {
        super(world, d, d1, d2);
        setParameters();
    }

    private void setParameters()
    {
        gun = (ItemGun)mod_ModernWarfare.itemGunLaser;
        ATTACK_DELAY = 50;
        range = 32F;
    }
}
