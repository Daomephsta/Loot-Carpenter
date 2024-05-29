package daomephsta.loot_carpenter.duck;

import daomephsta.loot_carpenter.LootCarpenter;
import net.minecraft.util.ResourceLocation;

public interface LootOriginAwareContainer
{
    static final String NBT_KEY = LootCarpenter.ID + ":loot_origin";

    public ResourceLocation loot_carpenter$getLootOrigin();
}
