package net.blueva.spoof.api.game

import net.blueva.spoof.api.FakePlayer
import org.bukkit.entity.Player
import java.util.Optional
import java.util.concurrent.CompletableFuture

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

    /**
     * Arenas a bot could join right now, most in need of players first.
     *
     * BlueSpoof uses this to keep arenas from sitting empty: a server whose minigames never start
     * because two people are waiting for a third is the problem fake players exist to solve, and it
     * only works if they can find the arena that needs them.
     *
     * Return only arenas that are genuinely open, and leave out anything a real player would be
     * refused from. The default is empty, which simply means BlueSpoof will not send bots anywhere
     * on its own.
     *
     * @since 3.9
     */
    fun joinableArenas(): List<JoinableArena> = emptyList()

    /**
     * Puts a bot into an arena.
     *
     * How is up to the provider: most will run their own join command as that player, which is what
     * makes the bot arrive through exactly the path a real player does, with the same checks and the
     * same events.
     *
     * @return future completing `true` if the bot was sent; `false` if the provider declined
     * @since 3.9
     */
    fun join(bot: FakePlayer, arenaId: String): CompletableFuture<Boolean> =
        CompletableFuture.completedFuture(false)

    /**
     * Takes a bot out of whatever match it is in.
     *
     * @since 3.9
     */
    fun leave(bot: FakePlayer): CompletableFuture<Boolean> =
        CompletableFuture.completedFuture(false)
}
