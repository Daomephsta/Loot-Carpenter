package daomephsta.zenloot;

import java.lang.reflect.Field;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.internal.NetworkModHolder;
import net.minecraftforge.fml.common.network.internal.NetworkModHolder.NetworkChecker;
import net.minecraftforge.fml.relauncher.Side;

public class ZenLootNetworkChecker extends NetworkChecker
{
    private static final Logger LOGGER = LogManager.getLogger(ZenLoot.NAME);

    private ZenLootNetworkChecker(NetworkModHolder parent)
    {
        parent.super();
    }

    @Override
    public boolean check(Map<String, String> modVersions, Side remoteSide)
    {
        return checkCompatible(modVersions, remoteSide) == null;
    }

    @Override
    public String checkCompatible(Map<String, String> modVersions, Side remoteSide)
    {
        //Reject vanilla clients or servers
        if (!modVersions.containsKey("forge"))
            return "Rejected vanilla install";
        String remoteLTVersion = modVersions.get(ZenLoot.ID);
        //Client without can connect to server with, but not vice versa
        if (remoteLTVersion == null)
        {
            if (remoteSide == Side.CLIENT)
            {
                LOGGER.info("Accepted non-existent client ZenLoot install");
                return null;
            }
            else
                return "Rejected non-existent server ZenLoot install";
        }
        //Network compatibility is not guaranteed between versions
        if (!remoteLTVersion.equals(ZenLoot.VERSION))
        {
            return String.format("Rejected %s ZenLoot install because its version %s differs from local version %s",
                remoteSide.name().toLowerCase(), remoteLTVersion, ZenLoot.VERSION);
        }
        return null;
    }

    public static void install()
    {
        ModContainer zenLoot = Loader.instance().getIndexedModList().get(ZenLoot.ID);
        try
        {
            NetworkModHolder holder = getHolderRegistry().get(zenLoot);
            Field checkerField = NetworkModHolder.class.getDeclaredField("checker");
            checkerField.setAccessible(true);
            checkerField.set(holder, new ZenLootNetworkChecker(holder));
        }
        catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e)
        {
            throw new RuntimeException("Failed to set network checker", e);
        }
        LOGGER.info("Successfully installed network checker");
    }

    @SuppressWarnings("unchecked")
    private static Map<ModContainer, NetworkModHolder> getHolderRegistry()
    {
        try
        {
            Field registryField = NetworkRegistry.class.getDeclaredField("registry");
            registryField.setAccessible(true);
            return (Map<ModContainer, NetworkModHolder>) registryField.get(NetworkRegistry.INSTANCE);
        }
        catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e)
        {
            throw new RuntimeException("Failed to get holder registry from network registry", e);
        }
    }
}
