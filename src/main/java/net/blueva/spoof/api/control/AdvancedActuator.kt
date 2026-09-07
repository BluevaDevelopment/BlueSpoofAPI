package net.blueva.spoof.api.control

import net.blueva.spoof.api.inventory.ContainerView
import org.bukkit.Location
import org.bukkit.block.BlockFace
import org.bukkit.entity.Entity
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import java.util.concurrent.CompletableFuture

/**
 * World interaction that needs a native server hook, and therefore is not available on every
 * supported Minecraft version.
 *
 * Obtain it from [Actuator.advanced], which returns an empty optional when the hook is missing.
 * Never assume it is present: check [Capabilities] once and pick a behaviour that fits, rather
 * than calling these and handling failures.
 *
 * ```
 * val advanced = bot.actuator().advanced().orElse(null) ?: return
 * advanced.dig(oreLocation).thenAccept { broken -> if (broken) bot.actuator().chat("got it") }
 * ```
 *
 * @since 3.9
 */
interface AdvancedActuator {
    /**
     * Digs a block with vanilla survival timing: the block cracks progressively and breaks once
     * the destroy progress completes, exactly as if a real client held left-click on it. Tool
     * speed, enchantments and potion effects apply, and the usual Bukkit interact and break events
     * fire.
     *
     * @param block the block to dig, which must be within vanilla reach
     * @return future completing `true` when the block breaks, or `false` if the dig was aborted
     *   (out of reach, interrupted, timed out, or [stopDigging] was called)
     * @since 3.9
     */
    fun dig(block: Location): CompletableFuture<Boolean>

    /**
     * Aborts the dig currently in progress, if any. Its pending [dig] future completes `false`.
     *
     * @since 3.9
     */
    fun stopDigging()

    /**
     * Right-clicks in the air with the item held in the given hand, the vanilla `useItem` path.
     * Covers casting and reeling a fishing rod, starting to eat, drawing a bow (release it with
     * [releaseUseItem]), raising a shield and throwing a projectile.
     *
     * @param hand [EquipmentSlot.HAND] or [EquipmentSlot.OFF_HAND]
     * @since 3.9
     */
    fun useItem(hand: EquipmentSlot): CompletableFuture<Void>

    /**
     * Releases the item currently being used: shoots a drawn bow, lowers a shield, throws a
     * charged trident.
     *
     * @since 3.9
     */
    fun releaseUseItem(): CompletableFuture<Void>

    /**
     * Right-clicks the given face of a block with the held item, the vanilla `useItemOn` path.
     * Covers placing blocks, opening doors and chests, flipping levers and emptying buckets.
     *
     * @since 3.9
     */
    fun useItemOn(block: Location, face: BlockFace, hand: EquipmentSlot): CompletableFuture<Void>

    /**
     * Right-clicks an entity with the held item, the vanilla `interactOn` path. Covers mounting,
     * leashing, trading, shearing, feeding and name-tagging.
     *
     * @since 3.9
     */
    fun interact(target: Entity, hand: EquipmentSlot): CompletableFuture<Void>

    /**
     * Places [item] against the given face of [block], selecting the item in the hotbar first and
     * looking at the face like a real client would. A convenience over [useItemOn] for the single
     * most common case in minigames: bridging and building.
     *
     * @return future completing `true` if a block was actually placed
     * @since 3.9
     */
    fun placeAgainst(block: Location, face: BlockFace, item: ItemStack): CompletableFuture<Boolean>

    /**
     * Crafts items through the real vanilla crafting menus: recipe matching, ingredient
     * consumption, leftovers, statistics and advancements are all handled by the server. Recipes
     * that fit the 2x2 inventory grid are always available; 3x3 recipes need a crafting table
     * within reach.
     *
     * @return future completing with the amount actually crafted, which may be less than asked
     * @since 3.9
     */
    fun craft(result: ItemStack, amount: Int): CompletableFuture<Int>

    /**
     * Opens the container block at the given location through the real server code path.
     *
     * Opening and reading contents works on every server version, so this is also reachable from
     * [Actuator.openContainer]; it is repeated here because clicking inside the returned view
     * needs this hook. See [ContainerView].
     *
     * @since 3.9
     */
    fun openContainer(block: Location): CompletableFuture<ContainerView>
}
