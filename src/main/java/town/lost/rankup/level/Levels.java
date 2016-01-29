package town.lost.rankup.level;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import town.lost.rankup.commodity.Commodities;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by peter on 29/01/16.
 */
public class Levels {
    private final Map<String, Level> levelMap = new HashMap<>();
    private final String defaultLevel;

    public Levels(YamlConfiguration yc, Commodities commodities) {
        ConfigurationSection commoditiesConf = yc.getConfigurationSection("levels");
        Level prev = null;
        String defaultLevel = "none";
        for (String s : commoditiesConf.getKeys(false)) {
            ConfigurationSection cs = commoditiesConf.getConfigurationSection(s);
            Level level = new Level(s, cs, commodities, prev);
            levelMap.put(s, level);
            prev = level;
            if (defaultLevel.equals("none"))
                defaultLevel = s + " " + 1;
        }
        this.defaultLevel = defaultLevel;
    }

    public String levelCost(String levelStr) {
        String[] parts = levelStr.split(" +", 2);
        Level level = getLevel(parts[0]);
        int sublevel = Integer.parseInt(parts[1]);
        int cost = level.getCost() + level.getIncrement() * (sublevel - 1);
        return cost + " " + level.getCommodity().getName();
    }

    public Level getLevel(String level) {
        Level level2 = levelMap.get(level);
        if (level2 == null)
            throw new IllegalArgumentException("Unbale to find level " + level);
        return level2;
    }

    public String defaultLevel() {
        return defaultLevel;
    }

    public boolean canLevelUp(String playerLevel, Level level, int sublevel) {
        String[] parts = playerLevel.split(" +", 2);
        Level level0 = getLevel(parts[0]);
        int sublevel0 = Integer.parseInt(parts[1]);
        // next sub-level
        return level == level0 && sublevel == sublevel0 + 1
                // next level after reaching max
                || sublevel == 1 && level0.getPrev() == level && sublevel0 >= level.getMax();
    }
}
