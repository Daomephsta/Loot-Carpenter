package daomephsta.loot_carpenter;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import daomephsta.loot_shared.CustomNetworkChecker;
import net.minecraftforge.fml.common.network.internal.NetworkModHolder;
import net.minecraftforge.fml.relauncher.Side;

public class LootCarpenterNetworkChecker extends CustomNetworkChecker
{
    private static final Logger LOGGER = LogManager.getLogger(LootCarpenter.NAME);

    private LootCarpenterNetworkChecker(NetworkModHolder parent)
    {
        super(parent);
    }

    @Override
    public String checkCompatible(Map<String, String> modVersions, Side remoteSide)
    {
        //Reject vanilla clients or servers
        if (!modVersions.containsKey("forge"))
            return "Rejected vanilla install";
        String remoteVersion = modVersions.get(LootCarpenter.ID);
        //Client without can connect to server with, but not vice versa
        if (remoteVersion == null)
        {
            if (remoteSide == Side.CLIENT)
            {
                LOGGER.info("Accepted non-existent client install");
                return null;
            }
            else
                return "Rejected non-existent server install";
        }
        //Network compatibility is not guaranteed between versions
        if (!remoteVersion.equals(LootCarpenter.VERSION))
        {
            return String.format("Rejected %s install because its version %s differs from local version %s",
                remoteSide.name().toLowerCase(), remoteVersion, LootCarpenter.VERSION);
        }
        return null;
    }

    public static void install()
    {
        install(LootCarpenterNetworkChecker::new, LootCarpenter.ID);
    }
}
