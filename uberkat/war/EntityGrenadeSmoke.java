package uberkat.war;

import java.util.Random;
import net.minecraft.server.*;

public class EntityGrenadeSmoke extends EntityGrenade
{
    protected String BOUNCE_SOUND;
    private static final int SMOKE_TIME = 500;
    private static final int MAX_DIAMETER_TIME = 250;
    private static final double MAX_DIAMETER = 8D;

    public EntityGrenadeSmoke(World world)
    {
        super(world);
        BOUNCE_SOUND = "war.smokegrenadebounce";
        itemStack = new ItemStack(mod_ModernWarfare.itemGrenadeSmoke, 1, 0);
    }

    public EntityGrenadeSmoke(World world, double d, double d1, double d2)
    {
        super(world, d, d1, d2);
        BOUNCE_SOUND = "war.smokegrenadebounce";
        itemStack = new ItemStack(mod_ModernWarfare.itemGrenadeSmoke, 1, 0);
    }

    public EntityGrenadeSmoke(World world, EntityLiving entityliving)
    {
        super(world, entityliving);
        BOUNCE_SOUND = "war.smokegrenadebounce";
        itemStack = new ItemStack(mod_ModernWarfare.itemGrenadeSmoke, 1, 0);
    }

    protected void explode()
    {
        if (!exploded)
        {
            exploded = true;
            world.makeSound(this, "war.smokegrenade", 1.0F, 1.0F / (random.nextFloat() * 0.1F + 0.95F));
        }

        if (fuse < -500)
        {
            dead = true;
        }

        if (exploded)
        {
            double d = Math.min(8D, ((double)(-fuse) * 8D) / 250D);
            int i = Math.min(250, -fuse);

            for (int j = 0; j < i; j++)
            {
                world.a("largesmoke", (locX + random.nextDouble() * d) - 0.5D * d, locY + random.nextDouble() * d, (locZ + random.nextDouble() * d) - 0.5D * d, 0.0D, 0.0D, 0.0D);
            }
        }
    }
}
