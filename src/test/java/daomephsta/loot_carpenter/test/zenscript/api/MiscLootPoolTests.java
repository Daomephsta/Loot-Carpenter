package daomephsta.loot_carpenter.test.zenscript.api;

import static daomephsta.loot_carpenter.test.support.assertion.LootCarpenterAssertions.assertThat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import daomephsta.loot_carpenter.test.support.TestErrorHandler.LootCarpenterException;
import daomephsta.loot_carpenter.test.support.TestsBase;
import daomephsta.loot_carpenter.test.support.mixin.condition.TestKilledByPlayerAccessors;
import daomephsta.loot_carpenter.zenscript.api.EditableLootPool;
import daomephsta.loot_carpenter.zenscript.api.ZenLootCondition;
import daomephsta.loot_shared.mixin.LootPoolAccessors;
import io.github.daomephsta.saddle.engine.SaddleTest;
import io.github.daomephsta.saddle.engine.SaddleTest.LoadPhase;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.KilledByPlayer;


public class MiscLootPoolTests extends TestsBase
{
    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void addConditions()
    {
        System.out.println(this.getClass().getClassLoader());
        LootTable foo = editFoo((table, context) -> {
            EditableLootPool bar = table.getPool("bar");
            bar.addConditions(new ZenLootCondition[] { loot.conditions.killedByPlayer() });
        });
        assertThat(foo.getPool("bar")).hasMatchingCondition(
            condition -> condition instanceof KilledByPlayer && !((TestKilledByPlayerAccessors) condition).isInverse(),
            "KilledByPlayer()");
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void removeExistingEntry()
    {
        assertThat(barOriginal.getPool("baz").getEntry("qux")).isNotNull();
        LootTable barNew = editBar((table, context) -> {
            EditableLootPool baz = table.getPool("baz");
            baz.removeEntry("qux");
        });
        assertThat(barNew.getPool("baz").getEntry("qux")).isNull();
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void removeNonExistentEntry()
    {
        assertThat(barOriginal.getPool("baz").getEntry("quuz")).isNull();

        assertThatThrownBy(() -> editBar((table, context) -> {
            EditableLootPool baz = table.getPool("baz");
            baz.removeEntry("quuz");
        }))
            .isInstanceOf(LootCarpenterException.class)
            .hasMessage("No entry with name quuz exists in pool 'baz' of table 'loot_carpenter_test:bar'");
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void clearConditions()
    {
        assertThat(((LootPoolAccessors) barOriginal.getPool("baz")).getConditions()).isNotEmpty();
        LootTable barNew = editBar((table, context) -> {
            EditableLootPool baz = table.getPool("baz");
            baz.setConditions(new ZenLootCondition[0]);
        });
        assertThat(((LootPoolAccessors) barNew.getPool("baz")).getConditions()).isEmpty();
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void clearEntries()
    {
        assertThat(((LootPoolAccessors) barOriginal.getPool("baz")).getEntries()).isNotEmpty();
        LootTable barNew = editBar((table, context) -> {
            EditableLootPool baz = table.getPool("baz");
            baz.clearEntries();
        });
        assertThat(((LootPoolAccessors) barNew.getPool("baz")).getEntries()).isEmpty();
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void setRolls()
    {
        LootTable foo = editFoo((table, context) -> {
            EditableLootPool bar = table.getPool("bar");
            bar.setRolls(2.0F, 5.0F);
        });
        LootPool bar = foo.getPool("bar");
        assertThat(bar.getRolls()).extracting(RandomValueRange::getMin).isEqualTo(2.0F);
        assertThat(bar.getRolls()).extracting(RandomValueRange::getMax).isEqualTo(5.0F);
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void setBonusRolls()
    {
        LootTable foo = editFoo((table, context) -> {
            EditableLootPool bar = table.getPool("bar");
            bar.setBonusRolls(1.0F, 3.0F);
        });
        LootPool bar = foo.getPool("bar");
        assertThat(bar.getBonusRolls()).extracting(RandomValueRange::getMin).isEqualTo(1.0F);
        assertThat(bar.getBonusRolls()).extracting(RandomValueRange::getMax).isEqualTo(3.0F);
    }
}