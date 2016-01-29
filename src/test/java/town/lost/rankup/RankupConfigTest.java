package town.lost.rankup;

import org.bukkit.configuration.InvalidConfigurationException;
import org.junit.Test;
import town.lost.rankup.commodity.Barter;
import town.lost.rankup.commodity.Commodities;

import java.io.File;
import java.io.IOException;

/**
 * Created by peter on 28/01/16.
 */
public class RankupConfigTest {
    @Test
    public void loadConfig() throws IOException, InvalidConfigurationException {
        RankupConfig rc = new RankupConfig(new File("src/main/resources"), "config.yml");
        Commodities commodities = rc.getCommodities();
        System.out.println("Currency");
        commodities.getForCurrency().forEach(System.out::println);

        System.out.println("\nBuying");
        commodities.getForBuying().forEach(System.out::println);

        System.out.println("\nSelling");
        commodities.getForSelling().forEach(System.out::println);

        double totalRatio = 0;
        int count = 0;
        for (int x = -400; x <= 400; x += 50) {
            int z = x;
            System.out.println("x: " + x + ", z: " + z);
            for (int i = 0; i < 10; i++) {
                Barter x1 = commodities.barterAt(x + i, z + i);
                System.out.println(x1);
                totalRatio += x1.getRatio();
                count++;
            }
            System.out.println();
        }
        System.out.println("Avg ratio=" + totalRatio / count);
    }

}