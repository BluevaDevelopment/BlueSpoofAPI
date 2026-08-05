package net.blueva.spoof.api

import org.bukkit.Location
import org.bukkit.entity.Player
import java.util.Optional
import java.util.UUID

/**
 * Entry point of the BlueSpoof public API.
 *
 * BlueSpoof spawns **fake players**: real server-side players (with physics,
 * inventory, health and tab-list presence) controlled by the server instead of a
 * network client. This API lets any plugin drive those fake players the same way
 * a real client would: walk, sprint, sneak, jump, look around, chat, swing, attack,
 * use items and follow paths.
 *
 * All methods are static and delegate to a [Provider] registered by the
 * BlueSpoof plugin while it is enabled. If BlueSpoof is not installed or not yet
 * enabled, every call throws [IllegalStateException]; guard with
 * [isAvailable] when your plugin soft-depends on BlueSpoof.
 *
 * @since 3.7
 */
object BlueSpoofAPI {
    /** API version in `major.api` format (Blueva API versioning convention). */
    const val VERSION: String = "3.7"

    @Volatile
    private var provider: Provider? = null

    /**
     * Registers the API provider.
     *
     * **Internal:** called exclusively by the BlueSpoof plugin on enable
     * (and with `null` on disable). Never call this from a consumer plugin.
     *
     * @param newProvider the provider implementation, or `null` to unregister
     */
    @JvmStatic
    fun setProvider(newProvider: Provider?) {
        provider = newProvider
    }

    /**
     * Returns whether the BlueSpoof plugin is installed and its API is ready to use.
     *
     * @return `true` if a provider is registered
     * @since 3.7
     */
    @JvmStatic
    fun isAvailable(): Boolean {
        return provider != null
    }

    /**
     * Returns the API version in `major.api` format.
     *
     * @return the API version, e.g. `"1.0"`
     * @since 3.7
     */
    @JvmStatic
    fun getVersion(): String {
        return VERSION
    }

    /**
     * Returns all fake players currently online on this server.
     *
     * @return immutable snapshot of online fake players
     * @throws IllegalStateException if the API is not available
     * @since 3.7
     */
    @JvmStatic
    fun getFakePlayers(): MutableCollection<FakePlayer> {
        return requireProvider().fakePlayers
    }

    /**
     * Returns the online fake player with the given name, if present.
     *
     * @param name exact player name (case-sensitive)
     * @return the fake player, or empty if no fake player with that name is online
     * @throws IllegalStateException if the API is not available
     * @since 3.7
     */
    @JvmStatic
    fun getFakePlayer(name: String?): Optional<FakePlayer> {
        return requireProvider().getFakePlayer(name)
    }

    /**
     * Returns the online fake player with the given UUID, if present.
     *
     * @param uuid player UUID
     * @return the fake player, or empty if no fake player with that UUID is online
     * @throws IllegalStateException if the API is not available
     * @since 3.7
     */
    @JvmStatic
    fun getFakePlayer(uuid: UUID?): Optional<FakePlayer> {
        return requireProvider().getFakePlayer(uuid)
    }

    /**
     * Wraps a Bukkit [Player] as a [FakePlayer], if that player is a
     * BlueSpoof fake player.
     *
     * @param player any online player
     * @return the fake player handle, or empty if the player is real (or offline)
     * @throws IllegalStateException if the API is not available
     * @since 3.7
     */
    @JvmStatic
    fun getFakePlayer(player: Player?): Optional<FakePlayer> {
        return requireProvider().getFakePlayer(player)
    }

    /**
     * Returns whether the given player is a BlueSpoof fake player.
     *
     * @param player any player
     * @return `true` if the player is a fake player created by BlueSpoof
     * @throws IllegalStateException if the API is not available
     * @since 3.7
     */
    @JvmStatic
    fun isFakePlayer(player: Player?): Boolean {
        return requireProvider().isFakePlayer(player)
    }

    /**
     * Returns whether an online fake player with the given name exists.
     *
     * @param name exact player name
     * @return `true` if a fake player with that name is online
     * @throws IllegalStateException if the API is not available
     * @since 3.7
     */
    @JvmStatic
    fun isFakePlayer(name: String?): Boolean {
        return requireProvider().isFakePlayer(name)
    }

    /**
     * Connects a new fake player with the given name at the given location.
     *
     * The fake player joins the server like a real player: join message,
     * tab-list entry, skin, `PlayerJoinEvent`, survival game mode and full
     * physics. Use [FakePlayer.disconnect] to remove it again.
     *
     * @param name     player name (must be a valid Minecraft name, 3-16 characters)
     * @param location spawn location
     * @return the connected fake player
     * @throws IllegalStateException    if the API is not available or a fake player
     * with that name is already online
     * @throws IllegalArgumentException if the name is not a valid Minecraft name
     * @since 3.7
     */
    @JvmStatic
    fun createFakePlayer(name: String?, location: Location?): FakePlayer {
        return requireProvider().createFakePlayer(name, location)
    }

    private fun requireProvider(): Provider {
        val current = provider
        checkNotNull(current) { "BlueSpoof API is not available: is the BlueSpoof plugin installed and enabled?" }
        return current
    }

    /**
     * Provider contract implemented by the BlueSpoof plugin.
     *
     * **Do not implement this in consumer plugins.** Methods added in future
     * API versions will be declared as `default` so existing implementations
     * keep working.
     *
     * @since 3.7
     */
    interface Provider {
        /** @see BlueSpoofAPI.getFakePlayers */
        val fakePlayers: MutableCollection<FakePlayer>

        /** @see BlueSpoofAPI.getFakePlayer */
        fun getFakePlayer(name: String?): Optional<FakePlayer>

        /** @see BlueSpoofAPI.getFakePlayer */
        fun getFakePlayer(uuid: UUID?): Optional<FakePlayer>

        /** @see BlueSpoofAPI.getFakePlayer */
        fun getFakePlayer(player: Player?): Optional<FakePlayer>

        /** @see BlueSpoofAPI.isFakePlayer */
        fun isFakePlayer(player: Player?): Boolean

        /** @see BlueSpoofAPI.isFakePlayer */
        fun isFakePlayer(name: String?): Boolean

        /** @see BlueSpoofAPI.createFakePlayer */
        fun createFakePlayer(name: String?, location: Location?): FakePlayer
    }
}
