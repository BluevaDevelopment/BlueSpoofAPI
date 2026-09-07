package net.blueva.spoof.api.pathfinding

import kotlin.math.max
import kotlin.math.min

/**
 * Movement configuration used by the [Pathfinder].
 *
 * Each fake player has its own instance, reached through [Pathfinder.movements]. Values are clamped
 * to sane ranges on construction, so a caller cannot ask for a 500-block drop or a 10-millisecond
 * search budget and quietly get one.
 *
 * @since 3.9
 */
data class Movements(
    /** Whether sprinting is allowed. */
    @JvmField val allowSprinting: Boolean = true,
    /** Whether diagonal moves are allowed. */
    @JvmField val allowDiagonal: Boolean = true,
    /** Whether parkour jumps across gaps are allowed. */
    @JvmField val allowParkour: Boolean = true,
    /** Widest gap in blocks a parkour jump may cross, clamped to 1 through 3. */
    @JvmField val maxParkourGap: Int = 1,
    /** Whether swimming is allowed. */
    @JvmField val allowSwimming: Boolean = true,
    /** Whether climbing ladders and vines is allowed. */
    @JvmField val allowClimbing: Boolean = true,
    /** Most blocks the fake player may drop down in one move, never negative. */
    @JvmField val maxDropDown: Int = 4,
    /** Most nodes the A* search may expand, or -1 for unlimited. */
    @JvmField val searchRadius: Int = 10000,
    /** Longest a path computation may take, in milliseconds, at least 250. */
    @JvmField val thinkTimeoutMillis: Long = 10000L
) {
    init {
        require(maxParkourGap == maxParkourGap.coerceIn(1, 3)) {
            "maxParkourGap must be between 1 and 3, got $maxParkourGap"
        }
        require(maxDropDown >= 0) { "maxDropDown cannot be negative, got $maxDropDown" }
        require(thinkTimeoutMillis >= 250L) {
            "thinkTimeoutMillis must be at least 250, got $thinkTimeoutMillis"
        }
    }

    companion object {
        /** The default configuration. */
        @JvmStatic
        val DEFAULT: Movements = Movements()

        /**
         * A configuration with every clamped value coerced into range rather than rejected, for
         * callers building one from user-supplied configuration.
         *
         * @since 3.9
         */
        @JvmStatic
        @JvmOverloads
        fun clamped(
            allowSprinting: Boolean = true,
            allowDiagonal: Boolean = true,
            allowParkour: Boolean = true,
            maxParkourGap: Int = 1,
            allowSwimming: Boolean = true,
            allowClimbing: Boolean = true,
            maxDropDown: Int = 4,
            searchRadius: Int = 10000,
            thinkTimeoutMillis: Long = 10000L
        ): Movements = Movements(
            allowSprinting,
            allowDiagonal,
            allowParkour,
            min(3, max(1, maxParkourGap)),
            allowSwimming,
            allowClimbing,
            max(0, maxDropDown),
            if (searchRadius < 0) -1 else searchRadius,
            max(250L, thinkTimeoutMillis)
        )
    }
}
