package uberkat.war;

import net.minecraft.server.World;
import net.minecraft.server.mod_ModernWarfare;
public class EntitySentryShotgun extends EntitySentry
{
    public EntitySentryShotgun(World world)
    {
        super(world);
        setParameters();
    }

    public EntitySentryShotgun(World world, double d, double d1, double d2)
    {
        super(world, d, d1, d2);
        setParameters();
    }

    private void setParameters()
    {
        gun = (ItemGun)mod_ModernWarfare.itemGunShotgun;
        ATTACK_DELAY = 100;
        range = 16F;
    }
}
