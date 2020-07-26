package mineplex.bungee.lobbyBalancer;

import java.io.File;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.api.scheduler.TaskScheduler;
import net.md_5.bungee.event.EventHandler;

public class LobbyBalancer implements Listener, Runnable
{
  private Plugin _plugin;
  private LobbyBalancerRepository _repository;
  private List<ServerStatusData> _sortedLobbies = new java.util.ArrayList();
  private static Object _serverLock = new Object();
  
  private int _bestServerIndex = 0;
  private int _playersSentToBestServer = 0;
  private int _maxPlayersToSendToBestServer = 1;
  
  public LobbyBalancer(Plugin plugin)
  {
    this._plugin = plugin;
    this._repository = new LobbyBalancerRepository();
    
    boolean us = !new File("eu.dat").exists();
    
    this._repository.initialize(us);
    
    loadLobbyServers();
    
    this._plugin.getProxy().getPluginManager().registerListener(this._plugin, this);
    this._plugin.getProxy().getScheduler().schedule(this._plugin, this, 2L, 2L, java.util.concurrent.TimeUnit.SECONDS);
  }
  
  @EventHandler
  public void playerConnect(ServerConnectEvent event)
  {
    if (!event.getTarget().getName().equalsIgnoreCase("Lobby")) {
      return;
    }
    synchronized (_serverLock)
    {
      if (this._playersSentToBestServer >= this._maxPlayersToSendToBestServer)
      {
        this._playersSentToBestServer = 0;
        
        while (this._bestServerIndex < this._sortedLobbies.size())
        {
          this._bestServerIndex += 1;
          this._maxPlayersToSendToBestServer = ((((ServerStatusData)this._sortedLobbies.get(this._bestServerIndex)).MaxPlayers - ((ServerStatusData)this._sortedLobbies.get(this._bestServerIndex)).Players) / 10);
          
          if (this._maxPlayersToSendToBestServer > 0) {
            break;
          }
        }
        if (this._maxPlayersToSendToBestServer == 0)
        {
          this._bestServerIndex = 0;
          this._maxPlayersToSendToBestServer = 1;
        }
      }
      
      if (this._bestServerIndex < this._sortedLobbies.size()) {
        event.setTarget(this._plugin.getProxy().getServerInfo(((ServerStatusData)this._sortedLobbies.get(this._bestServerIndex)).Name));
      }
      this._playersSentToBestServer += 1;
    }
  }
  
  public void run()
  {
    loadLobbyServers();
  }
  
  public void loadLobbyServers()
  {
    this._playersSentToBestServer = 0;
    
    List<ServerStatusData> serverStatusDataList = this._repository.retrieveServerStatuses();
    
    synchronized (_serverLock)
    {
      this._sortedLobbies.clear();
      
      for (ServerStatusData serverStatusData : serverStatusDataList)
      {
        if (serverStatusData.Name != null)
        {

          InetSocketAddress socketAddress = new InetSocketAddress(serverStatusData.Address, serverStatusData.Port);
          this._plugin.getProxy().getServers().put(serverStatusData.Name, this._plugin.getProxy().constructServerInfo(serverStatusData.Name, socketAddress, "LobbyBalancer", false));
          
          if (serverStatusData.Name.toUpperCase().contains("LOBBY"))
          {
            if ((serverStatusData.Motd == null) || (!serverStatusData.Motd.contains("Restarting")))
            {
              this._sortedLobbies.add(serverStatusData);
            }
          }
        }
      }
      java.util.Collections.sort(this._sortedLobbies, new LobbySorter());
      
      this._bestServerIndex = 0;
      
      if (this._sortedLobbies.size() > 0) {
        this._maxPlayersToSendToBestServer = ((((ServerStatusData)this._sortedLobbies.get(this._bestServerIndex)).MaxPlayers - ((ServerStatusData)this._sortedLobbies.get(this._bestServerIndex)).Players) / 10);
      }
    }
  }
}
