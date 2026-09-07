package net.blueva.spoof.api.game

/**
 * The phase a game is in, normalised across whatever the underlying minigame plugin calls it.
 *
 * @since 3.9
 */
enum class GameLifecycle {
    /** Players are gathering. The match has not been committed to yet. */
    WAITING,

    /** Enough players have gathered and the match is counting down. */
    COUNTDOWN,

    /** The match is being played. */
    PLAYING,

    /** The match is over and results are being shown. */
    ENDING,

    /** The match is finished and the arena is being cleaned up. */
    FINISHED,

    /** The provider could not determine the phase. Behaviours should act conservatively. */
    UNKNOWN
}
