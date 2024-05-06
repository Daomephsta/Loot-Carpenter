loot.functions.enchantRandomly(["minecraft:thorns"]);
loot.functions.enchantWithLevels(11, 26, false);
loot.functions.lootingEnchantBonus(1, 2, 3);
loot.functions.setCount(1, 3);
loot.functions.setDamage(0.2, 0.8);
loot.functions.setMetadata(23, 45);
loot.functions.setNBT({"foo": "bar"});
loot.functions.smelt();
loot.functions.smelt().addConditions([loot.conditions.killedByPlayer()]);

loot.tables.editTable("zenloot_test:foo", function(table, context) {
	val bar = table.getPool("bar");
	bar.addItemEntry(<minecraft:stick>, 1, 0,
	[
	    loot.functions.zenscript(function(stack, random, context)
	    {
	        return stack * random.nextInt(1, 64);
	    })
	], []);
});