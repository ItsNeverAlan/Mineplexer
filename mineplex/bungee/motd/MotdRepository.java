package mineplex.bungee.motd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MotdRepository
{
  private Connection _connection = null;
  private String _connectionString = "jdbc:mysql://db.mineplex.com:3306/BungeeServers?autoReconnect=true&failOverReadOnly=false&maxReconnects=10";
  private String _userName = "root";
  private String _password = "tAbechAk3wR7tuTh";
  
  private static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS BungeeMotd (id INT NOT NULL AUTO_INCREMENT, motd VARCHAR(256), PRIMARY KEY (id));";
  private static String RETRIEVE_MOTD = "SELECT motd FROM BungeeMotd;";
  
  public void initialize()
  {
    PreparedStatement preparedStatement = null;
    
    try
    {
      if ((this._connection == null) || (this._connection.isClosed())) {
        this._connection = DriverManager.getConnection(this._connectionString, this._userName, this._password);
      }
      
      preparedStatement = this._connection.prepareStatement(CREATE_TABLE);
      preparedStatement.execute();
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
      


      if (preparedStatement != null)
      {
        try
        {
          preparedStatement.close();
        }
        catch (SQLException e)
        {
          e.printStackTrace();
        }
      }
    }
    finally
    {
      if (preparedStatement != null)
      {
        try
        {
          preparedStatement.close();
        }
        catch (SQLException e)
        {
          e.printStackTrace();
        }
      }
    }
    
    System.out.println("Initialized MOTD.");
  }
  
  public String retrieveMotd()
  {
    String motd = "§b§l§m   §8§l§m[ §r §9§lMineplex§r §f§lGames§r §8§l§m ]§b§l§m   §r                        §c§l§m§kZ§6§l§m§kZ§e§l§m§kZ§a§l§m§kZ§b§l§m§kZ§r  §f§lPLAY NOW§r  §b§l§m§kZ§a§l§m§kZ§e§l§m§kZ§6§l§m§kZ§c§l§m§kZ";
    ResultSet resultSet = null;
    PreparedStatement preparedStatement = null;
    
    try
    {
      if ((this._connection == null) || (this._connection.isClosed())) {
        this._connection = DriverManager.getConnection(this._connectionString, this._userName, this._password);
      }
      preparedStatement = this._connection.prepareStatement(RETRIEVE_MOTD);
      resultSet = preparedStatement.executeQuery();
      
      if (resultSet.next())
      {
        return resultSet.getString(1);
      }
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    finally
    {
      if (preparedStatement != null)
      {
        try
        {
          preparedStatement.close();
        }
        catch (SQLException e)
        {
          e.printStackTrace();
        }
      }
      
      if (resultSet != null)
      {
        try
        {
          resultSet.close();
        }
        catch (SQLException e)
        {
          e.printStackTrace();
        }
      }
    }
    if (preparedStatement != null)
    {
      try
      {
        preparedStatement.close();
      }
      catch (SQLException e)
      {
        e.printStackTrace();
      }
    }
    
    if (resultSet != null)
    {
      try
      {
        resultSet.close();
      }
      catch (SQLException e)
      {
        e.printStackTrace();
      }
    }
    

    return motd;
  }
}
