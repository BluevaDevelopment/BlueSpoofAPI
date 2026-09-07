package net.blueva.spoof.api.senses

/**
 * Something the server said to a fake player: a chat line, a system message, a title, a subtitle or
 * an action bar line.
 *
 * This is how a behaviour learns what a minigame is doing without knowing anything about the plugin
 * running it. Every minigame tells its players what is happening through the UI, so reading the
 * bot's own UI is a perception channel that works for plugins BlueSpoof has never heard of.
 *
 * @since 3.9
 */
class ReceivedMessage(
    /** Which UI channel the message arrived on. */
    @JvmField val channel: MessageChannel,
    /** The message with colour and formatting codes still in it. */
    @JvmField val raw: String,
    /** The message with all colour and formatting stripped, for matching. */
    @JvmField val plain: String,
    /** The server tick at which it arrived. */
    @JvmField val tick: Long,
    /** Wall-clock time at which it arrived, in milliseconds since the epoch. */
    @JvmField val timestamp: Long
) {
    override fun toString(): String = "ReceivedMessage($channel, \"$plain\")"
}

/**
 * The UI channel a [ReceivedMessage] arrived on.
 *
 * @since 3.9
 */
enum class MessageChannel {
    /** A message from another player, through the chat pipeline. */
    CHAT,

    /** A message from the server or a plugin, not attributable to a player. */
    SYSTEM,

    /** A large title. */
    TITLE,

    /** The smaller line under a title. */
    SUBTITLE,

    /** The line above the hotbar. */
    ACTION_BAR
}
