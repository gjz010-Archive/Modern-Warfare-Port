package uberkat.war;

import forge.NetworkMod;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ModLoader;
import net.minecraft.server.WorldServer;

public abstract class WarBaseModMp extends NetworkMod
{
    private long gameClock;

    public WarBaseModMp()
    {
        gameClock = 0L;
    }

    public void load()
    {
        net.minecraft.server.ModLoader.setInGameHook(this, true, false);
    }

    public boolean onTickInGame(float f, MinecraftServer minecraftserver)
    {
        if (minecraftserver.getWorldServer(0) != null && gameClock != minecraftserver.getWorldServer(0).getTime())
        {
            gameClock = minecraftserver.getWorldServer(0).getTime();
            onTickInGameTick(minecraftserver);
        }

        return true;
    }

    public void onTickInGameTick(MinecraftServer minecraftserver)
    {
    }

    public abstract void onMainMenu();
}
