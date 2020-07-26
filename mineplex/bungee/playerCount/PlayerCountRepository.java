package mineplex.bungee.playerCount;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerCountRepository
{
  private Connection _connection = null;
  private String _connectionString = "jdbc:mysql://db.mineplex.com:3306/BungeeServers?autoReconnect=true&failOverReadOnly=false&maxReconnects=10";
  private String _userName = "root";
  private String _password = "tAbechAk3wR7tuTh";
  
  private static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS BungeeServers (id INT NOT NULL AUTO_INCREMENT, address VARCHAR(256), updated LONG, players INT, maxPlayers INT, ram INT, maxRam INT, PRIMARY KEY (id));";
  private static String INSERT_PLAYER_COUNT = "INSERT INTO BungeeServers(address, updated, players, maxPlayers, ram, maxRam) values(?, now(), ?, ?, ?, ?);";
  private static String UPDATE_PLAYER_COUNT = "UPDATE BungeeServers SET updated = now(), players = ?, maxPlayers = ?, ram = ?, maxRam = ? WHERE id = ?;";
  private static String RETRIEVE_ID = "SELECT id FROM BungeeServers WHERE address = ?;";
  private static String RETRIEVE_PLAYER_COUNT = "SELECT SUM(players) AS playerCount, SUM(maxPlayers) AS maxPlayerCount FROM BungeeServers WHERE TIME_TO_SEC(TIMEDIFF(now(), BungeeServers.updated)) < 10;";
  
  private int _id = -1;
  private String _address;
  private int _maxPlayers = 0;
  
  public PlayerCountRepository(String address, int maxPlayers)
  {
    this._address = address;
    this._maxPlayers = maxPlayers;
  }
  
  public void initialize()
  {
    ResultSet resultSet = null;
    PreparedStatement preparedStatement = null;
    PreparedStatement preparedStatementRetrieve = null;
    PreparedStatement preparedStatementInsert = null;
    
    try
    {
      if ((this._connection == null) || (this._connection.isClosed())) {
        this._connection = DriverManager.getConnection(this._connectionString, this._userName, this._password);
      }
      
      preparedStatement = this._connection.prepareStatement(CREATE_TABLE);
      preparedStatement.execute();
      


      preparedStatementRetrieve = this._connection.prepareStatement(RETRIEVE_ID);
      preparedStatementRetrieve.setString(1, this._address);
      resultSet = preparedStatementRetrieve.executeQuery();
      
      while (resultSet.next())
      {
        this._id = resultSet.getInt("id");
      }
      

      if (this._id == -1)
      {
        preparedStatementInsert = this._connection.prepareStatement(INSERT_PLAYER_COUNT, 1);
        
        preparedStatementInsert.setString(1, this._address);
        preparedStatementInsert.setInt(2, 0);
        preparedStatementInsert.setInt(3, this._maxPlayers);
        preparedStatementInsert.setInt(4, (int)((Runtime.getRuntime().maxMemory() - Runtime.getRuntime().freeMemory()) / 1048576L));
        preparedStatementInsert.setInt(5, (int)(Runtime.getRuntime().maxMemory() / 1048576L));
        
        int affectedRows = preparedStatementInsert.executeUpdate();
        
        if (affectedRows == 0)
        {
          throw new SQLException("Creating bungee server failed, no rows affected.");
        }
        
        resultSet.close();
        resultSet = preparedStatementInsert.getGeneratedKeys();
        
        if (resultSet.next())
        {
          this._id = resultSet.getInt(1);
          System.out.println("id = " + this._id);
        }
      }
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
      
      if (preparedStatementRetrieve != null)
      {
        try
        {
          preparedStatementRetrieve.close();
        }
        catch (SQLException e)
        {
          e.printStackTrace();
        }
      }
      
      if (preparedStatementInsert != null)
      {
        try
        {
          preparedStatementInsert.close();
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
      
      if (preparedStatementRetrieve != null)
      {
        try
        {
          preparedStatementRetrieve.close();
        }
        catch (SQLException e)
        {
          e.printStackTrace();
        }
      }
      
      if (preparedStatementInsert != null)
      {
        try
        {
          preparedStatementInsert.close();
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
    
    System.out.println("Initialized PlayerCount.");
  }
  
  public boolean updatePlayerCountInDatabase(int players)
  {
    PreparedStatement preparedStatement = null;
    
    try
    {
      if ((this._connection == null) || (this._connection.isClosed())) {
        this._connection = DriverManager.getConnection(this._connectionString, this._userName, this._password);
      }
      preparedStatement = this._connection.prepareStatement(UPDATE_PLAYER_COUNT, 1);
      
      preparedStatement.setInt(1, players);
      preparedStatement.setInt(2, this._maxPlayers);
      preparedStatement.setInt(3, (int)((Runtime.getRuntime().maxMemory() - Runtime.getRuntime().freeMemory()) / 1048576L));
      preparedStatement.setInt(4, (int)(Runtime.getRuntime().maxMemory() / 1048576L));
      preparedStatement.setInt(5, this._id);
      
      int affectedRows = preparedStatement.executeUpdate();
      
      if (affectedRows == 0)
      {
        throw new SQLException("Updating bungee server player count failed, no rows affected.");
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
      
      return updatePlayerCountInDatabase(players);
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
  
  public PlayerTotalData retrievePlayerCount()
  {
    PlayerTotalData playerData = new PlayerTotalData();
    ResultSet resultSet = null;
    PreparedStatement preparedStatement = null;
    
    try
    {
      if ((this._connection == null) || (this._connection.isClosed())) {
        this._connection = DriverManager.getConnection(this._connectionString, this._userName, this._password);
      }
      preparedStatement = this._connection.prepareStatement(RETRIEVE_PLAYER_COUNT);
      resultSet = preparedStatement.executeQuery();
      
      if (resultSet.next())
      {
        playerData.CurrentPlayers = resultSet.getInt(1);
        playerData.MaxPlayers = resultSet.getInt(2);
        return playerData;
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
    

    return playerData;
  }
}
