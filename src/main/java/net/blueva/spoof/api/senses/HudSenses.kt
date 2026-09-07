package net.blueva.spoof.api.senses

import java.util.Optional

/**
 * What the server has told this fake player through the interface: sidebar, titles, action bar,
 * chat and system messages.
 *
 * This is the most portable perception channel BlueSpoof has. Every minigame ever written tells its
 * players what is going on through the UI, so a behaviour that reads its own UI works against
 * plugins BlueSpoof knows nothing about, keeps working when those plugins are updated, and needs no
 * reflection at all. A bot that reads `Next colour: PINK` off a title knows what to stand on
 * without anybody having integrated anything.
 *
 * Messages are buffered per fake player with a bounded history. Everything here is a snapshot taken
 * when the call is made.
 *
 * Requires [net.blueva.spoof.api.control.Capabilities.hudInterception]; without it the buffers stay
 * empty rather than throwing, so a behaviour that treats "no HUD" and "nothing said yet" the same
 * way degrades correctly on its own.
 *
 * @since 3.9
 */
interface HudSenses {
    /**
     * The sidebar objective's display name, if the fake player has a sidebar.
     *
     * @since 3.9
     */
    fun scoreboardTitle(): Optional<String>

    /**
     * The sidebar lines, top to bottom, with formatting stripped. Empty when there is no sidebar.
     *
     * @since 3.9
     */
    fun scoreboardLines(): List<String>

    /**
     * The most recent title, if one is still worth considering current.
     *
     * @since 3.9
     */
    fun lastTitle(): Optional<ReceivedMessage>

    /**
     * The most recent subtitle.
     *
     * @since 3.9
     */
    fun lastSubtitle(): Optional<ReceivedMessage>

    /**
     * The most recent action bar line.
     *
     * @since 3.9
     */
    fun lastActionBar(): Optional<ReceivedMessage>

    /**
     * The most recent messages on the given channel, newest first, capped at [limit].
     *
     * @since 3.9
     */
    fun recent(channel: MessageChannel, limit: Int): List<ReceivedMessage>

    /**
     * Every buffered message that arrived after the given server tick, oldest first. The natural
     * call for a behaviour that processes what it has been told once per tick.
     *
     * @since 3.9
     */
    fun since(tick: Long): List<ReceivedMessage>

    /**
     * The most recent message on any channel whose plain text contains [text], ignoring case.
     *
     * @since 3.9
     */
    fun lastContaining(text: String): Optional<ReceivedMessage>
}
