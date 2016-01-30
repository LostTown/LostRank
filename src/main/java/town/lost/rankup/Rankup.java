package town.lost.rankup;

import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import town.lost.rankup.commodity.Barter;
import town.lost.rankup.commodity.Commodity;
import town.lost.rankup.level.Level;
import town.lost.rankup.sign.SignBlockListener;
import town.lost.rankup.sign.SignPlayerListener;

import java.util.logging.Logger;

/**
 * Created by peter on 27/01/16.
 */
public class Rankup extends JavaPlugin implements IRankup {
    private static final Logger LOGGER = Logger.getLogger(Rankup.class.getName());
    private RankupConfig config;
    private UserConfig users;


    @Override
    public void onEnable() {
        LOGGER.info("onEnable");
        PluginManager pm = getServer().getPluginManager();

        final SignBlockListener signBlockListener = new SignBlockListener(this);
        pm.registerEvents(signBlockListener, this);

        final SignPlayerListener signPlayerListener = new SignPlayerListener(this);
        pm.registerEvents(signPlayerListener, this);

        try {
            config = new RankupConfig(this, "config.yml");
            users = new UserConfig(this, "users.yml");
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }

    @Override
    public void onDisable() {
        LOGGER.info("onDisable");
    }

    @Override
    public Commodity getCommodity(String name) {
        return config.getCommodities().getCommodity(name);
    }

    @Override
    public Barter createBarterFor(int mx, int mz, int row, boolean buy) {
        return config.getCommodities().barterAt(mx, mz, row, buy);
    }

    @Override
    public String levelCost(String levelStr) {
        return config.levelCost(levelStr);
    }

    @Override
    public Level getLevel(String s) {
        return config.getLevel(s);
    }

    @Override
    public boolean canLevelUp(Player player, Level level, int sublevel) {
        String playerLevel = users.getLevel(player, config.defaultLevel());
        return config.canLevelUp(playerLevel, level, sublevel);
    }

    @Override
    public void levelUp(Player player, Level level, int sublevel) {
        users.levelUp(player, level, sublevel);
    }

    @Override
    public String levelNeeded(Level level, int sublevel) {
        return config.levelNeed(level, sublevel);
    }

    @Override
    public String nextLevelFor(Player player) {
        return config.nextLevel(getLevelFor(player));
    }

    @Override
    public String getLevelFor(Player player) {
        return users.getLevel(player, config.defaultLevel());
    }
}
