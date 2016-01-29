package town.lost.rankup.sign;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.material.MaterialData;
import town.lost.rankup.IRankup;
import town.lost.rankup.commodity.Barter;
import town.lost.rankup.commodity.Commodity;
import town.lost.rankup.level.Level;

/**
 * Created by peter on 27/01/16.
 */
@SuppressWarnings("unused")
public enum RankSign {
    ECHO {
        @Override
        public void onSignInteract(Block block, Sign sign, Player player, IRankup ru) {
            player.sendRawMessage("Echo: " + sign.getLine(1));
        }
    },
    PAY {
        @Override
        public void onSignInteract(Block block, Sign sign, Player player, IRankup ru) {
            PlayerInventory inventory = player.getInventory();
            String[] line1 = sign.getLine(1).split(" +", 2);
            int toRemove = Integer.parseInt(line1[0]);
            Commodity toRemoveComm = ru.getCommodity(line1[1]);
            if (canRemove(inventory, toRemove, toRemoveComm)) {
                remove(inventory, toRemove, toRemoveComm);
                player.sendMessage("§2Paid " + toRemove + " §6" + toRemoveComm.getName());
            } else {
                player.sendMessage("§4Not enough gold nuggets");
            }
        }
    },
    FLY {
        @Override
        public void onSignInteract(Block block, Sign sign, Player player, IRankup ru) {
            PlayerInventory inventory = player.getInventory();
            String[] line1 = sign.getLine(1).split(" +", 2);
            int toRemove = Integer.parseInt(line1[0]);
            Commodity toRemoveComm = ru.getCommodity(line1[1]);
            if (canRemove(inventory, toRemove, toRemoveComm)) {
                remove(inventory, toRemove, toRemoveComm);
                player.setAllowFlight(true);
                player.sendMessage("Flying enabled §2Paid " + toRemove + " §6" + toRemoveComm.getName());
            } else {
                player.sendMessage("§4Not enough gold nuggets");
            }
        }
    },
    BARTER {
        @Override
        public void onSignInteract(Block block, Sign sign, Player player, IRankup ru) {
            if (sign.getLine(1).trim().isEmpty() || player.isFlying()) {
                updateBarter(block, sign, ru);
                return;
            }

            PlayerInventory inventory = player.getInventory();
            String[] line1 = sign.getLine(1).split(" +", 2);
            String[] line2 = sign.getLine(2).split(" +", 2);
            int toAdd = Integer.parseInt(line1[0]);
            Commodity toAddComm = ru.getCommodity(line1[1]);
            int toRemove = Integer.parseInt(line2[0]);
            Commodity toRemoveComm = ru.getCommodity(line2[1]);

            if (!canRemove(inventory, toRemove, toRemoveComm)) {
                player.sendMessage("§4Not enough " + toRemoveComm.getName());
                return;
            }
            if (!canAdd(inventory, toAdd, toAddComm)) {
                player.sendMessage("§4Not enough space");
                return;
            }
            remove(inventory, toRemove, toRemoveComm);
            add(inventory, toAdd, toAddComm);
            player.sendMessage("§2You received  " + toAdd + " " + toAddComm.getName()
                    + " for §6" + toRemove + " " + toRemoveComm.getName());

            updateBarter(block, sign, ru);
        }
    },
    DAYTIME {
        public void onSignInteract(Block block, Sign sign, Player player, IRankup ru) {
            PlayerInventory inventory = player.getInventory();
            String[] line1 = sign.getLine(1).split(" +", 2);
            int toRemove = Integer.parseInt(line1[0]);
            Commodity toRemoveComm = ru.getCommodity(line1[1]);
            if (canRemove(inventory, toRemove, toRemoveComm)) {
                remove(inventory, toRemove, toRemoveComm);
                World world = player.getWorld();
                long time = world.getTime();
                time -= time % 24000;
                time += 24000;
                world.setTime(time);
                player.sendMessage("Time set 0, §2Paid " + toRemove + " §6" + toRemoveComm.getName());
            } else {
                player.sendMessage("§4Not enough " + toRemoveComm.getName());
            }
        }
    },
    RANK {
        public void onSignInteract(Block block, Sign sign, Player player, IRankup ru) {
            String levelStr = ru.getLevelFor(player);
            String nextLevelStr = ru.nextLevelFor(player);

            System.out.println(player.getName() + " is " + levelStr + " level.");
            player.sendMessage("§2You are " + levelStr + " level and the §6next level is " + nextLevelStr);
        }
    },
    TRAINING {
        @Override
        public void onSignInteract(Block block, Sign sign, Player player, IRankup ru) {
            if (sign.getLine(1).trim().isEmpty()) {
                String levelStr = ru.nextLevelFor(player);
                updateTraining(block, sign, levelStr, ru.levelCost(levelStr));
                return;
            }
            PlayerInventory inventory = player.getInventory();
            String[] line1 = sign.getLine(1).split(" +", 2);
            String[] line2 = sign.getLine(2).split(" +", 2);

            Level level = ru.getLevel(line1[0]);
            int sublevel = Integer.parseInt(line1[1]);

            if (!ru.canLevelUp(player, level, sublevel)) {
                player.sendMessage("§4You need to be a " + ru.levelNeeded(level, sublevel) + ", §2you are " + ru.getLevelFor(player));
                return;
            }

            int toRemove = Integer.parseInt(line2[0]);
            Commodity toRemoveComm = ru.getCommodity(line2[1]);

            if (!canRemove(inventory, toRemove, toRemoveComm)) {
                player.sendMessage("§4Not enough " + toRemoveComm.getName());
                return;
            }

            remove(inventory, toRemove, toRemoveComm);
            ru.levelUp(player, level, sublevel);

            player.chat("§2" + player.getName()
                    + " leveled up to " + sign.getLine(1)
                    + " for §6" + toRemove + " " + toRemoveComm.getName());
        }
    },
    PURCHASE {
        @Override
        public void onSignInteract(Block block, Sign sign, Player player, IRankup ru) {
            PlayerInventory inventory = player.getInventory();
            String[] line1 = sign.getLine(1).split(" +", 2);
            int toRemove = Integer.parseInt(line1[0]);
            Commodity toRemoveComm = ru.getCommodity(line1[1]);
            if (canRemove(inventory, toRemove, toRemoveComm)) {
                remove(inventory, toRemove, toRemoveComm);
                player.sendMessage("§2Paid " + toRemove + " §6" + toRemoveComm.getName());
                sign.setLine(0, "§2Owned By");
                sign.setLine(1, "");
                sign.setLine(2, "§2" + player.getDisplayName());
                sign.update();
                player.chat(player.getDisplayName() + " bought a house.");
            } else {
                player.sendMessage("§4Not enough " + toRemoveComm.getName());
            }
        }
    };

