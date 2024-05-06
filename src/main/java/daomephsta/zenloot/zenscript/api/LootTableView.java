package daomephsta.zenloot.zenscript.api;

import crafttweaker.annotations.ZenRegister;
import daomephsta.zenloot.ZenLoot;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass(ZenLoot.ID + ".LootTableView")
public interface LootTableView
{
    @ZenMethod
    public LootPoolView getPool(String name);
}
