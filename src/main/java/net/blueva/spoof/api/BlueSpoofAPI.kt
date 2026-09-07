package net.blueva.spoof.api

import net.blueva.spoof.api.behavior.HumanProfileRegistry
import net.blueva.spoof.api.brain.BrainRegistry
import net.blueva.spoof.api.game.GameRegistry
import net.blueva.spoof.api.learning.LearningService
import org.bukkit.Location
import org.bukkit.entity.Player
import java.util.Optional
import java.util.UUID

/**
 * Entry point of the BlueSpoof public API.
 *
 * BlueSpoof spawns **fake players**: real server-side players, with physics, inventory, health and
 * tab-list presence, controlled by the server instead of by a network client. This API lets any
 * plugin drive them the way a client would, and lets any plugin teach them to play its own content.
 *
 * There are two ways to use it, and most integrations want the second:
 *
 * 1. **Drive a bot directly.** Get a [FakePlayer], read [FakePlayer.senses], call
 *    [FakePlayer.actuator]. Fine for scripted set pieces.
 * 2. **Register a behaviour and let BlueSpoof run it.** Register a
 *    [net.blueva.spoof.api.brain.BotBrainFactory] with [brains] and BlueSpoof attaches it to the
 *    bots it claims, ticks it, arbitrates it against every other plugin's behaviours, and enforces
 *    that the result still looks like a person. Register a
 *    [net.blueva.spoof.api.game.GameSessionProvider] with [games] as well and your minigame becomes
 *    something every behaviour on the server understands, including the ones you did not write.
 *
 * All methods are static and delegate to a [Provider] registered by the BlueSpoof plugin while it
 * is enabled. If BlueSpoof is not installed or not yet enabled, every call throws
 * [IllegalStateException]; guard with [isAvailable] when soft-depending on BlueSpoof.
 *
 * @since 3.9
 */
object BlueSpoofAPI {

    /** API version, in the `major.api` form used across Blueva APIs. */
    const val VERSION: String = "3.9"

    @Volatile
    private var provider: Provider? = null

    /**
     * Registers the API provider.
     *
     * **Internal:** called exclusively by the BlueSpoof plugin on enable, and with `null` on
     * disable. Never call this from a consumer plugin.
     */
    @JvmStatic
    fun setProvider(newProvider: Provider?) {
        provider = newProvider
    }

    /**
     * Whether the BlueSpoof plugin is installed and its API is ready to use.
     *
     * @since 3.9
     */
    @JvmStatic
    fun isAvailable(): Boolean = provider != null

    /**
     * The API version.
     *
     * @since 3.9
     */
    @JvmStatic
    fun getVersion(): String = VERSION

    // ------------------------------------------------------------------
    // Fake players
    // ------------------------------------------------------------------

    /**
     * Every fake player currently online on this server.
     *
     * @since 3.9
     */
    @JvmStatic
    fun getFakePlayers(): Collection<FakePlayer> = requireProvider().fakePlayers()

    /**
     * The online fake player with the given name, if there is one.
     *
     * @param name exact player name, case-sensitive
     * @since 3.9
     */
    @JvmStatic
    fun getFakePlayer(name: String): Optional<FakePlayer> = requireProvider().getFakePlayer(name)

    /**
     * The online fake player with the given UUID, if there is one.
     *
     * @since 3.9
     */
    @JvmStatic
    fun getFakePlayer(uuid: UUID): Optional<FakePlayer> = requireProvider().getFakePlayer(uuid)

    /**
     * The given Bukkit player as a [FakePlayer], if it is one.
     *
     * @since 3.9
     */
    @JvmStatic
    fun getFakePlayer(player: Player): Optional<FakePlayer> = requireProvider().getFakePlayer(player)

    /**
     * Whether the given player is a BlueSpoof fake player.
     *
     * @since 3.9
     */
    @JvmStatic
    fun isFakePlayer(player: Player): Boolean = requireProvider().isFakePlayer(player)

    /**
     * Whether an online fake player with the given name exists.
     *
     * @since 3.9
     */
    @JvmStatic
    fun isFakePlayer(name: String): Boolean = requireProvider().isFakePlayer(name)

    /**
     * Connects a new fake player with the given name at the given location.
     *
     * The fake player joins like a real one: join message, tab-list entry, skin, `PlayerJoinEvent`,
     * survival game mode and full physics. Remove it again with [FakePlayer.disconnect].
     *
     * @throws IllegalStateException if a fake player with that name is already online
     * @throws IllegalArgumentException if the name is not a valid Minecraft name
     * @since 3.9
     */
    @JvmStatic
    fun createFakePlayer(name: String, location: Location): FakePlayer =
        requireProvider().createFakePlayer(name, location)

    // ------------------------------------------------------------------
    // Services
    // ------------------------------------------------------------------

    /**
     * Where behaviours are registered and arbitrated between plugins.
     *
     * @since 3.9
     */
    @JvmStatic
    fun brains(): BrainRegistry = requireProvider().brains()

    /**
     * Where minigame plugins describe their running matches so that bots can understand them.
     *
     * @since 3.9
     */
    @JvmStatic
    fun games(): GameRegistry = requireProvider().games()

    /**
     * Where the behavioural fingerprints that keep bots looking human are defined.
     *
     * @since 3.9
     */
    @JvmStatic
    fun profiles(): HumanProfileRegistry = requireProvider().profiles()

    /**
     * Demonstrations recorded from real players, and the policies built from them.
     *
     * @since 3.9
     */
    @JvmStatic
    fun learning(): LearningService = requireProvider().learning()

    private fun requireProvider(): Provider {
        val current = provider
        checkNotNull(current) {
            "BlueSpoof API is not available: is the BlueSpoof plugin installed and enabled?"
        }
        return current
    }

    /**
     * Provider contract implemented by the BlueSpoof plugin.
     *
     * **Do not implement this in a consumer plugin.**
     *
     * @since 3.9
     */
    interface Provider {
        fun fakePlayers(): Collection<FakePlayer>
        fun getFakePlayer(name: String): Optional<FakePlayer>
        fun getFakePlayer(uuid: UUID): Optional<FakePlayer>
        fun getFakePlayer(player: Player): Optional<FakePlayer>
        fun isFakePlayer(player: Player): Boolean
        fun isFakePlayer(name: String): Boolean
        fun createFakePlayer(name: String, location: Location): FakePlayer
        fun brains(): BrainRegistry
        fun games(): GameRegistry
        fun profiles(): HumanProfileRegistry
        fun learning(): LearningService
    }
}
