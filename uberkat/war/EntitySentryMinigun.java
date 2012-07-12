package uberkat.war;

import net.minecraft.server.World;
import net.minecraft.server.mod_ModernWarfare;

public class EntitySentryMinigun extends EntitySentry
{
    public EntitySentryMinigun(World world)
    {
        super(world);
        setParameters();
    }

    public EntitySentryMinigun(World world, double d, double d1, double d2)
    {
        super(world, d, d1, d2);
        setParameters();
    }

    private void setParameters()
    {
        gun = (ItemGun)mod_ModernWarfare.itemGunMinigun;
        ATTACK_DELAY = 5;
        range = 32F;
    }
}
