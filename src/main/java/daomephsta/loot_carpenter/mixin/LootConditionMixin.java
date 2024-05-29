package daomephsta.loot_carpenter.mixin;

import org.spongepowered.asm.mixin.Mixin;

import daomephsta.loot_carpenter.zenscript.api.ZenLootCondition;
import net.minecraft.world.storage.loot.conditions.LootCondition;


@Mixin(LootCondition.class)
public interface LootConditionMixin extends ZenLootCondition
{
    
}