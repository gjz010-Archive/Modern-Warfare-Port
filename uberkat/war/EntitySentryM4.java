package uberkat.war;

import net.minecraft.server.World;
import net.minecraft.server.mod_ModernWarfare;

public class EntitySentryM4 extends EntitySentry
{
    public EntitySentryM4(World world)
    {
        super(world);
        setParameters();
    }

    public EntitySentryM4(World world, double d, double d1, double d2)
    {
        super(world, d, d1, d2);
        setParameters();
    }

    private void setParameters()
    {
        gun = (ItemGun)mod_ModernWarfare.itemGunM4;
        ATTACK_DELAY = 125;
        range = 32F;
    }
}
