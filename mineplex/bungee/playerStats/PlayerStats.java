package mineplex.bungee.playerStats;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.TaskScheduler;

public class PlayerStats implements net.md_5.bungee.api.plugin.Listener
{
  private Plugin _plugin;
  private PlayerStatsRepository _repository;
  
  public PlayerStats(Plugin plugin)
  {
    this._plugin = plugin;
    
    this._plugin.getProxy().getPluginManager().registerListener(this._plugin, this);
    
    this._repository = new PlayerStatsRepository();
    this._repository.initialize();
  }
  
  @net.md_5.bungee.event.EventHandler
  public void playerConnect(final PostLoginEvent event)
  {
    this._plugin.getProxy().getScheduler().runAsync(this._plugin, new Runnable()
    {
      public void run()
      {
        PlayerStats.this._repository.addPlayer(event.getPlayer().getName());
      }
    });
  }
}
