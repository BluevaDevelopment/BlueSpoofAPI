package net.blueva.spoof.api.senses

import net.blueva.spoof.api.inventory.BotInventory
import net.blueva.spoof.api.inventory.ContainerView
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.util.Vector
import java.util.Optional

/**
 * What a fake player knows about itself.
 *
 * Every getter that used to live on `FakePlayer` lives here instead, so that a behaviour has one
 * place to read from and one place to write to ([net.blueva.spoof.api.control.Actuator]).
 *
 * All of these throw [IllegalStateException] if the fake player has gone offline.
 *
 * @since 3.9
 */
interface SelfSenses {
    /** A copy of the current location. */
    fun location(): Location

    /** The eye location, which is what aiming and line of sight are measured from. */
    fun eyeLocation(): Location

    /** The current world. */
    fun world(): World

    /** The current velocity. */
    fun velocity(): Vector

    /** Health, 0 to the maximum for this player. */
    fun health(): Double

    /** Maximum health. */
    fun maxHealth(): Double

    /** Food level, 0 to 20. */
    fun foodLevel(): Int

    /** Saturation. */
    fun saturation(): Float

    /** Current game mode. */
    fun gameMode(): GameMode

    /** Whether the fake player is standing on the ground. */
    fun isOnGround(): Boolean

    /** Whether the fake player is in water. */
    fun isInWater(): Boolean

    /** Whether the fake player is in lava. */
    fun isInLava(): Boolean

    /** Whether the fake player is sneaking. */
    fun isSneaking(): Boolean

    /** Whether the fake player is sprinting. */
    fun isSprinting(): Boolean

    /** Active potion effects. */
    fun effects(): Collection<PotionEffect>

    /** The item in the main hand, possibly air. */
    fun heldItem(): ItemStack

    /** The selected hotbar slot, 0 to 8. */
    fun heldItemSlot(): Int

    /** This fake player's own inventory. */
    fun inventory(): BotInventory

    /** The container this fake player currently has open, if any. */
    fun openContainer(): Optional<ContainerView>

    /**
     * The id of the team this fake player belongs to inside its current game, if it is in a game
     * whose provider supplied a team resolver.
     *
     * @since 3.9
     */
    fun team(): Optional<String>

    /**
     * How many ticks ago this fake player last took damage, or -1 if it has not been damaged since
     * it connected. The cheapest way for a behaviour to notice it is under attack.
     *
     * @since 3.9
     */
    fun ticksSinceDamage(): Long

    /**
     * The most recent source of damage, if anything has damaged this fake player.
     *
     * @since 3.9
     */
    fun lastDamageSource(): Optional<org.bukkit.entity.Entity>
}
