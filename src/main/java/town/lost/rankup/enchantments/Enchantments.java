package town.lost.rankup.enchantments;


import org.bukkit.enchantments.Enchantment;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by peter on 11/04/16.
 */
public enum Enchantments {
    ;
    public static final Map<String, Enchantment> ENCHANTMENTS = new LinkedHashMap<String, Enchantment>() {{
        put(("protection"), Enchantment.PROTECTION_ENVIRONMENTAL);
        put(("fire_protection"), Enchantment.PROTECTION_FIRE);
        put(("feather_falling"), Enchantment.PROTECTION_FALL);
        put(("blast_protection"), Enchantment.PROTECTION_EXPLOSIONS);
        put(("projectile_protection"), Enchantment.PROTECTION_PROJECTILE);
        put(("respiration"), Enchantment.OXYGEN);
        put(("aqua_affinity"), Enchantment.WATER_WORKER);
        put(("thorns"), Enchantment.THORNS);
        put(("depth_strider"), Enchantment.DEPTH_STRIDER);
        put(("frost_walker"), Enchantment.FROST_WALKER);
        put(("sharpness"), Enchantment.DAMAGE_ALL);
        put(("smite"), Enchantment.DAMAGE_UNDEAD);
        put(("bane_of_arthropods"), Enchantment.DAMAGE_ARTHROPODS);
        put(("knockback"), Enchantment.KNOCKBACK);
        put(("fire_aspect"), Enchantment.FIRE_ASPECT);
        put(("looting"), Enchantment.LOOT_BONUS_MOBS);
        put(("efficiency"), Enchantment.DIG_SPEED);
        put(("silk_touch"), Enchantment.SILK_TOUCH);
        put(("unbreaking"), Enchantment.DURABILITY);
        put(("fortune"), Enchantment.LOOT_BONUS_BLOCKS);
        put(("power"), Enchantment.ARROW_DAMAGE);
        put(("punch"), Enchantment.ARROW_KNOCKBACK);
        put(("flame"), Enchantment.ARROW_FIRE);
        put(("infinity"), Enchantment.ARROW_INFINITE);
        put(("luck_of_the_sea"), Enchantment.LUCK);
        put(("lure"), Enchantment.LURE);
        put(("mending"), Enchantment.MENDING);
    }};

    public static Enchantment getEnchantment(String name) {
        return ENCHANTMENTS.get(name);
    }

    public static void main(String[] args) {
        net.minecraft.server.v1_10_R1.Enchantment.f();
        for (String s : ENCHANTMENTS.keySet()) {
            net.minecraft.server.v1_10_R1.Enchantment e = net.minecraft.server.v1_10_R1.Enchantment.b(s);
            System.out.println("# " + s + ": " + e.getStartLevel() + " - " + e.getMaxLevel());
        }
    }
}
