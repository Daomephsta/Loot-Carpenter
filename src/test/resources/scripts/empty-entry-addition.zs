loot.tables.editTable("zenloot_test:foo", function(table, context) {
	val bar = table.getPool("bar");
	//addEmptyEntry
	bar.addEmptyEntry(2, "corge_empty");
	//addEmptyEntryWithQuality
	bar.addEmptyEntry(2, 3, "grault_empty");
	//addEmptyEntryWithCondition
	bar.addEmptyEntry(2, 3, [loot.conditions.killedByPlayer()], "garply__empty");
	//addEmptyEntry JSON
	bar.addEmptyEntry(2, 3, [{"condition": "minecraft:killed_by_player"}], "waldo_empty");
});