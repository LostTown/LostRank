package town.lost.rankup;

import org.bukkit.entity.Player;
import town.lost.rankup.commodity.Barter;
import town.lost.rankup.commodity.Commodity;
import town.lost.rankup.level.Level;

import java.io.File;

/**
 * Created by peter on 27/01/16.
 */
public interface IRankup {
    File getDataFolder();

    Commodity getCommodity(String name);

    Barter createBarterFor(int mx, int mz);

    String levelCost(String levelStr);

    Level getLevel(String s);

    boolean canLevelUp(Player player, Level level, int sublevel);

    void levelUp(Player player, Level level, int sublevel);

    String levelNeeded(Level level, int sublevel);

    String nextLevelFor(Player player);
}
