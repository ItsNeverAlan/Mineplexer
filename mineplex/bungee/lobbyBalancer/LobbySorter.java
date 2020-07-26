package mineplex.bungee.lobbyBalancer;

import java.util.Comparator;

public class LobbySorter
  implements Comparator<ServerStatusData>
{
  public int compare(ServerStatusData first, ServerStatusData second)
  {
    if (second.Players == 999) {
      return -1;
    }
    if (first.Players == 999) {
      return 1;
    }
    if ((first.MaxPlayers - first.Players > 15) && (second.MaxPlayers - second.Players <= 15)) {
      return -1;
    }
    if ((second.MaxPlayers - second.Players > 15) && (first.MaxPlayers - first.Players <= 15)) {
      return 1;
    }
    if ((first.Players < first.MaxPlayers / 2) && (second.Players >= second.MaxPlayers / 2)) {
      return -1;
    }
    if ((second.Players < second.MaxPlayers / 2) && (first.Players >= first.MaxPlayers / 2)) {
      return 1;
    }
    if (first.Players < first.MaxPlayers / 2)
    {
      if (first.Players > second.Players) {
        return -1;
      }
      if (second.Players > first.Players) {
        return 1;
      }
    }
    else {
      if (first.Players < second.Players) {
        return -1;
      }
      if (second.Players < first.Players) {
        return 1;
      }
    }
    if (Integer.parseInt(first.Name.split("-")[1]) < Integer.parseInt(second.Name.split("-")[1])) {
      return -1;
    }
    return 1;
  }
}
