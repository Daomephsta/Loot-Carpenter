package daomephsta.zenloot.test.zenscript.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import daomephsta.zenloot.test.support.TestErrorHandler.ZenLootException;
import daomephsta.zenloot.test.support.TestsBase;
import daomephsta.zenloot.zenscript.api.MutableZenLootPool;
import daomephsta.zenloot.zenscript.api.MutableZenLootTable;
import io.github.daomephsta.saddle.engine.SaddleTest;
import io.github.daomephsta.saddle.engine.SaddleTest.LoadPhase;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.RandomValueRange;

public class MutableZenLootTableTests extends TestsBase
{
    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void getExistingPool()
    {
        assertThat(fooOriginal.getPool("bar")).isNotNull();

        editFoo((table, context) -> table.getPool("bar"));
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void getNonExistentPool()
    {
        assertThatThrownBy(() -> editFoo((table, context) -> table.getPool("quuz")))
        .isInstanceOf(ZenLootException.class)
        .hasMessage("No loot pool with name quuz exists in table %s!", FOO_ID);
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void removeExistingPool()
    {
        editFoo((table, context) -> table.removePool("bar"));

        assertThat(fooOriginal.getPool("bar")).isNotNull();
        LootTable fooNew = loot.tables.withEdits(fooOriginal, FOO_ID);
        assertThat(fooNew.getPool("bar")).isNull();
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void removeNonExistentPool()
    {
        assertThat(fooOriginal.getPool("quuz")).isNull();
        assertThatThrownBy(() -> editFoo((table, context) -> table.removePool("quuz")))
        .isInstanceOf(ZenLootException.class)
        .hasMessage("No loot pool with name quuz exists in table %s!", FOO_ID);
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void addPool()
    {
        assertThat(fooOriginal.getPool("qux")).isNull();

        LootTable fooNew = editFoo((table, context) -> table.addPool("qux", 1, 2, 3, 4));
        LootPool qux = fooNew.getPool("qux");
        assertThat(qux).isNotNull();
        assertThat(qux.getRolls()).extracting(RandomValueRange::getMin).isEqualTo(1.0F);
        assertThat(qux.getRolls()).extracting(RandomValueRange::getMax).isEqualTo(2.0F);
        assertThat(qux.getBonusRolls()).extracting(RandomValueRange::getMin).isEqualTo(3.0F);
        assertThat(qux.getBonusRolls()).extracting(RandomValueRange::getMax).isEqualTo(4.0F);
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void poolWrapperCaching()
    {
        editFoo((table, context) ->
        {
            MutableZenLootTable fooTweaks = table;
            assertThat(fooTweaks.getPool("bar")).isNotNull();
            assertThat(fooTweaks.getPool("bar"))
            .withFailMessage(
                "Different invocations of getPool() returned different objects for the pool named 'bar'")
            .isEqualTo(fooTweaks.getPool("bar"));
            MutableZenLootPool qux = fooTweaks.addPool("qux", 1, 1, 0, 0);
            assertThat(fooTweaks.getPool("qux")).withFailMessage(
                "Wrapper returned by addPool() for added pool 'qux' was not returned by subsequent invocation of getPool()")
            .isEqualTo(qux);
        });
    }
}
