package daomephsta.loot_carpenter.test;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.extension.RegisterExtension;

import daomephsta.loot_carpenter.LootCarpenter;
import daomephsta.loot_carpenter.test.support.TestUtils;
import daomephsta.loot_carpenter.test.support.zenscript.CraftTweakerLoggerRedirect;
import daomephsta.loot_carpenter.test.support.zenscript.ScriptRunner;
import daomephsta.loot_carpenter.zenscript.api.ZenLootManager;
import io.github.daomephsta.saddle.engine.SaddleTest;
import io.github.daomephsta.saddle.engine.SaddleTest.LoadPhase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;


/**
 * Checks that all classes and methods are registered with and callable by
 * Zenscript. Functionality is tested elsewhere using the backing Java methods
 * directly.
 *
 * @author Daomephsta
 *
 */
public class ZenscriptSmokeTests
{
    @RegisterExtension
    public static final CraftTweakerLoggerRedirect redirect = new CraftTweakerLoggerRedirect(
        LogManager.getFormatterLogger("Saddle." + LootCarpenter.NAME));

    @SaddleTest(loadPhase = LoadPhase.INIT)
    public void jsonTests()
    {
        ScriptRunner.run("scripts/json-tests.zs");
    }

    @SaddleTest(loadPhase = LoadPhase.INIT)
    public void itemEntryAddition()
    {
        ScriptRunner.run("scripts/item-entry-addition.zs");
    }

    @SaddleTest(loadPhase = LoadPhase.INIT)
    public void lootTableEntryAddition()
    {
        ScriptRunner.run("scripts/loot-table-entry-addition.zs");
    }

    @SaddleTest(loadPhase = LoadPhase.INIT)
    public void emptyEntryAddition()
    {
        ScriptRunner.run("scripts/empty-entry-addition.zs");
    }

    @SaddleTest(loadPhase = LoadPhase.INIT)
    public void entryNaming()
    {
        ScriptRunner.run("scripts/entry-naming.zs");
    }

    @SaddleTest(loadPhase = LoadPhase.INIT)
    public void miscPoolTests()
    {
        ScriptRunner.run("scripts/misc-pool-tests.zs");
    }

    @SaddleTest(loadPhase = LoadPhase.INIT)
    public void lootConditionFactory()
    {
        ScriptRunner.run("scripts/loot-condition-factory.zs");
    }

    @SaddleTest(loadPhase = LoadPhase.INIT)
    public void lootFunctionFactory()
    {
        ScriptRunner.run("scripts/loot-function-factory.zs");
    }

    @SaddleTest(loadPhase = LoadPhase.INIT)
    public void createTable()
    {
        ScriptRunner.run("scripts/create-table.zs");
    }

    @AfterEach
    public void loadTweakedTables()
    {
        Map<ResourceLocation, ?> editorsByTable = ObfuscationReflectionHelper
            .getPrivateValue(ZenLootManager.LootTableManager.class, ZenLootManager.INSTANCE.tables, "editorsByTable");
        for (ResourceLocation tweakedTable : editorsByTable.keySet())
            ZenLootManager.INSTANCE.tables.withEdits(TestUtils.loadTable(tweakedTable), tweakedTable);
    }
}
