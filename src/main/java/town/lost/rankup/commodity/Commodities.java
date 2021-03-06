package town.lost.rankup.commodity;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.util.noise.SimplexOctaveGenerator;

import java.util.*;

/**
 * Created by peter on 28/01/16.
 */
public class Commodities {
    private final Map<String, Commodity> commodities = new HashMap<>();
    private final List<Commodity> forBuying = new ArrayList<>();
    private final List<Commodity> forSelling = new ArrayList<>();
    private final List<Commodity> forCurrency = new ArrayList<>();
    private final SimplexOctaveGenerator sog = new SimplexOctaveGenerator(1L, 8);
    private final Random rand = new Random();

    public Commodities(YamlConfiguration yc) {
        sog.setScale(1.0 / 64);
        sog.setWScale(1.0 / 2);
        ConfigurationSection commoditiesConf = yc.getConfigurationSection("commodities");

        int counter = 0;
        for (String s : commoditiesConf.getKeys(false)) {
            ConfigurationSection cs = commoditiesConf.getConfigurationSection(s);
            Commodity c = new Commodity(s, cs, counter++);
            commodities.put(s.toUpperCase(), c);
            if (c.isCurrency())
                forCurrency.add(c);
            if (c.getBuyPrice() > 0)
                forBuying.add(c);
            if (c.getSellPrice() > 0)
                forSelling.add(c);
        }
        forCurrency.sort(Comparator.comparing(Commodity::getBuyPrice));
        ;
    }

    public List<Commodity> getForBuying() {
        return forBuying;
    }

    public List<Commodity> getForSelling() {
        return forSelling;
    }

    public List<Commodity> getForCurrency() {
        return forCurrency;
    }

    public Barter barterAt(int mx, int mz, int row, boolean buy0) {
        int maxSize = Math.min(10000, (Math.abs(mx) + Math.abs(mz)) / 5);
        for (int i = 0; i < 100; i++) {
            int size = rand.nextInt(maxSize + 20) + 5;
            Commodity buy;
            Commodity sell;
            int maxCurrency = (Math.abs(mx) + Math.abs(mz)) / 20 + row;
            Commodity currency = forCurrency.get(rand.nextInt(1 + Math.min(maxCurrency, forCurrency.size() - 1)));
            if (buy0) {
                // buying
                buy = forBuying.get(rand.nextInt(forBuying.size()));
                sell = currency;
            } else {
                buy = currency;
                sell = forSelling.get(rand.nextInt(forSelling.size()));
            }
            if (buy == sell)
                continue;
            if (buy.getRarity() < 1 && buy.getRarity() < rand.nextFloat())
                continue;
            if (sell.getRarity() < 1 && sell.getRarity() < rand.nextFloat())
                continue;
            double buyP = 1.0 + sog.noise(mx, mz, buy.getId(), 0.5, 0.5, true) * 0.4 + i / 10.0;
            if (buy.getRarity() < 1)
                buyP /= Math.sqrt(buy.getRarity() + 0.01);
            double sellP = 1.0 + sog.noise(mx, mz, sell.getId(), 0.5, 0.5, true) * 0.4;
            int buyQ = (int) (size / buy.getBuyPrice() * buyP);
            int sellQ = (int) (size / sell.getSellPrice() * sellP);
            while (buyQ > buy.getMax() || (buyQ > 64 && sellQ > 64)) {
                buyQ = (buyQ + 1) / 2;
                sellQ = (sellQ + 1) / 2;
            }
            if (buyQ > 64 && buyQ <= 99)
                buyQ = 64;
            if (buyQ < 1 || buyQ > 64)
                continue;
            if (sellQ > 64 && sellQ <= 99)
                sellQ = 64;
            if (sellQ < 1 || sellQ > 64)
                continue;
            double buyV = buyQ * buy.getMidPrice();
            double sellV = sellQ * sell.getMidPrice();
            double ratio = Math.round(buyV / sellV * 1000) / 1e3;
            if (ratio < 2 && i < 3)
                continue;
            if (ratio < 1.5 && i < 5)
                continue;
            if (ratio < 1.0 && i < 7)
                continue;
            if (ratio < 0.8 && i < 10)
                continue;
            return new Barter(buyQ, buy, sellQ, sell, ratio);
        }
        throw new AssertionError();
    }

    public Commodity getCommodity(String name) {
        Commodity commodity = commodities.get(name.toUpperCase());
        if (commodity == null)
            throw new IllegalArgumentException("unable to find " + name);
        return commodity;
    }
}
