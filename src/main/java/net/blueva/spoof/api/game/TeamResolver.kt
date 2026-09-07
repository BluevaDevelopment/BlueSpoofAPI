package net.blueva.spoof.api.game

import org.bukkit.entity.Player
import java.util.Optional

/**
 * Tells BlueSpoof which side a player is on inside a game.
 *
 * Minigame plugins model teams in their own way, and few of them use Bukkit scoreboard teams, so a
 * provider supplies this rather than BlueSpoof guessing. Without one, every other player counts as
 * an enemy, which is correct for free-for-all games and wrong for everything else.
 *
 * @since 3.9
 */
interface TeamResolver {
    /**
     * The id of the team the player is on, or an empty optional if the player has no team.
     *
     * @since 3.9
     */
    fun teamOf(player: Player): Optional<String>

    /**
     * Whether the two players are on the same side. Defaults to comparing team ids, with two
     * team-less players counting as enemies rather than allies.
     *
     * @since 3.9
     */
    fun areAllies(first: Player, second: Player): Boolean {
        val firstTeam = teamOf(first)
        val secondTeam = teamOf(second)
        if (!firstTeam.isPresent || !secondTeam.isPresent) return false
        return firstTeam.get() == secondTeam.get()
    }
}
