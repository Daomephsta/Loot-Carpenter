package daomephsta.zenloot.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import daomephsta.zenloot.Texts;
import daomephsta.zenloot.loot.LootTableFinder;
import daomephsta.zenloot.loot.dump.LootTableDumper;
import daomephsta.zenloot.ZenLoot;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;


public class SubcommandDumpAll implements Subcommand
{
    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args)
    {
        if (!LootTableFinder.DEFAULT.fullScanPerformed())
            sender.sendMessage(ZenLoot.translation(".messages.info.locatingLootTables"));
        LootTableDumper dumper = LootTableDumper.DEFAULT;
        for (ResourceLocation tableId : LootTableFinder.DEFAULT.findAll())
        {
            try
            {
                dumper.dump(sender.getEntityWorld(), tableId);
            }
            catch (Exception e)
            {
                sender.sendMessage(ZenLoot.translation(".commands.dump.all.exception", tableId));
                LOGGER.error("Unable to dump {}", tableId, unwrap(e));
            }
        }
        sender.sendMessage(ZenLoot.translation(".commands.dump.all.done",
            Texts.fileLink(dumper.getFolder())));
    }

    private Throwable unwrap(Throwable t)
    {
        Throwable unwrapped = t;
        while (unwrapped.getCause() != null && unwrapped.getMessage().equals(unwrapped.getCause().toString()))
            unwrapped = unwrapped.getCause();
        return unwrapped;
    }
}
