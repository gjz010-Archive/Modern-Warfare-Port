package net.minecraft.server;

import forge.*;
import java.io.*;
import java.lang.reflect.Field;
import java.net.*;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.*;
import java.util.logging.Logger;
import net.minecraft.server.MinecraftServer;
import uberkat.war.*;

public class mod_ModernWarfare extends WarBaseModMp implements IGuiHandler, IConnectionHandler, IPacketHandler
{
    public static String modName = "Modern Warfare";
    public static String versionNumber = "1.0.3";
    public static Logger logger = Logger.getLogger("Minecraft");
    public static mod_ModernWarfare instance;
    public static Item itemLightometer;
    public static Item itemNightvisionGoggles;
    public static Item itemScubaTank;
    public static Item itemParachute;
    public static Item itemTelescope;
    public static Item itemGhillieHelmet;
    public static Item itemGhillieChest;
    public static Item itemGhilliePants;
    public static Item itemGhillieBoots;
    public static Item itemBulletCasing;
    public static Item itemBulletLight;
    public static Item itemBulletMedium;
    public static Item itemBulletShell;
    public static Item itemGunAk47;
    public static Item itemGunMp5;
    public static Item itemGunShotgun;
    public static Item itemGunDeagle;
    public static Item itemGrenade;
    public static Item itemBulletRocket;
    public static Item itemGunRocketLauncher;
    public static Item itemBulletRocketLaser;
    public static Item itemGunRocketLauncherLaser;
    public static Item itemBulletHeavy;
    public static Item itemGunSniper;
    public static Item itemGrenadeStun;
    public static Item itemGrenadeSmoke;
    public static Item itemBulletCasingShell;
    public static Item itemOil;
    public static Item itemGunFlamethrower;
    public static Item itemGrenadeSticky;
    public static Item itemGunSg552;
    public static Item itemGunMinigun;
    public static Item itemGunLaser;
    public static Item itemGunM4;
    public static Item itemGrenadeIncendiary;
    public static Item itemGrenadeIncendiaryLit;
    public static Item itemScope;
    public static Item itemWrench;
    public static Item itemSentry;
    public static Item itemOilDrop;
    public static Item itemJetPack;
    public static Item itemRope;
    public static Item itemGrapplingHook;
    public static Item itemCraftingPack;
    public static Item itemSmallBarrel;
    public static Item itemMediumBarrel;
    public static Item itemLongBarrel;
    public static Item itemFatBarrel;
    public static Item itemMetalGrip;
    public static Item itemWoodGrip;
    public static Item itemFuelTank;
    public static Item itemAtv;
    public static Item itemAtvBody;
    public static Item itemAtvWheel;
    public static Block blockRope;
    public static Block blockGrapplingHook;
    public static Block blockNuke;
    public static Block blockOil;
    public static boolean guns = true;
    public static boolean bulletsDestroyGlass = true;
    public static boolean showAmmoBar = true;
    public static boolean muzzleFlash = false;
    public static boolean grenades = true;
    public static boolean explosionsDestroyBlocks = true;
    public static boolean laserSetsFireToBlocks = true;
    public static boolean nuke = true;
    public static boolean oil = true;
    public static boolean lighter = true;
    public static boolean sentries = true;
    public static boolean sentriesKillAnimals = false;
    public static boolean atv = true;
    public static boolean jetPack = true;
    public static boolean ammoRestrictions = true;
    public static boolean ammoCasings = true;
    public static int nukeID = 130;
    public static int oilID = 131;
    public static int ropeID = 132;
    public static int grapplingHookID = 133;
    public static int blockOilLineIndexInTexture = 3;
    public static final String terrainTexturePath = "/war/terrain.png";
    public static final String itemsTexturePath = "/war/items.png";
    public static Map flashTimes = new HashMap();
    public static Map grapplingHooks = new HashMap();
    public static Map burstShots = new HashMap();
    public static Map reloadTimes = new HashMap();
    public static Map isSniperZoomedIn = new HashMap();
    public static Class sentryEntityClasses[];
    public static String sentryNames[] =
    {
        "AK47", "MP5", "Shotgun", "Desert Eagle", "Rocket Launcher", "Laser-Guided Rocket Launcher", "Sniper Rifle", "Flamethrower", "SG552", "Minigun",
        "Laser", "M4"
    };
    private Map jetPackOn;
    private static final double JET_PACK_LIFT = 0.059999999999999998D;
    private static final double JET_PACK_MAX_LIFT = 0.29999999999999999D;
    public static float nukeBlastDiameter = 8F;
    public static int nukeFuse = 80;
    public static int monsterSpawns = 70;
    public static final double RECOIL_FIX_FACTOR = 0.10000000000000001D;
    public static double currentRecoilV = 0.0D;
    public static double currentRecoilH = 0.0D;

    public mod_ModernWarfare()
    {
        setProperties();
        logger.info((new StringBuilder()).append("[").append(modName).append("] Properties loaded.").toString());
        addItems();
        logger.info((new StringBuilder()).append("[").append(modName).append("] Items loaded.").toString());
        addBlocks();
        logger.info((new StringBuilder()).append("[").append(modName).append("] Blocks loaded.").toString());
        addEntities();
        logger.info((new StringBuilder()).append("[").append(modName).append("] Entities loaded.").toString());
        addTileEntities();
        logger.info((new StringBuilder()).append("[").append(modName).append("] Tile entities loaded.").toString());
        addBulletRestrictions();
        logger.info((new StringBuilder()).append("[").append(modName).append("] Bullet restrictions loaded.").toString());
        addItemDamage();
        logger.info((new StringBuilder()).append("[").append(modName).append("] Item damage loaded.").toString());
        addBurnRates();
        logger.info((new StringBuilder()).append("[").append(modName).append("] Burn rates loaded.").toString());
        addRecipes();
        logger.info((new StringBuilder()).append("[").append(modName).append("] Recipes loaded.").toString());
    }

