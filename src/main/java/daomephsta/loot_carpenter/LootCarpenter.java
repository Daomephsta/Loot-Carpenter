package daomephsta.loot_carpenter;

import daomephsta.loot_carpenter.zenscript.api.ZenLootManager;
import daomephsta.loot_carpenter.zenscript.api.factory.ZenLambdaLootCondition;
import daomephsta.loot_carpenter.zenscript.api.factory.ZenLambdaLootFunction;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.storage.loot.conditions.LootConditionManager;
import net.minecraft.world.storage.loot.functions.LootFunctionManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(
    modid = LootCarpenter.ID, name = LootCarpenter.NAME, version = LootCarpenter.VERSION,
    dependencies = "required-after:crafttweaker@[4.1.20,); before:jeresources; required:forge@[14.23.5.2779,);"
    )
public class LootCarpenter
{
    public static final String NAME = "Loot Carpenter";
    public static final String ID = "loot_carpenter";
    public static final String VERSION = "@VERSION@";
    public static final String ZEN_PACKAGE = "mods." + ID;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        LootCarpenterConfig.onLoad();
        LootCarpenterNetworkChecker.install();
        ZenLootManager.register(); // Entrypoint for the primary API
        LootFunctionManager.registerFunction(ZenLambdaLootFunction.SERIALISER);
        LootConditionManager.registerCondition(ZenLambdaLootCondition.SERIALISER);
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
