package uberkat.war;

import net.minecraft.server.World;
import net.minecraft.server.mod_ModernWarfare;
public class EntitySentrySg552 extends EntitySentry
{
    public EntitySentrySg552(World world)
    {
        super(world);
        setParameters();
    }

    public EntitySentrySg552(World world, double d, double d1, double d2)
    {
        super(world, d, d1, d2);
        setParameters();
    }

    private void setParameters()
    {
        gun = (ItemGun)mod_ModernWarfare.itemGunSg552;
        ATTACK_DELAY = 50;
        range = 48F;
    }
}
