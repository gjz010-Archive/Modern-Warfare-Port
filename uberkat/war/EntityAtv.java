package uberkat.war;

import java.util.List;
import java.util.Random;

import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.InventoryHolder;

import net.minecraft.server.*;

public class EntityAtv extends EntityLandVehicle implements IInventory
{
    public ItemStack gunA;
    public ItemStack gunB;
    public int deathTime;
    public int DEATH_TIME_MAX;
    public int soundLoopTime;
    public String SOUND_RIDING;
    public int SOUND_LOOP_TIME_MAX;

    public EntityAtv(World world)
    {
        super(world);
        gunA = null;
        gunB = null;
        deathTime = -13;
        DEATH_TIME_MAX = 100;
        soundLoopTime = 0;
        SOUND_RIDING = "war.atv";
        SOUND_LOOP_TIME_MAX = 3;
        b(1.0F, 1.0F);
        height = 0.3F;
        bP = 1.0F;
    }

    public EntityAtv(World world, double d, double d1, double d2)
    {
        this(world);
        setPosition(d, d1 + (double)height, d2);
        motX = 0.0D;
        motY = 0.0D;
        motZ = 0.0D;
        lastX = d;
        lastY = d1;
        lastZ = d2;
    }

    /**
     * Returns true if this entity should push and be pushed by other entities when colliding.
     */
    public boolean e_()
    {
        return true;
    }

    /**
     * Returns the Y offset from the entity's position for any entity riding this one.
     */
    public double x_()
    {
        return 0.29999999999999999D;
    }

    public float getHeadHeight()
    {
        return 0.7F;
    }

    public void onHurt()
    {
        world.makeSound(this, "war.mechhurt", 1.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);
    }

