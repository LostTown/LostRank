package town.lost.rankup.sign;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import town.lost.rankup.IRankup;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;


public class SignPlayerListener implements Listener {
    private static final Logger LOGGER = Logger.getLogger(SignBlockListener.class.getName());
    private final IRankup ru;

    public SignPlayerListener(final IRankup ru) {
        this.ru = ru;
    }

    //This following code below listens to cancelled events to fix a bukkit issue
    //Right clicking signs with a block in hand, can now fire cancelled events.
    //This is because when the block place is cancelled (for example not enough space for the block to be placed),
    //the event will be marked as cancelled, thus preventing 30% of sign purchases.
    @EventHandler(priority = EventPriority.LOW)
    public void onSignPlayerInteract(final PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.RIGHT_CLICK_AIR) {
            return;
        }
        final Block block;
        if (event.isCancelled() && event.getAction() == Action.RIGHT_CLICK_AIR) {
            try {
                block = event.getPlayer().getTargetBlock((Set<Material>) null, 5);
            } catch (Exception ex) {
                LOGGER.log(Level.WARNING, ex.getMessage(), ex);
                return;
            }
        } else {
            block = event.getClickedBlock();
        }
        if (block == null) {
            return;
        }

        final Material mat = block.getType();
        if (mat == Material.SIGN_POST || mat == Material.WALL_SIGN) {
            Sign sign = (Sign) block.getState();
            final String csign = sign.getLine(0);
            LOGGER.info("onSignPlayerInteract " + sign + " '" + csign + "'");
            if (csign.startsWith("§1(") && csign.endsWith(")")) {
                String name = csign.substring(3, csign.length() - 1).toUpperCase();
                LOGGER.info("... name " + name);
                try {
                    RankSign rankSign = RankSign.valueOf(name);
                    LOGGER.info("... rankSign " + rankSign);
                    rankSign.onSignInteract(block, sign, event.getPlayer(), ru);
                    LOGGER.info("... done");
                    event.setCancelled(true);
                } catch (IllegalArgumentException e) {
                    LOGGER.info("... invalid sign " + e);
                }
            }
        }
    }
}
