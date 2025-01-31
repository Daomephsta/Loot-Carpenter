package daomephsta.loot_carpenter.zenscript.impl.entry;

import java.util.List;

import daomephsta.loot_shared.utility.loot.LootConditions;
import net.minecraft.world.storage.loot.LootEntryEmpty;
import net.minecraft.world.storage.loot.conditions.LootCondition;


public class MutableLootEntryEmpty extends AbstractMutableLootEntry
{
    MutableLootEntryEmpty(LootEntryEmpty entry)
    {
        super(entry);
    }

    public MutableLootEntryEmpty(String name, int weight, int quality, LootCondition[] conditions)
    {
        super(name, weight, quality, conditions);
    }

    public MutableLootEntryEmpty(String name, int weight, int quality, List<LootCondition> conditions)
    {
        super(name, weight, quality, conditions);
    }

    @Override
    public MutableLootEntryEmpty deepClone()
    {
        return new MutableLootEntryEmpty(getName(), getWeight(), getQuality(),
            LootConditions.deepClone(getConditions()));
    }

    @Override
    public LootEntryEmpty toImmutable()
    {
        return new LootEntryEmpty(getWeight(), getQuality(), getConditions().toArray(LootConditions.NONE),
            getName());
    }
}