    private static void setProperties()
    {
        Properties properties = new Properties();
        File file = new File((new StringBuilder()).append(getMinecraftDir()).append("/mods").toString());
        File file1 = new File((new StringBuilder()).append(getMinecraftDir()).append("/mods/ModernWarfare").toString());
        File file2 = new File((new StringBuilder()).append(getMinecraftDir()).append("/mods/ModernWarfare/war.properties").toString());

        try
        {
            if (file.exists())
            {
                if (file1.exists())
                {
                    if (file2.exists())
                    {
                        properties.load(new FileInputStream(file2));
                        String s = properties.getProperty("guns");
                        guns = s.trim().equalsIgnoreCase("true");
                        s = properties.getProperty("bulletsDestroyGlass");
                        bulletsDestroyGlass = s.trim().equalsIgnoreCase("true");
                        s = properties.getProperty("showAmmoBar");
                        showAmmoBar = s.trim().equalsIgnoreCase("true");
                        s = properties.getProperty("muzzleFlash");
                        muzzleFlash = s.trim().equalsIgnoreCase("true");
                        s = properties.getProperty("grenades");
                        grenades = s.trim().equalsIgnoreCase("true");
                        s = properties.getProperty("explosionsDestroyBlocks");
                        explosionsDestroyBlocks = s.trim().equalsIgnoreCase("true");
                        s = properties.getProperty("laserSetsFireToBlocks");
                        laserSetsFireToBlocks = s.trim().equalsIgnoreCase("true");
                        s = properties.getProperty("nuke");
                        nuke = s.trim().equalsIgnoreCase("true");
                        s = properties.getProperty("oil");
                        oil = s.trim().equalsIgnoreCase("true");
                        s = properties.getProperty("lighter");
                        lighter = s.trim().equalsIgnoreCase("true");
                        s = properties.getProperty("sentries");
                        sentries = s.trim().equalsIgnoreCase("true");
                        s = properties.getProperty("sentriesKillAnimals");
                        sentriesKillAnimals = s.trim().equalsIgnoreCase("true");
                        s = properties.getProperty("atv");
                        atv = s.trim().equalsIgnoreCase("true");
                        s = properties.getProperty("jetPack");
                        jetPack = s.trim().equalsIgnoreCase("true");
                        s = properties.getProperty("ammoRestrictions");
                        ammoRestrictions = s.trim().equalsIgnoreCase("true");
                        s = properties.getProperty("ammoCasings");
                        ammoCasings = s.trim().equalsIgnoreCase("true");
                        s = properties.getProperty("nukeBlastDiameter");
                        nukeBlastDiameter = Float.parseFloat(s.trim());
                        s = properties.getProperty("nukeID");
                        nukeID = Integer.parseInt(s.trim());
                        s = properties.getProperty("oilID");
                        oilID = Integer.parseInt(s.trim());
                        s = properties.getProperty("nukeFuse");
                        nukeFuse = Integer.parseInt(s.trim());
                        s = properties.getProperty("ropeID");
                        ropeID = Integer.parseInt(s.trim());
                        s = properties.getProperty("grapplingHookID");
                        grapplingHookID = Integer.parseInt(s.trim());
                        s = properties.getProperty("monsterSpawns");
                        monsterSpawns = Integer.parseInt(s.trim());
                    }
                    else
                    {
                        file2.createNewFile();
                        logger.info((new StringBuilder()).append("[").append(modName).append("] Created 'war.properties' configuration file.").toString());
                        FileOutputStream fileoutputstream = new FileOutputStream(file2);
                        properties.setProperty("guns", Boolean.toString(true));
                        properties.setProperty("bulletsDestroyGlass", Boolean.toString(true));
                        properties.setProperty("showAmmoBar", Boolean.toString(true));
                        properties.setProperty("muzzleFlash", Boolean.toString(false));
                        properties.setProperty("grenades", Boolean.toString(true));
                        properties.setProperty("explosionsDestroyBlocks", Boolean.toString(true));
                        properties.setProperty("laserSetsFireToBlocks", Boolean.toString(true));
                        properties.setProperty("nuke", Boolean.toString(true));
                        properties.setProperty("oil", Boolean.toString(true));
                        properties.setProperty("lighter", Boolean.toString(true));
                        properties.setProperty("sentries", Boolean.toString(true));
                        properties.setProperty("sentriesKillAnimals", Boolean.toString(false));
                        properties.setProperty("atv", Boolean.toString(true));
                        properties.setProperty("jetPack", Boolean.toString(true));
                        properties.setProperty("ammoRestrictions", Boolean.toString(true));
                        properties.setProperty("ammoCasings", Boolean.toString(true));
                        properties.setProperty("nukeBlastDiameter", Float.toString(8F));
                        properties.setProperty("nukeID", Integer.toString(130));
                        properties.setProperty("oilID", Integer.toString(131));
                        properties.setProperty("nukeFuse", Integer.toString(80));
                        properties.setProperty("ropeID", Integer.toString(132));
                        properties.setProperty("grapplingHookID", Integer.toString(133));
                        properties.setProperty("monsterSpawns", Integer.toString(70));
                        properties.store(fileoutputstream, "Modern Warfare Official Properties");
                    }
                }
                else
                {
                    file1.mkdir();
                    logger.info((new StringBuilder()).append("[").append(modName).append("] Created 'ModernWarfare' directory.").toString());

                    if (file2.exists())
                    {
                        properties.load(new FileInputStream(file2));
                        String s1 = properties.getProperty("guns");
                        guns = s1.trim().equalsIgnoreCase("true");
                        s1 = properties.getProperty("bulletsDestroyGlass");
                        bulletsDestroyGlass = s1.trim().equalsIgnoreCase("true");
                        s1 = properties.getProperty("showAmmoBar");
                        showAmmoBar = s1.trim().equalsIgnoreCase("true");
                        s1 = properties.getProperty("muzzleFlash");
                        muzzleFlash = s1.trim().equalsIgnoreCase("true");
                        s1 = properties.getProperty("grenades");
                        grenades = s1.trim().equalsIgnoreCase("true");
                        s1 = properties.getProperty("explosionsDestroyBlocks");
                        explosionsDestroyBlocks = s1.trim().equalsIgnoreCase("true");
                        s1 = properties.getProperty("laserSetsFireToBlocks");
                        laserSetsFireToBlocks = s1.trim().equalsIgnoreCase("true");
                        s1 = properties.getProperty("nuke");
                        nuke = s1.trim().equalsIgnoreCase("true");
                        s1 = properties.getProperty("oil");
                        oil = s1.trim().equalsIgnoreCase("true");
                        s1 = properties.getProperty("lighter");
                        lighter = s1.trim().equalsIgnoreCase("true");
                        s1 = properties.getProperty("sentries");
                        sentries = s1.trim().equalsIgnoreCase("true");
                        s1 = properties.getProperty("sentriesKillAnimals");
                        sentriesKillAnimals = s1.trim().equalsIgnoreCase("true");
                        s1 = properties.getProperty("atv");
                        atv = s1.trim().equalsIgnoreCase("true");
                        s1 = properties.getProperty("jetPack");
                        jetPack = s1.trim().equalsIgnoreCase("true");
                        s1 = properties.getProperty("ammoRestrictions");
                        ammoRestrictions = s1.trim().equalsIgnoreCase("true");
                        s1 = properties.getProperty("ammoCasings");
                        ammoCasings = s1.trim().equalsIgnoreCase("true");
                        s1 = properties.getProperty("nukeBlastDiameter");
                        nukeBlastDiameter = Float.parseFloat(s1.trim());
                        s1 = properties.getProperty("nukeID");
                        nukeID = Integer.parseInt(s1.trim());
                        s1 = properties.getProperty("oilID");
                        oilID = Integer.parseInt(s1.trim());
                        s1 = properties.getProperty("nukeFuse");
                        nukeFuse = Integer.parseInt(s1.trim());
                        s1 = properties.getProperty("ropeID");
                        ropeID = Integer.parseInt(s1.trim());
                        s1 = properties.getProperty("grapplingHookID");
                        grapplingHookID = Integer.parseInt(s1.trim());
                        s1 = properties.getProperty("monsterSpawns");
                        monsterSpawns = Integer.parseInt(s1.trim());
                    }
                    else
                    {
                        file2.createNewFile();
                        logger.info((new StringBuilder()).append("[").append(modName).append("] Created 'war.properties' configuration file.").toString());
                        FileOutputStream fileoutputstream1 = new FileOutputStream(file2);
                        properties.setProperty("guns", Boolean.toString(true));
                        properties.setProperty("bulletsDestroyGlass", Boolean.toString(true));
                        properties.setProperty("showAmmoBar", Boolean.toString(true));
                        properties.setProperty("muzzleFlash", Boolean.toString(false));
                        properties.setProperty("grenades", Boolean.toString(true));
                        properties.setProperty("explosionsDestroyBlocks", Boolean.toString(true));
                        properties.setProperty("laserSetsFireToBlocks", Boolean.toString(true));
                        properties.setProperty("nuke", Boolean.toString(true));
                        properties.setProperty("oil", Boolean.toString(true));
                        properties.setProperty("lighter", Boolean.toString(true));
                        properties.setProperty("sentries", Boolean.toString(true));
                        properties.setProperty("sentriesKillAnimals", Boolean.toString(false));
                        properties.setProperty("atv", Boolean.toString(true));
                        properties.setProperty("jetPack", Boolean.toString(true));
                        properties.setProperty("ammoRestrictions", Boolean.toString(true));
                        properties.setProperty("ammoCasings", Boolean.toString(true));
                        properties.setProperty("nukeBlastDiameter", Float.toString(8F));
                        properties.setProperty("nukeID", Integer.toString(130));
                        properties.setProperty("oilID", Integer.toString(131));
                        properties.setProperty("nukeFuse", Integer.toString(80));
                        properties.setProperty("ropeID", Integer.toString(132));
                        properties.setProperty("grapplingHookID", Integer.toString(133));
                        properties.setProperty("monsterSpawns", Integer.toString(70));
                        properties.store(fileoutputstream1, "Modern Warfare Official Properties");
                    }
                }
            }
            else
            {
                file.mkdir();
                logger.info((new StringBuilder()).append("[").append(modName).append("] Created 'mods' directory.").toString());

                if (file1.exists())
                {
                    if (file2.exists())
                    {
                        properties.load(new FileInputStream(file2));
                        String s2 = properties.getProperty("guns");
                        guns = s2.trim().equalsIgnoreCase("true");
                        s2 = properties.getProperty("bulletsDestroyGlass");
                        bulletsDestroyGlass = s2.trim().equalsIgnoreCase("true");
                        s2 = properties.getProperty("showAmmoBar");
                        showAmmoBar = s2.trim().equalsIgnoreCase("true");
                        s2 = properties.getProperty("muzzleFlash");
                        muzzleFlash = s2.trim().equalsIgnoreCase("true");
                        s2 = properties.getProperty("grenades");
                        grenades = s2.trim().equalsIgnoreCase("true");
                        s2 = properties.getProperty("explosionsDestroyBlocks");
                        explosionsDestroyBlocks = s2.trim().equalsIgnoreCase("true");
                        s2 = properties.getProperty("laserSetsFireToBlocks");
                        laserSetsFireToBlocks = s2.trim().equalsIgnoreCase("true");
                        s2 = properties.getProperty("nuke");
                        nuke = s2.trim().equalsIgnoreCase("true");
                        s2 = properties.getProperty("oil");
                        oil = s2.trim().equalsIgnoreCase("true");
                        s2 = properties.getProperty("lighter");
                        lighter = s2.trim().equalsIgnoreCase("true");
                        s2 = properties.getProperty("sentries");
                        sentries = s2.trim().equalsIgnoreCase("true");
                        s2 = properties.getProperty("sentriesKillAnimals");
                        sentriesKillAnimals = s2.trim().equalsIgnoreCase("true");
                        s2 = properties.getProperty("atv");
                        atv = s2.trim().equalsIgnoreCase("true");
                        s2 = properties.getProperty("jetPack");
                        jetPack = s2.trim().equalsIgnoreCase("true");
                        s2 = properties.getProperty("ammoRestrictions");
                        ammoRestrictions = s2.trim().equalsIgnoreCase("true");
                        s2 = properties.getProperty("ammoCasings");
                        ammoCasings = s2.trim().equalsIgnoreCase("true");
                        s2 = properties.getProperty("nukeBlastDiameter");
                        nukeBlastDiameter = Float.parseFloat(s2.trim());
                        s2 = properties.getProperty("nukeID");
                        nukeID = Integer.parseInt(s2.trim());
                        s2 = properties.getProperty("oilID");
                        oilID = Integer.parseInt(s2.trim());
                        s2 = properties.getProperty("nukeFuse");
                        nukeFuse = Integer.parseInt(s2.trim());
                        s2 = properties.getProperty("ropeID");
                        ropeID = Integer.parseInt(s2.trim());
                        s2 = properties.getProperty("grapplingHookID");
                        grapplingHookID = Integer.parseInt(s2.trim());
                        s2 = properties.getProperty("monsterSpawns");
                        monsterSpawns = Integer.parseInt(s2.trim());
                    }
                    else
                    {
                        file2.createNewFile();
                        logger.info((new StringBuilder()).append("[").append(modName).append("] Created 'war.properties' configuration file.").toString());
                        FileOutputStream fileoutputstream2 = new FileOutputStream(file2);
                        properties.setProperty("guns", Boolean.toString(true));
                        properties.setProperty("bulletsDestroyGlass", Boolean.toString(true));
                        properties.setProperty("showAmmoBar", Boolean.toString(true));
                        properties.setProperty("muzzleFlash", Boolean.toString(false));
                        properties.setProperty("grenades", Boolean.toString(true));
                        properties.setProperty("explosionsDestroyBlocks", Boolean.toString(true));
                        properties.setProperty("laserSetsFireToBlocks", Boolean.toString(true));
                        properties.setProperty("nuke", Boolean.toString(true));
                        properties.setProperty("oil", Boolean.toString(true));
                        properties.setProperty("lighter", Boolean.toString(true));
                        properties.setProperty("sentries", Boolean.toString(true));
                        properties.setProperty("sentriesKillAnimals", Boolean.toString(false));
                        properties.setProperty("atv", Boolean.toString(true));
                        properties.setProperty("jetPack", Boolean.toString(true));
                        properties.setProperty("ammoRestrictions", Boolean.toString(true));
                        properties.setProperty("ammoCasings", Boolean.toString(true));
                        properties.setProperty("nukeBlastDiameter", Float.toString(8F));
                        properties.setProperty("nukeID", Integer.toString(130));
                        properties.setProperty("oilID", Integer.toString(131));
                        properties.setProperty("nukeFuse", Integer.toString(80));
                        properties.setProperty("ropeID", Integer.toString(132));
                        properties.setProperty("grapplingHookID", Integer.toString(133));
                        properties.setProperty("monsterSpawns", Integer.toString(70));
                        properties.store(fileoutputstream2, "Modern Warfare Official Properties");
                    }
                }
                else
                {
                    file1.mkdir();
                    logger.info((new StringBuilder()).append("[").append(modName).append("] Created 'ModernWarfare' directory.").toString());

                    if (file2.exists())
                    {
                        properties.load(new FileInputStream(file2));
                        String s3 = properties.getProperty("guns");
                        guns = s3.trim().equalsIgnoreCase("true");
                        s3 = properties.getProperty("bulletsDestroyGlass");
                        bulletsDestroyGlass = s3.trim().equalsIgnoreCase("true");
                        s3 = properties.getProperty("showAmmoBar");
                        showAmmoBar = s3.trim().equalsIgnoreCase("true");
                        s3 = properties.getProperty("muzzleFlash");
                        muzzleFlash = s3.trim().equalsIgnoreCase("true");
                        s3 = properties.getProperty("grenades");
                        grenades = s3.trim().equalsIgnoreCase("true");
                        s3 = properties.getProperty("explosionsDestroyBlocks");
                        explosionsDestroyBlocks = s3.trim().equalsIgnoreCase("true");
                        s3 = properties.getProperty("laserSetsFireToBlocks");
                        laserSetsFireToBlocks = s3.trim().equalsIgnoreCase("true");
                        s3 = properties.getProperty("nuke");
                        nuke = s3.trim().equalsIgnoreCase("true");
                        s3 = properties.getProperty("oil");
                        oil = s3.trim().equalsIgnoreCase("true");
                        s3 = properties.getProperty("lighter");
                        lighter = s3.trim().equalsIgnoreCase("true");
                        s3 = properties.getProperty("sentries");
                        sentries = s3.trim().equalsIgnoreCase("true");
                        s3 = properties.getProperty("sentriesKillAnimals");
                        sentriesKillAnimals = s3.trim().equalsIgnoreCase("true");
                        s3 = properties.getProperty("atv");
                        atv = s3.trim().equalsIgnoreCase("true");
                        s3 = properties.getProperty("jetPack");
                        jetPack = s3.trim().equalsIgnoreCase("true");
                        s3 = properties.getProperty("ammoRestrictions");
                        ammoRestrictions = s3.trim().equalsIgnoreCase("true");
                        s3 = properties.getProperty("ammoCasings");
                        ammoCasings = s3.trim().equalsIgnoreCase("true");
                        s3 = properties.getProperty("nukeBlastDiameter");
                        nukeBlastDiameter = Float.parseFloat(s3.trim());
                        s3 = properties.getProperty("nukeID");
                        nukeID = Integer.parseInt(s3.trim());
                        s3 = properties.getProperty("oilID");
                        oilID = Integer.parseInt(s3.trim());
                        s3 = properties.getProperty("nukeFuse");
                        nukeFuse = Integer.parseInt(s3.trim());
                        s3 = properties.getProperty("ropeID");
                        ropeID = Integer.parseInt(s3.trim());
                        s3 = properties.getProperty("grapplingHookID");
                        grapplingHookID = Integer.parseInt(s3.trim());
                        s3 = properties.getProperty("monsterSpawns");
                        monsterSpawns = Integer.parseInt(s3.trim());
                    }
                    else
                    {
                        file2.createNewFile();
                        logger.info((new StringBuilder()).append("[").append(modName).append("] Created 'war.properties' configuration file.").toString());
                        FileOutputStream fileoutputstream3 = new FileOutputStream(file2);
                        properties.setProperty("guns", Boolean.toString(true));
                        properties.setProperty("bulletsDestroyGlass", Boolean.toString(true));
                        properties.setProperty("showAmmoBar", Boolean.toString(true));
                        properties.setProperty("muzzleFlash", Boolean.toString(false));
                        properties.setProperty("grenades", Boolean.toString(true));
                        properties.setProperty("explosionsDestroyBlocks", Boolean.toString(true));
                        properties.setProperty("laserSetsFireToBlocks", Boolean.toString(true));
                        properties.setProperty("nuke", Boolean.toString(true));
                        properties.setProperty("oil", Boolean.toString(true));
                        properties.setProperty("lighter", Boolean.toString(true));
                        properties.setProperty("sentries", Boolean.toString(true));
                        properties.setProperty("sentriesKillAnimals", Boolean.toString(false));
                        properties.setProperty("atv", Boolean.toString(true));
                        properties.setProperty("jetPack", Boolean.toString(true));
                        properties.setProperty("ammoRestrictions", Boolean.toString(true));
                        properties.setProperty("ammoCasings", Boolean.toString(true));
                        properties.setProperty("nukeBlastDiameter", Float.toString(8F));
                        properties.setProperty("nukeID", Integer.toString(130));
                        properties.setProperty("oilID", Integer.toString(131));
                        properties.setProperty("nukeFuse", Integer.toString(80));
                        properties.setProperty("ropeID", Integer.toString(132));
                        properties.setProperty("grapplingHookID", Integer.toString(133));
                        properties.setProperty("monsterSpawns", Integer.toString(70));
                        properties.store(fileoutputstream3, "Modern Warfare Official Properties");
                    }
                }
            }
        }
        catch (IOException ioexception)
        {
            System.err.println((new StringBuilder()).append("[").append(modName).append("] An error occured while reading from configuration file.").toString());
            ioexception.printStackTrace();
        }
    }

