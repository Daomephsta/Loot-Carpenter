package daomephsta.zenloot.zenscript.api;

import crafttweaker.annotations.ZenRegister;
import daomephsta.zenloot.ZenLoot;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass(ZenLoot.ID + ".EditableLootTable")
public interface MutableZenLootTable extends LootTableView
{
    @Override
    @ZenMethod
    public MutableZenLootPool getPool(String name);
    
    @ZenMethod
    public MutableZenLootPool addPool(String name, float minRolls, float maxRolls, float minBonusRolls, float maxBonusRolls);
    
    @ZenMethod
    public void removePool(String name);

    @ZenMethod
    public void clearPools();
}
