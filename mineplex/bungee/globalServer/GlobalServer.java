package mineplex.bungee.globalServer;

import java.net.ServerSocket;
import net.md_5.bungee.api.plugin.Plugin;






















public class GlobalServer
{
  public GlobalServer(final Plugin plugin)
  {
    new Thread(new Runnable()
    {
      public void run()
      {
        ServerSocket serverSocket = null;
        
        try
        {
          serverSocket = new ServerSocket(4444);
          
          for (;;)
          {
            new GlobalServerMultiThread(plugin, serverSocket.accept()).start();
          }
        }
        catch (Exception e)
        {
          e.printStackTrace();
        }
      }
    })
    


















      .start();
  }
}
