package mineplex.bungee.motd;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.api.scheduler.TaskScheduler;

public class MotdManager implements Listener, Runnable
{
  private Plugin _plugin;
  private MotdRepository _repository;
  private String _motd = "§b§l§m   §8§l§m[ §r §9§lMineplex§r §f§lGames§r §8§l§m ]§b§l§m   §r                        §c§l§m§kZ§6§l§m§kZ§e§l§m§kZ§a§l§m§kZ§b§l§m§kZ§r  §f§lPLAY NOW§r  §b§l§m§kZ§a§l§m§kZ§e§l§m§kZ§6§l§m§kZ§c§l§m§kZ";
  
  public MotdManager(Plugin plugin)
  {
    this._plugin = plugin;
    
    this._plugin.getProxy().getScheduler().schedule(this._plugin, this, 30L, 30L, java.util.concurrent.TimeUnit.SECONDS);
    this._plugin.getProxy().getPluginManager().registerListener(this._plugin, this);
    
    this._repository = new MotdRepository();
    this._repository.initialize();
  }
  
  @net.md_5.bungee.event.EventHandler
  public void ServerPing(ProxyPingEvent event)
  {
    ServerPing serverPing = event.getResponse();
    
    event.setResponse(new ServerPing(serverPing.getVersion(), serverPing.getPlayers(), this._motd, serverPing.getFaviconObject()));
  }
  

  public void run()
  {
    this._motd = this._repository.retrieveMotd();
  }
}
