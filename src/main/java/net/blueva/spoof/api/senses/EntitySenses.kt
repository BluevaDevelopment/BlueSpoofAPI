package net.blueva.spoof.api.senses

import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import java.util.Optional
import java.util.function.Predicate

/**
 * What a fake player can see of the entities around it.
 *
 * Results are bounded by the configured sense radius and, when line-of-sight filtering is enabled,
 * by whether the fake player could actually see the entity. A behaviour therefore cannot target
 * something through a wall, which is one of the clearest tells of a bot.
 *
 * Unlike [BlockSenses], these return live Bukkit entities: an entity handle is only useful if it
 * stays current, and a behaviour that acts on a stale one gets a harmless no-op from the actuator.
 *
 * @since 3.9
 */
interface EntitySenses {
    /**
     * Every visible entity within [radius], nearest first.
     *
     * @since 3.9
     */
    fun visible(radius: Double): List<Entity>

    /**
     * Every visible player within [radius], nearest first, excluding this fake player itself.
     *
     * @since 3.9
     */
    fun visiblePlayers(radius: Double): List<Player>

    /**
     * Every visible entity within [radius] matching [match], nearest first.
     *
     * @since 3.9
     */
    fun find(match: Predicate<Entity>, radius: Double): List<Entity>

    /**
     * The nearest visible hostile mob within [radius].
     *
     * @since 3.9
     */
    fun nearestHostile(radius: Double): Optional<LivingEntity>

    /**
     * The nearest visible player within [radius] that this fake player should consider an enemy.
     *
     * "Enemy" means: not itself, not a spectator, and, when the bot is inside a game whose provider
     * supplied a team resolver, not a team mate. With no game and no resolver, every other player
     * counts.
     *
     * @since 3.9
     */
    fun nearestEnemy(radius: Double): Optional<Player>

    /**
     * The nearest visible dropped item within [radius].
     *
     * @since 3.9
     */
    fun nearestItem(radius: Double): Optional<Entity>

    /**
     * The entity currently being looked at, within [maxDistance], if any.
     *
     * @since 3.9
     */
    fun lookingAt(maxDistance: Double): Optional<Entity>
}
