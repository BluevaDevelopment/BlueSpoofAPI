package net.blueva.spoof.api.game

/**
 * An arena that is waiting for players and would take another.
 *
 * Reported by a [GameSessionProvider] so BlueSpoof can decide where its bots would do the most good.
 * The counts are what that decision is made from: an arena two short of starting is worth filling,
 * one that is already full is not, and one with nobody in it at all may be worth seeding or may be
 * better left alone depending on how the server is configured.
 *
 * @since 3.9
 */
class JoinableArena(
    /** The arena's id, as [GameSession.arenaId] reports it. */
    @JvmField val arenaId: String,
    /** The game it is set to play, or an empty string when it votes for one later. */
    @JvmField val gameId: String,
    /** How many players are in it now, bots included. */
    @JvmField val players: Int,
    /** How many of those are fake. */
    @JvmField val bots: Int,
    /** How many it needs before it will start. */
    @JvmField val minPlayers: Int,
    /** How many it will hold. */
    @JvmField val maxPlayers: Int
) {
    /** Free slots. */
    val freeSlots: Int
        get() = maxOf(0, maxPlayers - players)

    /** How many more it needs to start, or zero when it has enough. */
    val shortBy: Int
        get() = maxOf(0, minPlayers - players)

    /** Real players in it. */
    val humans: Int
        get() = maxOf(0, players - bots)

    override fun toString(): String =
        "JoinableArena($arenaId, $gameId, $players/$maxPlayers, needs $shortBy more)"
}
