package daomephsta.zenloot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.LangKey;
import net.minecraftforge.common.config.Config.RequiresMcRestart;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


@Config(modid = ZenLoot.ID)
@Mod.EventBusSubscriber(modid = ZenLoot.ID)
public class ZenLootConfig
{
    private static final Logger LOGGER = LogManager.getLogger(ZenLoot.NAME);

    @LangKey(ZenLoot.ID + ".config.warnings.category")
    public static Warnings warnings = new Warnings();

    public static class Warnings
    {
        @RequiresMcRestart
        @Comment("Warns about deprecated methods on world load. Resets if the version of ZenLoot is changed.")
        @LangKey(ZenLoot.ID + ".config.warnings.deprecation")
        public boolean deprecation = true;

        @RequiresMcRestart
        @Comment("Warns when newTable() is passed a table id that implictly or explicitly uses the 'minecraft' namescape.")
        @LangKey(ZenLoot.ID + ".config.warnings.newTableMinecraftNamespace")
        public boolean newTableMinecraftNamespace = true;
    }

    @LangKey(ZenLoot.ID + ".config.workarounds.category")
    public static Workarounds workarounds = new Workarounds();

    public static class Workarounds
    {
        @Comment("Classes to force initialise during pre-init. Use only if directed to by ZenLoot author.")
        @LangKey(ZenLoot.ID + ".config.workarounds.forceInitClasses")
        public String[] forceInitClasses = {};
    }

    @Comment("Do not touch!")
    @LangKey(ZenLoot.ID + ".config.lastCfgVersion")
    public static String lastCfgVersion = ZenLoot.VERSION;

    public static void onLoad()
    {
        if (!lastCfgVersion.equals(ZenLoot.VERSION))
        {
            lastCfgVersion = ZenLoot.VERSION;
            warnings.deprecation = true;
        }
        forceInitClasses();
        ConfigManager.sync(ZenLoot.ID, Config.Type.INSTANCE);
    }

    @SubscribeEvent
    public static void syncConfig(OnConfigChangedEvent e)
    {
        if (e.getModID().equals(ZenLoot.ID))
        {
            forceInitClasses();
            ConfigManager.sync(ZenLoot.ID, Config.Type.INSTANCE);
        }
    }

    private static void forceInitClasses()
    {
        for (String className : workarounds.forceInitClasses)
        {
            try
            {
                Class.forName(className);
                LOGGER.info("{} initialisation ensured", className);
            }
            catch (ClassNotFoundException e)
            {
                LOGGER.error("Could not ensure initialisation of {}", className, e);
            }
        }
    }
}
