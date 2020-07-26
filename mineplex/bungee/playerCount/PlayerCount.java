package mineplex.bungee.playerCount;

import java.net.InetSocketAddress;
import java.util.Collection;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.config.ConfigurationAdapter;
import net.md_5.bungee.api.config.ListenerInfo;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

public class PlayerCount implements net.md_5.bungee.api.plugin.Listener, Runnable
{
  private Plugin _plugin;
  private PlayerCountRepository _repository;
  private int _totalPlayers = -1;
  private int _totalMaxPlayers;
  
  public PlayerCount(Plugin plugin)
  {
    this._plugin = plugin;
    
    this._plugin.getProxy().getScheduler().schedule(this._plugin, this, 3L, 3L, java.util.concurrent.TimeUnit.SECONDS);
    this._plugin.getProxy().getPluginManager().registerListener(this._plugin, this);
    
    ListenerInfo listenerInfo = (ListenerInfo)this._plugin.getProxy().getConfigurationAdapter().getListeners().iterator().next();
    
    this._repository = new PlayerCountRepository(listenerInfo.getHost().getAddress().getHostAddress() + ":" + listenerInfo.getHost().getPort(), listenerInfo.getMaxPlayers());
    this._repository.initialize();
  }
  
  public void run()
  {
    this._repository.updatePlayerCountInDatabase(this._plugin.getProxy().getOnlineCount());
    
    PlayerTotalData playerTotalData = this._repository.retrievePlayerCount();
    
    this._totalPlayers = playerTotalData.CurrentPlayers;
    this._totalMaxPlayers = playerTotalData.MaxPlayers;
  }
  
  @EventHandler
  public void ServerPing(ProxyPingEvent event)
  {
    ServerPing serverPing = event.getResponse();
    
    event.setResponse(new ServerPing(serverPing.getVersion(), new net.md_5.bungee.api.ServerPing.Players(this._totalMaxPlayers, this._totalPlayers, null), serverPing.getDescription(), serverPing.getFaviconObject()));
  }
}
