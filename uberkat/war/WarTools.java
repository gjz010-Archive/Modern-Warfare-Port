package uberkat.war;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.logging.Logger;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.*;

public class WarTools
{
    private static Method blockFireSetBurnRate = null;
    public static MinecraftServer minecraftserver = ModLoader.getMinecraftServerInstance();
    public static Random random = new Random();
    private static List recipes;

    public WarTools()
    {
    }

    public static int getNumberInFirstStackInInventory(PlayerInventory playerinventory, int i)
    {
        for (int j = 0; j < playerinventory.items.length; j++)
        {
            if (playerinventory.items[j] != null && playerinventory.items[j].id == i)
            {
                if (playerinventory.items[j].getItem().getMaxDurability() > 0)
                {
                    return (playerinventory.items[j].getItem().getMaxDurability() - playerinventory.items[j].getData()) + 1;
                }
                else
                {
                    return playerinventory.items[j].count;
                }
            }
        }

        return -1;
    }

    public static int getNumberInInventory(PlayerInventory playerinventory, int i)
    {
        int j = 0;

        for (int k = 0; k < playerinventory.items.length; k++)
        {
            if (playerinventory.items[k] != null && playerinventory.items[k].id == i)
            {
                j++;
            }
        }

        return j;
    }

    public static int getNumberInFirstStackInHotbar(PlayerInventory playerinventory, int i)
    {
        for (int j = 0; j < 9; j++)
        {
            if (playerinventory.items[j] != null && playerinventory.items[j].id == i)
            {
                return playerinventory.items[j].count;
            }
        }

        return -1;
    }

    public static boolean playerInventoryEmpty(PlayerInventory playerinventory)
    {
        for (int i = 0; i < playerinventory.items.length; i++)
        {
            if (playerinventory.items[i] != null)
            {
                return false;
            }
        }

        for (int j = 0; j < playerinventory.armor.length; j++)
        {
            if (playerinventory.armor[j] != null)
            {
                return false;
            }
        }

        return true;
    }

    public static void setBurnRate(int i, int j, int k)
    {
        if (blockFireSetBurnRate == null)
        {
            try
            {
                try
                {
                    blockFireSetBurnRate = (net.minecraft.server.BlockFire.class).getDeclaredMethod("a", new Class[]
                            {
                                Integer.TYPE, Integer.TYPE, Integer.TYPE
                            });
                }
                catch (NoSuchMethodException nosuchmethodexception)
                {
                    blockFireSetBurnRate = (net.minecraft.server.BlockFire.class).getDeclaredMethod("setBurnRate", new Class[]
                            {
                                Integer.TYPE, Integer.TYPE, Integer.TYPE
                            });
                }

                blockFireSetBurnRate.setAccessible(true);
            }
            catch (NoSuchMethodException nosuchmethodexception1)
            {
                ModLoader.getLogger().throwing("WarTools", "setBurnRate", nosuchmethodexception1);
                ThrowException("Aidan, you forgot to update your obfuscated reflection!", nosuchmethodexception1);
                return;
            }
        }

        try
        {
            blockFireSetBurnRate.invoke(Block.FIRE, new Object[]
                    {
                        Integer.valueOf(i), Integer.valueOf(j), Integer.valueOf(k)
                    });
        }
        catch (InvocationTargetException invocationtargetexception)
        {
            ModLoader.getLogger().throwing("WarTools", "setBurnRate", invocationtargetexception);
            ThrowException("An impossible error has occurred!", invocationtargetexception);
            return;
        }
        catch (IllegalAccessException illegalaccessexception)
        {
            ModLoader.getLogger().throwing("WarTools", "setBurnRate", illegalaccessexception);
            ThrowException("An impossible error has occurred!", illegalaccessexception);
            return;
        }
    }