    public void load()
    {
        MinecraftForge.setGuiHandler(this, this);
        MinecraftForge.registerConnectionHandler(this);
        jetPackOn = new HashMap();
        super.load();
    }

    public static void addBurnRates()
    {
        WarTools.setBurnRate(blockOil.id, 1000, 1000);
    }

    public String getVersion()
    {
        return versionNumber;
    }

    public void onTickInGameTick1(MinecraftServer minecraftserver)
    {
        Iterator iterator = (new ArrayList(minecraftserver.serverConfigurationManager.players)).iterator();

        do
        {
            if (!iterator.hasNext())
            {
                break;
            }

            EntityPlayer entityplayer = (EntityPlayer)iterator.next();
            ItemStack itemstack = entityplayer.inventory.armor[2];

            if (itemstack != null && itemstack.id == itemScubaTank.id)
            {
                entityplayer.setAirTicks(300);
            }
        }
        while (true);
    }

    public void onTickInGameTick(MinecraftServer minecraftserver)
    {
        onTickInGameTick1(minecraftserver);
        handleReload();
        handleFlash();

        for (int i = 0; i < minecraftserver.getWorldServer(0).players.size(); i++)
        {
            handleJetPack(minecraftserver.getWorldServer(0), (EntityPlayer)minecraftserver.getWorldServer(0).players.get(i));
        }

        for (int j = 0; j < minecraftserver.getWorldServer(-1).players.size(); j++)
        {
            handleJetPack(minecraftserver.getWorldServer(-1), (EntityPlayer)minecraftserver.getWorldServer(-1).players.get(j));
        }

        for (int k = 0; k < minecraftserver.getWorldServer(1).players.size(); k++)
        {
            handleJetPack(minecraftserver.getWorldServer(1), (EntityPlayer)minecraftserver.getWorldServer(1).players.get(k));
        }
    }

