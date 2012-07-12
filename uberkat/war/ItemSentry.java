package uberkat.war;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Logger;
import net.minecraft.server.*;

public class ItemSentry extends ItemWar
{
    public ItemSentry(int i)
    {
        super(i);
    }

    /**
     * Returns the metadata of the block which this Item (ItemBlock) can place
     */
    public int filterData(int i)
    {
        return i;
    }

    public String a(ItemStack itemstack)
    {
        return (new StringBuilder()).append(mod_ModernWarfare.sentryNames[itemstack.getData()]).append(" Sentry").toString();
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

        if (movingobjectposition.type == EnumMovingObjectType.TILE && movingobjectposition.face == 1)
        {
            int i = movingobjectposition.b;
            int j = movingobjectposition.c;
            int k = movingobjectposition.d;
            int l = world.getTypeId(i, j + 1, k);
            int i1 = world.getTypeId(i, j + 2, k);

            if (l == 0 && i1 == 0)
            {
                try
                {
                    Constructor constructor = mod_ModernWarfare.sentryEntityClasses[itemstack.getData()].getConstructor(new Class[]
                            {
                                net.minecraft.server.World.class
                            });
                    EntitySentry entitysentry = (EntitySentry)constructor.newInstance(new Object[]
                            {
                                world
                            });
                    entitysentry.setOwner(entityhuman.name);
                    entitysentry.setPositionRotation((double)i + 0.5D, (double)j + 1.0D, (double)k + 0.5D, 0.0F, 0.0F);
                    world.addEntity(entitysentry);
                }
                catch (NoSuchMethodException nosuchmethodexception)
                {
                    ModLoader.getLogger().throwing("BlockSentry", "onBlockPlaced", nosuchmethodexception);
                    WarTools.ThrowException("An impossible error has occured!", nosuchmethodexception);
                    return itemstack;
                }
                catch (InvocationTargetException invocationtargetexception)
                {
                    ModLoader.getLogger().throwing("BlockSentry", "onBlockPlaced", invocationtargetexception);
                    WarTools.ThrowException("An impossible error has occured!", invocationtargetexception);
                    return itemstack;
                }
                catch (IllegalAccessException illegalaccessexception)
                {
                    ModLoader.getLogger().throwing("BlockSentry", "onBlockPlaced", illegalaccessexception);
                    WarTools.ThrowException("An impossible error has occured!", illegalaccessexception);
                    return itemstack;
                }
                catch (InstantiationException instantiationexception)
                {
                    ModLoader.getLogger().throwing("BlockSentry", "onBlockPlaced", instantiationexception);
                    WarTools.ThrowException("An impossible error has occured!", instantiationexception);
                    return itemstack;
                }

                itemstack.count--;
            }
        }

        return itemstack;
    }
}
