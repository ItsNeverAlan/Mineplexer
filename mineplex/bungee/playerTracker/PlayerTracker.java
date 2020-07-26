package mineplex.bungee.playerTracker;

import net.md_5.bungee.api.plugin.Plugin;

public class PlayerTracker
{
  private Plugin _plugin;
  private PlayerTrackerRepository _repository;
  
  public PlayerTracker(Plugin plugin)
  {
    this._plugin = plugin;
    
    this._plugin.getProxy().getPluginManager().registerCommand(this._plugin, new FindCommand(this._plugin));
    
    this._repository = new PlayerTrackerRepository();
    this._repository.initialize();
  }
}
