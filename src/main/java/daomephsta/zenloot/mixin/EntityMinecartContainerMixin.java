package daomephsta.zenloot.mixin;

import javax.annotation.Nullable;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import daomephsta.zenloot.duck.ZenLootGeneratedFrom;
import net.minecraft.entity.item.EntityMinecartContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants.NBT;

@Mixin(EntityMinecartContainer.class)
public class EntityMinecartContainerMixin implements ZenLootGeneratedFrom
{
    @Unique
    private ResourceLocation ZenLoot_generatedFrom;
    @Shadow
    private ResourceLocation lootTable;

    @Inject(method = "readEntityFromNBT", at = @At("HEAD"))
    private void zenloot$readNbt(NBTTagCompound nbt, CallbackInfo info)
    {
        if (nbt.hasKey(ZenLootGeneratedFrom.NBT_KEY, NBT.TAG_STRING))
            this.ZenLoot_generatedFrom = new ResourceLocation(nbt.getString(ZenLootGeneratedFrom.NBT_KEY));
    }

    @Inject(method = "writeEntityToNBT", at = @At("HEAD"))
    private void zenloot$writeNbt(NBTTagCompound nbt, CallbackInfo info)
    {
        if (ZenLoot_generatedFrom != null)
            nbt.setString(ZenLootGeneratedFrom.NBT_KEY, ZenLoot_generatedFrom.toString());
    }

    @Inject(method = "addLoot", at = @At(value = "FIELD",
        target = "Lnet/minecraft/entity/item/EntityMinecartContainer;lootTable:Lnet/minecraft/util/ResourceLocation;", opcode = Opcodes.PUTFIELD))
    private void zenloot$setGeneratedFrom(@Nullable EntityPlayer player, CallbackInfo info)
    {
        this.ZenLoot_generatedFrom = this.lootTable;
    }

    @Override
    public ResourceLocation ZenLoot_getGeneratedFrom()
    {
        return ZenLoot_generatedFrom;
    }
}
