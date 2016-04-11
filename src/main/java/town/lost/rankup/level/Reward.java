package town.lost.rankup.level;

import town.lost.rankup.commodity.Commodities;
import town.lost.rankup.commodity.Commodity;

/**
 * Created by peter on 11/04/16.
 */
public class Reward {

    public static final Reward NONE = new Reward(0, null);

    private final int number;
    private final Commodity commodity;

    public Reward(int number, Commodity commodity) {
        this.number = number;

        this.commodity = commodity;
    }

    public static Reward parse(String s, Commodities commodities) {
        String[] parts = s.split(" ");
        int number = Integer.parseInt(parts[0]);
        Commodity commodity = commodities.getCommodity(parts[1]);
        return new Reward(number, commodity);
    }

    public int getNumber() {
        return number;
    }

    public Commodity getCommodity() {
        return commodity;
    }
}
