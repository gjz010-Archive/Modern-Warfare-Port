package uberkat.war;

import net.minecraft.server.EnumArmorMaterial;

public class ItemParachute extends ItemArmorWar
{
    public ItemParachute(int i, int j)
    {
        super(i, EnumArmorMaterial.CLOTH, j, 1);
        setMaxDurability(7);
    }
}
