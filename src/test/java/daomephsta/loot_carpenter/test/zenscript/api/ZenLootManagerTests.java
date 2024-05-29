package daomephsta.loot_carpenter.test.zenscript.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import daomephsta.loot_carpenter.test.support.TestsBase;
import daomephsta.loot_carpenter.test.support.TestErrorHandler.LootCarpenterException;
import daomephsta.loot_carpenter.zenscript.api.EditableLootTable;
import io.github.daomephsta.saddle.engine.SaddleTest;
import io.github.daomephsta.saddle.engine.SaddleTest.LoadPhase;
import net.minecraft.util.ResourceLocation;


public class ZenLootManagerTests extends TestsBase
{
    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void getTableCheckExisting()
    {
        editFoo((table, context) -> {});
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void getTableCheckNonExistent()
    {
        ResourceLocation nonExistentTableId = new ResourceLocation(TEST_NAMESPACE, "non_existent_table");

        assertThatThrownBy(() -> loot.tables.editTable(nonExistentTableId.toString(), (table, context) -> {}))
        .isInstanceOf(LootCarpenterException.class)
        .hasMessage("No loot table with name %s exists!", nonExistentTableId);
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void tableWrapperCaching()
    {
        EditableLootTable[] foos = {null, null};
        editFoo((table, context) ->
        {
            foos[0] = table;
        });
        editFoo((table, context) ->
        {
            foos[1] = table;
        });
        assertThat(foos[0]).isNotNull().isEqualTo(foos[1]);
    }

    public void newTable()
    {
        loot.tables.newTable(TEST_NAMESPACE + ":qux", (table, context) -> assertThat(table).isNotNull());
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void newTableCollision()
    {
        String existingTableId = FOO_ID.toString();
        assertThatThrownBy(() -> loot.tables.newTable(existingTableId, (table, context) -> {}))
            .isInstanceOf(LootCarpenterException.class)
            .hasMessage("Table name '%s' already in use", existingTableId);
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void newTableGettable()
    {
        String tableName = TEST_NAMESPACE + ":qux";
        loot.tables.newTable(tableName, (table, context) -> {});
        loot.tables.editTable(tableName, (table, context) -> {});
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void newTableWarnMinecraftNamespace()
    {
        String implicitMinecraftNamespace = "qux";
        String explicitMinecraftNamespace = "minecraft:quuz";
        assertThatThrownBy(() -> loot.tables.newTable(implicitMinecraftNamespace, (table, context) -> {}))
            .isInstanceOf(LootCarpenterException.class)
            .hasMessage("Table name '%s' implicitly uses the minecraft namespace, this is discouraged",
                implicitMinecraftNamespace);
        assertThatThrownBy(() -> loot.tables.newTable(explicitMinecraftNamespace, (table, context) -> {}))
            .isInstanceOf(LootCarpenterException.class)
            .hasMessage("Table name '%s' explicitly uses the minecraft namespace, this is discouraged",
                explicitMinecraftNamespace);
    }
}
