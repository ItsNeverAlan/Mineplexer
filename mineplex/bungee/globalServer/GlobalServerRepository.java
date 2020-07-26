package mineplex.bungee.globalServer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class GlobalServerRepository
{
  private String _connectionString = "jdbc:mysql://db.mineplex.com:3306/BungeeServers";
  private String _userName = "root";
  private String _password = "tAbechAk3wR7tuTh";
  
  private static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS BungeeServers (id INT NOT NULL AUTO_INCREMENT, address VARCHAR(256), updated LONG, players INT, maxPlayers INT, ram INT, maxRam INT, PRIMARY KEY (id));";
  
  public void initialize()
  {
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    
    try
    {
      connection = DriverManager.getConnection(this._connectionString, this._userName, this._password);
      

      preparedStatement = connection.prepareStatement(CREATE_TABLE);
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
      
      if (connection != null)
      {
        try
        {
          connection.close();
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
      
      if (connection != null)
      {
        try
        {
          connection.close();
        }
        catch (SQLException e)
        {
          e.printStackTrace();
        }
      }
    }
  }
}
