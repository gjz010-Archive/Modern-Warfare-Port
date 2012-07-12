package uberkat.war;

import forge.ITextureProvider;
import net.minecraft.server.Block;
import net.minecraft.server.Material;

public class BlockWar extends Block implements ITextureProvider
{
    public BlockWar(int i, int j, Material material)
    {
        super(i, j, material);
    }

    public String getTextureFile()
    {
        return "/war/terrain.png";
    }
}