    public boolean handleJetPack(World world, EntityPlayer entityplayer)
    {
        ItemStack itemstack = entityplayer.inventory.armor[2];

        if (itemstack != null && itemstack.id == itemJetPack.id && !WarTools.onGroundOrInWater(world, entityplayer) && entityplayer.vehicle == null && jetPackOn.get(entityplayer) != null && ((Boolean)jetPackOn.get(entityplayer)).booleanValue() && useJetPackFuel(entityplayer))
        {
            entityplayer.motY = Math.min(entityplayer.motY + 0.059999999999999998D + 0.059999999999999998D, 0.29999999999999999D);
            entityplayer.fallDistance = 0.0F;
            return true;
        }
        else
        {
            return false;
        }
    }

    private boolean useJetPackFuel(EntityPlayer entityplayer)
    {
        return WarTools.useItemInInventory(entityplayer, itemOil.id) > 0;
    }

    public void onMainMenu()
    {
        reset();
        instance = this;
    }

    private void reset()
    {
        setHardcore();
    }

    public void addTileEntities()
    {
        ModLoader.registerTileEntity(uberkat.war.TileEntityRope.class, "Rope");
    }

    public void addItemDamage()
    {
        itemBulletLight.setMaxDurability(0);
        itemBulletMedium.setMaxDurability(0);
        itemBulletShell.setMaxDurability(0);
        itemBulletRocket.setMaxDurability(0);
        itemBulletRocketLaser.setMaxDurability(0);
        itemBulletHeavy.setMaxDurability(0);
        Item.REDSTONE.setMaxDurability(0);
    }

    public void addEntities()
    {
        ModLoader.registerEntityID(uberkat.war.EntitySentryAk47.class, "SentryAk47", 136);
        ModLoader.registerEntityID(uberkat.war.EntitySentryDeagle.class, "SentryDeagle", 137);
        ModLoader.registerEntityID(uberkat.war.EntitySentryMp5.class, "SentryMp5", 138);
        ModLoader.registerEntityID(uberkat.war.EntitySentryRocketLauncher.class, "SentryRocketLauncher", 139);
        ModLoader.registerEntityID(uberkat.war.EntitySentryRocketLauncherLaser.class, "SentryRocketLauncherLaser", 140);
        ModLoader.registerEntityID(uberkat.war.EntitySentryShotgun.class, "SentryShotgun", 141);
        ModLoader.registerEntityID(uberkat.war.EntitySentrySniper.class, "SentrySniper", 142);
        ModLoader.registerEntityID(uberkat.war.EntitySentryFlamethrower.class, "SentryFlamethrower", 143);
        ModLoader.registerEntityID(uberkat.war.EntitySentrySg552.class, "SentrySg552", 144);
        ModLoader.registerEntityID(uberkat.war.EntitySentryMinigun.class, "SentryMinigun", 145);
        ModLoader.registerEntityID(uberkat.war.EntitySentryLaser.class, "SentryLaser", 146);
        ModLoader.registerEntityID(uberkat.war.EntitySentryM4.class, "SentryM4", 147);
        ModLoader.registerEntityID(uberkat.war.EntityBulletShot.class, "Shell", ModLoader.getUniqueEntityId());
        ModLoader.registerEntityID(uberkat.war.EntityBulletCasing.class, "Casing", ModLoader.getUniqueEntityId());
        ModLoader.registerEntityID(uberkat.war.EntityBulletCasingShell.class, "ShellCasing", ModLoader.getUniqueEntityId());
        ModLoader.registerEntityID(uberkat.war.EntityGrenade.class, "Grenade", ModLoader.getUniqueEntityId());
        ModLoader.registerEntityID(uberkat.war.EntityNukePrimed.class, "Nuke", ModLoader.getUniqueEntityId());
        ModLoader.registerEntityID(uberkat.war.EntityBulletRocket.class, "Rocket", ModLoader.getUniqueEntityId());
        ModLoader.registerEntityID(uberkat.war.EntityBulletRocketLaser.class, "RocketLaser", ModLoader.getUniqueEntityId());
        ModLoader.registerEntityID(uberkat.war.EntityGrenadeStun.class, "GrenadeStun", ModLoader.getUniqueEntityId());
        ModLoader.registerEntityID(uberkat.war.EntityGrenadeSmoke.class, "GrenadeSmoke", ModLoader.getUniqueEntityId());
        ModLoader.registerEntityID(uberkat.war.EntityBulletFlame.class, "Flame", ModLoader.getUniqueEntityId());
        ModLoader.registerEntityID(uberkat.war.EntityGrenadeSticky.class, "GrenadeSticky", ModLoader.getUniqueEntityId());
        ModLoader.registerEntityID(uberkat.war.EntityBulletLaser.class, "Laser", ModLoader.getUniqueEntityId());
        ModLoader.registerEntityID(uberkat.war.EntityGrenadeIncendiary.class, "GrenadeIncendiary", ModLoader.getUniqueEntityId());
        ModLoader.registerEntityID(uberkat.war.EntityParachute.class, "Parachute", ModLoader.getUniqueEntityId());
        ModLoader.registerEntityID(uberkat.war.EntityGrapplingHook.class, "GrapplingHook", ModLoader.getUniqueEntityId());
        ModLoader.registerEntityID(uberkat.war.EntityAtv.class, "ATV", ModLoader.getUniqueEntityId());
        MinecraftForge.registerEntity(uberkat.war.EntityBulletAk47.class, this, 100, 40, 5, true);
        MinecraftForge.registerEntity(uberkat.war.EntityBulletCasing.class, this, 101, 40, 5, true);
        MinecraftForge.registerEntity(uberkat.war.EntityBulletCasingShell.class, this, 102, 40, 5, true);
        MinecraftForge.registerEntity(uberkat.war.EntityBulletDeagle.class, this, 103, 40, 5, true);
        MinecraftForge.registerEntity(uberkat.war.EntityBulletFlame.class, this, 104, 40, 5, true);
        MinecraftForge.registerEntity(uberkat.war.EntityBulletLaser.class, this, 105, 40, 5, true);
        MinecraftForge.registerEntity(uberkat.war.EntityBulletM4.class, this, 106, 40, 5, true);
        MinecraftForge.registerEntity(uberkat.war.EntityBulletMinigun.class, this, 107, 40, 5, true);
        MinecraftForge.registerEntity(uberkat.war.EntityBulletMp5.class, this, 108, 40, 5, true);
        MinecraftForge.registerEntity(uberkat.war.EntityBulletRocket.class, this, 109, 40, 5, true);
        MinecraftForge.registerEntity(uberkat.war.EntityBulletRocketLaser.class, this, 110, 40, 5, true);
        MinecraftForge.registerEntity(uberkat.war.EntityBulletSg552.class, this, 111, 40, 5, true);
        MinecraftForge.registerEntity(uberkat.war.EntityBulletShot.class, this, 112, 40, 5, true);
        MinecraftForge.registerEntity(uberkat.war.EntityBulletSniper.class, this, 113, 40, 5, true);
        MinecraftForge.registerEntity(uberkat.war.EntityGrenade.class, this, 114, 40, 5, true);
        MinecraftForge.registerEntity(uberkat.war.EntityGrenadeIncendiary.class, this, 115, 40, 5, true);
        MinecraftForge.registerEntity(uberkat.war.EntityGrenadeSmoke.class, this, 116, 40, 5, true);
        MinecraftForge.registerEntity(uberkat.war.EntityGrenadeSticky.class, this, 117, 40, 5, true);
        MinecraftForge.registerEntity(uberkat.war.EntityGrenadeStun.class, this, 118, 40, 5, true);
        MinecraftForge.registerEntity(uberkat.war.EntityNukePrimed.class, this, 119, 40, 10, true);
        MinecraftForge.registerEntity(uberkat.war.EntityParachute.class, this, 120, 40, 3, true);
        MinecraftForge.registerEntity(uberkat.war.EntityGrapplingHook.class, this, 121, 40, 5, true);
        MinecraftForge.registerEntity(uberkat.war.EntityAtv.class, this, 122, 40, 10, true);
    }

