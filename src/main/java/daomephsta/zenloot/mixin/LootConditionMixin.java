package daomephsta.zenloot.mixin;

import org.spongepowered.asm.mixin.Mixin;

import daomephsta.zenloot.zenscript.api.ZenLootCondition;
import net.minecraft.world.storage.loot.conditions.LootCondition;


@Mixin(LootCondition.class)
public interface LootConditionMixin extends ZenLootCondition
{
    
}