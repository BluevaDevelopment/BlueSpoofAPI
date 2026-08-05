package net.blueva.spoof.api

import net.blueva.spoof.api.inventory.BotInventory
import net.blueva.spoof.api.inventory.ContainerView
import net.blueva.spoof.api.pathfinding.Pathfinder
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.block.BlockFace
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector
import java.util.UUID
import java.util.concurrent.CompletableFuture

/**
 * A fake player controlled by BlueSpoof.
 * 
 * 
 * A fake player is a **real server-side player**: it has a genuine
 * [Player] object ([.getBukkitPlayer]), physics, health, food,
 * inventory and a tab-list entry, and it fires the usual Bukkit events
 * (`PlayerJoinEvent`, `PlayerMoveEvent`, `EntityDamageEvent`, …).
 * What it lacks is a network client — this interface plays that role, letting your
 * plugin drive the fake player exactly like a real client would: holding movement
 * keys, turning the camera, jumping, attacking, chatting and walking to goals.
 * 
 * <h3>Movement model</h3>
 * 
 * Movement is client-style input, not teleports. Holding
 * [ControlState.FORWARD] makes the fake player accelerate, walk and stop with
 * vanilla friction; jumping produces the real jump arc; knockback, gravity, water
 * and ladders are all handled by the server's own physics. On server versions
 * without native input support BlueSpoof falls back to a physics-approximating
 * stepper, so every method in this interface works on every supported version.
 * 
 * <h3>Asynchronous actions</h3>
 * 
 * Actions that have an observable outcome return a [CompletableFuture]
 * that completes when the outcome happens (e.g. [.lookAt]
 * completes once the fake player faces the target). Futures complete
 * exceptionally with [IllegalStateException] if the fake player is no
 * longer online. Action futures always complete on the server's main thread
 * (or the owning region thread on Folia), so it is safe to touch the Bukkit API
 * from continuations registered with the plain `thenAccept`/`thenRun`
 * variants.
 * 
 * @since 3.7
 */
interface FakePlayer {
    // ------------------------------------------------------------------
    // Identity & lifecycle
    // ------------------------------------------------------------------
    /**
     * Returns the fake player's exact in-game name.
     * 
     * @return player name
     * @since 3.7
     */
    val name: String?

    /**
     * Returns the fake player's UUID.
     * 
     * @return player UUID
     * @since 3.7
     */
    val uniqueId: UUID?

    /**
     * Returns the underlying Bukkit [Player].
     * 
     * 
     * This is a real online player object: anything that works on a real
     * player works on it (scoreboards, inventories, effects, permissions…).
     * Prefer the methods of this interface for locomotion so BlueSpoof can keep
     * its movement simulation coherent.
     * 
     * @return the Bukkit player, or `null` if the fake player is offline
     * @since 3.7
     */
    val bukkitPlayer: Player?

    /**
     * Returns whether the fake player is currently online.
     * 
     * @return `true` if online
     * @since 3.7
     */
    val isOnline: Boolean

    /**
     * Disconnects the fake player from the server, firing the usual quit events.
     * 
     * @since 3.7
     */
    fun disconnect()

    // ------------------------------------------------------------------
    // State
    // ------------------------------------------------------------------
    /**
     * Returns a copy of the fake player's current location.
     * 
     * @return current location
     * @throws IllegalStateException if the fake player is offline
     * @since 3.7
     */
    val location: Location?

    /**
     * Returns the world the fake player is in.
     * 
     * @return current world
     * @throws IllegalStateException if the fake player is offline
     * @since 3.7
     */
    val world: World?

    /**
     * Returns the fake player's current velocity.
     * 
     * @return velocity vector
     * @throws IllegalStateException if the fake player is offline
     * @since 3.7
     */
    /**
     * Sets the fake player's velocity. Vanilla physics (gravity, friction,
     * collisions) takes over from the next tick, exactly like knockback on a
     * real player.
     * 
     * @param velocity new velocity
     * @throws IllegalStateException if the fake player is offline
     * @since 3.7
     */
    var velocity: Vector?

