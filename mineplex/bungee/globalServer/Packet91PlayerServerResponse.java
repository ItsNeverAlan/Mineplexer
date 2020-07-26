package mineplex.bungee.globalServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet91PlayerServerResponse extends Packet
{
  private String _playerName;
  private String _serverName;
  
  public Packet91PlayerServerResponse(String playerName, String serverName)
  {
    this._playerName = playerName;
    this._serverName = serverName;
  }
  
  public void parseStream(DataInputStream inputStream)
    throws IOException
  {
    this._playerName = readString(inputStream, 16);
    this._serverName = readString(inputStream, 24);
  }
  
  public void write(DataOutputStream dataOutput)
    throws IOException
  {
    writeString(this._playerName, dataOutput);
    writeString(this._serverName, dataOutput);
  }
}
