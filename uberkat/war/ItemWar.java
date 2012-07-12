package uberkat.war;

import forge.ITextureProvider;
import net.minecraft.server.Item;

public class ItemWar extends Item implements ITextureProvider
{
    public ItemWar(int i)
    {
        super(i);
    }

    public String getTextureFile()
    {
        return "/war/items.png";
    }
}
