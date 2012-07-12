package uberkat.war;

import net.minecraft.server.*;

public class ItemAtv extends Item
{
    public ItemAtv(int i)
    {
        super(i);
        maxStackSize = 1;
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack a(ItemStack itemstack, World world, EntityHuman entityhuman)
    {
        float f = 1.0F;
        float f1 = entityhuman.lastPitch + (entityhuman.pitch - entityhuman.lastPitch) * f;
        float f2 = entityhuman.lastYaw + (entityhuman.yaw - entityhuman.lastYaw) * f;
        double d = entityhuman.lastX + (entityhuman.locX - entityhuman.lastX) * (double)f;
        double d1 = (entityhuman.lastY + (entityhuman.locY - entityhuman.lastY) * (double)f + 1.6200000000000001D) - (double)entityhuman.height;
        double d2 = entityhuman.lastZ + (entityhuman.locZ - entityhuman.lastZ) * (double)f;
        Vec3D vec3d = Vec3D.create(d, d1, d2);
        float f3 = MathHelper.cos(-f2 * 0.01745329F - (float)Math.PI);
        float f4 = MathHelper.sin(-f2 * 0.01745329F - (float)Math.PI);
        float f5 = -MathHelper.cos(-f1 * 0.01745329F);
        float f6 = MathHelper.sin(-f1 * 0.01745329F);
        float f7 = f4 * f5;
        float f8 = f6;
        float f9 = f3 * f5;
        double d3 = 5D;
        Vec3D vec3d1 = vec3d.add((double)f7 * d3, (double)f8 * d3, (double)f9 * d3);
        MovingObjectPosition movingobjectposition = world.rayTrace(vec3d, vec3d1, true);

        if (movingobjectposition == null)
        {
            return itemstack;
        }

        if (movingobjectposition.type == EnumMovingObjectType.TILE)
        {
            int i = movingobjectposition.b;
            int j = movingobjectposition.c;
            int k = movingobjectposition.d;

            if (!world.isStatic)
            {
                world.addEntity(new EntityAtv(world, (float)i + 0.5F, (float)j + 1.0F, (float)k + 0.5F));
            }

            itemstack.count--;
        }

        return itemstack;
    }
}
