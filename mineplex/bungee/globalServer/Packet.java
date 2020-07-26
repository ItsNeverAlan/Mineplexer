package mineplex.bungee.globalServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public abstract class Packet
{
  public abstract void parseStream(DataInputStream paramDataInputStream) throws IOException;
  
  public abstract void write(DataOutputStream paramDataOutputStream) throws IOException;
  
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
