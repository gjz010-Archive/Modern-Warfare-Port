package uberkat.war;

import net.minecraft.server.World;
import net.minecraft.server.mod_ModernWarfare;
public class EntitySentryRocketLauncher extends EntitySentry
{
    public EntitySentryRocketLauncher(World world)
    {
        super(world);
        setParameters();
    }

    public EntitySentryRocketLauncher(World world, double d, double d1, double d2)
    {
        super(world, d, d1, d2);
        setParameters();
    }

    private void setParameters()
    {
        gun = (ItemGun)mod_ModernWarfare.itemGunRocketLauncher;
        ATTACK_DELAY = 150;
        range = 32F;
    }
}
