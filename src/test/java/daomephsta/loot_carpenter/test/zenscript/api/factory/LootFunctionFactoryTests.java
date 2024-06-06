package daomephsta.loot_carpenter.test.zenscript.api.factory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.InstanceOfAssertFactories.type;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.assertj.core.api.Condition;

import crafttweaker.api.data.DataMap;
import crafttweaker.api.data.DataString;
import crafttweaker.api.data.IData;
import daomephsta.loot_carpenter.test.support.TestErrorHandler.LootCarpenterException;
import daomephsta.loot_carpenter.test.support.TestUtils;
import daomephsta.loot_carpenter.test.support.TestsBase;
import daomephsta.loot_carpenter.test.support.mixin.function.TestEnchantRandomlyAccessors;
import daomephsta.loot_carpenter.test.support.mixin.function.TestEnchantWithLevelsAccessors;
import daomephsta.loot_carpenter.test.support.mixin.function.TestLootingEnchantBonusAccessors;
import daomephsta.loot_carpenter.test.support.mixin.function.TestSetCountAccessors;
import daomephsta.loot_carpenter.test.support.mixin.function.TestSetDamageAccessors;
import daomephsta.loot_carpenter.test.support.mixin.function.TestSetMetadataAccessors;
import daomephsta.loot_carpenter.test.support.mixin.function.TestSetNBTAccessors;
import daomephsta.loot_carpenter.zenscript.api.ZenLootCondition;
import daomephsta.loot_carpenter.zenscript.api.ZenLootFunction;
import daomephsta.loot_carpenter.zenscript.api.factory.LootFunctionFactory;
import io.github.daomephsta.saddle.engine.SaddleTest;
import io.github.daomephsta.saddle.engine.SaddleTest.LoadPhase;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.KilledByPlayer;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.conditions.RandomChance;
import net.minecraft.world.storage.loot.functions.EnchantRandomly;
import net.minecraft.world.storage.loot.functions.EnchantWithLevels;
import net.minecraft.world.storage.loot.functions.LootingEnchantBonus;
import net.minecraft.world.storage.loot.functions.SetCount;
import net.minecraft.world.storage.loot.functions.SetDamage;
import net.minecraft.world.storage.loot.functions.SetMetadata;
import net.minecraft.world.storage.loot.functions.SetNBT;
import net.minecraft.world.storage.loot.functions.Smelt;


