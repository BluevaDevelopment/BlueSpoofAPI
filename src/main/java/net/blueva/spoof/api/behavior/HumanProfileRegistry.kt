package net.blueva.spoof.api.behavior

import net.blueva.spoof.api.FakePlayer
import java.util.Optional

/**
 * Where [HumanProfile]s are defined and looked up.
 *
 * BlueSpoof ships a built-in set and derives one deterministically for every fake player from its
 * name. A plugin that wants a specific bot to behave a specific way registers its own profile and
 * assigns it.
 *
 * @since 3.9
 */
interface HumanProfileRegistry {
    /**
     * Registers a profile under its own id, replacing any profile already registered with that id.
     *
     * @since 3.9
     */
    fun register(profile: HumanProfile)

    /**
     * Looks up a profile by id.
     *
     * @since 3.9
     */
    fun byId(id: String): Optional<HumanProfile>

    /**
     * Every registered profile id.
     *
     * @since 3.9
     */
    fun ids(): Set<String>

    /**
     * The profile currently assigned to the given fake player. Never empty: a bot with no explicit
     * assignment gets one derived from its name.
     *
     * @since 3.9
     */
    fun forBot(bot: FakePlayer): HumanProfile

    /**
     * Assigns a profile to a fake player for as long as it stays connected. Passing `null` returns
     * it to the name-derived default.
     *
     * @since 3.9
     */
    fun assign(bot: FakePlayer, profileId: String?)
}
