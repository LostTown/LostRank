package town.lost.rankup.commodity;

/**
 * Created by peter on 28/01/16.
 */
public class Barter {
    private final int buyQ;
    private final Commodity buy;
    private final int sellQ;
    private final Commodity sell;
    private final double ratio;

    public Barter(int buyQ, Commodity buy, int sellQ, Commodity sell, double ratio) {
        this.buyQ = buyQ;
        this.buy = buy;
        this.sellQ = sellQ;
        this.sell = sell;
        this.ratio = ratio;
    }

    @Override
    public String toString() {
        return "Barter{" + buyQ +
                " * " + buy.getMaterialData() +
                " for " + sellQ +
                " * " + sell.getMaterialData() +
                " r: " + ratio +
                '}';
    }

    public int getBuyQ() {
        return buyQ;
    }

    public Commodity getBuy() {
        return buy;
    }

    public int getSellQ() {
        return sellQ;
    }

    public Commodity getSell() {
        return sell;
    }

    public double getRatio() {
        return ratio;
    }
}
