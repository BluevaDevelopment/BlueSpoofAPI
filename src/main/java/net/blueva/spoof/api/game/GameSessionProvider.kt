package net.blueva.spoof.api.game

import net.blueva.spoof.api.FakePlayer
import org.bukkit.entity.Player
import java.util.Optional

/**
 * Tells BlueSpoof about the matches a minigame plugin is running.
 *
 * Implement this to make BlueSpoof's bots understand your games. You own the code on your side, so
 * no reflection is involved: read your own arena objects and answer the questions
 * [GameSession] asks. In return, every behaviour BlueSpoof ships and every behaviour a third party
 * has registered starts working inside your minigames, including the generic one that keeps a bot
 * looking plausible in a game nobody wrote a specific adapter for.
 *
 * Register with `BlueSpoofAPI.games().register(provider)` on enable, and unregister on disable.
 *
 * @since 3.9
 */
interface GameSessionProvider {
    /**
     * A stable id for this provider, unique across the server, for example your plugin's name in
     * lower case.
     *
     * @since 3.9
     */
    fun id(): String

    /**
     * The session the given fake player is currently in, if any.
     *
     * Called often. Keep it to a map lookup.
     *
     * @since 3.9
     */
    fun sessionOf(bot: FakePlayer): Optional<GameSession>

    /**
     * The session the given player, fake or real, is currently in, if any.
     *
     * @since 3.9
     */
    fun sessionOf(player: Player): Optional<GameSession>

    /**
     * Every session currently running.
     *
     * @since 3.9
     */
    fun sessions(): Collection<GameSession>
}
