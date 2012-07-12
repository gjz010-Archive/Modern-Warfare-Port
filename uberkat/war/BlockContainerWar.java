package uberkat.war;

import forge.ITextureProvider;
import net.minecraft.server.*;

public abstract class BlockContainerWar extends BlockContainer implements ITextureProvider
{
    protected BlockContainerWar(int i, Material material)
    {
        super(i, material);
    }

    protected BlockContainerWar(int i, int j, Material material)
    {
        super(i, j, material);
    }

    public String getTextureFile()
    {
        return "/war/terrain.png";
    }
    
}
