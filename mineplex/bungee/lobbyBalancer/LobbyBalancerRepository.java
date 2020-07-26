package mineplex.bungee.lobbyBalancer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class LobbyBalancerRepository
{
  private Connection _connection = null;
  private String _connectionString = "jdbc:mysql://db.mineplex.com:3306/ServerStatus?autoReconnect=true&failOverReadOnly=false&maxReconnects=10";
  private String _userName = "root";
  private String _password = "tAbechAk3wR7tuTh";
  
  private boolean _us;
  private static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS ServerStatus (id INT NOT NULL AUTO_INCREMENT, serverName VARCHAR(256), serverGroup VARCHAR(256), address VARCHAR(256), port VARCHAR(11), updated LONG, motd VARCHAR(256), players INT, maxPlayers INT, tps INT, ram INT, maxRam INT, PRIMARY KEY (id));";
  private static String RETRIEVE_SERVER_STATUSES = "SELECT ServerStatus.serverName, ServerStatus.address, ServerStatus.port, motd, players, maxPlayers, now(), updated FROM ServerStatus INNER JOIN DynamicServers ON ServerStatus.address = DynamicServers.privateAddress WHERE DynamicServers.US = ?;";
  
  public void initialize(boolean us)
  {
    this._us = us;
    
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
    
    System.out.println("Initialized LobbyBalancer.");
  }
  
  public List<ServerStatusData> retrieveServerStatuses()
  {
    ResultSet resultSet = null;
    PreparedStatement preparedStatement = null;
    List<ServerStatusData> serverData = new java.util.ArrayList();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    try
    {
      if ((this._connection == null) || (this._connection.isClosed())) {
        this._connection = DriverManager.getConnection(this._connectionString, this._userName, this._password);
      }
      preparedStatement = this._connection.prepareStatement(RETRIEVE_SERVER_STATUSES);
      preparedStatement.setBoolean(1, this._us);
      
      resultSet = preparedStatement.executeQuery();
      
      while (resultSet.next())
      {
        ServerStatusData serverStatusData = new ServerStatusData();
        
        serverStatusData.Name = resultSet.getString(1);
        
        serverStatusData.Address = resultSet.getString(2);
        serverStatusData.Port = Integer.parseInt(resultSet.getString(3));
        serverStatusData.Motd = resultSet.getString(4);
        serverStatusData.Players = resultSet.getInt(5);
        serverStatusData.MaxPlayers = resultSet.getInt(6);
        
        long current = dateFormat.parse(resultSet.getString(7)).getTime();
        long updated = dateFormat.parse(resultSet.getString(8)).getTime();
        
        if (current - updated < 10000L) {
          serverData.add(serverStatusData);
        }
      }
    }
    catch (Exception exception) {
      exception.printStackTrace();
      
      try
      {
        Thread.sleep(10L);
      }
      catch (InterruptedException e)
      {
        e.printStackTrace();
      }
      
      List localList = retrieveServerStatuses();return localList;
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
    
    return serverData;
  }
}
