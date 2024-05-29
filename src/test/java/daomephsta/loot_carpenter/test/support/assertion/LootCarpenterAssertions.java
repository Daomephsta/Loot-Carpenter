package daomephsta.loot_carpenter.test.support.assertion;

import daomephsta.loot_carpenter.test.support.assertion.loot.LootPoolAssert;
import daomephsta.loot_carpenter.test.support.assertion.loot.entry.LootEntryAssert;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootPool;


public class LootCarpenterAssertions
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
