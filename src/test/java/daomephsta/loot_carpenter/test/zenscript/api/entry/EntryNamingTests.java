package daomephsta.loot_carpenter.test.zenscript.api.entry;

import static daomephsta.loot_carpenter.test.support.TestUtils.iitemstack;
import static daomephsta.loot_carpenter.test.support.assertion.LootCarpenterAssertions.assertThat;

import daomephsta.loot_carpenter.test.support.TestsBase;
import daomephsta.loot_carpenter.zenscript.api.EditableLootPool;
import io.github.daomephsta.saddle.engine.SaddleTest;
import io.github.daomephsta.saddle.engine.SaddleTest.LoadPhase;
import net.minecraft.init.Items;
import net.minecraft.world.storage.loot.LootTable;


public class EntryNamingTests extends TestsBase
{
    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void identicalItems()
    {
        LootTable foo = editFoo((table, context) -> {
            EditableLootPool bar = table.getPool("bar");
            bar.addItemEntry(iitemstack(Items.DYE, 2), 5, null);
            bar.addItemEntry(iitemstack(Items.DYE, 1), 2, null);
        });
        assertThat(foo.getPool("bar")).hasEntry("loot_carpenter#1").hasEntry("loot_carpenter#2");
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void customNamedItemEntry()
    {
        LootTable foo = editFoo((table, context) -> {
            EditableLootPool bar = table.getPool("bar");
            bar.addItemEntry(iitemstack(Items.DYE, 2), 5, "garple");
            bar.addItemEntry(iitemstack(Items.DYE, 1), 2, null);
        });
        assertThat(foo.getPool("bar")).hasEntry("loot_carpenter#1").hasEntry("garple");
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void identicalTableReferences()
    {
        LootTable foo = editFoo((table, context) -> {
            EditableLootPool barTweaks = table.getPool("bar");
            barTweaks.addLootTableEntry(BAR_ID.toString(), 5, null);
            barTweaks.addLootTableEntry(BAR_ID.toString(), 2, null);
        });
        assertThat(foo.getPool("bar")).hasEntry("loot_carpenter#1").hasEntry("loot_carpenter#2");
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void customNamedTableReference()
    {
        LootTable foo = editFoo((table, context) -> {
            EditableLootPool barTweaks = table.getPool("bar");
            barTweaks.addLootTableEntry(BAR_ID.toString(), 5, "garple");
            barTweaks.addLootTableEntry(BAR_ID.toString(), 2, null);
        });
        assertThat(foo.getPool("bar")).hasEntry("loot_carpenter#1").hasEntry("garple");
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void multipleEmpties()
    {
        LootTable foo = editFoo((table, context) -> {
            EditableLootPool barTweaks = table.getPool("bar");
            barTweaks.addEmptyEntry(5, null);
            barTweaks.addEmptyEntry(2, null);
        });
        assertThat(foo.getPool("bar")).hasEntry("loot_carpenter#1").hasEntry("loot_carpenter#2");
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void customNamedEmpty()
    {
        LootTable foo = editFoo((table, context) -> {
            EditableLootPool barTweaks = table.getPool("bar");
            barTweaks.addEmptyEntry(5, "garple");
            barTweaks.addEmptyEntry(2, null);
        });
        assertThat(foo.getPool("bar")).hasEntry("loot_carpenter#1").hasEntry("garple");
    }
}
