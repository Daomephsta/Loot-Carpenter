package daomephsta.zenloot.test.support.assertion;

import daomephsta.zenloot.test.support.assertion.loot.LootPoolAssert;
import daomephsta.zenloot.test.support.assertion.loot.entry.LootEntryAssert;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootPool;


public class ZenLootAssertions
{
    public static LootPoolAssert assertThat(LootPool pool)
    {
        return new LootPoolAssert(pool);
    }

    public static LootEntryAssert assertThat(LootEntry lootEntry)
    {
        return new LootEntryAssert(lootEntry);
    }
}
