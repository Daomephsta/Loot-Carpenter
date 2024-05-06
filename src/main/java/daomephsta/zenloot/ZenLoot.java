package daomephsta.zenloot;

import crafttweaker.mc1120.commands.CTChatCommand;
import daomephsta.zenloot.command.CommandZenLoot;
import daomephsta.zenloot.zenscript.api.ZenLootManager;
import daomephsta.zenloot.zenscript.api.factory.ZenLambdaLootCondition;
import daomephsta.zenloot.zenscript.api.factory.ZenLambdaLootFunction;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.storage.loot.conditions.LootConditionManager;
import net.minecraft.world.storage.loot.functions.LootFunctionManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(
    modid = ZenLoot.ID, name = ZenLoot.NAME, version = ZenLoot.VERSION,
    dependencies = "required-after:crafttweaker@[4.1.20,); before:jeresources; required:forge@[14.23.5.2779,);"
    )
public class ZenLoot
{
    public static final String NAME = "ZenLoot";
    public static final String ID = "zenloot";
    public static final String VERSION = "@VERSION@";

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        ZenLootConfig.onLoad();
        ZenLootNetworkChecker.install();
        ZenLootManager.register(); // Entrypoint for the API
        LootFunctionManager.registerFunction(ZenLambdaLootFunction.SERIALISER);
        LootConditionManager.registerCondition(ZenLambdaLootCondition.SERIALISER);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        CTChatCommand.registerCommand(new CommandZenLoot());
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event)
    {
        ZenLootManager.INSTANCE.tables.writeGeneratedFiles(event.getServer());
    }

    public static TextComponentTranslation translation(String keySuffix, Object... args)
    {
        return new TextComponentTranslation(ID + keySuffix, args);
    }
}
