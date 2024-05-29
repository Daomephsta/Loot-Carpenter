package daomephsta.loot_carpenter.command;

import static java.util.stream.Collectors.toList;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;

import com.google.common.collect.ImmutableMap;

import crafttweaker.mc1120.commands.CraftTweakerCommand;
import daomephsta.loot_carpenter.LootCarpenter;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;


public class CommandLootCarpenter extends CraftTweakerCommand
{
    private final Map<String, Subcommand> subcommands = ImmutableMap.<String, Subcommand>builder()
        .put("all", new SubcommandDumpAll())
        .put("byName", new SubcommandDumpNamed())
        .put("target", new SubcommandDumpTargetsLootTable())
        .put("list", new SubcommandListLootTables())
        .put("generate", new SubcommandGenerate())
        .build();

    public CommandLootCarpenter()
    {
        super(LootCarpenter.ID);
    }

    @Override
    protected void init()
    {
        setDescription(LootCarpenter.translation(".commands.dump.desc"));
    }

    @Override
    public void executeCommand(MinecraftServer server, ICommandSender sender, String[] args)
    {
        if (args.length < 1)
        {
            sender.sendMessage(LootCarpenter.translation(".commands.dump.usage"));
            return;
        }
        Subcommand subcommand = subcommands.get(args[0]);
        if (subcommand != null)
        {
            subcommand.execute(server, sender, args);
        }
        else
            sender.sendMessage(LootCarpenter.translation(".commands.dump.unknownSubcommand", args[0]));
    }

    @Override
    public List<String> getSubSubCommand(MinecraftServer server, ICommandSender sender, String[] args,
        BlockPos targetPos)
    {
        if (args.length == 1)
            return subcommands.keySet().stream().filter(s -> s.startsWith(args[0])).collect(toList());

        Subcommand subcommand = subcommands.get(args[0]);
        if (subcommand != null)
            return subcommand.getCompletions(server, sender, ArrayUtils.subarray(args, 1, args.length), targetPos);
        return Collections.emptyList();
    }
}
