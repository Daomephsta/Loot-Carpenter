package daomephsta.loot_carpenter.test.support;

import daomephsta.loot_carpenter.zenscript.api.ZenLootManager;
import daomephsta.loot_carpenter.zenscript.api.ZenLootManager.LootTableManager.LootTableEditor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTable;


public class TestsBase
{
    protected static final String TEST_NAMESPACE = "loot_carpenter_test";
    protected static final ResourceLocation FOO_ID = new ResourceLocation(TEST_NAMESPACE, "foo");
    protected static final ResourceLocation BAR_ID = new ResourceLocation(TEST_NAMESPACE, "bar");
    protected ZenLootManager loot = TestUtils.testInstance(ZenLootManager.class);
    protected LootTable fooOriginal = TestUtils.loadTable(FOO_ID);
    protected LootTable barOriginal = TestUtils.loadTable(BAR_ID);

    protected LootTable editFoo(LootTableEditor editor)
    {
        return edit(FOO_ID, fooOriginal, editor);
    }

    protected LootTable editBar(LootTableEditor editor)
    {
        return edit(BAR_ID, barOriginal, editor);
    }

    private LootTable edit(ResourceLocation name, LootTable table, LootTableEditor editor)
    {
        loot.tables.editTable(name.toString(), editor);
        return loot.tables.withEdits(table, name);
    }
}
