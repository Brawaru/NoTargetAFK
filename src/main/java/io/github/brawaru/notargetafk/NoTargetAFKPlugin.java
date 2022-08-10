package io.github.brawaru.notargetafk;

import com.earth2me.essentials.Essentials;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.ess3.api.events.AfkStatusChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

public final class NoTargetAFKPlugin extends JavaPlugin implements Listener {
    @Nullable
    private Essentials essentials;

    /**
     * A map of lists of mobs that target player, per player.
     * <p>
     * This is used to remove targets.
     */
    private final Map<UUID, List<UUID>> playerTargetingEntities = new HashMap<>();

    /**
     * Reverse lookup map of mobs' targets.
     * <p>
     * Used to quickly lookup which player entity was previously targeting.
     */
    private final Map<UUID, UUID> entitiesTargets = new HashMap<>();

    @Override
    public void onEnable() {
        essentials = (Essentials) Bukkit.getPluginManager().getPlugin("Essentials");

        if (essentials == null) {
            getLogger().severe("Essentials plugin not found. Plugin will not work");

            return;
        }

        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    private void onEntityTarget(EntityTargetLivingEntityEvent e) {
        if (essentials == null) return;

        var entity = e.getEntity();
        var entityUuid = entity.getUniqueId();
        var target = e.getTarget();

        if (target instanceof Player player) {
            if (essentials.getUser(player).isAfk() && target.hasPermission("notargetafk.mobs-dont-target")) {
                e.setCancelled(true);
            } else {
                var playerUuid = player.getUniqueId();

                playerTargetingEntities.computeIfAbsent(playerUuid, (key) -> new ArrayList<>()).add(entityUuid);

                entitiesTargets.put(entityUuid, playerUuid);
            }
        } else {
            var previouslyTargetedPlayerUuid = entitiesTargets.get(entityUuid);

            if (previouslyTargetedPlayerUuid != null) {
                var targetedBy = playerTargetingEntities.get(previouslyTargetedPlayerUuid);

                if (targetedBy != null) {
                    targetedBy.remove(entityUuid);
                }

                entitiesTargets.remove(entityUuid);
            }
        }
    }

    private void clearTargetsForPlayer(Player player) {
        var playerUuid = player.getUniqueId();

        var targetingEntities = playerTargetingEntities.get(playerUuid);

        if (targetingEntities != null && !targetingEntities.isEmpty()) {
            for (var iterator = targetingEntities.listIterator(); iterator.hasNext(); ) {
                var targetingEntityUuid = iterator.next();
                var targetingEntity = Bukkit.getEntity(targetingEntityUuid);

                if (targetingEntity instanceof Mob mob) {
                    var mobTarget = mob.getTarget();

                    if (mobTarget != null && mobTarget.getUniqueId().equals(playerUuid)) {
                        mob.setTarget(null);

                        iterator.remove();
                    }
                }
            }
        }
    }

    @EventHandler
    private void onAfk(AfkStatusChangeEvent e) {
        if (e.getValue()) {
            var player = e.getAffected().getBase();

            if (player.hasPermission("notargetafk.mobs-lose-track")) {
                clearTargetsForPlayer(player);
            }
        }
    }

    @EventHandler
    private void onDisconnect(PlayerQuitEvent e) {
        playerTargetingEntities.remove(e.getPlayer().getUniqueId());
    }

    @Override
    public void onDisable() {
        playerTargetingEntities.clear();
        entitiesTargets.clear();
    }
}
