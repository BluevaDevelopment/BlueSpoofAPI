package net.blueva.spoof.api.game

import net.blueva.spoof.api.FakePlayer
import org.bukkit.entity.Player
import java.util.Optional

/**
 * Where [GameSessionProvider]s are registered and queried.
 *
 * Reached with `BlueSpoofAPI.games()`.
 *
 * @since 3.9
 */
interface GameRegistry {
    /**
     * Registers a provider, replacing any provider already registered with the same id.
     *
     * @since 3.9
     */
    fun register(provider: GameSessionProvider)

    /**
     * Unregisters a provider by id.
     *
     * @since 3.9
     */
    fun unregister(id: String)

    /**
     * Every registered provider id.
     *
     * @since 3.9
     */
    fun providers(): Set<String>

    /**
     * The session the given fake player is in, asking every registered provider. If more than one
     * provider claims the same bot, the first registration wins and a warning is logged once.
     *
     * @since 3.9
     */
    fun sessionOf(bot: FakePlayer): Optional<GameSession>

    /**
     * The session the given player is in, asking every registered provider.
     *
     * @since 3.9
     */
    fun sessionOf(player: Player): Optional<GameSession>

    /**
     * Every session currently running, across every provider.
     *
     * @since 3.9
     */
    fun sessions(): Collection<GameSession>
}
