package daomephsta.loot_carpenter.test.zenscript.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.InstanceOfAssertFactories.type;

import java.util.Map;

import org.assertj.core.api.Condition;

import com.google.common.collect.ImmutableMap;

import daomephsta.loot_carpenter.test.support.TestUtils;
import daomephsta.loot_carpenter.test.support.TestErrorHandler.LootCarpenterException;
import daomephsta.loot_carpenter.test.support.mixin.condition.TestEntityHasPropertyAccessors;
import daomephsta.loot_carpenter.test.support.mixin.condition.TestEntityOnFireAccessors;
import daomephsta.loot_carpenter.test.support.mixin.condition.TestKilledByPlayerAccessors;
import daomephsta.loot_carpenter.test.support.mixin.function.TestSetCountAccessors;
import daomephsta.loot_carpenter.zenscript.api.ZenLootCondition;
import daomephsta.loot_carpenter.zenscript.api.ZenLootFunction;
import daomephsta.loot_carpenter.zenscript.impl.JsonMapConversions;
import io.github.daomephsta.saddle.engine.SaddleTest;
import io.github.daomephsta.saddle.engine.SaddleTest.LoadPhase;
import net.minecraft.world.storage.loot.LootContext.EntityTarget;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.EntityHasProperty;
import net.minecraft.world.storage.loot.conditions.KilledByPlayer;
import net.minecraft.world.storage.loot.functions.SetCount;
import net.minecraft.world.storage.loot.functions.Smelt;
import net.minecraft.world.storage.loot.properties.EntityOnFire;
import net.minecraft.world.storage.loot.properties.EntityProperty;


public class JsonMapConversionTests
{
    private static JsonMapConversions.Impl jsonMapConversions = TestUtils.testInstance(JsonMapConversions.Impl.class);

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void parseSimpleCondition()
    {
        Map<String, Object> json = ImmutableMap.of("condition", "minecraft:killed_by_player");
        assertThat(jsonMapConversions.asLootCondition(json)).isNotEqualTo(ZenLootCondition.INVALID)
            .asInstanceOf(type(KilledByPlayer.class))
            .satisfies(new Condition<>(x -> !((TestKilledByPlayerAccessors) x).isInverse(), "KilledByPlayer()"));
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void parseNestingCondition()
    {
        Map<String, Object> json = ImmutableMap.of("condition", "minecraft:entity_properties", "entity", "this",
            "properties", ImmutableMap.of("on_fire", true));
        assertThat(jsonMapConversions.asLootCondition(json)).isNotEqualTo(ZenLootCondition.INVALID)
            .asInstanceOf(type(EntityHasProperty.class))
            .satisfies(new Condition<>(entityHasProperty ->
            {
                if (((TestEntityHasPropertyAccessors) entityHasProperty).getTarget() != EntityTarget.THIS) return false;
                EntityProperty[] properties = ((TestEntityHasPropertyAccessors) entityHasProperty).getProperties();
                if (properties.length != 1) return false;
                if (properties[0] instanceof EntityOnFire)
                    return ((TestEntityOnFireAccessors) properties[0]).isOnFire();
                return false;
            }, "EntityHasProperty(on_fire = true)"));
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void parseMalformedCondition()
    {
        Map<String, Object> json = ImmutableMap.of("condition", "garBaGe");
        assertThatThrownBy(() -> jsonMapConversions.asLootCondition(json)).isInstanceOf(LootCarpenterException.class)
            .hasMessage("Unknown condition 'minecraft:garbage'");
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void parseSimpleFunction()
    {
        Map<String, Object> json = ImmutableMap.of("function", "minecraft:furnace_smelt");
        assertThat(jsonMapConversions.asLootFunction(json)).isNotEqualTo(ZenLootFunction.INVALID)
            .isInstanceOf(Smelt.class);
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void parseNestingFunction()
    {
        Map<String, Object> json = ImmutableMap.of("function", "minecraft:set_count", "count",
            ImmutableMap.of("min", 0, "max", 2));
        assertThat(jsonMapConversions.asLootFunction(json)).isNotEqualTo(ZenLootFunction.INVALID)
            .asInstanceOf(type(SetCount.class))
            .satisfies(new Condition<>(setCount ->
            {
                RandomValueRange countRange = ((TestSetCountAccessors) setCount).getCountRange();
                return countRange.getMin() == 0F && countRange.getMax() == 2F;
            }, "SetCount(min = 0, max = 2)"));
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void parseMalformedFunction()
    {
        Map<String, Object> json = ImmutableMap.of("function", "garBaGe");
        assertThatThrownBy(() -> jsonMapConversions.asLootFunction(json)).isInstanceOf(LootCarpenterException.class)
            .hasMessage("Unknown function 'minecraft:garbage'");
    }
}