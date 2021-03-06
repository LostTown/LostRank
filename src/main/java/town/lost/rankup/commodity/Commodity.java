package town.lost.rankup.commodity;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.material.MaterialData;
import town.lost.rankup.enchantments.Enchantments;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by peter on 28/01/16.
 */
public class Commodity {
    private static final Set<String> KEYS = new HashSet<>(Arrays.asList("material,data,buy,sell,max,rarity,currency".split(",")));
    private final String name;
    private final int id;
    private final MaterialData materialData;
    private final double buyPrice;
    private final double sellPrice;
    private final double rarity;
    private final int max;
    private final boolean currency;
    private final Map<Enchantment, Integer> enchant;

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
        enchant = Enchantments.ENCHANTMENTS.keySet().stream()
                .filter(cs::isSet)
                .collect(Collectors.toMap(Enchantments::getEnchantment, cs::getInt));
        for (String s : cs.getKeys(false)) {
            if (!Enchantments.ENCHANTMENTS.keySet().contains(s) && !KEYS.contains(s)) {
                System.out.println("Commodity: " + name + " has an attribute which is ignored: " + s);
            }
        }
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
            return sellPrice;
        if (Double.isNaN(sellPrice))
            return buyPrice * 0.6;
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
                (enchant.isEmpty() ? "" : ", enchant=" + enchant) +
                '}';
    }

    public boolean isCurrency() {
        return currency;
    }

    public Map<Enchantment, Integer> getEnchant() {
        return enchant;
    }
}
