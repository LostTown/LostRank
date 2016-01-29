package town.lost.rankup;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import town.lost.rankup.commodity.Commodities;
import town.lost.rankup.level.Level;
import town.lost.rankup.level.Levels;

import java.io.*;

/**
 * Created by peter on 28/01/16.
 */
public class RankupConfig {
    private final YamlConfiguration yc = new YamlConfiguration();
    private final Commodities commodities;
    private final Levels levels;

    public RankupConfig(IRankup rankup, String filename) throws IOException, InvalidConfigurationException {
        this(rankup.getDataFolder(), filename);
    }

    public RankupConfig(File dataFolder, String filename) throws IOException, InvalidConfigurationException {
        final File configFile = new File(dataFolder, filename);
        if (!configFile.canRead() || configFile.length() < 3)
            loadTemplate(configFile, filename);
        yc.load(configFile);

        System.out.println("Sections " + yc.getKeys(false));
        commodities = new Commodities(yc);
        levels = new Levels(yc, commodities);
    }


    private void loadTemplate(File configFile, String filename) throws IOException {
        File parentFile = configFile.getParentFile();
        if (parentFile != null)
            parentFile.mkdirs();
        try (InputStream in = RankupConfig.class.getClassLoader().getResourceAsStream(filename);
             OutputStream out = new FileOutputStream(configFile)) {
            if (in == null) {
                throw new FileNotFoundException("Unable to find resource " + filename);
            }
            byte[] bytes = new byte[1024];
            for (int len; (len = in.read(bytes)) > 0; )
                out.write(bytes, 0, len);
        }
        System.out.println("Copied " + filename + " from jar");
    }

    public Commodities getCommodities() {
        return commodities;
    }

    public String levelCost(String levelStr) {
        return levels.levelCost(levelStr);
    }

    public String levelNeed(String levelStr) {
        String[] parts = levelStr.split(" +", 2);
        Level level = getLevel(parts[0]);
        int sublevel = Integer.parseInt(parts[1]);
        return levelNeed(level, sublevel);
    }

    public String levelNeed(Level level, int subLevel) {
        if (subLevel > 1)
            return level.getName() + " " + (subLevel - 1);
        Level prev = level.getPrev();
        if (prev == null)
            return defaultLevel();
        return prev + " " + prev.getMax();
    }

    public Level getLevel(String level) {
        return levels.getLevel(level);
    }

    public String defaultLevel() {
        return levels.defaultLevel();
    }

    public boolean canLevelUp(String playerLevel, Level level, int sublevel) {
        return levels.canLevelUp(playerLevel, level, sublevel);
    }

    public String nextLevel(String playerLevel) {
        return null;
    }
}
