package daomephsta.loot_carpenter.command;

import daomephsta.loot_carpenter.LootCarpenter;
import daomephsta.loot_carpenter.loot.LootTableFinder;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;


public class SubcommandListLootTables implements Subcommand
{
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args)
    {
        if (!LootTableFinder.DEFAULT.fullScanPerformed())
            sender.sendMessage(LootCarpenter.translation(".messages.info.locatingLootTables"));
        for (ResourceLocation table : LootTableFinder.DEFAULT.findAll())
        {
            sender.sendMessage(new TextComponentString(table.toString()));
        }
    }
}