    /**
     * Returns the fake player's health (0-20).
     * 
     * @return health
     * @throws IllegalStateException if the fake player is offline
     * @since 3.7
     */
    val health: Double

    /**
     * Returns the fake player's food level (0-20).
     * 
     * @return food level
     * @throws IllegalStateException if the fake player is offline
     * @since 3.7
     */
    val foodLevel: Int

    /**
     * Returns the fake player's game mode.
     * 
     * @return game mode
     * @throws IllegalStateException if the fake player is offline
     * @since 3.7
     */
    val gameMode: GameMode?

    /**
     * Returns whether the fake player is standing on the ground.
     * 
     * @return `true` if on ground
     * @throws IllegalStateException if the fake player is offline
     * @since 3.7
     */
    val isOnGround: Boolean

    /**
     * Returns whether the fake player is sneaking.
     * 
     * @return `true` if sneaking
     * @throws IllegalStateException if the fake player is offline
     * @since 3.7
     */
    val isSneaking: Boolean

    /**
     * Returns whether the fake player is sprinting.
     * 
     * @return `true` if sprinting
     * @throws IllegalStateException if the fake player is offline
     * @since 3.7
     */
    val isSprinting: Boolean

    /**
     * Returns the item the fake player is holding in its main hand.
     * 
     * @return held item, possibly `null` or air
     * @throws IllegalStateException if the fake player is offline
     * @since 3.7
     */
    val heldItem: ItemStack?

    /**
     * Returns the selected hotbar slot (0-8).
     * 
     * @return hotbar slot
     * @throws IllegalStateException if the fake player is offline
     * @since 3.7
     */
    val heldItemSlot: Int

    // ------------------------------------------------------------------
    // Looking
    // ------------------------------------------------------------------
    /**
     * Smoothly turns the fake player's head to the given rotation, like a real
     * player moving their mouse.
     * 
     * @param yaw   target yaw in degrees (Minecraft convention: 0 = south)
     * @param pitch target pitch in degrees (-90 = up, 90 = down)
     * @return future completing once the fake player faces the target rotation
     * @throws IllegalStateException if the fake player is offline
     * @since 3.7
     */
    fun look(yaw: Double, pitch: Double): CompletableFuture<Void?>?

    /**
     * Smoothly turns the fake player's head to face the given location (eye height).
     * 
     * @param target the point to face
     * @return future completing once the fake player faces the target
     * @throws IllegalStateException if the fake player is offline
     * @since 3.7
     */
    fun lookAt(target: Location?): CompletableFuture<Void?>?

    /**
     * Instantly snaps the fake player's head to the given rotation.
     * 
     * @param yaw   yaw in degrees
     * @param pitch pitch in degrees
     * @throws IllegalStateException if the fake player is offline
     * @since 3.7
     */
    fun lookInstantly(yaw: Double, pitch: Double)

    /**
     * Instantly snaps the fake player's head to face the given location.
     * 
     * @param target the point to face
     * @throws IllegalStateException if the fake player is offline
     * @since 3.7
     */
    fun lookAtInstantly(target: Location?)

    // ------------------------------------------------------------------
    // Control states (held keys)
    // ------------------------------------------------------------------
    /**
     * Presses or releases a movement control, exactly like a real client holding
     * or releasing a key. States persist until changed; the fake player keeps
     * walking/jumping/sprinting every tick while a state is active.
     * 
     * @param state  the control to change
     * @param active `true` to hold, `false` to release
     * @throws IllegalStateException if the fake player is offline
     * @since 3.7
     */
    fun setControlState(state: ControlState?, active: Boolean)

    /**
     * Returns whether the given control is currently held.
     * 
     * @param state the control to query
     * @return `true` if held
     * @since 3.7
     */
    fun isControlStateActive(state: ControlState?): Boolean

