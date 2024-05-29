package daomephsta.loot_carpenter.test.zenscript.api.entry;

import static daomephsta.loot_carpenter.test.support.assertion.LootCarpenterAssertions.assertThat;

import daomephsta.loot_carpenter.test.support.TestsBase;
import daomephsta.loot_carpenter.test.support.mixin.condition.TestKilledByPlayerAccessors;
import daomephsta.loot_carpenter.zenscript.api.ZenLootCondition;
import io.github.daomephsta.saddle.engine.SaddleTest;
import io.github.daomephsta.saddle.engine.SaddleTest.LoadPhase;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.conditions.KilledByPlayer;


public class EmptyEntryAdditionTests extends TestsBase
{
    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void addEmptyEntry()
    {
        LootTable foo = editFoo((table, context) -> {
            table.getPool("bar").addEmptyEntry(2, "corge");
        });
        assertThat(foo.getPool("bar")).extractEntry("corge")
            .hasWeight(2)
            .hasNoLootConditions()
            .isEmptyEntry();
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void addEmptyEntryWithQuality()
    {
        LootTable foo = editFoo((table, context) -> {
            table.getPool("bar").addEmptyEntry(2, 3, "corge");
        });
        assertThat(foo.getPool("bar")).extractEntry("corge")
            .hasWeight(2)
            .hasQuality(3)
            .hasNoLootConditions()
            .isEmptyEntry();
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void addEmptyEntryWithCondition()
    {
        LootTable foo = editFoo((table, context) -> {
            table.getPool("bar").addEmptyEntry(2, 3, 
                new ZenLootCondition[] { loot.conditions.killedByPlayer() }, "corge");
        });
        assertThat(foo.getPool("bar")).extractEntry("corge")
            .hasWeight(2)
            .hasQuality(3)
            .hasMatchingCondition(
                condition -> condition instanceof KilledByPlayer && !((TestKilledByPlayerAccessors) condition).isInverse(),
                "KilledByPlayer()")
            .isEmptyEntry();
    }
}