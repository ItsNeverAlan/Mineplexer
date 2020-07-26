

java.io.File
java.io.FileInputStream
java.io.FilenameFilter
java.io.IOException
java.io.PrintStream
java.util.HashMap
java.util.concurrent.TimeUnit
net.md_5.bungee.BungeeCord
net.md_5.bungee.api.ChatColor
net.md_5.bungee.api.ProxyServer
net.md_5.bungee.api.plugin.Plugin
org.apache.commons.codec.digest.DigestUtils

FileUpdater

  _plugin
  , _jarMd5Map = ()
  
  _needUpdate
  _enabled = 
  _timeTilRestart = 5
  
  FileUpdater
  
    _plugin = plugin;
    
    getPluginMd5s();
    
    if (new File("IgnoreUpdates.dat").exists()) {
      this._enabled = false;
    }
    this._plugin.getProxy().getScheduler().schedule(this._plugin, this, 2L, 2L, TimeUnit.MINUTES);
  }
  
  public void checkForNewFiles()
  {
    if ((this._needUpdate) || (!this._enabled)) {
      return;
    }
    boolean windows = System.getProperty("os.name").startsWith("Windows");
    
    File updateDir = new File((windows ? "C:" : new StringBuilder(String.valueOf(File.separator)).append("home").append(File.separator).append("mineplex").toString()) + File.separator + "update");
    
    updateDir.mkdirs();
    
    FilenameFilter statsFilter = new FilenameFilter()
    {
      public boolean accept(File paramFile, String paramString)
      {
        if (paramString.endsWith("jar"))
        {
          return true;
        }
        
        return false;
      }
    };
    
    for (File f : updateDir.listFiles(statsFilter))
    {
      FileInputStream fis = null;
      
      try
      {
        if (this._jarMd5Map.containsKey(f.getName()))
        {
          fis = new FileInputStream(f);
          String md5 = DigestUtils.md5Hex(fis);
          
          if (!md5.equals(this._jarMd5Map.get(f.getName())))
          {
            System.out.println(f.getName() + " old jar : " + (String)this._jarMd5Map.get(f.getName()));
            System.out.println(f.getName() + " new jar : " + md5);
            this._needUpdate = true;
          }
        }
      }
      catch (Exception ex)
      {
        ex.printStackTrace();
        


        if (fis != null)
        {
          try
          {
            fis.close();
          }
          catch (IOException e)
          {
            e.printStackTrace();
          }
        }
      }
      finally
      {
        if (fis != null)
        {
          try
          {
            fis.close();
          }
          catch (IOException e)
          {
            e.printStackTrace();
          }
        }
      }
    }
  }
  
  private void getPluginMd5s()
  {
    File pluginDir = new File("plugins");
    
    pluginDir.mkdirs();
    
    FilenameFilter statsFilter = new FilenameFilter()
    {
      public boolean accept(File paramFile, String paramString)
      {
        if (paramString.endsWith("jar"))
        {
          return true;
        }
        
        return false;
      }
    };
    
    for (File f : pluginDir.listFiles(statsFilter))
    {
      FileInputStream fis = null;
      
      try
      {
        fis = new FileInputStream(f);
        this._jarMd5Map.put(f.getName(), DigestUtils.md5Hex(fis));
      }
      catch (Exception ex)
      {
        ex.printStackTrace();
        


        if (fis != null)
        {
          try
          {
            fis.close();
          }
          catch (IOException e)
          {
            e.printStackTrace();
          }
        }
      }
      finally
      {
        if (fis != null)
        {
          try
          {
            fis.close();
          }
          catch (IOException e)
          {
            e.printStackTrace();
          }
        }
      }
    }
  }
  

  public void run()
  {
    checkForNewFiles();
    
    if (this._needUpdate)
    {
      BungeeCord.getInstance().broadcast(ChatColor.RED + "Connection Node" + ChatColor.DARK_GRAY + ">" + ChatColor.YELLOW + "This connection node will be restarting in " + this._timeTilRestart + " minutes.");
    }
    else
    {
      return;
    }
    
    this._timeTilRestart -= 2;
    
    if ((this._timeTilRestart < 0) || (!this._enabled))
    {
      BungeeCord.getInstance().stop();
    }
  }
}