    public static boolean removeShapedRecipe(ItemStack itemstack, Object aobj[])
    {
        try
        {
            if (recipes == null)
            {
                recipes = (List)ModLoader.getPrivateValue(net.minecraft.server.CraftingManager.class, CraftingManager.getInstance(), 1);
            }

            String s = "";
            int i = 0;
            int j = 0;
            int k = 0;

            if (aobj[i] instanceof String[])
            {
                String as[] = (String[])aobj[i++];

                for (int l = 0; l < as.length; l++)
                {
                    String s2 = as[l];
                    k++;
                    j = s2.length();
                    s = (new StringBuilder()).append(s).append(s2).toString();
                }
            }
            else
            {
                do
                {
                    String s1 = (String)aobj[i++];
                    k++;
                    j = s1.length();
                    s = (new StringBuilder()).append(s).append(s1).toString();
                }
                while (aobj[i] instanceof String);
            }

            HashMap hashmap = new HashMap();

            for (; i < aobj.length; i += 2)
            {
                Character character = (Character)aobj[i];
                ItemStack itemstack1 = null;

                if (aobj[i + 1] instanceof Item)
                {
                    itemstack1 = new ItemStack((Item)aobj[i + 1]);
                }
                else if (aobj[i + 1] instanceof Block)
                {
                    itemstack1 = new ItemStack((Block)aobj[i + 1], 1, -1);
                }
                else if (aobj[i + 1] instanceof ItemStack)
                {
                    itemstack1 = (ItemStack)aobj[i + 1];
                }

                hashmap.put(character, itemstack1);
            }

            ItemStack aitemstack[] = new ItemStack[j * k];

            for (int i1 = 0; i1 < j * k; i1++)
            {
                char c = s.charAt(i1);

                if (hashmap.containsKey(Character.valueOf(c)))
                {
                    aitemstack[i1] = ((ItemStack)hashmap.get(Character.valueOf(c))).cloneItemStack();
                }
                else
                {
                    aitemstack[i1] = null;
                }
            }

            ShapedRecipes shapedrecipes = new ShapedRecipes(j, k, aitemstack, itemstack);

            for (int j1 = 0; j1 < recipes.size(); j1++)
            {
                CraftingRecipe craftingrecipe = (CraftingRecipe)recipes.get(j1);

                if (craftingrecipe instanceof ShapedRecipes)
                {
                    ShapedRecipes shapedrecipes1 = (ShapedRecipes)craftingrecipe;

                    if (shapedrecipes.a() == shapedrecipes1.a())
                    {
                        ItemStack itemstack2 = (ItemStack)ModLoader.getPrivateValue(net.minecraft.server.ShapedRecipes.class, shapedrecipes, 3);
                        ItemStack itemstack3 = (ItemStack)ModLoader.getPrivateValue(net.minecraft.server.ShapedRecipes.class, shapedrecipes1, 3);

                        if (itemstack2 != null && itemstack3 != null && ItemStack.matches(itemstack2, itemstack3))
                        {
                            ItemStack aitemstack1[] = (ItemStack[])ModLoader.getPrivateValue(net.minecraft.server.ShapedRecipes.class, shapedrecipes, 2);
                            ItemStack aitemstack2[] = (ItemStack[])ModLoader.getPrivateValue(net.minecraft.server.ShapedRecipes.class, shapedrecipes1, 2);
                            boolean flag = true;
                            int k1 = 0;

                            do
                            {
                                if (k1 >= aitemstack1.length)
                                {
                                    break;
                                }

                                if ((aitemstack1[k1] != null || aitemstack2[k1] != null) && (aitemstack1[k1] == null || aitemstack2[k1] == null || !ItemStack.matches(aitemstack1[k1], aitemstack2[k1])))
                                {
                                    flag = false;
                                    break;
                                }

                                k1++;
                            }
                            while (true);

                            if (flag)
                            {
                                recipes.remove(j1);
                                return true;
                            }
                        }
                    }
                }
            }
        }
        catch (IllegalArgumentException illegalargumentexception)
        {
            ModLoader.getLogger().throwing("WarTools", "removeShapedRecipe", illegalargumentexception);
            ThrowException("An impossible error has occurred!", illegalargumentexception);
            return false;
        }
        catch (SecurityException securityexception)
        {
            ModLoader.getLogger().throwing("WarTools", "removeShapedRecipe", securityexception);
            ThrowException("An impossible error has occurred!", securityexception);
            return false;
        }
        catch (Exception exception)
        {
            ModLoader.getLogger().throwing("WarTools", "removeShapedRecipe", exception);
            ThrowException("An impossible error has occurred!", exception);
            return false;
        }

        return false;
    }

