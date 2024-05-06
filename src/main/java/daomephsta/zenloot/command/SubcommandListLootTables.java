package daomephsta.zenloot.command;

import daomephsta.zenloot.loot.LootTableFinder;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;


public class SubcommandListLootTables implements Subcommand
{
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args)
    {
        if (!LootTableFinder.DEFAULT.fullScanPerformed())
            sender.sendMessage(new TextComponentTranslation("ZenLoot.messages.info.locatingLootTables"));
        for (ResourceLocation table : LootTableFinder.DEFAULT.findAll())
        {
            sender.sendMessage(new TextComponentString(table.toString()));
        }
    }
}
