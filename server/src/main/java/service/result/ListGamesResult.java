package service.result;

import model.GameData;
import java.util.Collection;

/**
 * Data returned containing a list of all games.
 *
 * @param games the collection of active games
 */
public record ListGamesResult(Collection<GameData> games) {
}
