loot.tables.editTable("loot_carpenter_test:foo", function(table, context) {
	val bar = table.getPool("bar");
	bar.addLootTableEntry("loot_carpenter_test:qux", 2, "corge_table");
	//addLootTableEntryWithQuality
	bar.addLootTableEntry("loot_carpenter_test:qux", 2, 3, "grault_table");
	//addLootTableEntryWithCondition
	bar.addLootTableEntry("loot_carpenter_test:qux", 2, 3, [loot.conditions.killedByPlayer()], "garply__table");
	//addLootTableEntry Helper
	bar.addLootTableEntry("loot_carpenter_test:qux", 2, 3, [loot.conditions.killedByPlayer()], "apple_table");
	//addLootTableEntry JSON
	bar.addLootTableEntry("loot_carpenter_test:qux", 2, 3, [{"condition": "minecraft:killed_by_player"}], "banana_table");
});