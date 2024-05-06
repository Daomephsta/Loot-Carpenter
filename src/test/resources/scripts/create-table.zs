loot.tables.newTable("foo:test/bar", function(table, context) {
	val main = table.addPool("main", 2, 2, 0, 0);
	main.addItemEntry(<minecraft:stick>, 10);	
});