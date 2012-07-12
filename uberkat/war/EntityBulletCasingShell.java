package uberkat.war;

import net.minecraft.server.*;

public class EntityBulletCasingShell extends EntityBulletCasing
{
    public EntityBulletCasingShell(World world)
    {
        super(world);
        droppedItem = null;
    }

    public EntityBulletCasingShell(World world, double d, double d1, double d2)
    {
        super(world, d, d1, d2);
    }

    public EntityBulletCasingShell(World world, Entity entity, float f)
    {
        super(world, entity, f);
        droppedItem = mod_ModernWarfare.itemBulletCasingShell;
    }
}
