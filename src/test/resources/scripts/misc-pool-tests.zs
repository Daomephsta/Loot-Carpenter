loot.tables.editTable("zenloot_test:foo", function(table, context) {
	val bar = table.getPool("bar");
	//addConditions
	bar.addConditions([loot.conditions.killedByPlayer()]);
	//setRolls
	bar.setRolls(2.0, 5.0);
	//setBonusRolls
	bar.setBonusRolls(1.0, 3.0);
});

loot.tables.editTable("zenloot_test:bar", function(table, context) {
	val baz = table.getPool("baz");
	//removeExistingEntry
	baz.removeEntry("qux");
	//clearConditions
	baz.conditions = [];
	//clearEntries
	baz.clearEntries();
});