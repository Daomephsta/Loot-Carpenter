loot.tables.editTable("zenloot_test:foo", function(table, context) {
	val bar = table.getPool("bar");
	//addItemEntry
	bar.addItemEntry(<minecraft:apple>, 2, "qux");
	//addItemEntryWithQuality
	bar.addItemEntry(<minecraft:apple>, 2, 3, "quuz");
	//addItemEntry Helper
	bar.addItemEntry(<minecraft:baked_potato>, 2, 3, [], [loot.conditions.killedByPlayer()], "corge");
	//addItemEntry JSON
	bar.addItemEntry(<minecraft:baked_potato>, 2, 3, 
		[], [{"condition": "minecraft:killed_by_player"}], "grault");
	//addItemEntry
	bar.addItemEntry(<minecraft:baked_potato>, 2, 3, 
		[], [{"condition": "minecraft:killed_by_player"}], "grault_");
	//addItemEntryWithImplicitSetCount
	bar.addItemEntry(<minecraft:arrow> * 3, 2, "garply");
	//addItemEntryWithExplicitSetCount
	bar.addItemEntry(<minecraft:arrow>, 2, 1, [loot.functions.setCount(3, 3)], [], "waldo");
	//addItemEntryWithImplicitSetDamage
	bar.addItemEntry(<minecraft:bow:32>, 2, "fred");
	//addItemEntryWithExplicitSetDamage
	bar.addItemEntry(<minecraft:bow>, 2, 1, [loot.functions.setDamage(0.5, 0.5)], [], "plugh");
	//addItemEntryWithImplicitSetMetadata
	bar.addItemEntry(<minecraft:dye:8>, 2, "thud");
	//addItemEntryWithExplicitSetMetadata
	bar.addItemEntry(<minecraft:dye>, 2, 1, [loot.functions.setMetadata(8, 8)], [], "grox");
	//addItemEntryWithImplicitSetNBT
	bar.addItemEntry(<minecraft:bread>.withDisplayName("Super Bread"), 2, "warg");
	//addItemEntryWithExplicitSetNBT
	bar.addItemEntry(<minecraft:bread>, 2, 1,
		[loot.functions.setNBT({"display": {"Name": "Super Bread"}})], [], "nerf");
});