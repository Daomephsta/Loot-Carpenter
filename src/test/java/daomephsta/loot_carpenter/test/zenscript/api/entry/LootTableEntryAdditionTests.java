package daomephsta.loot_carpenter.test.zenscript.api.entry;

import static daomephsta.loot_carpenter.test.support.assertion.LootCarpenterAssertions.assertThat;

import daomephsta.loot_carpenter.test.support.TestsBase;
import daomephsta.loot_carpenter.test.support.mixin.condition.TestKilledByPlayerAccessors;
import daomephsta.loot_carpenter.zenscript.api.ZenLootCondition;
import io.github.daomephsta.saddle.engine.SaddleTest;
import io.github.daomephsta.saddle.engine.SaddleTest.LoadPhase;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.conditions.KilledByPlayer;


public class LootTableEntryAdditionTests extends TestsBase
{
    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void addLootTableEntry()
    {
        LootTable foo = editFoo((table, context) -> {
            table.getPool("bar").addLootTableEntry(TEST_NAMESPACE + ":qux", 2, "corge");
        });
        assertThat(foo.getPool("bar")).extractEntry("corge")
            .hasWeight(2)
            .hasNoLootConditions()
            .asLootTableEntry()
            .spawnsFromTable(TEST_NAMESPACE + ":qux");
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void addLootTableEntryWithQuality()
    {
        LootTable foo = editFoo((table, context) -> {
            table.getPool("bar").addLootTableEntry(TEST_NAMESPACE + ":qux", 2, 3, "corge");
        });
        assertThat(foo.getPool("bar")).extractEntry("corge")
            .hasWeight(2)
            .hasQuality(3)
            .hasNoLootConditions()
            .asLootTableEntry()
            .spawnsFromTable(TEST_NAMESPACE + ":qux");
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void addLootTableEntryWithCondition()
    {
        LootTable foo = editFoo((table, context) -> {
            table.getPool("bar").addLootTableEntry(TEST_NAMESPACE + ":qux", 2, 3,
                new ZenLootCondition[] { loot.conditions.killedByPlayer() }, "corge");
        });
        assertThat(foo.getPool("bar")).extractEntry("corge")
            .hasWeight(2)
            .hasQuality(3)
            .hasMatchingCondition(
                condition -> condition instanceof KilledByPlayer && !((TestKilledByPlayerAccessors) condition).isInverse(),
                "KilledByPlayer()")
            .asLootTableEntry()
            .spawnsFromTable(TEST_NAMESPACE + ":qux");
    }
}