    private void setHardcore()
    {
        try
        {
            Field afield[] = (net.minecraft.server.EnumCreatureType.class).getDeclaredFields();

            for (int i = 0; i < afield.length; i++)
            {
                if (afield[i].getType() == Integer.TYPE)
                {
                    afield[i].setAccessible(true);
                    Field field = (java.lang.reflect.Field.class).getDeclaredField("modifiers");
                    field.setAccessible(true);
                    field.setInt(afield[i], afield[i].getModifiers() & 0xffffffef);
                    afield[i].set(EnumCreatureType.MONSTER, Integer.valueOf(monsterSpawns));
                }
            }
        }
        catch (Exception exception)
        {
            ModLoader.getLogger().throwing("mod_ModernWarfare", "setHardcore", exception);
            WarTools.ThrowException("Aidan, you forgot to update your obfuscated reflection!", exception);
            return;
        }
    }

    public void addBulletRestrictions()
    {
        itemBulletLight.maxStackSize = 32;
        itemBulletMedium.maxStackSize = ammoRestrictions ? 8 : 32;
        itemBulletShell.maxStackSize = ammoRestrictions ? 8 : 32;
        itemGrenade.maxStackSize = ammoRestrictions ? 4 : 64;
        itemBulletRocket.maxStackSize = ammoRestrictions ? 4 : 32;
        itemBulletRocketLaser.maxStackSize = ammoRestrictions ? 4 : 32;
        itemBulletHeavy.maxStackSize = ammoRestrictions ? 4 : 32;
        itemGrenadeStun.maxStackSize = ammoRestrictions ? 4 : 64;
        itemGrenadeSmoke.maxStackSize = ammoRestrictions ? 4 : 64;
        itemGrenadeSticky.maxStackSize = ammoRestrictions ? 4 : 64;
        itemGrenadeIncendiary.maxStackSize = ammoRestrictions ? 4 : 64;
    }

