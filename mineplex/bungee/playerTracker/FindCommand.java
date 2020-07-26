package mineplex.bungee.playerTracker;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;

public class FindCommand extends Command
{
  private Plugin _plugin;
  
  public FindCommand(Plugin plugin)
  {
    super("mineplex.bungee.playertracker.find", "", new String[] { "" });
    
    this._plugin = plugin;
  }
  
  public void execute(CommandSender arg0, String[] arg1) {}
}
