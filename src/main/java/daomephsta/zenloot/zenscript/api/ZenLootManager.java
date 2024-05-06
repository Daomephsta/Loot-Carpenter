package daomephsta.zenloot.zenscript.api;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import daomephsta.zenloot.ZenLoot;
import daomephsta.zenloot.ZenLootConfig;
import daomephsta.zenloot.loot.LootTableFinder;
import daomephsta.zenloot.loot.dump.LootTableDumper;
import daomephsta.zenloot.zenscript.ErrorHandler;
import daomephsta.zenloot.zenscript.api.factory.LootConditionFactory;
import daomephsta.zenloot.zenscript.api.factory.LootFunctionFactory;
import daomephsta.zenloot.zenscript.impl.MutableLootTable;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import stanhebben.zenscript.annotations.ZenProperty;

@ZenRegister
@ZenClass(ZenLoot.ID + ".LootManager")
public class ZenLootManager
{
    public static final ZenLootManager INSTANCE = new ZenLootManager(new ErrorHandler.CraftTweakerLog());
    @ZenProperty
    public final LootTableManager tables;
    @ZenProperty
    public final LootConditionFactory conditions;
    @ZenProperty
    public final LootFunctionFactory functions;

    ZenLootManager(ErrorHandler errorHandler)
    {
        this.tables = new LootTableManager(errorHandler);
        this.conditions = new LootConditionFactory();
        this.functions = new LootFunctionFactory(errorHandler);
        MinecraftForge.EVENT_BUS.register(this.tables);
    }

    public static void register()
    {
        CraftTweakerAPI.registerGlobalSymbol("loot", CraftTweakerAPI.getJavaStaticFieldSymbol(ZenLootManager.class, "INSTANCE"));
    }

    @ZenRegister
    @ZenClass(ZenLoot.ID + ".LootTableManager")
    public static class LootTableManager
    {
        private final ErrorHandler errorHandler;
        private final Map<ResourceLocation, List<LootTableEditor>> editorsByTable = new LinkedHashMap<>();
        private final Map<ResourceLocation, List<LootTableEditor>> newTables = new LinkedHashMap<>();

        private LootTableManager(ErrorHandler errorHandler)
        {
            this.errorHandler = errorHandler;
        }

        @ZenRegister
        @ZenClass(ZenLoot.ID + ".LootTableEditor")
        @FunctionalInterface
        public interface LootTableEditor
        {
            public void apply(MutableZenLootTable table, Object context);
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
            if (ZenLootConfig.warnings.newTableMinecraftNamespace && tableName.getNamespace().equals("minecraft"))
            {
                if (name.startsWith("minecraft"))
                    errorHandler.warn("Table name '%s' explicitly uses the minecraft namespace, this is discouraged", name);
                else
                    errorHandler.warn("Table name '%s' implicitly uses the minecraft namespace, this is discouraged", name);
            }
            if (tableName.getNamespace().equals(ZenLoot.ID))
                errorHandler.warn("Table name '%s' uses the zenloot namespace, this is discouraged", name);
            if (LootTableFinder.DEFAULT.exists(tableName) || newTables.containsKey(tableName))
            {
                errorHandler.error("Table name '%s' already in use", tableName);
                return;
            }
            addEditor(newTables, tableName, editor);
            CraftTweakerAPI.logInfo("Created new table '" + tableName + "'");
        }

        private boolean addEditor(Map<ResourceLocation, List<LootTableEditor>> editorMap,
            ResourceLocation tableName, LootTableEditor editor)
        {
            return editorMap.computeIfAbsent(tableName, k -> new ArrayList<>()).add(editor);
        }

        @SubscribeEvent(priority = EventPriority.LOWEST)
        public void onTableLoad(LootTableLoadEvent event)
        {
            event.setTable(withEdits(event.getTable(), event.getName()));
        }

        public LootTable withEdits(LootTable table, ResourceLocation name)
        {
            MutableLootTable mutable = MutableLootTable.fromTable(table, name, errorHandler);
            for (LootTableEditor editor : editorsByTable.getOrDefault(mutable.getId(), Collections.emptyList()))
                editor.apply(mutable, null);
            return mutable.toImmutable();
        }

        public void writeGeneratedFiles(MinecraftServer server)
        {
            File worldLootTables = server.getActiveAnvilConverter()
                .getFile(server.getFolderName(), "data/loot_tables");
            LootTableDumper dumper = LootTableDumper.robust(worldLootTables);
            newTables.forEach((name, editors) ->
            {
                MutableLootTable mutable = new MutableLootTable(name, new HashMap<>(), errorHandler);
                for (LootTableEditor editor : editors)
                    editor.apply(mutable, null);
                dumper.dump(mutable.toImmutable(), name);
            });
        }
    }
}
