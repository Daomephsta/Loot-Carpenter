package daomephsta.zenloot.test.zenscript.api.factory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.type;

import org.assertj.core.api.Condition;

import daomephsta.zenloot.test.support.TestUtils;
import daomephsta.zenloot.test.support.TestsBase;
import daomephsta.zenloot.test.support.mixin.condition.TestKilledByPlayerAccessors;
import daomephsta.zenloot.test.support.mixin.condition.TestRandomChanceAccessors;
import daomephsta.zenloot.test.support.mixin.condition.TestRandomChanceWithLootingAccessors;
import daomephsta.zenloot.zenscript.api.ZenLootCondition;
import daomephsta.zenloot.zenscript.api.factory.LootConditionFactory;
import io.github.daomephsta.saddle.engine.SaddleTest;
import io.github.daomephsta.saddle.engine.SaddleTest.LoadPhase;
import net.minecraft.world.storage.loot.conditions.KilledByPlayer;
import net.minecraft.world.storage.loot.conditions.RandomChance;
import net.minecraft.world.storage.loot.conditions.RandomChanceWithLooting;


public class LootConditionFactoryTests extends TestsBase
{
    private final LootConditionFactory factory = TestUtils.testInstance(LootConditionFactory.class);

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void randomChance()
    {
        assertThat(factory.randomChance(0.21F)).isNotEqualTo(ZenLootCondition.INVALID)
            .asInstanceOf(type(RandomChance.class))
            .satisfies(new Condition<>(rc -> ((TestRandomChanceAccessors) rc).getChance() == 0.21F, "RandomChance(0.21)"));
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void randomChanceWithLooting()
    {
        assertThat(factory.randomChanceWithLooting(0.35F, 1.2F)).isNotEqualTo(ZenLootCondition.INVALID)
            .asInstanceOf(type(RandomChanceWithLooting.class))
            .satisfies(new Condition<>(rcwl -> ((TestRandomChanceWithLootingAccessors) rcwl).getChance() == 0.35F &&
                ((TestRandomChanceWithLootingAccessors) rcwl).getLootingMultiplier() == 1.2F,
                "RandomChanceWithLooting(0.35, 1.2)"));
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void killedByPlayer()
    {
        assertThat(factory.killedByPlayer()).isNotEqualTo(ZenLootCondition.INVALID)
            .asInstanceOf(type(KilledByPlayer.class))
            .satisfies(new Condition<>(condition -> condition instanceof KilledByPlayer && !((TestKilledByPlayerAccessors) condition).isInverse(), "KilledByPlayer()"));
    }

    @SaddleTest(loadPhase = LoadPhase.PRE_INIT)
    public void killedByNonPlayer()
    {
        assertThat(factory.killedByNonPlayer()).isNotEqualTo(ZenLootCondition.INVALID)
            .asInstanceOf(type(KilledByPlayer.class))
            .satisfies(new Condition<>(condition -> condition instanceof KilledByPlayer && ((TestKilledByPlayerAccessors) condition).isInverse(), "KilledByNonPlayer()"));
    }
}