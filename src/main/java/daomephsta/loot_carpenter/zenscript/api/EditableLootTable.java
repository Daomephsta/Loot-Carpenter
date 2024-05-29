package daomephsta.loot_carpenter.zenscript.api;

import crafttweaker.annotations.ZenRegister;
import daomephsta.loot_carpenter.LootCarpenter;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass(LootCarpenter.ZEN_PACKAGE + ".EditableLootTable")
public interface EditableLootTable extends LootTableView
{
    @Override
    @ZenMethod
    public EditableLootPool getPool(String name);
    
    @ZenMethod
    public EditableLootPool addPool(String name, float minRolls, float maxRolls, float minBonusRolls, float maxBonusRolls);
    
    @ZenMethod
    public void removePool(String name);

    @ZenMethod
    public void removeAllPools();
}
