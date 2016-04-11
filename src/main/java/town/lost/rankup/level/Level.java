package town.lost.rankup.level;

import org.bukkit.configuration.ConfigurationSection;
import town.lost.rankup.commodity.Commodities;
import town.lost.rankup.commodity.Commodity;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
    private final List<Reward> rewards;
    private Level next;

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

        if (cs.isList("rewards")) {
            rewards = cs.getStringList("rewards").stream().map(s -> Reward.parse(s, commodities)).collect(Collectors.toList());
        } else {
            rewards = Collections.emptyList();
        }

        if (prev != null && prev.next == null)
            prev.next = this;
    }

    public Level getNext() {
        return next;
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

    public Reward getReward(int sublevel) {
        return sublevel <= rewards.size() ? rewards.get(sublevel - 1) : Reward.NONE;
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
