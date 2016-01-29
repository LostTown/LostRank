package town.lost.rankup;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import town.lost.rankup.level.Level;

import java.io.File;
import java.io.IOException;

/**
 * Created by peter on 28/01/16.
 */
public class UserConfig {
    private final YamlConfiguration yc = new YamlConfiguration();
    private final File configFile;

    public UserConfig(IRankup rankup, String filename) throws IOException, InvalidConfigurationException {
        configFile = new File(rankup.getDataFolder(), filename);

        if (configFile.canRead() && configFile.length() >= 0) {
            yc.load(configFile);
        }
    }

    public void update() {
        try {
            File tmp = new File(configFile + ".tmp");
            yc.save(tmp);
            tmp.renameTo(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getLevel(Player player, String defaultLevel) {
        ConfigurationSection configurationSection = yc.getConfigurationSection(player.getName());
        if (configurationSection == null)
            return defaultLevel;
        String level = configurationSection.getString("level");
        return level == null || level.trim().isEmpty() ? defaultLevel : level;
    }

    public void levelUp(Player player, Level level, int sublevel) {
        String name = player.getName();
        ConfigurationSection configurationSection = yc.getConfigurationSection(name);
        if (configurationSection == null) {
            configurationSection = yc.createSection(name);
        }
        configurationSection.set("level", level.getName() + " " + sublevel);
    }
}
