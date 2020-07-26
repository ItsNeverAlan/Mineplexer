package mineplex.bungee.groupManager;

import java.util.concurrent.TimeUnit;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

public class GroupManager implements Listener, Runnable
{
  private Plugin _plugin;
  private GroupRepository _repository;
  
  public GroupManager(Plugin plugin)
  {
    this._plugin = plugin;
    
    this._plugin.getProxy().getScheduler().schedule(this._plugin, this, 3L, 3L, TimeUnit.SECONDS);
    this._plugin.getProxy().getPluginManager().registerListener(this._plugin, this);
    
    this._repository = new GroupRepository();
    this._repository.initialize();
  }
  
  public void run() {}
}