    public void addRecipes()
    {
        ModLoader.addRecipe(new ItemStack(blockNuke, 1), new Object[]
                {
                    "X#X", "#X#", "X#X", 'X', Block.TNT, '#', Item.REDSTONE
                });
        ModLoader.addRecipe(new ItemStack(Item.IRON_INGOT, 1), new Object[]
                {
                    "XX", "XX", 'X', itemBulletCasing
                });
        ModLoader.addRecipe(new ItemStack(itemBulletLight, ammoRestrictions ? 4 : itemBulletLight.maxStackSize), new Object[]
                {
                    "X", "#", 'X', Item.IRON_INGOT, '#', Item.SULPHUR
                });
        ModLoader.addRecipe(new ItemStack(itemBulletMedium, ammoRestrictions ? 4 : itemBulletMedium.maxStackSize), new Object[]
                {
                    "X ", "##", 'X', Item.IRON_INGOT, '#', Item.SULPHUR
                });
        ModLoader.addRecipe(new ItemStack(itemBulletShell, ammoRestrictions ? 4 : itemBulletShell.maxStackSize), new Object[]
                {
                    "X ", "#Y", 'X', Item.IRON_INGOT, '#', Item.SULPHUR, 'Y', Item.PAPER
                });
        ModLoader.addRecipe(new ItemStack(itemBulletShell, ammoRestrictions ? 4 : itemBulletShell.maxStackSize), new Object[]
                {
                    " X", "#Y", 'X', Item.IRON_INGOT, '#', Item.SULPHUR, 'Y', Item.PAPER
                });
        ModLoader.addRecipe(new ItemStack(itemBulletRocket, ammoRestrictions ? 1 : itemBulletRocket.maxStackSize), new Object[]
                {
                    "###", "#X#", "XXX", 'X', Item.SULPHUR, '#', Item.IRON_INGOT
                });
        ModLoader.addRecipe(new ItemStack(itemBulletRocketLaser, ammoRestrictions ? 1 : itemBulletRocketLaser.maxStackSize), new Object[]
                {
                    "#Y#", "#X#", "XXX", 'X', Item.SULPHUR, '#', Item.IRON_INGOT, 'Y', Item.REDSTONE
                });
        ModLoader.addRecipe(new ItemStack(itemBulletHeavy, itemBulletHeavy.maxStackSize), new Object[]
                {
                    "XX", "##", 'X', Item.IRON_INGOT, '#', Item.SULPHUR
                });
        ModLoader.addRecipe(new ItemStack(Item.IRON_INGOT, 1), new Object[]
                {
                    "XX", "XX", 'X', itemBulletCasingShell
                });
        ModLoader.addRecipe(new ItemStack(itemScope), new Object[]
                {
                    "X#X", 'X', Item.DIAMOND, '#', Item.IRON_INGOT
                });
        ModLoader.addRecipe(new ItemStack(itemGrenade, 4), new Object[]
                {
                    "X#X", "#X#", "X#X", 'X', Item.SULPHUR, '#', Item.IRON_INGOT
                });
        ModLoader.addRecipe(new ItemStack(itemGrenadeStun, 4), new Object[]
                {
                    "X#X", "#Y#", "X#X", 'X', Item.SULPHUR, '#', Item.IRON_INGOT, 'Y', Item.GLOWSTONE_DUST
                });
        ModLoader.addRecipe(new ItemStack(itemGrenadeSmoke, 4), new Object[]
                {
                    "X#X", "#Y#", "X#X", 'X', Item.SULPHUR, '#', Item.IRON_INGOT, 'Y', Item.FLINT
                });
        ModLoader.addRecipe(new ItemStack(itemGrenadeIncendiary, 4), new Object[]
                {
                    " X ", "#Y#", " # ", 'X', Item.PAPER, '#', Block.GLASS, 'Y', itemOil
                });
        ModLoader.addRecipe(new ItemStack(itemOilDrop), new Object[]
                {
                    " X ", "X#X", " X ", 'X', Item.SULPHUR, '#', Item.WATER_BUCKET
                });
        ModLoader.addRecipe(new ItemStack(itemWrench), new Object[]
                {
                    "X", "X", "X", 'X', Item.IRON_INGOT
                });
        ModLoader.addRecipe(new ItemStack(itemJetPack), new Object[]
                {
                    "###", "#X#", "###", '#', Item.IRON_INGOT, 'X', itemOil
                });
        ModLoader.addRecipe(new ItemStack(itemJetPack), new Object[]
                {
                    "###", "#X#", "###", '#', Item.IRON_INGOT, 'X', itemOilDrop
                });
        ModLoader.addRecipe(new ItemStack(itemGrapplingHook, 1), new Object[]
                {
                    "X", "#", "#", '#', itemRope, 'X', Item.IRON_INGOT
                });
        ModLoader.addRecipe(new ItemStack(itemRope, 1), new Object[]
                {
                    "##", "##", "##", '#', Item.STRING
                });
        ModLoader.addRecipe(new ItemStack(itemLightometer, 1), new Object[]
                {
                    " # ", "#X#", " # ", '#', Item.IRON_INGOT, 'X', Item.GLOWSTONE_DUST
                });
        ModLoader.addRecipe(new ItemStack(itemNightvisionGoggles, 1), new Object[]
                {
                    "#X#", '#', Item.DIAMOND, 'X', Item.STRING
                });
        ModLoader.addRecipe(new ItemStack(itemScubaTank, 1), new Object[]
                {
                    "###", "# #", "###", '#', Item.IRON_INGOT
                });
        ModLoader.addRecipe(new ItemStack(itemParachute, 1), new Object[]
                {
                    "###", "XYX", "YYY", '#', Block.WOOL, 'X', Item.STRING, 'Y', Item.LEATHER
                });
        ModLoader.addRecipe(new ItemStack(itemTelescope), new Object[]
                {
                    "#", "X", "X", '#', Item.DIAMOND, 'X', Item.IRON_INGOT
                });
        ModLoader.addRecipe(new ItemStack(itemGhillieHelmet), new Object[]
                {
                    "###", "# #", '#', Block.VINE
                });
        ModLoader.addRecipe(new ItemStack(itemGhillieChest), new Object[]
                {
                    "# #", "###", "###", '#', Block.VINE
                });
        ModLoader.addRecipe(new ItemStack(itemGhilliePants), new Object[]
                {
                    "###", "# #", "# #", '#', Block.VINE
                });
        ModLoader.addRecipe(new ItemStack(itemGhillieBoots), new Object[]
                {
                    "# #", "# #", '#', Block.VINE
                });
        ModLoader.addRecipe(new ItemStack(itemCraftingPack, 1), new Object[]
                {
                    "L L", "LCL", "LLL", 'L', Item.LEATHER, 'C', Block.WORKBENCH
                });
        ModLoader.addRecipe(new ItemStack(itemGunSg552), new Object[]
                {
                    "IM", "W ", 'I', Item.IRON_INGOT, 'M', itemMediumBarrel, 'W', itemWoodGrip
                });
        ModLoader.addRecipe(new ItemStack(itemGunMinigun), new Object[]
                {
                    "IF", "M ", 'I', Item.IRON_INGOT, 'F', itemFatBarrel, 'B', itemMetalGrip
                });
        ModLoader.addRecipe(new ItemStack(itemGunLaser), new Object[]
                {
                    "R ", "IL", "M ", 'R', Item.REDSTONE, 'I', Item.IRON_INGOT, 'L', itemLongBarrel, 'M',
                    itemMetalGrip
                });
        ModLoader.addRecipe(new ItemStack(itemGunM4), new Object[]
                {
                    "IB", "M ", 'I', Item.IRON_INGOT, 'L', itemMediumBarrel, 'M', itemMetalGrip
                });
        ModLoader.addRecipe(new ItemStack(itemGunAk47), new Object[]
                {
                    "IS", "W ", 'I', Item.IRON_INGOT, 'S', itemSmallBarrel, 'W', itemWoodGrip
                });
        ModLoader.addRecipe(new ItemStack(itemGunMp5), new Object[]
                {
                    "IS", "M ", 'I', Item.IRON_INGOT, 'S', itemSmallBarrel, 'M', itemMetalGrip
                });
        ModLoader.addRecipe(new ItemStack(itemGunShotgun), new Object[]
                {
                    "IL", "W ", 'I', Item.IRON_INGOT, 'L', itemLongBarrel, 'W', itemWoodGrip
                });
        ModLoader.addRecipe(new ItemStack(itemGunDeagle), new Object[]
                {
                    "II", "M ", 'I', Item.IRON_INGOT, 'M', itemMetalGrip
                });
        ModLoader.addRecipe(new ItemStack(itemGunRocketLauncherLaser), new Object[]
                {
                    "R ", "OF", "M ", 'R', Item.REDSTONE, 'O', Block.OBSIDIAN, 'F', itemFatBarrel, 'M',
                    itemMetalGrip
                });
        ModLoader.addRecipe(new ItemStack(itemGunRocketLauncher), new Object[]
                {
                    "OF", "M ", 'O', Block.OBSIDIAN, 'F', itemFatBarrel, 'M', itemMetalGrip
                });
        ModLoader.addRecipe(new ItemStack(itemGunSniper), new Object[]
                {
                    "S ", "IL", "M ", 'S', itemScope, 'I', Item.IRON_INGOT, 'L', itemLongBarrel, 'M',
                    itemMetalGrip
                });
        ModLoader.addRecipe(new ItemStack(itemGunFlamethrower), new Object[]
                {
                    "IF", "GT", 'I', Item.IRON_INGOT, 'F', itemFatBarrel, 'G', itemMetalGrip, 'T', itemFuelTank
                });
        ModLoader.addRecipe(new ItemStack(itemWoodGrip), new Object[]
                {
                    "PP", "P ", 'P', Block.WOOD
                });
        ModLoader.addRecipe(new ItemStack(itemMetalGrip), new Object[]
                {
                    "II", "I ", 'I', Item.IRON_INGOT
                });
        ModLoader.addRecipe(new ItemStack(itemSmallBarrel), new Object[]
                {
                    "II", 'I', Item.IRON_INGOT
                });
        ModLoader.addRecipe(new ItemStack(itemMediumBarrel), new Object[]
                {
                    "SI", 'S', itemSmallBarrel, 'I', Item.IRON_INGOT
                });
        ModLoader.addRecipe(new ItemStack(itemLongBarrel), new Object[]
                {
                    "MI", 'M', itemMediumBarrel, 'I', Item.IRON_INGOT
                });
        ModLoader.addRecipe(new ItemStack(itemFatBarrel), new Object[]
                {
                    "III", "III", 'I', Item.IRON_INGOT
                });
        ModLoader.addRecipe(new ItemStack(itemFuelTank), new Object[]
                {
                    "II", "II", "II", 'I', Item.IRON_INGOT
                });
        ModLoader.addRecipe(new ItemStack(itemAtvWheel), new Object[]
                {
                    " X ", "XYX", " X ", 'X', Item.LEATHER, 'Y', Item.IRON_INGOT
                });
        ModLoader.addRecipe(new ItemStack(itemAtvBody), new Object[]
                {
                    "ZXZ", "XYX", "ZXZ", 'X', Item.IRON_INGOT, 'Y', Block.FURNACE, 'Z', Item.REDSTONE
                });
        ModLoader.addRecipe(new ItemStack(itemAtv), new Object[]
                {
                    " X ", "XYX", " X ", 'X', itemAtvWheel, 'Y', itemAtvBody
                });
        ModLoader.addShapelessRecipe(new ItemStack(itemOil), new Object[]
                {
                    new ItemStack(itemOilDrop), new ItemStack(Item.BUCKET)
                });
        ModLoader.addShapelessRecipe(new ItemStack(itemGrenadeSticky), new Object[]
                {
                    new ItemStack(itemGrenade), new ItemStack(Item.SLIME_BALL)
                });
    }

    public void addBlocks()
    {
        blockRope = (new BlockRope(ropeID, 4, ModLoader.getUniqueBlockModelID(this, false))).c(-1F).b(6000000F).a(Block.k).a("blockRope");
        blockGrapplingHook = (new BlockGrapplingHook(grapplingHookID, 0, ModLoader.getUniqueBlockModelID(this, false))).c(0.0F).a(Block.i).a("blockGrapplingHook");
        blockNuke = (new BlockNuke(nukeID, 1)).c(0.0F).a(Block.g).a("blockNuke");
        blockOil = (new BlockOil(oilID, 2, ModLoader.getUniqueBlockModelID(this, false))).c(-1F).a("blockOil");
        ModLoader.registerBlock(blockRope);
        ModLoader.registerBlock(blockGrapplingHook);
        ModLoader.registerBlock(blockNuke);
        ModLoader.registerBlock(blockOil);
    }

