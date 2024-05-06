package daomephsta.zenloot.zenscript.api;

import java.util.Arrays;
import java.util.stream.Stream;

import crafttweaker.annotations.ZenRegister;
import daomephsta.zenloot.ZenLoot;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import stanhebben.zenscript.annotations.ZenClass;

@ZenRegister
@ZenClass(ZenLoot.ID + ".LootCondition")
public interface ZenLootCondition
{
    public static final ZenLootCondition INVALID = new ZenLootCondition()
    {
        @Override
        public String toString()
        {
            return "Invalid loot condition";
        }
    };

    public static LootCondition[] toVanilla(ZenLootCondition[] conditions)
    {
        return streamToVanilla(conditions)
            .toArray(LootCondition[]::new);
    }

    public static Stream<LootCondition> streamToVanilla(ZenLootCondition[] conditions)
    {
        return Arrays.stream(conditions)
            .filter(LootCondition.class::isInstance)
            .map(LootCondition.class::cast);
    }

    public static Stream<ZenLootCondition> streamFromVanilla(LootCondition[] conditions)
    {
        return Arrays.stream(conditions)
            .filter(ZenLootCondition.class::isInstance)
            .map(ZenLootCondition.class::cast);
    }
}