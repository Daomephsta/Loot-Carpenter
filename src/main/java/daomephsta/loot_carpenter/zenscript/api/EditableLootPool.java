package daomephsta.loot_carpenter.zenscript.api;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import daomephsta.loot_carpenter.LootCarpenter;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import stanhebben.zenscript.annotations.ZenSetter;

@ZenRegister
@ZenClass(LootCarpenter.ZEN_PACKAGE + ".EditableLootPool")
public interface EditableLootPool extends LootPoolView
{
    @ZenMethod
    public void removeEntry(String name);

    @ZenMethod
    public void addItemEntry(IItemStack stack, int weight, @Optional String name);

    @ZenMethod
    public void addItemEntry(IItemStack stack, int weight, int quality, @Optional String name);

    @ZenMethod
    public void addItemEntry(IItemStack stack, int weight, int quality,
        ZenLootFunction[] functions, ZenLootCondition[] conditions, @Optional String name);

    @ZenMethod
    public void addLootTableEntry(String tableName, int weight, @Optional String name);

    @ZenMethod
    public void addLootTableEntry(String tableName, int weight, int quality, @Optional String name);

    @ZenMethod
    public void addLootTableEntry(String tableName, int weight, int quality,
        ZenLootCondition[] conditions, @Optional String name);

    @ZenMethod
    public void addEmptyEntry(int weight, @Optional String name);

    @ZenMethod
    public void addEmptyEntry(int weight, int quality, @Optional String name);

    @ZenMethod
    public void addEmptyEntry(int weight, int quality,
        ZenLootCondition[] conditions, @Optional String name);

    @ZenMethod
    @ZenSetter("conditions")
    public void setConditions(ZenLootCondition[] conditions);

    @ZenMethod
    public void clearConditions();

    @ZenMethod
    public void addConditions(ZenLootCondition[] conditions);

    @ZenMethod
    public void setRolls(float minRolls, float maxRolls);

    @ZenMethod
    public void setBonusRolls(float minBonusRolls, float maxBonusRolls);

    @ZenMethod
    public void clearEntries();
}
