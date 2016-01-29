package town.lost.rankup.commodity;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.material.MaterialData;

/**
 * Created by peter on 28/01/16.
 */
public class Commodity {
    private final String name;
    private final int id;
    private final MaterialData materialData;
    private final double buyPrice;
    private final double sellPrice;
    private final double rarity;
    private final int max;
    private final boolean currency;

    public Commodity(String name, ConfigurationSection cs, int id) {
        this.name = name;
        this.id = id;
        String material = cs.getString("material");
        int data = cs.getInt("data", 0);
        materialData = new MaterialData(Material.valueOf(material.toUpperCase()));
        materialData.setData((byte) data);

        buyPrice = cs.getDouble("buy", Double.NaN);
        sellPrice = cs.getDouble("sell", Double.NaN);
        max = cs.getInt("max", 64);
        rarity = cs.getDouble("rarity", 1.0);
        currency = cs.getBoolean("currency", false);
    }

    public String getName() {
        return name;
    }

    public MaterialData getMaterialData() {
        return materialData;
    }

    public double getBuyPrice() {
        return buyPrice;
    }

    public double getSellPrice() {
        return sellPrice;
    }

    public int getId() {
        return id;
    }

    public double getMidPrice() {
        if (Double.isNaN(buyPrice))
            return sellPrice / 2;
        if (Double.isNaN(sellPrice))
            return buyPrice;
        return (buyPrice + sellPrice) / 2;
    }

    public double getRarity() {
        return rarity;
    }

    public int getMax() {
        return max;
    }

    @Override
    public String toString() {
        return "Commodity{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", materialData=" + materialData +
                ", buyPrice=" + buyPrice +
                ", sellPrice=" + sellPrice +
                '}';
    }

    public boolean isCurrency() {
        return currency;
    }
}