    public void onDeath()
    {
        if (deathTime == -13)
        {
            deathTime = DEATH_TIME_MAX;
        }
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void F_()
    {
        super.F_();

        if (random.nextInt(MAX_HEALTH) > health * 2)
        {
            if (Math.random() < 0.75D)
            {
                spawnParticles("smoke", 4, false);
            }
            else
            {
                spawnParticles("largesmoke", 1, false);
            }
        }

        if (health > 0 && deathTime != -13)
        {
            deathTime = -13;
        }

        if (deathTime >= 0)
        {
            if (deathTime == 0)
            {
                Explosion explosion = new Explosion(world, null, locX, (float)locY, (float)locZ, 3F);
                explosion.a();
                world.makeSound(locX, locY, locZ, "random.explode", 4F, (1.0F + (world.random.nextFloat() - world.random.nextFloat()) * 0.2F) * 0.7F);
                spawnParticles("explode", 64, true);
                spawnParticles("smoke", 64, true);
                die();
            }
            else if (random.nextInt(DEATH_TIME_MAX) > deathTime)
            {
                spawnParticles("flame", 8, false);
            }

            deathTime--;
        }

        if (passenger != null)
        {
            if (soundLoopTime <= 0)
            {
                world.makeSound(locX + motX * 1.5D, locY + (onGround ? 0.0D : motY) * 1.5D, locZ + motZ * 1.5D, SOUND_RIDING, 1.0F, 1.0F + (float)(getSpeed() / MAX_SPEED / 4D));
                soundLoopTime = SOUND_LOOP_TIME_MAX;
            }

            soundLoopTime--;
        }
        else
        {
            soundLoopTime = 0;
        }
    }

    public void spawnParticles(String s, int i, boolean flag)
    {
        for (int j = 0; j < i; j++)
        {
            double d = (locX + random.nextDouble() * (double)width * 1.5D) - (double)width * 0.75D;
            double d1 = ((locY + random.nextDouble() * (double)length) - (double)length * 0.5D) + 0.25D;
            double d2 = (locZ + random.nextDouble() * (double)width) - (double)width * 0.5D;
            double d3 = flag ? random.nextDouble() - 0.5D : 0.0D;
            double d4 = flag ? random.nextDouble() - 0.5D : 0.0D;
            double d5 = flag ? random.nextDouble() - 0.5D : 0.0D;

            if (Math.random() < 0.75D)
            {
                world.a(s, d, d1, d2, d3, d4, d5);
            }
            else
            {
                world.a(s, d, d1, d2, d3, d4, d5);
            }
        }
    }

    /**
     * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
     */
    public boolean b(EntityHuman entityhuman)
    {
        if (entityhuman.U() != null && entityhuman.U().id == mod_ModernWarfare.itemWrench.id)
        {
            if (health > 0 && health < MAX_HEALTH)
            {
                world.makeSound(this, "war.wrench", 1.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);
                health = Math.min(health + 4, MAX_HEALTH);
                entityhuman.C_();
                entityhuman.U().damage(1, entityhuman);

                if (entityhuman.U().getData() <= 0)
                {
                    entityhuman.inventory.items[entityhuman.inventory.itemInHandIndex] = null;
                }
            }

            return true;
        }

        if (passenger != null && (passenger instanceof EntityHuman) && passenger != entityhuman)
        {
            return true;
        }

        if (!world.isStatic)
        {
            entityhuman.mount(this);
            ItemCustomUseDelay.doNotUseThisTick = world.getTime();
        }

        return true;
    }

    /**
     * Reads the entity from NBT (calls an abstract helper method to read specialized data)
     */
    public void e(NBTTagCompound nbttagcompound)
    {
        NBTTagList nbttaglist = nbttagcompound.getList("Pos");
        NBTTagList nbttaglist1 = nbttagcompound.getList("Motion");
        NBTTagList nbttaglist2 = nbttagcompound.getList("Rotation");
        setPosition(0.0D, 0.0D, 0.0D);
        motX = ((NBTTagDouble)nbttaglist1.get(0)).data;
        motY = ((NBTTagDouble)nbttaglist1.get(1)).data;
        motZ = ((NBTTagDouble)nbttaglist1.get(2)).data;

        if (Math.abs(motX) > 10D)
        {
            motX = 0.0D;
        }

        if (Math.abs(motY) > 10D)
        {
            motY = 0.0D;
        }

        if (Math.abs(motZ) > 10D)
        {
            motZ = 0.0D;
        }

        lastX = bL = locX = ((NBTTagDouble)nbttaglist.get(0)).data;
        lastY = bM = locY = ((NBTTagDouble)nbttaglist.get(1)).data;
        lastZ = bN = locZ = ((NBTTagDouble)nbttaglist.get(2)).data;
        lastYaw = yaw = ((NBTTagFloat)nbttaglist2.get(0)).data;
        lastPitch = pitch = ((NBTTagFloat)nbttaglist2.get(1)).data;
        fallDistance = nbttagcompound.getFloat("FallDistance");
        setOnFire(nbttagcompound.getShort("Fire"));
        setAirTicks(nbttagcompound.getShort("Air"));
        onGround = nbttagcompound.getBoolean("OnGround");
        setPosition(locX, locY, locZ);
        a(nbttagcompound);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void a(NBTTagCompound nbttagcompound)
    {
        NBTTagList nbttaglist = nbttagcompound.getList("GunA");

        if (nbttaglist.size() > 0)
        {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.get(0);
            byte byte0 = nbttagcompound1.getByte("Slot");

            if (byte0 == 0)
            {
                gunA = ItemStack.a(nbttagcompound1);
            }
        }

        NBTTagList nbttaglist1 = nbttagcompound.getList("GunB");

        if (nbttaglist1.size() > 0)
        {
            NBTTagCompound nbttagcompound2 = (NBTTagCompound)nbttaglist1.get(0);
            byte byte1 = nbttagcompound2.getByte("Slot");

            if (byte1 == 0)
            {
                gunB = ItemStack.a(nbttagcompound2);
            }
        }

        health = nbttagcompound.getInt("Health");
        deathTime = nbttagcompound.getInt("DeathTime");
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void b(NBTTagCompound nbttagcompound)
    {
        NBTTagList nbttaglist = new NBTTagList();

        if (gunA != null)
        {
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();
            nbttagcompound1.setByte("Slot", (byte)0);
            gunA.save(nbttagcompound1);
            nbttaglist.add(nbttagcompound1);
        }

        nbttagcompound.set("GunA", nbttaglist);
        NBTTagList nbttaglist1 = new NBTTagList();

        if (gunB != null)
        {
            NBTTagCompound nbttagcompound2 = new NBTTagCompound();
            nbttagcompound2.setByte("Slot", (byte)0);
            gunB.save(nbttagcompound2);
            nbttaglist1.add(nbttagcompound2);
        }

        nbttagcompound.set("GunB", nbttaglist1);
        nbttagcompound.setInt("Health", health);
        nbttagcompound.setInt("DeathTime", deathTime);
    }

    public void fireGuns()
    {
        if (gunA != null)
        {
            ((ItemGun)gunA.getItem()).onItemRightClickEntity(gunA, world, this, -1.8F, 0.0F, 0.5625F, 90F, 0.0F);
        }

        if (gunB != null)
        {
            ((ItemGun)gunB.getItem()).onItemRightClickEntity(gunB, world, this, -1.8F, 0.0F, -0.3125F, 90F, 0.0F);
        }
    }

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSize()
    {
        return 2;
    }

    /**
     * Returns the stack in slot i
     */
    public ItemStack getItem(int i)
    {
        if (i == 0)
        {
            return gunA;
        }

        if (i == 1)
        {
            return gunB;
        }
        else
        {
            return null;
        }
    }

    /**
     * When some containers are closed they call this on each slot, then drop whatever it returns as an EntityItem -
     * like when you close a workbench GUI.
     */
    public ItemStack splitWithoutUpdate(int i)
    {
        if (i == 0 && gunA != null)
        {
            ItemStack itemstack = gunA;
            gunA = null;
            return itemstack;
        }

        if (i == 1 && gunB != null)
        {
            ItemStack itemstack1 = gunB;
            gunB = null;
            return itemstack1;
        }
        else
        {
            return null;
        }
    }

    /**
     * Decrease the size of the stack in slot (first int arg) by the amount of the second int arg. Returns the new
     * stack.
     */
    public ItemStack splitStack(int i, int j)
    {
        ItemStack itemstack = null;

        if (i == 0 && gunA != null)
        {
            itemstack = gunA;
            gunA = null;
        }
        else if (i == 1 && gunB != null)
        {
            itemstack = gunB;
            gunB = null;
        }

        return itemstack;
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setItem(int i, ItemStack itemstack)
    {
        if (itemstack == null || (itemstack.getItem() instanceof ItemGun))
        {
            if (i == 0)
            {
                gunA = itemstack;
            }
            else if (i == 1)
            {
                gunB = itemstack;
            }
        }
    }

    /**
     * Returns the name of the inventory.
     */
    public String getName()
    {
        return "ATV";
    }

    /**
     * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended. *Isn't
     * this more of a set than a get?*
     */
    public int getMaxStackSize()
    {
        return 1;
    }

    /**
     * Called when an the contents of an Inventory change, usually
     */
    public void update()
    {
    }

    /**
     * Do not make give this method the name canInteractWith because it clashes with Container
     */
    public boolean a(EntityHuman entityhuman)
    {
        return entityhuman.e(locX, locY, locZ) <= 64D;
    }

    public void f()
    {
    }

    public void g()
    {
    }

    /**
     * When some containers are closed they call this on each slot, then drop whatever it returns as an EntityItem -
     * like when you close a workbench GUI.
     */
	@Override
	public ItemStack[] getContents() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InventoryHolder getOwner() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<HumanEntity> getViewers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onClose(CraftHumanEntity arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onOpen(CraftHumanEntity arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMaxStackSize(int arg0) {
		// TODO Auto-generated method stub
		
	}
}
