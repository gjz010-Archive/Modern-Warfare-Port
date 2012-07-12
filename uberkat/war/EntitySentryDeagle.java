package uberkat.war;

import net.minecraft.server.World;
import net.minecraft.server.mod_ModernWarfare;

public class EntitySentryDeagle extends EntitySentry
{
    public EntitySentryDeagle(World world)
    {
        super(world);
        setParameters();
    }

    public EntitySentryDeagle(World world, double d, double d1, double d2)
    {
        super(world, d, d1, d2);
        setParameters();
    }

    private void setParameters()
    {
        gun = (ItemGun)mod_ModernWarfare.itemGunDeagle;
        ATTACK_DELAY = 40;
        range = 32F;
    }
}