    public void addItems()
    {
        itemLightometer = (new Item(13236)).d(ModLoader.getUniqueSpriteIndex("/gui/items.png")).a("itemLightometer");
        itemParachute = (new ItemParachute(13237, ModLoader.addArmor("parachute"))).d(50).a("itemParachute");
        itemTelescope = (new ItemTelescope(13238)).d(58).a("itemTelescope");
        itemBulletCasing = (new ItemWar(13239)).d(8).a("bulletCasing");
        itemBulletLight = (new ItemWar(13240)).e(32).d(5).a("bullet9mm");
        itemBulletMedium = (new ItemWar(13241)).e(8).d(7).a("bullet357");
        itemBulletShell = (new ItemWar(13242)).e(8).d(14).a("bulletShell");
        itemGunAk47 = (new ItemGunAk47(13243)).d(30).a("gunAk47");
        itemGunMp5 = (new ItemGunMp5(13244)).d(36).a("gunMp5");
        itemGunShotgun = (new ItemGunShotgun(13245)).d(40).a("gunShotgun");
        itemGunDeagle = (new ItemGunDeagle(13246)).d(31).a("gunDeagle");
        itemGrenade = (new ItemGrenade(13247)).e(4).d(22).a("grenade");
        itemBulletRocket = (new ItemWar(13248)).e(4).d(11).a("bulletRocket");
        itemGunRocketLauncher = (new ItemGunRocketLauncher(13249)).d(37).a("gunRocketLauncher");
        itemBulletRocketLaser = (new ItemWar(13250)).e(4).d(12).a("bulletRocketLaser");
        itemGunRocketLauncherLaser = (new ItemGunRocketLauncherLaser(13251)).d(38).a("gunRocketLauncherLaser");
        itemBulletHeavy = (new ItemWar(13252)).e(4).d(6).a("bullet50Cal");
        itemGunSniper = (new ItemGunSniper(13253)).d(41).a("gunSniper");
        itemGrenadeStun = (new ItemGrenadeStun(13254)).e(4).d(27).a("grenadeStun");
        itemGrenadeSmoke = (new ItemGrenadeSmoke(13255)).e(4).d(25).a("grenadeSmoke");
        itemBulletCasingShell = (new ItemWar(13256)).d(10).a("bulletCasingShell");
        itemOil = (new ItemOil(13257)).d(48).a("itemOil");
        itemGunFlamethrower = (new ItemGunFlamethrower(13258)).d(32).a("gunFlamethrower");
        itemGrenadeSticky = (new ItemGrenadeSticky(13259)).e(4).d(26).a("grenadeSticky");
        itemGunSg552 = (new ItemGunSg552(13260)).d(39).a("gunSg552");
        itemGunMinigun = (new ItemGunMinigun(13261)).d(35).a("gunMinigun");
        itemGunLaser = (new ItemGunLaser(13262)).d(33).a("gunLaser");
        itemGunM4 = (new ItemGunM4(13263)).d(34).a("gunM4");
        itemGrenadeIncendiary = (new ItemGrenadeIncendiary(13264)).e(4).d(23).a("grenadeIncendiary");
        itemGrenadeIncendiaryLit = (new ItemGrenadeIncendiary(13265)).e(4).d(24).a("grenadeIncendiaryLit");
        itemScope = (new ItemWar(13277)).d(54).a("itemScope");
        itemWrench = (new ItemWar(13278)).e(1).setMaxDurability(15).d(59).a("itemWrench");
        itemSentry = (new ItemSentry(13279)).d(42).e(1).setMaxDurability(0).a(true).a("itemSentry");
        itemOilDrop = (new ItemWar(13280)).d(49).a("itemOilDrop");
        itemNightvisionGoggles = (new ItemArmorWar(13281, EnumArmorMaterial.CLOTH, ModLoader.addArmor("nightvision"), 0)).d(47).a("itemNightvision");
        itemScubaTank = (new ItemArmorWar(13282, EnumArmorMaterial.CLOTH, ModLoader.addArmor("scubaTank"), 1)).d(4).a("itemScubaTank");
        itemGhillieHelmet = (new ItemArmorWar(13283, EnumArmorMaterial.CLOTH, ModLoader.addArmor("ghillie"), 0)).d(17).a("itemGhillieHelmet");
        itemGhillieChest = (new ItemArmorWar(13284, EnumArmorMaterial.CLOTH, ModLoader.addArmor("ghillie"), 1)).d(16).a("itemGhillieChest");
        itemGhilliePants = (new ItemArmorWar(13285, EnumArmorMaterial.CLOTH, ModLoader.addArmor("ghillie"), 2)).d(18).a("itemGhilliePants");
        itemGhillieBoots = (new ItemArmorWar(13286, EnumArmorMaterial.CLOTH, ModLoader.addArmor("ghillie"), 3)).d(15).a("itemGhillieBoots");
        itemRope = (new ItemWar(13287)).d(53).a("itemRope");
        itemGrapplingHook = (new ItemGrapplingHook(13288)).d(20).a("itemGrapplingHook");
        itemJetPack = (new ItemArmorWar(13289, EnumArmorMaterial.CLOTH, ModLoader.addArmor("jetPack"), 1)).d(43).a("itemJetPack");
        itemCraftingPack = (new ItemCraftingPack(13290)).d(19).a("itemCraftingPack");
        itemWoodGrip = (new ItemWar(13291)).d(29).a("itemWoodGrip");
        itemMetalGrip = (new ItemWar(13292)).d(28).a("itemMetalGrip");
        itemSmallBarrel = (new ItemWar(13293)).d(0).a("itemSmallBarrel");
        itemMediumBarrel = (new ItemWar(13294)).d(1).a("itemMediumBarrel");
        itemLongBarrel = (new ItemWar(13295)).d(2).a("itemLongBarrel");
        itemFatBarrel = (new ItemWar(13296)).d(3).a("itemFatBarrel");
        itemFuelTank = (new ItemWar(13297)).d(4).a("itemFuelTank");
        itemAtv = (new ItemAtv(13298)).d(51).a("itemAtv");
        itemAtvBody = (new ItemWar(13299)).d(52).a("itemAtvBody");
        itemAtvWheel = (new ItemWar(13300)).d(56).a("itemAtvWheel");
    }

    public void handleParachuteKey(EntityHuman entityhuman)
    {
        ItemStack itemstack = entityhuman.inventory.armor[2];

        if (itemstack != null && itemstack.id == itemParachute.id)
        {
            if (WarTools.minecraftserver.getWorldServer(0).players.contains(entityhuman))
            {
                useParachute(itemstack, WarTools.minecraftserver.getWorldServer(0), entityhuman);
            }
            else if (WarTools.minecraftserver.getWorldServer(-1).players.contains(entityhuman))
            {
                useParachute(itemstack, WarTools.minecraftserver.getWorldServer(-1), entityhuman);
            }
            else if (WarTools.minecraftserver.getWorldServer(1).players.contains(entityhuman))
            {
                useParachute(itemstack, WarTools.minecraftserver.getWorldServer(1), entityhuman);
            }
        }
    }

    private void handleFlash()
    {
        for (Iterator iterator = flashTimes.entrySet().iterator(); iterator.hasNext();)
        {
            java.util.Map.Entry entry = (java.util.Map.Entry)iterator.next();
            int i = ((Integer)((Pair)entry.getValue()).getLeft()).intValue() - 1;

            if (i <= 0)
            {
                ((EntityLiving)entry.getKey()).bb = ((Float)((Pair)entry.getValue()).getRight()).floatValue();
                iterator.remove();
            }
            else
            {
                entry.setValue(new Pair(Integer.valueOf(i), ((Pair)entry.getValue()).getRight()));
            }
        }
    }

    private void handleReload()
    {
        for (Iterator iterator = reloadTimes.entrySet().iterator(); iterator.hasNext();)
        {
            java.util.Map.Entry entry = (java.util.Map.Entry)iterator.next();
            int i = ((Integer)entry.getValue()).intValue() - 1;

            if (i <= 0)
            {
                iterator.remove();
            }
            else
            {
                entry.setValue(Integer.valueOf(i));
            }
        }
    }

    public static void useZoom()
    {
    }

    private void useParachute(ItemStack itemstack, World world, EntityHuman entityhuman)
    {
        if (!WarTools.onGroundOrInWater(world, entityhuman))
        {
            itemstack.damage(1, entityhuman);

            if (itemstack.count == 0)
            {
                boolean flag = false;
                int i = 0;

                do
                {
                    if (i >= entityhuman.inventory.armor.length)
                    {
                        break;
                    }

                    if (entityhuman.inventory.armor[i] == itemstack)
                    {
                        entityhuman.inventory.armor[i] = null;
                        flag = true;
                        break;
                    }

                    i++;
                }
                while (true);

                if (!flag)
                {
                    int j = 0;

                    do
                    {
                        if (j >= entityhuman.inventory.items.length)
                        {
                            break;
                        }

                        if (entityhuman.inventory.items[j] == itemstack)
                        {
                            entityhuman.inventory.items[j] = null;
                            boolean flag1 = true;
                            break;
                        }

                        j++;
                    }
                    while (true);
                }
            }

            world.makeSound(entityhuman, "war.parachute", 0.5F, 1.0F / (WarTools.random.nextFloat() * 0.1F + 0.95F));

            if (!world.isStatic)
            {
                world.addEntity(new EntityParachute(world, entityhuman));
            }
        }
    }

