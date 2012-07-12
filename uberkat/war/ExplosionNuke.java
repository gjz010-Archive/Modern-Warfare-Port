package uberkat.war;

import java.util.*;
import net.minecraft.server.*;

public class ExplosionNuke
{
    public boolean isFlaming;
    private Random explosionRNG;
    private World worldObj;
    public double explosionX;
    public double explosionY;
    public double explosionZ;
    public Entity exploder;
    public float explosionSize;
    public Set destroyedBlockPositions;
    public float dropChance;

    public ExplosionNuke(World world, Entity entity, double d, double d1, double d2, float f, float f1, boolean flag)
    {
        isFlaming = flag;
        explosionRNG = new Random();
        destroyedBlockPositions = new HashSet();
        worldObj = world;
        exploder = entity;
        explosionSize = f;
        explosionX = d;
        explosionY = d1;
        explosionZ = d2;
        dropChance = f1;
    }

    public void doExplosionA()
    {
        float f = explosionSize;
        int i = 16;

        for (int j = 0; j < i; j++)
        {
            for (int l = 0; l < i; l++)
            {
                label0:

                for (int j1 = 0; j1 < i; j1++)
                {
                    if (j != 0 && j != i - 1 && l != 0 && l != i - 1 && j1 != 0 && j1 != i - 1)
                    {
                        continue;
                    }

                    double d = ((float)j / ((float)i - 1.0F)) * 2.0F - 1.0F;
                    double d1 = ((float)l / ((float)i - 1.0F)) * 2.0F - 1.0F;
                    double d2 = ((float)j1 / ((float)i - 1.0F)) * 2.0F - 1.0F;
                    double d3 = Math.sqrt(d * d + d1 * d1 + d2 * d2);
                    d /= d3;
                    d1 /= d3;
                    d2 /= d3;
                    float f1 = explosionSize * (0.7F + worldObj.random.nextFloat() * 0.6F);
                    double d5 = explosionX;
                    double d7 = explosionY;
                    double d9 = explosionZ;
                    float f2 = 0.3F;

                    do
                    {
                        if (f1 <= 0.0F)
                        {
                            continue label0;
                        }

                        int j4 = MathHelper.floor(d5);
                        int k4 = MathHelper.floor(d7);
                        int l4 = MathHelper.floor(d9);
                        int i5 = worldObj.getTypeId(j4, k4, l4);

                        if (i5 > 0)
                        {
                            f1 -= (Block.byId[i5].a(exploder) + 0.3F) * f2;
                        }

                        if (f1 > 0.0F)
                        {
                            destroyedBlockPositions.add(new ChunkPosition(j4, k4, l4));
                        }

                        d5 += d * (double)f2;
                        d7 += d1 * (double)f2;
                        d9 += d2 * (double)f2;
                        f1 -= f2 * 0.75F;
                    }
                    while (true);
                }
            }
        }

        explosionSize *= 2.0F;
        int k = MathHelper.floor(explosionX - (double)explosionSize - 1.0D);
        int i1 = MathHelper.floor(explosionX + (double)explosionSize + 1.0D);
        int k1 = MathHelper.floor(explosionY - (double)explosionSize - 1.0D);
        int l1 = MathHelper.floor(explosionY + (double)explosionSize + 1.0D);
        int i2 = MathHelper.floor(explosionZ - (double)explosionSize - 1.0D);
        int j2 = MathHelper.floor(explosionZ + (double)explosionSize + 1.0D);
        List list = worldObj.getEntities(exploder, AxisAlignedBB.b(k, k1, i2, i1, l1, j2));
        Vec3D vec3d = Vec3D.create(explosionX, explosionY, explosionZ);

        for (int k2 = 0; k2 < list.size(); k2++)
        {
            Entity entity = (Entity)list.get(k2);
            double d4 = entity.f(explosionX, explosionY, explosionZ) / (double)explosionSize;

            if (d4 <= 1.0D)
            {
                double d6 = entity.locX - explosionX;
                double d8 = entity.locY - explosionY;
                double d10 = entity.locZ - explosionZ;
                double d11 = MathHelper.sqrt(d6 * d6 + d8 * d8 + d10 * d10);
                d6 /= d11;
                d8 /= d11;
                d10 /= d11;
                double d12 = worldObj.a(vec3d, entity.boundingBox);
                double d13 = (1.0D - d4) * d12;
                entity.damageEntity(DamageSource.EXPLOSION, (int)(((d13 * d13 + d13) / 2D) * 8D * (double)explosionSize + 1.0D));
                double d14 = d13;
                entity.motX += d6 * d14;
                entity.motY += d8 * d14;
                entity.motZ += d10 * d14;
            }
        }

        explosionSize = f;
        ArrayList arraylist = new ArrayList();
        arraylist.addAll(destroyedBlockPositions);

        if (isFlaming)
        {
            for (int l2 = arraylist.size() - 1; l2 >= 0; l2--)
            {
                ChunkPosition chunkposition = (ChunkPosition)arraylist.get(l2);
                int i3 = chunkposition.x;
                int j3 = chunkposition.y;
                int k3 = chunkposition.z;
                int l3 = worldObj.getTypeId(i3, j3, k3);
                int i4 = worldObj.getTypeId(i3, j3 - 1, k3);

                if (l3 == 0 && Block.n[i4])
                {
                    worldObj.setTypeId(i3, j3, k3, Block.FIRE.id);
                }
            }
        }
    }

    public void doExplosionB()
    {
        worldObj.makeSound(explosionX, explosionY, explosionZ, "random.explode", 4F, (1.0F + (worldObj.random.nextFloat() - worldObj.random.nextFloat()) * 0.2F) * 0.7F);
        ArrayList arraylist = new ArrayList();
        arraylist.addAll(destroyedBlockPositions);

        for (int i = arraylist.size() - 1; i >= 0; i--)
        {
            ChunkPosition chunkposition = (ChunkPosition)arraylist.get(i);
            int j = chunkposition.x;
            int k = chunkposition.y;
            int l = chunkposition.z;
            int i1 = worldObj.getTypeId(j, k, l);

            for (int j1 = 0; j1 < 1; j1++)
            {
                double d = (float)j + worldObj.random.nextFloat();
                double d1 = (float)k + worldObj.random.nextFloat();
                double d2 = (float)l + worldObj.random.nextFloat();
                double d3 = d - explosionX;
                double d4 = d1 - explosionY;
                double d5 = d2 - explosionZ;
                double d6 = MathHelper.sqrt(d3 * d3 + d4 * d4 + d5 * d5);
                d3 /= d6;
                d4 /= d6;
                d5 /= d6;
                double d7 = 0.5D / (d6 / (double)explosionSize + 0.10000000000000001D);
                d7 *= worldObj.random.nextFloat() * worldObj.random.nextFloat() + 0.3F;
                d3 *= d7;
                d4 *= d7;
                d5 *= d7;
                worldObj.a("explode", (d + explosionX * 1.0D) / 2D, (d1 + explosionY * 1.0D) / 2D, (d2 + explosionZ * 1.0D) / 2D, d3, d4, d5);
                worldObj.a("smoke", d, d1, d2, d3, d4, d5);
            }

            if (i1 > 0)
            {
                Block.byId[i1].dropNaturally(worldObj, j, k, l, worldObj.getData(j, k, l), dropChance, 0);
                worldObj.setTypeId(j, k, l, 0);
                Block.byId[i1].wasExploded(worldObj, j, k, l);
            }
        }
    }
}
