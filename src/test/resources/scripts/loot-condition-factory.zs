loot.conditions.randomChance(0.21);
loot.conditions.randomChanceWithLooting(0.35, 1.2);
loot.conditions.killedByPlayer();
loot.conditions.killedByNonPlayer();

loot.tables.editTable("zenloot_test:foo", function(table, context) {
	val bar = table.getPool("bar");
	bar.addItemEntry(<minecraft:stick>, 1, 0, [],
	[
		loot.conditions.zenscript(function(random, context)
		{
			return random.nextBoolean();
		})
	]);
});