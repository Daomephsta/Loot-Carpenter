package daomephsta.loot_carpenter.zenscript.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.world.IWorld;
import daomephsta.loot_carpenter.LootCarpenter;
import daomephsta.loot_carpenter.LootCarpenterConfig;
import daomephsta.loot_shared.ErrorHandler;
import daomephsta.loot_shared.LootTableTweakManager;
import daomephsta.loot_shared.utility.loot.LootTableFinder;
import daomephsta.loot_shared.zenscript.api.EditableLootTable;
import daomephsta.loot_shared.zenscript.api.LootGenerator;
import daomephsta.loot_shared.zenscript.api.factory.LootConditionFactory;
import daomephsta.loot_shared.zenscript.api.factory.LootFunctionFactory;
import daomephsta.loot_shared.zenscript.impl.MutableLootTable;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import stanhebben.zenscript.annotations.ZenProperty;

@ZenRegister
@ZenClass(LootCarpenter.ZEN_PACKAGE + ".LootManager")
public class ZenLootManager
{
    public static final ZenLootManager INSTANCE = new ZenLootManager(new ErrorHandler.CraftTweakerLog());
    @ZenProperty
    public final LootTableManager tables;
    @ZenProperty
    public final LootConditionFactory conditions;
    @ZenProperty
    public final LootFunctionFactory functions;
	private final ErrorHandler errorHandler;

    ZenLootManager(ErrorHandler errorHandler)
    {
        this.tables = new LootTableManager(errorHandler);
        this.conditions = new LootConditionFactory();
        this.functions = new LootFunctionFactory(errorHandler);
        this.errorHandler = errorHandler;
        MinecraftForge.EVENT_BUS.register(this.tables);
    }

    public static void register()
    {
        CraftTweakerAPI.registerGlobalSymbol("loot", CraftTweakerAPI.getJavaStaticFieldSymbol(ZenLootManager.class, "INSTANCE"));
    }
    
	@ZenMethod
	public LootGenerator createLootGenerator(IWorld world)
	{
		return LootGenerator.create(world, errorHandler); 
	}

    @ZenRegister
    @ZenClass(LootCarpenter.ZEN_PACKAGE + ".LootTableManager")
    public static class LootTableManager extends LootTableTweakManager
    {
        private final ErrorHandler errorHandler;
        private final Map<ResourceLocation, List<LootTableEditor>> editorsByTable = new LinkedHashMap<>();
        private final Map<ResourceLocation, List<LootTableEditor>> newTables = new LinkedHashMap<>();

        private LootTableManager(ErrorHandler errorHandler)
        {
        	super(errorHandler);
            this.errorHandler = errorHandler;
        }

        @ZenRegister
        @ZenClass(LootCarpenter.ZEN_PACKAGE + ".LootTableEditor")
        @FunctionalInterface
        public interface LootTableEditor
        {
            public void apply(EditableLootTable table, Object context);
        }

        @ZenMethod
        public void editTable(String name, LootTableEditor editor)
        {
            ResourceLocation tableName = new ResourceLocation(name);
            if (LootTableFinder.DEFAULT.exists(tableName))
                addEditor(editorsByTable, tableName, editor);
            else if (newTables.containsKey(tableName))
                newTables.get(tableName).add(editor);
            else
                errorHandler.error("No loot table with name %s exists!", tableName);
        }

        @ZenMethod
        public void newTable(String name, LootTableEditor editor)
        {
            ResourceLocation tableName = new ResourceLocation(name);
            if (!validateNewTableName(name, LootCarpenter.ID, LootCarpenterConfig.warnings.newTableMinecraftNamespace))
                return;
            addEditor(newTables, tableName, editor);
            CraftTweakerAPI.logInfo("Created new table '" + tableName + "'");
        }

        private boolean addEditor(Map<ResourceLocation, List<LootTableEditor>> editorMap,
            ResourceLocation tableName, LootTableEditor editor)
        {
            return editorMap.computeIfAbsent(tableName, k -> new ArrayList<>()).add(editor);
        }

    	@Override
    	public Iterator<MutableLootTable> yieldNewTables() 
    	{
    		return newTables.entrySet().stream()
    				.map(entry -> 
    				{
    					MutableLootTable mutable = new MutableLootTable(entry.getKey(), new HashMap<>(), errorHandler);
    					for (LootTableEditor editor : entry.getValue())
    	                    editor.apply(mutable, null);
    		            return mutable;
    				})
    				.iterator();
    	}

        @Override
        public void applyEdits(MutableLootTable table) 
        {
            for (LootTableEditor editor : editorsByTable.get(table.getId()))
                editor.apply(table, null);
        }
        
        @Override
        public Collection<ResourceLocation> getEditedTableIds() 
        {
        	return editorsByTable.keySet();
        }
        
        @Override
        public Collection<ResourceLocation> getNewTableIds() 
        {
        	return newTables.keySet();
        }
    }
}
