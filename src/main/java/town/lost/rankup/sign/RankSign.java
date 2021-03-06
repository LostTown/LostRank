package town.lost.rankup.sign;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.material.MaterialData;
import town.lost.rankup.IRankup;
import town.lost.rankup.commodity.Barter;
import town.lost.rankup.commodity.Commodity;
import town.lost.rankup.level.Level;
import town.lost.rankup.level.Reward;

import java.util.Map;

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

            if (Math.random() < 0.2)
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
            if (sign.getLine(1).trim().isEmpty() || player.isFlying()) {
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

            Reward reward = level.getReward(sublevel);
            System.out.println("Reward " + reward);
            if (reward.getNumber() > 0) {
                if (!canAdd(inventory, reward.getNumber(), reward.getCommodity())) {
                    player.sendMessage("§4Not enough space for the reward of " + reward.getCommodity().getName());
                    return;
                }
            }

            remove(inventory, toRemove, toRemoveComm);
            ru.levelUp(player, level, sublevel);

            player.chat("§2" + player.getName()
                    + " leveled up to " + sign.getLine(1)
                    + " for §6" + toRemove + " " + toRemoveComm.getName());

            if (reward.getNumber() > 0) {
                add(inventory, reward.getNumber(), reward.getCommodity());
                player.chat("§2" + player.getName()
                        + " has been rewarded with §6" + reward.getNumber() + " " + reward.getCommodity().getName());
            }
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

    public static final int MAX_STACK = 99;

    private static void updateTraining(Block block, Sign sign, String levelStr, String levelCost) {
        sign.setLine(1, levelStr);
        sign.setLine(2, levelCost);
        sign.update();
    }

    static void updateBarter(Block block, Sign sign, IRankup ru) {
        int row = 0;
        if (block.getRelative(BlockFace.DOWN).getType() != Material.WOOD) {
            row++;
            if (block.getRelative(0, -2, 0).getType() != Material.WOOD) {
                row++;
            }
        }
        Barter barter = ru.createBarterFor(block.getX() / 16, block.getZ() / 16, row, (sign.getRawData() & 1) == 0);
        sign.setLine(1, barter.getBuyQ() + " " + barter.getBuy().getName());
        sign.setLine(2, barter.getSellQ() + " " + barter.getSell().getName());
        System.out.println(barter);
        double ratio = barter.getRatio();
        sign.setLine(3, ratio > 1.4 ? "§2:D" : ratio > 1. ? "§2:)" :
                ratio > 0.7 ? "§4:/" :
                        ratio > 0.4 ? "§6:(" : "§68-(|");
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
                free = MAX_STACK;
            } else if (itemStack.getData().equals(materialData)) {
                free = Math.max(0, MAX_STACK - itemStack.getAmount());
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
        if (toAddComm.getEnchant().isEmpty()) {
            for (ItemStack itemStack : inventory.getContents()) {
                if (itemStack != null
                        && itemStack.getData().equals(materialData)) {

                    int amount = itemStack.getAmount();
                    int free = Math.min(MAX_STACK - amount, toAdd);
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
        }

        while (toAdd > 0) {
            ItemStack is = new ItemStack(materialData.getItemType());
            is.setData(materialData);
            for (Map.Entry<Enchantment, Integer> entry : toAddComm.getEnchant().entrySet()) {
                System.out.println("... adding " + entry);
                is.addUnsafeEnchantment(entry.getKey(), entry.getValue());
            }

            int free = Math.min(MAX_STACK, toAdd);
            if (free > 0) {
                toAdd -= free;
                is.setAmount(free);
                inventory.addItem(is);
            }
        }
    }

    public abstract void onSignInteract(Block block, Sign sign, Player player, IRankup ru);

}
