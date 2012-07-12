package uberkat.war;

import java.util.Random;
import net.minecraft.server.*;

public class BlockRope extends BlockContainerWar
{
    public float ascensionSpeed;
    public float descensionSpeed;
    private int renderType;

    public BlockRope(int i, int j, int k)
    {
        super(i, j, Material.CLOTH);
        ascensionSpeed = 0.2F;
        descensionSpeed = -0.15F;
        renderType = k;
    }

    /**
     * Triggered whenever an entity collides with this block (enters into the block). Args: world, x, y, z, entity
     */
    public void a(World world, int i, int j, int k, Entity entity)
    {
        if (entity instanceof EntityLiving)
        {
            entity.fallDistance = 0.0F;

            if (entity.motY < (double)descensionSpeed)
            {
                entity.motY = descensionSpeed;
            }

            if (entity.positionChanged)
            {
                entity.motY = ascensionSpeed;
            }
        }
    }

    /**
     * Returns the TileEntity used by this block.
     */
    public TileEntity a_()
    {
        return new TileEntityRope();
    }

    /**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
    public AxisAlignedBB e(World world, int i, int j, int k)
    {
        int l = world.getData(i, j, k);
        float f = 0.125F;

        if (l == 2)
        {
            a(0.0F, 0.0F, 1.0F - f, 1.0F, 1.0F, 1.0F);
        }

        if (l == 3)
        {
            a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, f);
        }

        if (l == 4)
        {
            a(1.0F - f, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        }

        if (l == 5)
        {
            a(0.0F, 0.0F, 0.0F, f, 1.0F, 1.0F);
        }

        return super.e(world, i, j, k);
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    public boolean a()
    {
        return false;
    }

    /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
    public boolean b()
    {
        return false;
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    public int a(Random random)
    {
        return 0;
    }
}
