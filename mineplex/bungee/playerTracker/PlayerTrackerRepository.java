package mineplex.bungee.playerTracker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerTrackerRepository
{
  private String _connectionString = "jdbc:mysql://db.mineplex.com:3306/PlayerTracker";
  private String _userName = "root";
  private String _password = "tAbechAk3wR7tuTh";
  
  private static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS PlayerTracker (id INT NOT NULL AUTO_INCREMENT, name VARCHAR(16), server VARCHAR(255), PRIMARY KEY (id));";
  private static String INSERT_PLAYER_SERVER = "INSERT INTO PlayerTracker values(default, ?, ?);";
  private static String UPDATE_PLAYER_SERVER = "UPDATE PlayerTracker SET server = ? WHERE name = ?;";
  private static String DELETE_PLAYER = "DELETE FROM PlayerTracker WHERE name = ?;";
  private static String RETRIEVE_PLAYER_SERVER = "SELECT server FROM PlayerTracker WHERE name = ?;";
  
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
    
    System.out.println("Initialized PlayerTracker.");
  }
  
  public boolean updatePlayerServer(String name, String server)
  {
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    
    try
    {
      connection = DriverManager.getConnection(this._connectionString, this._userName, this._password);
      
      preparedStatement = connection.prepareStatement(UPDATE_PLAYER_SERVER);
      
      preparedStatement.setString(1, server);
      preparedStatement.setString(2, name);
      
      int affectedRows = preparedStatement.executeUpdate();
      
      if (affectedRows == 0)
      {
        preparedStatement = connection.prepareStatement(INSERT_PLAYER_SERVER);
        
        preparedStatement.setString(1, name);
        preparedStatement.setString(2, server);
        
        affectedRows = preparedStatement.executeUpdate();
        
        if (affectedRows == 0)
        {
          throw new SQLException("Updating player server failed, no rows affected.");
        }
      }
      
      return true;
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
      return false;
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
  
  public String retrievePlayerServer(String name)
  {
    Connection connection = null;
    ResultSet resultSet = null;
    PreparedStatement preparedStatement = null;
    
    try
    {
      connection = DriverManager.getConnection(this._connectionString, this._userName, this._password);
      
      preparedStatement = connection.prepareStatement(RETRIEVE_PLAYER_SERVER);
      preparedStatement.setString(1, name);
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
    

    return "Lobby";
  }
  
  public boolean removePlayer(String name)
  {
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    
    try
    {
      connection = DriverManager.getConnection(this._connectionString, this._userName, this._password);
      
      preparedStatement = connection.prepareStatement(DELETE_PLAYER);
      
      preparedStatement.setString(1, name);
      
      int affectedRows = preparedStatement.executeUpdate();
      
      if (affectedRows == 0)
      {
        throw new SQLException("Updating player server failed, no rows affected.");
      }
      
      return true;
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
      return false;
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