    /**
     * Releases every control state. The fake player decelerates to a stop with
     * vanilla friction and stops jumping/sneaking/sprinting.
     * 
     * @since 3.7
     */
    fun clearControlStates()

    // ------------------------------------------------------------------
    // Actions
    // ------------------------------------------------------------------
    /**
     * Makes the fake player jump once (if on ground) or swim upward (if in a
     * liquid), like a real client tapping space.
     * 
     * @return future completing once the jump impulse has been applied
     * @throws IllegalStateException if the fake player is offline
     * @since 3.7
     */
    fun jump(): CompletableFuture<Void?>?

    /**
     * Swings the fake player's arm (animation only, no interaction).
     * 
     * @param hand [EquipmentSlot.HAND] or [EquipmentSlot.OFF_HAND]
     * @return future completing once the animation has been broadcast
     * @throws IllegalArgumentException if `hand` is not a hand slot
     * @throws IllegalStateException    if the fake player is offline
     * @since 3.7
     */
    fun swingArm(hand: EquipmentSlot?): CompletableFuture<Void?>?

    /**
     * Attacks the given entity once with the held item (melee), producing real
     * damage, knockback and swing animation through the vanilla combat path.
     * 
     * @param target the entity to hit
     * @return future completing once the attack has been processed
     * @throws IllegalStateException if the fake player is offline
     * @since 3.7
     */
    fun attack(target: Entity?): CompletableFuture<Void?>?

    /**
     * Drops items from the selected hotbar slot in front of the fake player.
     * 
     * @param fullStack `true` to drop the whole stack, `false` for one item
     * @return future completing once the item has been dropped
     * @throws IllegalStateException if the fake player is offline
     * @since 3.7
     */
    fun dropItem(fullStack: Boolean): CompletableFuture<Void?>?

    /**
     * Selects a hotbar slot (0-8), broadcasting the held-item change.
     * 
     * @param slot hotbar slot index
     * @return future completing once the slot has been selected
     * @throws IllegalArgumentException if `slot` is outside 0-8
     * @throws IllegalStateException    if the fake player is offline
     * @since 3.7
     */
    fun setHeldItemSlot(slot: Int): CompletableFuture<Void?>?

    /**
     * Equips an item in the given slot (main hand, off hand or armor).
     * 
     * @param slot equipment slot
     * @param item item to equip, or `null`/air to clear the slot
     * @throws IllegalStateException if the fake player is offline
     * @since 3.7
     */
    fun equip(slot: EquipmentSlot?, item: ItemStack?)

    /**
     * Teleports the fake player, like a real player being teleported by the
     * server (fires `PlayerTeleportEvent`).
     * 
     * @param location destination
     * @return future completing once the teleport has been applied
     * @throws IllegalStateException if the fake player is offline
     * @since 3.7
     */
    fun teleport(location: Location?): CompletableFuture<Void?>?

    // ------------------------------------------------------------------
    // Advanced world interaction (Minecraft 26.1+)
    // ------------------------------------------------------------------
    /**
     * Returns whether this fake player supports the advanced world-interaction
     * methods ([.dig], [.useItem],
     * [.useItemOn],
     * [.interact], [.releaseUseItem]).
     * They require a native server hook, currently Minecraft 26.1+; on older
     * versions they throw [UnsupportedOperationException].
     * 
     * @return `true` if advanced actions are supported
     * @since 3.7
     */
    fun supportsAdvancedActions(): Boolean {
        return false
    }

