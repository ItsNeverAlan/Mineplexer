package mineplex.bungee.playerStats;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PlayerStatsRepository
{
  private Connection _connection = null;
  private String _connectionString = "jdbc:mysql://sqlstats.mineplex.com:3306/PlayerStats?autoReconnect=true&failOverReadOnly=false&maxReconnects=10";
  private String _userName = "root";
  private String _password = "tAbechAk3wR7tuTh";
  
  private static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS DailyUnique (id INT NOT NULL AUTO_INCREMENT, day VARCHAR(100), playerName VARCHAR(20), PRIMARY KEY (id), UNIQUE KEY unique_player_per_day (day, playerName));";
  private static String INSERT_PLAYER = "INSERT INTO DailyUnique (day, playerName) values(curdate(), ?) ON DUPLICATE KEY UPDATE playerName=playerName;";
  
  private static String CREATE_VER_TABLE = "CREATE TABLE IF NOT EXISTS PlayerVersion (id INT NOT NULL AUTO_INCREMENT, playerName VARCHAR(20), version VARCHAR(40), PRIMARY KEY (id), UNIQUE KEY unique_player (playerName));";
  private static String INSERT_VER_PLAYER = "INSERT INTO PlayerVersion (playerName, version) values(?, ?) ON DUPLICATE KEY UPDATE version=version;";
  
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
      
      preparedStatement.close();
      
      preparedStatement = this._connection.prepareStatement(CREATE_VER_TABLE);
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
    
    System.out.println("Initialized PlayerStats.");
  }
  
  public boolean addPlayer(String playerName)
  {
    PreparedStatement preparedStatement = null;
    
    try
    {
      if ((this._connection == null) || (this._connection.isClosed())) {
        this._connection = DriverManager.getConnection(this._connectionString, this._userName, this._password);
      }
      preparedStatement = this._connection.prepareStatement(INSERT_PLAYER, 1);
      
      preparedStatement.setString(1, playerName);
      
      int affectedRows = preparedStatement.executeUpdate();
      
      if (affectedRows == 0)
      {
        throw new SQLException("Adding unique player record failed, no rows affected.");
      }
      
      return true;
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
      
      try
      {
        Thread.sleep(10L);
      }
      catch (InterruptedException e)
      {
        e.printStackTrace();
      }
      
      return addPlayer(playerName);
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
  }
  
  public boolean addPlayerVersion(String playerName, String version)
  {
    PreparedStatement preparedStatement = null;
    
    try
    {
      if ((this._connection == null) || (this._connection.isClosed())) {
        this._connection = DriverManager.getConnection(this._connectionString, this._userName, this._password);
      }
      preparedStatement = this._connection.prepareStatement(INSERT_VER_PLAYER, 1);
      
      preparedStatement.setString(1, playerName);
      preparedStatement.setString(2, version);
      
      int affectedRows = preparedStatement.executeUpdate();
      
      if (affectedRows == 0)
      {
        throw new SQLException("Adding player version record failed, no rows affected.");
      }
      
      return true;
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
      
      try
      {
        Thread.sleep(10L);
      }
      catch (InterruptedException e)
      {
        e.printStackTrace();
      }
      
      return addPlayer(playerName);
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
  }
}
