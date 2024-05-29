package daomephsta.loot_carpenter;

import daomephsta.loot_carpenter.zenscript.ErrorHandler;
import net.minecraft.world.storage.loot.RandomValueRange;


public class RandomValueRanges
{
    public static RandomValueRange checked(ErrorHandler errorHandler, float min, float max)
    {
        if (min > max) errorHandler.error("Minimum (%f) must be less than or equal to maximum (%f)", min, max);
        return new RandomValueRange(min, max);
    }
}