    /**
     * Right-clicks in the air with the item held in the given hand — the
     * vanilla `useItem` path. This single call covers a lot of minigame
     * mechanics: casting **and** reeling a fishing rod (a second call reels
     * in; bite/catch events arrive as standard `PlayerFishEvent`s),
     * starting to eat, drawing a bow (release with [.releaseUseItem]),
     * raising a shield or throwing a projectile.
     * 
     * @param hand [EquipmentSlot.HAND] or [EquipmentSlot.OFF_HAND]
     * @return future completing once the interaction has been processed
     * @throws UnsupportedOperationException if not supported on this server version
     * @throws IllegalStateException         if the fake player is offline
     * @since 3.7
     */
    fun useItem(hand: EquipmentSlot?): CompletableFuture<Void?>? {
        throw UnsupportedOperationException(
            "useItem() requires Minecraft 26.1+ (advanced actions not supported on this version)"
        )
    }

    /**
     * Releases the item the fake player is currently using: shoots a drawn
     * bow, lowers a shield, throws a charged trident.
     * 
     * @return future completing once the release has been processed
     * @throws UnsupportedOperationException if not supported on this server version
     * @throws IllegalStateException         if the fake player is offline
     * @since 3.7
     */
    fun releaseUseItem(): CompletableFuture<Void?>? {
        throw UnsupportedOperationException(
            "releaseUseItem() requires Minecraft 26.1+ (advanced actions not supported on this version)"
        )
    }

    /**
     * Right-clicks the given face of a block with the held item — the vanilla
     * `useItemOn` path. Covers placing blocks, opening doors and chests,
     * flipping levers, emptying buckets on a surface, etc.
     * 
     * @param block the target block location
     * @param face  the clicked face
     * @param hand  [EquipmentSlot.HAND] or [EquipmentSlot.OFF_HAND]
     * @return future completing once the interaction has been processed
     * @throws UnsupportedOperationException if not supported on this server version
     * @throws IllegalStateException         if the fake player is offline
     * @since 3.7
     */
    fun useItemOn(block: Location?, face: BlockFace?, hand: EquipmentSlot?): CompletableFuture<Void?>? {
        throw UnsupportedOperationException(
            "useItemOn() requires Minecraft 26.1+ (advanced actions not supported on this version)"
        )
    }

    /**
     * Right-clicks an entity with the held item — the vanilla
     * `interactOn` path. Covers mounting, leashing, trading, shearing,
     * feeding, name-tagging, etc.
     * 
     * @param target the entity to interact with
     * @param hand   [EquipmentSlot.HAND] or [EquipmentSlot.OFF_HAND]
     * @return future completing once the interaction has been processed
     * @throws UnsupportedOperationException if not supported on this server version
     * @throws IllegalStateException         if the fake player is offline
     * @since 3.7
     */
    fun interact(target: Entity?, hand: EquipmentSlot?): CompletableFuture<Void?>? {
        throw UnsupportedOperationException(
            "interact() requires Minecraft 26.1+ (advanced actions not supported on this version)"
        )
    }

    /**
     * Digs the given block with vanilla survival timing: the block cracks
     * progressively and breaks once the destroy progress completes, exactly
     * as if a real client held left-click on it (tool speed, enchantments and
     * effects apply; the usual Bukkit interact/break events fire).
     * 
     * 
     * Example — a minigame miner:
     * <pre>`fake.lookAt(oreLocation).thenCompose(v -> fake.dig(oreLocation))     .thenAccept(broken -> {         if (broken) fake.chat(" mined it!");     }); `</pre>
     * 
     * @param block the block to dig (must be within vanilla reach)
     * @return future completing `true` when the block breaks, or
     * `false` if the dig was aborted (out of reach, interrupted,
     * timeout or [.stopDigging])
     * @throws UnsupportedOperationException if not supported on this server version
     * @throws IllegalStateException         if the fake player is offline
     * @since 3.7
     */
    fun dig(block: Location?): CompletableFuture<Boolean?>? {
        throw UnsupportedOperationException(
            "dig() requires Minecraft 26.1+ (advanced actions not supported on this version)"
        )
    }

