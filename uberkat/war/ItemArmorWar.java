package uberkat.war;

import forge.ITextureProvider;
import net.minecraft.server.EnumArmorMaterial;
import net.minecraft.server.ItemArmor;
import net.minecraft.server.World;
import net.minecraft.server.mod_ModernWarfare;
public class ItemArmorWar extends ItemArmor implements ITextureProvider
{
    public ItemArmorWar(int i, EnumArmorMaterial enumarmormaterial, int j, int k)
    {
        super(i, enumarmormaterial, j, k);
    }

    public String getTextureFile()
    {
        return "/war/items.png";
    }
}
