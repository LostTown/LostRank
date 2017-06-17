package town.lost.rankup.sign;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import town.lost.rankup.IRankup;


public class SignBlockListener implements Listener {

    public SignBlockListener(IRankup ru) {
    }

    @EventHandler()
    public void onPlayerBreakBlock(BlockBreakEvent event) {
        Block block = event.getBlock();
        System.out.println("BreakBlock " + block.getType());
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onSignSignChange2(final SignChangeEvent event) {
        final String topLine = event.getLine(0);
        if (topLine.startsWith("(") && topLine.endsWith(")")) {
            String toUpper = topLine.substring(1, topLine.length() - 1).toUpperCase();
            try {
                RankSign.valueOf(toUpper);
                event.setLine(0, "§1" + topLine);
            } catch (IllegalArgumentException ignored) {
                // ignored
            }
        }
    }
}
