package daomephsta.loot_carpenter.test.support;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import daomephsta.loot_shared.ErrorHandler;
import daomephsta.loot_shared.mixin.LootTableAccessors;
import daomephsta.loot_shared.mixin.LootTableManagerAccessors;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;


public class TestUtils
{
    private TestUtils()
    {}

    public static IItemStack iitemstack(Item item)
    {
        return iitemstack(item, 1);
    }

    public static IItemStack iitemstack(Item item, int amount)
    {
        return iitemstack(item, amount, 0);
    }

    public static IItemStack iitemstack(Item item, int amount, int damage)
    {
        return CraftTweakerMC.getItemStack(item, amount, damage);
    }

    public static LootTable loadTable(String namespace, String path)
    {
        return loadTable(new ResourceLocation(namespace, path));
    }

    public static LootTable loadTable(ResourceLocation name)
    {
        String location = "assets/" + name.getNamespace() + "/loot_tables/" + name.getPath() + ".json";
        StringBuilder dataBuilder = new StringBuilder();
        InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream(location);
        if (stream == null) throw new IllegalArgumentException("No such loot table " + name);
        try (Scanner tableSource = new Scanner(stream))
        {
            while (tableSource.hasNextLine())
                dataBuilder.append(tableSource.nextLine());
        }
        LootTable table = ForgeHooks.loadLootTable(LootTableManagerAccessors.getGsonInstance(), name,
            dataBuilder.toString(), true, null);
        // Unfreeze table & pools, because doing tests will be a PITA otherwise
        ObfuscationReflectionHelper.setPrivateValue(LootTable.class, table, false, "isFrozen");
        for (LootPool pool : ((LootTableAccessors) table).getPools())
            ObfuscationReflectionHelper.setPrivateValue(LootPool.class, pool, false, "isFrozen");
        return table;
    }

    @FunctionalInterface
    interface TestInstanceFactory<T>
    {
        T get() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException;
    }

    private static final Map<Class<?>, TestInstanceFactory<?>> testingFactories = new HashMap<>();

    public static <C> C testInstance(Class<C> clazz)
    {
        @SuppressWarnings("unchecked")
        TestInstanceFactory<C> factory = (TestInstanceFactory<C>) testingFactories.computeIfAbsent(clazz, TestUtils::testInstanceFactory);
        try
        {
            return factory.get();
        }
        catch (InstantiationException | IllegalAccessException | IllegalArgumentException
            | InvocationTargetException e)
        {
            throw new RuntimeException("Could not call testing constructor for " + clazz.getCanonicalName(), e);
        }
    }

    private static <C> TestInstanceFactory<C> testInstanceFactory(Class<C> clazz)
    {
        try
        {
            for (Constructor<?> constructor : clazz.getDeclaredConstructors())
            {
                if (constructor.getParameterCount() == 1 && constructor.getParameterTypes()[0] == ErrorHandler.class)
                {
                    @SuppressWarnings("unchecked")
                    Constructor<C> withErrorHandler = (Constructor<C>) constructor;
                    withErrorHandler.setAccessible(true);
                    return () -> withErrorHandler.newInstance(new TestErrorHandler());
                }
                else if (constructor.getParameterCount() == 0)
                {
                    @SuppressWarnings("unchecked")
                    Constructor<C> nullary = (Constructor<C>) constructor;;
                    nullary.setAccessible(true);
                    return () -> nullary.newInstance();
                }
            }
        }
        catch (SecurityException | IllegalArgumentException e)
        {
            throw new RuntimeException("Could not find testing constructor for " + clazz.getCanonicalName(), e);
        }
        throw new RuntimeException("Could not find testing constructor for " + clazz.getCanonicalName());
    }
}
