package net.blueva.spoof.api.game

import net.blueva.spoof.api.FakePlayer
import org.bukkit.Location
import org.bukkit.entity.Player
import java.util.Optional

/**
 * One running match, described in terms every behaviour can use, whatever plugin is actually
 * running it.
 *
 * This is the interface that lets a behaviour reason about a minigame without knowing anything
 * about the minigame plugin. BlueSpoof supplies an implementation for BlueArcade; any other plugin
 * supplies its own by implementing [GameSessionProvider], and immediately gets every behaviour
 * BlueSpoof ships plus anything third parties have registered.
 *
 * Everything here is a snapshot taken when the call is made.
 *
 * @since 3.9
 */
interface GameSession {
    /**
     * The id of the provider that produced this session, for example `bluearcade`.
     *
     * @since 3.9
     */
    fun providerId(): String

    /**
     * The id of the game being played, for example `race` or `skywars`. Behaviours key off this to
     * decide whether they understand the match.
     *
     * @since 3.9
     */
    fun gameId(): String

    /**
     * The id of the arena the match is running in, unique within the provider.
     *
     * @since 3.9
     */
    fun arenaId(): String

    /**
     * A key identifying the *map* rather than the match, stable across restarts and across
     * temporary worlds cloned from the same template. Learned behaviour is stored against this, so
     * a bot that learned a map does not forget it when the arena is regenerated.
     *
     * @since 3.9
     */
    fun mapKey(): String

    /** The current phase. */
    fun phase(): GameLifecycle

    /** The current round, 1-based, or 1 for games without rounds. */
    fun round(): Int

    /** The total number of rounds, or 1 for games without rounds. */
    fun maxRounds(): Int

    /** Seconds left in the current phase, or -1 when the provider does not expose one. */
    fun secondsRemaining(): Int

    /**
     * The playable area, if the provider knows it. Behaviours use it to avoid wandering out of the
     * arena and as the bound for area scans.
     *
     * @since 3.9
     */
    fun bounds(): Optional<Region>

    /**
     * The configured spawn points for this match.
     *
     * @since 3.9
     */
    fun spawns(): List<Location>

    /**
     * Named pieces of geometry the game has configured, for example `finish_line`, `floors`,
     * `islands`, `beds` or `generators`.
     *
     * The keys are whatever the game itself uses. A behaviour that recognises a key uses it; one
     * that does not, ignores it. This is what lets a generic behaviour do something sensible in a
     * game nobody wrote an adapter for: if there is a `finish_line`, walk towards it.
     *
     * @since 3.9
     */
    fun namedRegions(key: String): List<Region>

    /**
     * Every named-region key this session knows about.
     *
     * @since 3.9
     */
    fun regionKeys(): Set<String>

    /**
     * A value from the game's own configuration, by dotted path.
     *
     * This is how a behaviour learns the things a game is *configured* to do rather than the things
     * it is currently doing: which block a floor decays into, how long a round lasts, which item
     * marks the objective. A server owner who retunes their minigame retunes the bots with it, for
     * free, because both read the same setting.
     *
     * Paths are the game's own, so a behaviour asking for one is coupled to that game, which is
     * fine: it is a behaviour for that game. A behaviour that asks for a path the game does not
     * have gets an empty optional and should have a default ready.
     *
     * @since 3.9
     */
    fun setting(path: String): Optional<String>

    /**
     * A list value from the game's own configuration. Empty when the path is missing or is not a
     * list.
     *
     * @since 3.9
     */
    fun settingList(path: String): List<String>

    /**
     * Reads the values out of a message the game sent, using the game's own message template.
     *
     * Minigames announce what they are doing in prose: "Next colour: PINK", "30 seconds left",
     * "RedTeam captured the wool". A bot can see that text on its own interface
     * ([net.blueva.spoof.api.senses.HudSenses]) but cannot do much with it as a string. This turns
     * it back into the values the game filled in.
     *
     * Given the template key the game uses (`messages.round.reveal`) and a line the bot received,
     * returns the placeholders the template names, mapped to what they were filled in with. So a
     * template of `"Next colour: {block}"` and a received line of `"Next colour: PINK"` gives
     * `{block=PINK}`.
     *
     * Matching against the game's own template rather than against a hard-coded English phrase is
     * what makes this survive translation: a server running in Spanish sends the Spanish template
     * and the same call still returns `{block=ROSA}`. It also survives a server owner rewording
     * their messages, as long as they leave the placeholders in.
     *
     * Empty when the game has no such template, when the line does not match it, or when the
     * provider does not publish templates at all.
     *
     * @since 3.9
     */
    fun readMessage(templateKey: String, message: String): Optional<Map<String, String>>

    /**
     * A provider-specific value, for behaviours written against one specific provider.
     *
     * Documented as unstable on purpose: anything reached through here is outside the contract the
     * rest of this interface offers, and a behaviour that depends on it is no longer portable. Use
     * it as an escape hatch, and prefer asking for a real accessor.
     *
     * @since 3.9
     */
    fun raw(key: String): Optional<Any>

    /** Every player in the match: playing, spectating and eliminated. */
    fun participants(): List<Player>

    /** Every player still actively playing. */
    fun alive(): List<Player>

    /** Every player watching rather than playing. */
    fun spectators(): List<Player>

    /** The fake players in this match. */
    fun bots(): Set<FakePlayer>

    /** Whether the given player is in this match at all. */
    fun contains(player: Player): Boolean

    /** Whether the given player is still actively playing. */
    fun isAlive(player: Player): Boolean

    /**
     * The team resolver for this match, if the game has teams.
     *
     * @since 3.9
     */
    fun teams(): Optional<TeamResolver>
}
