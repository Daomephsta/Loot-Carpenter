package daomephsta.loot_carpenter.test.support.mixin.entry;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.item.Item;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.functions.LootFunction;


@Mixin(LootEntryItem.class)
public interface TestLootEntryItemAccessors
{
    @Accessor
    public Item getItem();

    @Accessor
    public LootFunction[] getFunctions();
}
