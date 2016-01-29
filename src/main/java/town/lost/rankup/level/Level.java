package town.lost.rankup.level;

import org.bukkit.configuration.ConfigurationSection;
import town.lost.rankup.commodity.Commodities;
import town.lost.rankup.commodity.Commodity;

/**
 * Created by peter on 29/01/16.
 */
public class Level {
    private final String name;
    private final Level prev;
    private final Commodity commodity;
    private final int cost;
    private final int increment;
    private final int max;

    public Level(String name, ConfigurationSection cs, Commodities commodities, Level prev) {
        this.name = name;
        this.prev = prev;

        cost = cs.getInt("cost", 64);
        increment = cs.getInt("increment", 64);
        String commodityName = cs.getString("commodity");
        if (commodityName == null || commodityName.trim().isEmpty())
            throw new IllegalArgumentException("Could not find commodity " + commodityName + " for " + name);
        commodity = commodities.getCommodity(commodityName);
        if (commodity == null)
            throw new IllegalArgumentException("Could not find commodity " + commodityName + " for " + name);
        max = cs.getInt("max", 64);
    }

    public String getName() {
        return name;
    }

    public Level getPrev() {
        return prev;
    }

    public Commodity getCommodity() {
        return commodity;
    }

    public int getCost() {
        return cost;
    }

    public int getIncrement() {
        return increment;
    }

    public int getMax() {
        return max;
    }
}
