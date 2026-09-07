package net.blueva.spoof.api.brain

/**
 * Why a [BotBrain] stopped controlling a fake player.
 *
 * @since 3.9
 */
enum class DetachReason {
    /** Another brain with a higher priority claimed the bot. */
    REPLACED,

    /** The brain gave the bot up itself, through [BrainContext.yield]. */
    YIELDED,

    /** The fake player disconnected. */
    BOT_OFFLINE,

    /** The plugin that registered the brain's factory was disabled. */
    PLUGIN_DISABLED,

    /** A server operator took manual control of the bot. */
    OPERATOR_OVERRIDE,

    /** The brain threw too many times in a row and was detached to protect the server. */
    FAILED
}