    public static boolean removeShapelessRecipe(ItemStack itemstack, Object aobj[])
    {
        try
        {
            if (recipes == null)
            {
                recipes = (List)ModLoader.getPrivateValue(net.minecraft.server.CraftingManager.class, CraftingManager.getInstance(), 1);
            }

            ArrayList arraylist = new ArrayList();

            for (int i = 0; i < aobj.length; i++)
            {
                Object obj = aobj[i];

                if (obj instanceof ItemStack)
                {
                    arraylist.add(((ItemStack)obj).cloneItemStack());
                    continue;
                }

                if (obj instanceof Item)
                {
                    arraylist.add(new ItemStack((Item)obj));
                    continue;
                }

                if (obj instanceof Block)
                {
                    arraylist.add(new ItemStack((Block)obj));
                }
                else
                {
                    throw new RuntimeException("Invalid shapeless recipe!");
                }
            }

            for (int j = 0; j < recipes.size(); j++)
            {
                if (recipes.get(j) instanceof ShapelessRecipes)
                {
                    ShapelessRecipes shapelessrecipes = (ShapelessRecipes)recipes.get(j);
                    ItemStack itemstack1 = (ItemStack)ModLoader.getPrivateValue(net.minecraft.server.ShapelessRecipes.class, shapelessrecipes, 0);
                    List list = (List)ModLoader.getPrivateValue(net.minecraft.server.ShapelessRecipes.class, shapelessrecipes, 1);

                    if (ItemStack.matches(itemstack1, itemstack) && arraylist.size() == list.size())
                    {
                        boolean flag = true;

                        for (int k = 0; k < arraylist.size(); k++)
                        {
                            ItemStack itemstack2 = (ItemStack)list.get(k);
                            ItemStack itemstack3 = (ItemStack)arraylist.get(k);

                            if (!ItemStack.matches(itemstack2, itemstack3))
                            {
                                flag = false;
                            }
                        }

                        if (flag)
                        {
                            return true;
                        }
                    }
                }
            }
        }
        catch (IllegalArgumentException illegalargumentexception)
        {
            ModLoader.getLogger().throwing("WarTools", "removeShapelessRecipe", illegalargumentexception);
            ThrowException("An impossible error has occurred!", illegalargumentexception);
            return false;
        }
        catch (SecurityException securityexception)
        {
            ModLoader.getLogger().throwing("WarTools", "removeShapelessRecipe", securityexception);
            ThrowException("An impossible error has occurred!", securityexception);
            return false;
        }
        catch (Exception exception)
        {
            ModLoader.getLogger().throwing("WarTools", "removeShapelessRecipe", exception);
            ThrowException("An impossible error has occurred!", exception);
            return false;
        }

        return false;
    }

    public static void attackEntityIgnoreDelay(EntityLiving entityliving, DamageSource damagesource, int i)
    {
        try
        {
            int j;
            int k;
            int l;

            try
            {
                j = ((Integer)ModLoader.getPrivateValue(net.minecraft.server.EntityLiving.class, entityliving, "aU")).intValue();
                k = ((Integer)ModLoader.getPrivateValue(net.minecraft.server.Entity.class, entityliving, "bW")).intValue();
                l = ((Integer)ModLoader.getPrivateValue(net.minecraft.server.EntityLiving.class, entityliving, "S")).intValue();
            }
            catch (Exception exception1)
            {
                j = ((Integer)ModLoader.getPrivateValue(net.minecraft.server.EntityLiving.class, entityliving, "naturalArmorRating")).intValue();
                k = ((Integer)ModLoader.getPrivateValue(net.minecraft.server.Entity.class, entityliving, "heartsLife")).intValue();
                l = ((Integer)ModLoader.getPrivateValue(net.minecraft.server.EntityLiving.class, entityliving, "heartsHalvesLife")).intValue();
            }

            if ((float)k > (float)l / 2.0F)
            {
                entityliving.damageEntity(damagesource, j + i);
            }
            else
            {
                entityliving.damageEntity(damagesource, i);
            }
        }
        catch (Exception exception)
        {
            ModLoader.getLogger().throwing("WarTools", "attackEntityIgnoreDelay", exception);
            ThrowException("Aidan, you forgot to update your obfuscated reflection!", exception);
            return;
        }
    }

