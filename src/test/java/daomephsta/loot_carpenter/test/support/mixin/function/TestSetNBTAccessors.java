package daomephsta.loot_carpenter.test.support.mixin.function;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.storage.loot.functions.SetNBT;

@Mixin(SetNBT.class)
public interface TestSetNBTAccessors
{
    @Accessor
    public NBTTagCompound getTag();
}