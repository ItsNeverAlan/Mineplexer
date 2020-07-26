package mineplex.bungee;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import mineplex.bungee.lobbyBalancer.LobbyBalancer;
import mineplex.bungee.motd.MotdManager;
import mineplex.bungee.playerCount.PlayerCount;
import mineplex.bungee.playerStats.PlayerStats;
import net.md_5.bungee.api.plugin.Plugin;


















































public class Mineplexer
  extends Plugin
{
  public void onEnable()
  {
    new MotdManager(this);
    new LobbyBalancer(this);
    new PlayerCount(this);
    new FileUpdater(this);
    new PlayerStats(this);
  }
  
  protected String readString(DataInputStream dataInputStream, int maxLength) throws IOException
  {
    short length = dataInputStream.readShort();
    
    if (length > maxLength)
    {
      throw new IOException("Received string length longer than maximum allowed (" + length + " > " + maxLength + ")");
    }
    if (length < 0)
    {
      throw new IOException("Received string length is less than zero! Weird string!");
    }
    

    StringBuilder stringBuilder = new StringBuilder();
    
    for (int i = 0; i < length; i++)
    {
      stringBuilder.append(dataInputStream.readChar());
    }
    
    return stringBuilder.toString();
  }
  
  protected void writeString(String string, DataOutputStream dataOutputStream)
    throws IOException
  {
    dataOutputStream.writeShort(string.length());
    dataOutputStream.writeChars(string);
  }
}
