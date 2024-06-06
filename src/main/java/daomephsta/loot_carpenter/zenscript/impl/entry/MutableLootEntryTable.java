package daomephsta.loot_carpenter.zenscript.impl.entry;

import java.util.List;

import daomephsta.loot_shared.mixin.LootEntryTableAccessors;
import daomephsta.loot_shared.utility.loot.LootConditions;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootEntryTable;
import net.minecraft.world.storage.loot.conditions.LootCondition;


public class MutableLootEntryTable extends AbstractMutableLootEntry
{
    private ResourceLocation delegateTableId;

    MutableLootEntryTable(LootEntryTable entry)
    {
        super(entry);
        this.delegateTableId = ((LootEntryTableAccessors) entry).getTable();
    }

    public MutableLootEntryTable(String name, int weight, int quality, LootCondition[] conditions,
        ResourceLocation delegateTableId)
    {
        super(name, weight, quality, conditions);
        this.delegateTableId = delegateTableId;
    }

    public MutableLootEntryTable(String name, int weight, int quality, List<LootCondition> conditions,
        ResourceLocation delegateTableId)
    {
        super(name, weight, quality, conditions);
        this.delegateTableId = delegateTableId;
    }

    @Override
    public MutableLootEntryTable deepClone()
    {
        return new MutableLootEntryTable(getName(), getWeight(), getQuality(),
            LootConditions.deepClone(getConditions()), delegateTableId);
    }

    @Override
    public LootEntryTable toImmutable()
    {
        return new LootEntryTable(delegateTableId, getWeight(), getQuality(),
            getConditions().toArray(LootConditions.NONE), getName());
    }

    public ResourceLocation getDelegateTableId()
    {
        return delegateTableId;
    }

    public void setDelegateTableId(ResourceLocation delegateTableId)
    {
        this.delegateTableId = delegateTableId;
    }
}
