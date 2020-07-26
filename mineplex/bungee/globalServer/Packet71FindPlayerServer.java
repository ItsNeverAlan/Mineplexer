package mineplex.bungee.globalServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet71FindPlayerServer
  extends Packet
{
  private String _playerName;
  
  public void parseStream(DataInputStream inputStream) throws IOException
  {
    this._playerName = readString(inputStream, 16);
  }
  
  public void write(DataOutputStream dataOutput)
    throws IOException
  {
    writeString(this._playerName, dataOutput);
  }
  
  public String getPlayerName()
  {
    return this._playerName;
  }
}
