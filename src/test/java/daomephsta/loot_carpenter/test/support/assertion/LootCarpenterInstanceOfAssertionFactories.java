package daomephsta.loot_carpenter.test.support.assertion;

import org.assertj.core.api.InstanceOfAssertFactory;

import daomephsta.loot_carpenter.test.support.assertion.loot.entry.LootEntryEmptyAssert;
import daomephsta.loot_carpenter.test.support.assertion.loot.entry.LootEntryItemAssert;
import daomephsta.loot_carpenter.test.support.assertion.loot.entry.LootEntryTableAssert;
import net.minecraft.world.storage.loot.LootEntryEmpty;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootEntryTable;


public class LootCarpenterInstanceOfAssertionFactories
{
    public static final InstanceOfAssertFactory<LootEntryItem, LootEntryItemAssert> LOOT_ENTRY_ITEM = new InstanceOfAssertFactory<>(
        LootEntryItem.class, LootEntryItemAssert::new);
    public static final InstanceOfAssertFactory<LootEntryTable, LootEntryTableAssert> LOOT_ENTRY_TABLE = new InstanceOfAssertFactory<>(
        LootEntryTable.class, LootEntryTableAssert::new);
    public static final InstanceOfAssertFactory<LootEntryEmpty, LootEntryEmptyAssert> LOOT_ENTRY_EMPTY = new InstanceOfAssertFactory<>(
        LootEntryEmpty.class, LootEntryEmptyAssert::new);
}