    /**
     * Aborts the dig currently in progress, if any (the pending
     * [.dig] future completes `false`).
     * 
     * @throws UnsupportedOperationException if not supported on this server version
     * @since 3.7
     */
    fun stopDigging() {
        throw UnsupportedOperationException(
            "stopDigging() requires Minecraft 26.1+ (advanced actions not supported on this version)"
        )
    }

    // ------------------------------------------------------------------
    // Inventory & containers
    // ------------------------------------------------------------------
    /**
     * Returns the fake player's own inventory (hotbar, main, armor, off-hand).
     * Works on every server version.
     * 
     * @return this fake player's inventory
     * @since 3.7
     */
    fun inventory(): BotInventory?

    /**
     * Opens the container block at the given location (chest, barrel, furnace,
     * dispenser, ...) through the real server code path and returns a view of
     * the session. The fake player must be within vanilla interaction range of
     * the block.
     * 
     * 
     * Opening and reading the contents works on every server version;
     * clicking inside the container requires the advanced-actions native hook
     * (Minecraft 26.1+) — see [ContainerView].
     * 
     * @param block the container block location
     * @return future completing with the open container view, or exceptionally
     * if the block is not a container or is out of reach
     * @throws IllegalStateException if the fake player is offline
     * @since 3.7
     */
    fun openContainer(block: Location?): CompletableFuture<ContainerView?>?

    /**
     * Crafts items through the real vanilla crafting menus, exactly like a
     * player placing ingredients and taking the result: recipe matching,
     * ingredient consumption, remaining items (buckets, bottles), statistics
     * and advancements are all handled by the server itself. Recipes that fit
     * the 2×2 inventory grid are always available; 3×3 recipes require a
     * crafting table within vanilla reach of the fake player.
     * 
     * 
     * Example — a minigame lumberjack:
     * <pre>`fake.craft(new ItemStack(Material.OAK_PLANKS, 8), 8)     .thenAccept(crafted -> fake.chat("crafted " + crafted + " planks")); `</pre>
     * 
     * @param result the item to craft (matched against the server's recipes by
     * type and components)
     * @param amount how many items to craft (crafts fewer if ingredients are
     * missing or the result would not fit the inventory)
     * @return future completing with the amount actually crafted
     * @throws UnsupportedOperationException if not supported on this server
     * version (requires Minecraft 26.1+)
     * @throws IllegalStateException if the fake player is offline
     * @since 3.7
     */
    fun craft(result: ItemStack?, amount: Int): CompletableFuture<Int?>? {
        throw UnsupportedOperationException(
            "craft() requires Minecraft 26.1+ (advanced actions not supported on this version)"
        )
    }

    // ------------------------------------------------------------------
    // Chat
    // ------------------------------------------------------------------
    /**
     * Sends a chat message as the fake player, going through the normal chat
     * pipeline (`AsyncPlayerChatEvent`, formatters, other plugins see it
     * exactly like a real player's message).
     * 
     * @param message the message to send
     * @return future completing once the message has been broadcast
     * @throws IllegalStateException if the fake player is offline
     * @since 3.7
     */
    fun chat(message: String?): CompletableFuture<Void?>?

    /**
     * Sends a private message **to** the fake player (only the bot "sees" it;
     * useful for scripted interactions and testing).
     * 
     * @param message the message to deliver
     * @throws IllegalStateException if the fake player is offline
     * @since 3.7
     */
    fun sendMessage(message: String?)

    // ------------------------------------------------------------------
    // Pathfinding
    // ------------------------------------------------------------------
    /**
     * Returns the pathfinder that drives this fake player toward goals.
     * 
     * 
     * Example:
     * <pre>`fake.pathfinder().goTo(new GoalNear(100, 64, -30, 2))     .thenAccept(result -> {         if (result.getStatus() == PathStatus.SUCCESS) {             fake.chat("I'm here!");         }     }); `</pre>
     * 
     * @return this fake player's pathfinder (never `null`)
     * @since 3.7
     */
    fun pathfinder(): Pathfinder?
}
