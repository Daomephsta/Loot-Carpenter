package daomephsta.zenloot.test.zenscript.api.entry;

import static daomephsta.zenloot.test.support.TestUtils.iitemstack;
import static daomephsta.zenloot.test.support.assertion.ZenLootAssertions.assertThat;

import com.google.common.collect.ImmutableMap;

import crafttweaker.api.data.DataMap;
import crafttweaker.api.data.DataString;
import crafttweaker.api.data.IData;
import daomephsta.zenloot.test.support.TestsBase;
import daomephsta.zenloot.test.support.mixin.condition.TestKilledByPlayerAccessors;
import daomephsta.zenloot.test.support.mixin.function.TestSetCountAccessors;
import daomephsta.zenloot.test.support.mixin.function.TestSetDamageAccessors;
import daomephsta.zenloot.test.support.mixin.function.TestSetMetadataAccessors;
import daomephsta.zenloot.test.support.mixin.function.TestSetNBTAccessors;
import daomephsta.zenloot.zenscript.api.ZenLootCondition;
import daomephsta.zenloot.zenscript.api.ZenLootFunction;
import io.github.daomephsta.saddle.engine.SaddleTest;
import io.github.daomephsta.saddle.engine.SaddleTest.LoadPhase;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.KilledByPlayer;
import net.minecraft.world.storage.loot.functions.SetCount;
import net.minecraft.world.storage.loot.functions.SetDamage;
import net.minecraft.world.storage.loot.functions.SetMetadata;
import net.minecraft.world.storage.loot.functions.SetNBT;


