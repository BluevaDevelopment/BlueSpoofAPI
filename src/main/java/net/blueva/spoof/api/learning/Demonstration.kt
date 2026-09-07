package net.blueva.spoof.api.learning

/**
 * One recorded stretch of a player playing, the raw material every learned behaviour is built from.
 *
 * Demonstrations come from real people: recorded live through BlueSpoof's Trace module, imported
 * from a minigame plugin's own replay files, or captured from bots whose match outcome was good
 * enough to be worth imitating. BlueSpoof learns by imitation rather than by trial and error,
 * because exploring on a live server produces visibly broken bots and nobody wants to watch that.
 *
 * @since 3.9
 */
interface Demonstration {
    /** A unique id. */
    fun id(): String

    /** The game this was recorded in, for example `race`. */
    fun gameId(): String

    /**
     * The map this was recorded on, matching [net.blueva.spoof.api.game.GameSession.mapKey], so a
     * demonstration recorded before an arena was regenerated still applies afterwards.
     */
    fun mapKey(): String

    /** Where it came from: `trace`, `replay`, `bot` or a provider's own label. */
    fun source(): String

    /** When it was recorded, in milliseconds since the epoch. */
    fun recordedAt(): Long

    /** How many frames it holds. */
    fun frameCount(): Int

    /**
     * The frames, in order. May be loaded lazily, so hold the returned list rather than calling
     * this in a loop.
     */
    fun frames(): List<DemoFrame>

    /**
     * Free-form labels attached at capture time, for example `outcome=win`, `rank=1`,
     * `finish_seconds=41.2`. Queries filter on these, and policies weight demonstrations by them,
     * which is how "imitate the people who did well" is expressed.
     */
    fun tags(): Map<String, String>
}
