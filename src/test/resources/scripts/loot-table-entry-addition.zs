loot.tables.editTable("zenloot_test:foo", function(table, context) {
	val bar = table.getPool("bar");
	bar.addLootTableEntry("zenloot_test:qux", 2, "corge_table");
	//addLootTableEntryWithQuality
	bar.addLootTableEntry("zenloot_test:qux", 2, 3, "grault_table");
	//addLootTableEntryWithCondition
	bar.addLootTableEntry("zenloot_test:qux", 2, 3, [loot.conditions.killedByPlayer()], "garply__table");
	//addLootTableEntry Helper
	bar.addLootTableEntry("zenloot_test:qux", 2, 3, [loot.conditions.killedByPlayer()], "apple_table");
	//addLootTableEntry JSON
	bar.addLootTableEntry("zenloot_test:qux", 2, 3, [{"condition": "minecraft:killed_by_player"}], "banana_table");
});