public class ItemEntryAdditionTests extends TestsBase
{
    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void addItemEntry()
    {
        LootTable foo = editFoo((table, context) -> {
            table.getPool("bar").addItemEntry(iitemstack(Items.APPLE), 2, "qux");
        });
        assertThat(foo.getPool("bar")).extractEntry("qux")
            .hasWeight(2)
            .hasNoLootConditions()
            .asItemEntry()
            .spawnsItem(Items.APPLE)
            .hasNoLootFunctions();
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void addItemEntryWithQuality()
    {
        LootTable foo = editFoo((table, context) -> {
            table.getPool("bar").addItemEntry(iitemstack(Items.APPLE), 2, 3, "qux");
        });
        assertThat(foo.getPool("bar")).extractEntry("qux")
            .hasWeight(2)
            .hasQuality(3)
            .hasNoLootConditions()
            .asItemEntry()
            .spawnsItem(Items.APPLE)
            .hasNoLootFunctions();
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void addItemEntryWithCondition()
    {
        LootTable foo = editFoo((table, context) -> {
            table.getPool("bar").addItemEntry(iitemstack(Items.BAKED_POTATO), 2, 3, new ZenLootFunction[0],
                new ZenLootCondition[] { loot.conditions.killedByPlayer() }, "qux");
        });
        assertThat(foo.getPool("bar")).extractEntry("qux")
            .hasWeight(2)
            .hasQuality(3)
            .hasMatchingCondition(
                condition -> condition instanceof KilledByPlayer && !((TestKilledByPlayerAccessors) condition).isInverse(),
                "KilledByPlayer()")
            .asItemEntry()
            .spawnsItem(Items.BAKED_POTATO)
            .hasNoLootFunctions();
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void addItemEntryWithImplicitSetCount()
    {
        LootTable foo = editFoo((table, context) -> {
            table.getPool("bar").addItemEntry(iitemstack(Items.ARROW, 3), 2, "qux");
        });
        int expectedCount = 3;
        assertThat(foo.getPool("bar")).extractEntry("qux")
            .hasWeight(2)
            .hasNoLootConditions()
            .asItemEntry()
            .spawnsItem(Items.ARROW)
            .hasMatchingFunction(function ->
            {
                if (function instanceof SetCount)
                {
                    RandomValueRange countRange = ((TestSetCountAccessors) function).getCountRange();
                    return countRange.getMin() == expectedCount && countRange.getMax() == expectedCount;
                }
                return false;
            }, "SetCount(%d)", expectedCount);
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void addItemEntryWithExplicitSetCount()
    {
        LootTable foo = editFoo((table, context) -> {
            table.getPool("bar").addItemEntry(iitemstack(Items.ARROW), 2, 1,
            new ZenLootFunction[] { loot.functions.setCount(3, 3) }, new ZenLootCondition[0],
            "qux");
        });
        int expectedCount = 3;
        assertThat(foo.getPool("bar")).extractEntry("qux")
            .hasWeight(2)
            .hasQuality(1)
            .hasNoLootConditions()
            .asItemEntry()
            .spawnsItem(Items.ARROW)
            .hasMatchingFunction(function ->
            {
                if (function instanceof SetCount)
                {
                    RandomValueRange countRange = ((TestSetCountAccessors) function).getCountRange();
                    return countRange.getMin() == expectedCount && countRange.getMax() == expectedCount;
                }
                return false;
            }, "SetCount(%d)", expectedCount);
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void addItemEntryWithImplicitSetDamage()
    {
        LootTable foo = editFoo((table, context) -> {
            @SuppressWarnings("deprecation")
            int damage = Items.BOW.getMaxDamage() / 2;
            // set empty tag to work around weird Mojang code where items without NBT are undamageable
            table.getPool("bar").addItemEntry(iitemstack(Items.BOW, 1, damage).withTag(DataMap.EMPTY, true), 2, "qux");
        });
        float expectedDamage = 0.5F;
        assertThat(foo.getPool("bar")).extractEntry("qux")
            .hasWeight(2)
            .hasNoLootConditions()
            .asItemEntry()
            .spawnsItem(Items.BOW)
            .hasMatchingFunction(function ->
            {
                if (function instanceof SetDamage)
                {
                    RandomValueRange damageRange = ((TestSetDamageAccessors) function).getDamageRange();
                    return damageRange.getMin() == expectedDamage && damageRange.getMax() == expectedDamage;
                }
                return false;
            }, "SetDamage(%f)", expectedDamage);
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void addItemEntryWithExplicitSetDamage()
    {
        LootTable foo = editFoo((table, context) -> {
            table.getPool("bar").addItemEntry(iitemstack(Items.BOW), 2, 1,
                new ZenLootFunction[] { loot.functions.setDamage(0.5F, 0.5F) },
                new ZenLootCondition[0], "qux");
        });
        float expectedDamage = 0.5F;
        assertThat(foo.getPool("bar")).extractEntry("qux")
            .hasWeight(2)
            .hasQuality(1)
            .hasNoLootConditions()
            .asItemEntry()
            .spawnsItem(Items.BOW)
            .hasMatchingFunction(function ->
            {
                if (function instanceof SetDamage)
                {
                    RandomValueRange damageRange = ((TestSetDamageAccessors) function).getDamageRange();
                    return damageRange.getMin() == expectedDamage && damageRange.getMax() == expectedDamage;
                }
                return false;
            }, "SetDamage(%f)", expectedDamage);
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void addItemEntryWithImplicitSetMetadata()
    {
        LootTable foo = editFoo((table, context) -> {
            table.getPool("bar").addItemEntry(iitemstack(Items.DYE, 1, 8), 2, "qux");
        });
        int expectedMetadata = 8;
        assertThat(foo.getPool("bar")).extractEntry("qux")
            .hasWeight(2)
            .hasNoLootConditions()
            .asItemEntry()
            .spawnsItem(Items.DYE)
            .hasMatchingFunction(function ->
            {
                if (function instanceof SetMetadata)
                {
                    RandomValueRange metaRange = ((TestSetMetadataAccessors) function).getMetaRange();
                    return metaRange.getMin() == expectedMetadata && metaRange.getMax() == expectedMetadata;
                }
                return false;
            }, "SetMetadata(%d)", expectedMetadata);
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void addItemEntryWithExplicitSetMetadata()
    {
        LootTable foo = editFoo((table, context) -> {
            table.getPool("bar").addItemEntry(iitemstack(Items.DYE), 2, 1,
                new ZenLootFunction[] { loot.functions.setMetadata(8, 8) }, new ZenLootCondition[0],
                "qux");
        });
        int expectedMetadata = 8;
        assertThat(foo.getPool("bar")).extractEntry("qux")
            .hasWeight(2)
            .hasQuality(1)
            .hasNoLootConditions()
            .asItemEntry()
            .spawnsItem(Items.DYE)
            .hasMatchingFunction(function ->
            {
                if (function instanceof SetMetadata)
                {
                    RandomValueRange metaRange = ((TestSetMetadataAccessors) function).getMetaRange();
                    return metaRange.getMin() == expectedMetadata && metaRange.getMax() == expectedMetadata;
                }
                return false;
            }, "SetMetadata(%d)", expectedMetadata);
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void addItemEntryWithImplicitSetNBT()
    {
        LootTable foo = editFoo((table, context) -> {
            table.getPool("bar").addItemEntry(iitemstack(Items.BREAD).withDisplayName("Super Bread"), 2, "qux");
        });
        NBTTagCompound expectedTag = new NBTTagCompound();
        {
            NBTTagCompound display = new NBTTagCompound();
            display.setString("Name", "Super Bread");
            expectedTag.setTag("display", display);
        }
        assertThat(foo.getPool("bar")).extractEntry("qux")
            .hasWeight(2)
            .hasNoLootConditions()
            .asItemEntry()
            .spawnsItem(Items.BREAD)
            .hasMatchingFunction(function ->
            {
                if (function instanceof SetNBT)
                    return expectedTag.equals(((TestSetNBTAccessors) function).getTag());
                return false;
            }, "SetNBT(%s)", expectedTag);
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void addItemEntryWithExplicitSetNBT()
    {
        LootTable foo = editFoo((table, context) -> {
            IData displayData = new DataMap(
                ImmutableMap.<String, IData>builder().put("Name", new DataString("Super Bread")).build(), true);
            IData nbtData = new DataMap(ImmutableMap.<String, IData>builder().put("display", displayData).build(),
                true);
            table.getPool("bar").addItemEntry(iitemstack(Items.BREAD), 2, 1,
                new ZenLootFunction[] { loot.functions.setNBT(nbtData) }, new ZenLootCondition[0],
                "qux");
        });
        NBTTagCompound expectedTag = new NBTTagCompound();
        {
            NBTTagCompound display = new NBTTagCompound();
            display.setString("Name", "Super Bread");
            expectedTag.setTag("display", display);
        }
        assertThat(foo.getPool("bar")).extractEntry("qux")
            .hasWeight(2)
            .hasQuality(1)
            .hasNoLootConditions()
            .asItemEntry()
            .spawnsItem(Items.BREAD)
            .hasMatchingFunction(function ->
            {
                if (function instanceof SetNBT)
                    return expectedTag.equals(((TestSetNBTAccessors) function).getTag());
                return false;
            }, "SetNBT(%s)", expectedTag);
    }
}