public class LootFunctionFactoryTests extends TestsBase
{
    private final LootFunctionFactory factory = TestUtils.testInstance(LootFunctionFactory.class);

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void enchantRandomly()
    {
        assertThat(factory.enchantRandomly(new String[] { "minecraft:thorns" })).isNotEqualTo(ZenLootFunction.INVALID)
            .asInstanceOf(type(EnchantRandomly.class))
            .satisfies(new Condition<>(enchantRandomly ->
            {
                List<Enchantment> enchantments = ((TestEnchantRandomlyAccessors) enchantRandomly).getEnchantments();
                return enchantments.size() == 1
                    && enchantments.get(0).getRegistryName().equals(new ResourceLocation("minecraft:thorns"));
            }, "LootingEnchantBonus([1, 3])"));
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void enchantRandomlyInvalidId()
    {
        assertThatThrownBy(() -> factory.enchantRandomly(new String[] { "minecraft:garbage" }))
            .isInstanceOf(LootCarpenterException.class)
            .hasMessage("minecraft:garbage is not a valid enchantment id");
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void enchantWithLevels()
    {
        assertThat(factory.enchantWithLevels(11, 26, false)).isNotEqualTo(ZenLootFunction.INVALID)
            .asInstanceOf(type(EnchantWithLevels.class))
            .satisfies(new Condition<>(enchantWithLevels ->
            {
                RandomValueRange levelRange = ((TestEnchantWithLevelsAccessors) enchantWithLevels).getLevelRange();
                return levelRange.getMin() == 11.0F && levelRange.getMax() == 26.0F
                    && !((TestEnchantWithLevelsAccessors) enchantWithLevels).isTreasure();
            }, "EnchantWithLevels([11, 26], isTreasure: false)"));
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void lootingEnchantBonus()
    {
        assertThat(factory.lootingEnchantBonus(1, 2, 3)).isNotEqualTo(ZenLootFunction.INVALID)
            .asInstanceOf(type(LootingEnchantBonus.class))
            .satisfies(new Condition<>(lootingEnchantBonus ->
            {
                RandomValueRange bonusRange = ((TestLootingEnchantBonusAccessors) lootingEnchantBonus).getBonusRange();
                return bonusRange.getMin() == 1.0F && bonusRange.getMax() == 2.0F
                    && ((TestLootingEnchantBonusAccessors) lootingEnchantBonus).getLimit() == 3;
            }, "LootingEnchantBonus([1, 2], limit: 3)"));
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void setCount()
    {
        assertThat(factory.setCount(1, 3)).isNotEqualTo(ZenLootFunction.INVALID)
            .asInstanceOf(type(SetCount.class))
            .satisfies(new Condition<>(setCount ->
            {
                RandomValueRange countRange = ((TestSetCountAccessors) setCount).getCountRange();
                return countRange.getMin() == 1.0F && countRange.getMax() == 3.0F;
            }, "SetCount([1, 3])"));
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void setDamage()
    {
        assertThat(factory.setDamage(0.2F, 0.8F)).isNotEqualTo(ZenLootFunction.INVALID)
            .asInstanceOf(type(SetDamage.class))
            .satisfies(new Condition<>(setDamage ->
            {
                RandomValueRange damageRange = ((TestSetDamageAccessors) setDamage).getDamageRange();
                return damageRange.getMin() == 0.2F && damageRange.getMax() == 0.8F;
            }, "SetDamage([0.2, 0.8])"));
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void setDamageInvalidRange()
    {
        assertThatThrownBy(() -> factory.setDamage(0.2F, 1.8F)).isInstanceOf(LootCarpenterException.class)
            .hasMessage("Items cannot recieve more than 100% damage!");
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void setMetadata()
    {
        assertThat(factory.setMetadata(23, 45)).isNotEqualTo(ZenLootFunction.INVALID)
            .asInstanceOf(type(SetMetadata.class))
            .satisfies(new Condition<>(setMetadata ->
            {
                RandomValueRange metaRange = ((TestSetMetadataAccessors) setMetadata).getMetaRange();
                return metaRange.getMin() == 23.0F && metaRange.getMax() == 45.0F;
            }, "SetMetadata([23, 45])"));
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void setNBT()
    {
        Map<String, IData> data = new HashMap<>();
        data.put("foo", new DataString("bar"));
        IData nbtData = new DataMap(data, false);

        NBTTagCompound expectedTag = new NBTTagCompound();
        expectedTag.setString("foo", "bar");

        assertThat(factory.setNBT(nbtData)).isNotEqualTo(ZenLootFunction.INVALID)
            .asInstanceOf(type(SetNBT.class))
            .satisfies(new Condition<>(setNbt -> ((TestSetNBTAccessors) setNbt).getTag().equals(expectedTag),
                "SetNBT(%s)", expectedTag));
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void setNBTNonCompound()
    {

        DataString invalidData = new DataString("bar");
        assertThatThrownBy(() -> factory.setNBT(invalidData)).isInstanceOf(LootCarpenterException.class)
            .hasMessage("Expected compound nbt tag, got %s", invalidData);
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void smelt()
    {
        assertThat(factory.smelt()).isNotEqualTo(ZenLootFunction.INVALID)
            .isInstanceOf(Smelt.class);
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void addConditions()
    {
        ZenLootFunction function = (ZenLootFunction) new Smelt(new LootCondition[] { new RandomChance(0.5F) });
        ZenLootCondition condition = (ZenLootCondition) new KilledByPlayer(false);
        function.addConditions(new ZenLootCondition[] { condition });
        assertThat(function.getConditions()).hasSize(2)
            .hasAtLeastOneElementOfType(RandomChance.class)
            .hasAtLeastOneElementOfType(KilledByPlayer.class);
    }
}