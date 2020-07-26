package mineplex.bungee.globalServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

public class GlobalServerMultiThread extends Thread
{
  private Plugin _plugin;
  private Socket _socket = null;
  
  public GlobalServerMultiThread(Plugin plugin, Socket socket)
  {
    super("GlobalServerMultiThread");
    
    this._plugin = plugin;
    this._socket = socket;
  }
  
  public void run()
  {
    DataInputStream socketInputStream = null;
    DataOutputStream socketOutputStream = null;
    
    try
    {
      socketInputStream = new DataInputStream(this._socket.getInputStream());
      socketOutputStream = new DataOutputStream(new java.io.BufferedOutputStream(this._socket.getOutputStream(), 5120));
      
      int id = socketInputStream.readShort();
      
      if (id == 71)
      {
        System.out.println("Received packet 71");
        Packet71FindPlayerServer packet = new Packet71FindPlayerServer();
        packet.parseStream(socketInputStream);
        
        System.out.println("Looking for player: " + packet.getPlayerName());
        
        ProxiedPlayer player = this._plugin.getProxy().getPlayer(packet.getPlayerName());
        
        if (player != null)
        {
          Packet91PlayerServerResponse responsePacket = new Packet91PlayerServerResponse(player.getName(), player.getServer().getInfo().getName());
          socketOutputStream.writeShort(91);
          responsePacket.write(socketOutputStream);
        }
        
        socketOutputStream.close();
        socketOutputStream = null;
      }
    }
    catch (IOException e)
    {
      e.printStackTrace();
      


      try
      {
        if (socketInputStream != null) {
          socketInputStream.close();
        }
      }
      catch (IOException e) {
        e.printStackTrace();
      }
      
      try
      {
        if (socketOutputStream != null) {
          socketOutputStream.close();
        }
      }
      catch (IOException e) {
        e.printStackTrace();
      }
      
      try
      {
        if (this._socket != null) {
          this._socket.close();
        }
      }
      catch (IOException e) {
        e.printStackTrace();
      }
    }
    finally
    {
      try
      {
        if (socketInputStream != null) {
          socketInputStream.close();
        }
      }
      catch (IOException e) {
        e.printStackTrace();
      }
      
      try
      {
        if (socketOutputStream != null) {
          socketOutputStream.close();
        }
      }
      catch (IOException e) {
        e.printStackTrace();
      }
      
      try
      {
        if (this._socket != null) {
          this._socket.close();
        }
      }
      catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
