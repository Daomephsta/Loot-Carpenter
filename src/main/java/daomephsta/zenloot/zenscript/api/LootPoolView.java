package daomephsta.zenloot.zenscript.api;

import java.util.List;

import crafttweaker.annotations.ZenRegister;
import daomephsta.zenloot.ZenLoot;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass(ZenLoot.ID + ".LootPoolView")
public interface LootPoolView
{
    @ZenMethod
    @ZenGetter("conditions")
    public List<ZenLootCondition> getConditions();
}