    public static void handleReload(World world, EntityHuman entityhuman, boolean flag)
    {
        if (!reloadTimes.containsKey(entityhuman))
        {
            ItemStack itemstack = entityhuman.U();

            if (itemstack != null && (itemstack.getItem() instanceof ItemGun))
            {
                Item item = ((ItemGun)itemstack.getItem()).requiredBullet;
                int i = -1;
                int j = -1;
                int k = -1;
                boolean flag1 = false;

                do
                {
                    k = -1;
                    PlayerInventory playerinventory = entityhuman.inventory;

                    for (int l = i + 1; l < playerinventory.items.length; l++)
                    {
                        if (playerinventory.items[l] == null || playerinventory.items[l].id != item.id)
                        {
                            continue;
                        }

                        if (item.getMaxDurability() > 0)
                        {
                            int i1 = item.getMaxDurability() + 1;

                            if (i == -1)
                            {
                                j = i1 - playerinventory.items[l].getData();

                                if (!flag && item.getMaxDurability() > 0 && j == item.getMaxDurability() + 1)
                                {
                                    break;
                                }
                            }
                            else
                            {
                                if (!flag1)
                                {
                                    reload(world, entityhuman);
                                    flag1 = true;
                                }

                                k = i1 - playerinventory.items[l].getData();
                                int k1 = Math.min(i1 - j, k);
                                j += k1;
                                k -= k1;
                                playerinventory.items[i].setData(i1 - j);
                                playerinventory.items[l].setData(i1 - k);

                                if (k == 0)
                                {
                                    playerinventory.items[l] = new ItemStack(Item.BUCKET);
                                }

                                break;
                            }
                        }
                        else if (i == -1)
                        {
                            j = playerinventory.items[l].count;

                            if (!flag && item.getMaxDurability() == 0 && j == item.maxStackSize)
                            {
                                break;
                            }
                        }
                        else
                        {
                            if (!flag1)
                            {
                                reload(world, entityhuman);
                                flag1 = true;
                            }

                            k = playerinventory.items[l].count;
                            int j1 = Math.min(item.maxStackSize - j, k);
                            j += j1;
                            k -= j1;
                            playerinventory.items[i].count = j;
                            playerinventory.items[l].count = k;

                            if (k == 0)
                            {
                                playerinventory.items[l] = null;
                            }

                            break;
                        }

                        if (i == -1)
                        {
                            i = l;
                        }
                    }

                    if (i == -1)
                    {
                        break;
                    }

                    if (flag1 || !flag)
                    {
                        continue;
                    }

                    reload(world, entityhuman);
                    break;
                }
                while (k != -1 && (item.getMaxDurability() != 0 || j != item.maxStackSize) && (item.getMaxDurability() <= 0 || j != item.getMaxDurability() + 1));
            }
        }
    }

    public static String getMinecraftDir()
    {
        try
        {
            String s = (net.minecraft.server.ModLoader.class).getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
            return s.substring(0, s.lastIndexOf('/'));
        }
        catch (URISyntaxException urisyntaxexception)
        {
            return null;
        }
    }

    public static void reload(World world, EntityHuman entityhuman)
    {
        world.makeSound(entityhuman, "war.reload", 1.0F, 1.0F / (entityhuman.random.nextFloat() * 0.1F + 0.95F));
        reloadTimes.put(entityhuman, Integer.valueOf(40));
    }

    public static boolean getSniperZoomedIn(EntityHuman entityhuman)
    {
        Boolean boolean1 = (Boolean)isSniperZoomedIn.get(entityhuman);
        return boolean1 == null ? false : boolean1.booleanValue();
    }

    public void onPacketData(NetworkManager networkmanager, String s, byte abyte0[])
    {
        DataInputStream datainputstream = new DataInputStream(new ByteArrayInputStream(abyte0));
        EntityPlayer entityplayer = ((NetServerHandler)networkmanager.getNetHandler()).getPlayerEntity();

        if (s.equals("ModernWarfare"))
        {
            try
            {
                int i = datainputstream.readInt();

                if (i == 0)
                {
                    isSniperZoomedIn.put(entityplayer, Boolean.valueOf(false));
                    System.out.println((new StringBuilder()).append("[ModernWarfare] Received '0' packet from ").append(entityplayer.name).append(".").toString());
                }

                if (i == 1)
                {
                    isSniperZoomedIn.put(entityplayer, Boolean.valueOf(true));
                    System.out.println((new StringBuilder()).append("[ModernWarfare] Received '1' packet from ").append(entityplayer.name).append(".").toString());
                }

                if (i == 2)
                {
                    handleParachuteKey(entityplayer);
                    System.out.println((new StringBuilder()).append("[ModernWarfare] Received '2' packet from ").append(entityplayer.name).append(".").toString());
                }

                if (i == 3)
                {
                    System.out.println((new StringBuilder()).append("[ModernWarfare] Received '3' packet from ").append(entityplayer.name).append(".").toString());

                    if (entityplayer.activeContainer instanceof ContainerCraftingPack)
                    {
                        entityplayer.closeInventory();
                        entityplayer.H();
                    }
                    else if (entityplayer.inventory.d(itemCraftingPack.id))
                    {
                        entityplayer.openGui(this, 14, entityplayer.world, (int)entityplayer.locX, (int)entityplayer.locY, (int)entityplayer.locZ);
                    }
                }

                if (i == 4)
                {
                    System.out.println((new StringBuilder()).append("[ModernWarfare] Received '4' packet from ").append(entityplayer.name).append(".").toString());

                    if (entityplayer.vehicle instanceof EntityAtv);

                    if (entityplayer.activeContainer instanceof ContainerAtv)
                    {
                        entityplayer.closeInventory();
                    }
                    else
                    {
                        entityplayer.openGui(this, 15, entityplayer.world, (int)entityplayer.locX, (int)entityplayer.locY, (int)entityplayer.locZ);
                    }
                }

                if (i == 5)
                {
                    System.out.println((new StringBuilder()).append("[ModernWarfare] Received '5' packet from ").append(entityplayer.name).append(".").toString());

                    if (entityplayer.vehicle instanceof EntityAtv)
                    {
                        ((EntityAtv)entityplayer.vehicle).fireGuns();
                    }
                }

                if (i == 6)
                {
                    System.out.println((new StringBuilder()).append("[ModernWarfare] Received '6' packet from ").append(entityplayer.name).append(".").toString());
                    handleReload(entityplayer.world, entityplayer, false);
                }

                if (i == 7)
                {
                    System.out.println((new StringBuilder()).append("[ModernWarfare] Received '7' packet from ").append(entityplayer.name).append(".").toString());

                    if (jetPackOn.containsKey(entityplayer))
                    {
                        jetPackOn.remove(entityplayer);
                    }

                    jetPackOn.put(entityplayer, Boolean.valueOf(true));
                }

                if (i == 8)
                {
                    System.out.println((new StringBuilder()).append("[ModernWarfare] Received '8' packet from ").append(entityplayer.name).append(".").toString());

                    if (jetPackOn.containsKey(entityplayer))
                    {
                        jetPackOn.remove(entityplayer);
                    }

                    jetPackOn.put(entityplayer, Boolean.valueOf(false));
                }
            }
            catch (IOException ioexception)
            {
                ioexception.printStackTrace();
            }
        }
    }

    public void onConnect(NetworkManager networkmanager)
    {
    }

    public void onLogin(NetworkManager networkmanager, Packet1Login packet1login)
    {
        MessageManager.getInstance().registerChannel(networkmanager, this, "ModernWarfare");
    }

    public void onDisconnect(NetworkManager networkmanager, String s, Object aobj[])
    {
    }

    public Object getGuiElement(int i, EntityHuman entityhuman, World world, int j, int k, int l)
    {
        switch (i)
        {
            case 14:
                return new ContainerCraftingPack(entityhuman.inventory, world);

            case 15:
                return new ContainerAtv(entityhuman.inventory, (EntityAtv)entityhuman.vehicle);
        }

        return null;
    }

    static
    {
        sentryEntityClasses = (new Class[]
                {
                    uberkat.war.EntitySentryAk47.class, uberkat.war.EntitySentryMp5.class, uberkat.war.EntitySentryShotgun.class, uberkat.war.EntitySentryDeagle.class, uberkat.war.EntitySentryRocketLauncher.class, uberkat.war.EntitySentryRocketLauncherLaser.class, uberkat.war.EntitySentrySniper.class, uberkat.war.EntitySentryFlamethrower.class, uberkat.war.EntitySentrySg552.class, uberkat.war.EntitySentryMinigun.class,
                    uberkat.war.EntitySentryLaser.class, uberkat.war.EntitySentryM4.class
                });
    }
}