    public static void ThrowException(String s, Throwable throwable)
    {
        try
        {
            ModLoader.setPrivateValue(java.lang.Throwable.class, throwable, "detailMessage", s);
        }
        catch (Exception exception) { }

        ModLoader.throwException(s, throwable);
    }

    public static boolean isFlammable(int i)
    {
        try
        {
            int ai[];

            try
            {
                ai = (int[])ModLoader.getPrivateValue(net.minecraft.server.BlockFire.class, Block.FIRE, "b");
            }
            catch (Exception exception1)
            {
                ai = (int[])ModLoader.getPrivateValue(net.minecraft.server.BlockFire.class, Block.FIRE, "abilityToCatchFire");
            }

            return ai[i] != 0 || i == Block.NETHERRACK.id;
        }
        catch (Exception exception)
        {
            ModLoader.getLogger().throwing("WarTools", "isFlammable", exception);
            ThrowException("Aidan, you forgot to update your obfuscated reflection!", exception);
            return false;
        }
    }

    public static int useItemInInventory(EntityHuman entityhuman, int i)
    {
        int j = getInventorySlotContainItem(entityhuman.inventory, i);

        if (j < 0)
        {
            return 0;
        }

        if (Item.byId[i].getMaxDurability() > 0)
        {
            entityhuman.inventory.items[j].damage(1, entityhuman);

            if (entityhuman.inventory.items[j].count == 0)
            {
                entityhuman.inventory.items[j] = new ItemStack(Item.BUCKET);

                if (getInventorySlotContainItem(entityhuman.inventory, i) >= 0)
                {
                    return 2;
                }
            }
        }
        else if (--entityhuman.inventory.items[j].count <= 0)
        {
            entityhuman.inventory.items[j] = null;

            if (getInventorySlotContainItem(entityhuman.inventory, i) >= 0)
            {
                return 2;
            }
        }

        return 1;
    }

    private static int getInventorySlotContainItem(PlayerInventory playerinventory, int i)
    {
        for (int j = 0; j < playerinventory.items.length; j++)
        {
            if (playerinventory.items[j] != null && playerinventory.items[j].id == i)
            {
                return j;
            }
        }

        return -1;
    }

    public static int useItemInHotbar(EntityHuman entityhuman, int i)
    {
        int j = getHotbarSlotContainItem(entityhuman.inventory, i);

        if (j < 0)
        {
            return 0;
        }

        if (Item.byId[i].getMaxDurability() > 0)
        {
            entityhuman.inventory.items[j].damage(1, entityhuman);

            if (entityhuman.inventory.items[j].count == 0)
            {
                entityhuman.inventory.items[j] = new ItemStack(Item.BUCKET);

                if (getHotbarSlotContainItem(entityhuman.inventory, i) >= 0)
                {
                    return 2;
                }
            }
        }
        else if (--entityhuman.inventory.items[j].count <= 0)
        {
            entityhuman.inventory.items[j] = null;

            if (getHotbarSlotContainItem(entityhuman.inventory, i) >= 0)
            {
                return 2;
            }
        }

        return 1;
    }

    private static int getHotbarSlotContainItem(PlayerInventory playerinventory, int i)
    {
        for (int j = 0; j < 9; j++)
        {
            if (playerinventory.items[j] != null && playerinventory.items[j].id == i)
            {
                return j;
            }
        }

        return -1;
    }

    public static boolean onGroundOrInWater(World world, Entity entity)
    {
        return entity.onGround || world.a(entity.boundingBox, Material.WATER, entity);
    }
}