    private static void updateTraining(Block block, Sign sign, String levelStr, String levelCost) {
        sign.setLine(1, levelStr);
        sign.setLine(2, levelCost);
        sign.update();
    }

    static void updateBarter(Block block, Sign sign, IRankup ru) {
        Barter barter = ru.createBarterFor(block.getX() / 16, block.getZ() / 16);
        sign.setLine(1, barter.getBuyQ() + " " + barter.getBuy().getName());
        sign.setLine(2, barter.getSellQ() + " " + barter.getSell().getName());
        System.out.println(barter);
        double ratio = barter.getRatio();
        sign.setLine(3, ratio > 1.6 ? "§2:D" : ratio > 1.2 ? "§2:)" :
                ratio > 0.8 ? "§4:/" :
                        ratio > 0.5 ? "§6:(" : "§68-(|");
        sign.update();
    }

    static boolean canRemove(PlayerInventory inventory, int toRemove, Commodity toRemoveComm) {
        int available = 0;
        MaterialData materialData = toRemoveComm.getMaterialData();
        for (ItemStack itemStack : inventory.getContents()) {
            if (itemStack != null
                    && itemStack.getData().equals(materialData)) {
                available += itemStack.getAmount();
                if (available >= toRemove)
                    break;
            }
        }
        return available >= toRemove;
    }

    static boolean canAdd(PlayerInventory inventory, int toAdd, Commodity toAddComm) {
        int available = 0;
        MaterialData materialData = toAddComm.getMaterialData();
        for (ItemStack itemStack : inventory.getContents()) {
            int free = 0;
            if (itemStack == null) {
                free = 64;
            } else if (itemStack.getData().equals(materialData)) {
                free = Math.max(0, 64 - itemStack.getAmount());
            }
            available += free;
            if (available >= toAdd)
                break;
        }
        return available >= toAdd;
    }


    static void remove(PlayerInventory inventory, int toRemove, Commodity toRemoveComm) {
        MaterialData materialData = toRemoveComm.getMaterialData();
        for (ItemStack itemStack : inventory.getContents()) {
            if (itemStack != null
                    && itemStack.getData().equals(materialData)) {

                int diff = itemStack.getAmount() - toRemove;
                inventory.remove(itemStack);
                if (diff > 0) {
                    itemStack.setAmount(diff);
                    inventory.addItem(itemStack);
                }
                if (diff >= 0) {
                    break;
                }
                toRemove = -diff;
            }
        }
    }

    static void add(PlayerInventory inventory, int toAdd, Commodity toAddComm) {
        MaterialData materialData = toAddComm.getMaterialData();
        for (ItemStack itemStack : inventory.getContents()) {
            if (itemStack != null
                    && itemStack.getData().equals(materialData)) {

                int amount = itemStack.getAmount();
                int free = Math.min(64 - amount, toAdd);
                if (free > 0) {
                    amount += free;
                    toAdd -= free;
                    inventory.remove(itemStack);
                    itemStack.setAmount(amount);
                    inventory.addItem(itemStack);
                }
                if (toAdd <= 0)
                    return;
            }
        }

        while (toAdd > 0) {
            ItemStack is = new ItemStack(materialData.getItemType());
            is.setData(materialData);

            int free = Math.min(64, toAdd);
            if (free > 0) {
                toAdd -= free;
                is.setAmount(free);
                inventory.addItem(is);
            }
        }
    }

    public abstract void onSignInteract(Block block, Sign sign, Player player, IRankup ru);

}
