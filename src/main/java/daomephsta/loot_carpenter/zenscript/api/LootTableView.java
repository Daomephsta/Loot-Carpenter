package daomephsta.loot_carpenter.zenscript.api;

import crafttweaker.annotations.ZenRegister;
import daomephsta.loot_carpenter.LootCarpenter;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass(LootCarpenter.ZEN_PACKAGE + ".LootTableView")
public interface LootTableView
{
    @ZenMethod
    public LootPoolView getPool(String name);
}
