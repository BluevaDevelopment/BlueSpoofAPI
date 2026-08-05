package net.blueva.spoof.api.pathfinding

import kotlin.math.max
import kotlin.math.min

/**
 * Movement configuration used by the [Pathfinder].
 *
 * Each fake player has its own instance; mutate it through
 * [Pathfinder.movements] or replace it with [Pathfinder.setMovements].
 *
 * @since 3.7
 */
class Movements {

    /** `true` if sprinting is allowed (default `true`). */
    var isAllowSprinting: Boolean = true

    /** `true` if diagonal moves are allowed (default `true`). */
    var isAllowDiagonal: Boolean = true

    /** `true` if parkour jumps are allowed (default `true`). */
    var isAllowParkour: Boolean = true

    private var backingMaxParkourGap: Int = 1

    /** Maximum gap width (in blocks) the fake player may leap with a parkour jump (clamped to 1-3). */
    var maxParkourGap: Int
        get() = backingMaxParkourGap
        set(value) {
            backingMaxParkourGap = max(1, min(3, value))
        }

    /** `true` if swimming is allowed (default `true`). */
    var isAllowSwimming: Boolean = true

    /** `true` if climbing is allowed (default `true`). */
    var isAllowClimbing: Boolean = true

    private var backingMaxDropDown: Int = 4

    /** Maximum number of blocks the fake player may drop down in one move. */
    var maxDropDown: Int
        get() = backingMaxDropDown
        set(value) {
            backingMaxDropDown = max(0, value)
        }

    private var backingSearchRadius: Int = 10000

    /** Maximum number of nodes the A* search may expand, or `-1` for unlimited. */
    var searchRadius: Int
        get() = backingSearchRadius
        set(value) {
            backingSearchRadius = if (value < 0) -1 else value
        }

    private var backingThinkTimeoutMillis: Long = 10000L

    /** Maximum wall-clock time a path computation may take, in milliseconds. */
    var thinkTimeoutMillis: Long
        get() = backingThinkTimeoutMillis
        set(value) {
            backingThinkTimeoutMillis = max(250L, value)
        }

    /**
     * Creates a configuration with the default values.
     */
    constructor()

    /**
     * Copy constructor.
     *
     * @param other configuration to copy
     */
    constructor(other: Movements) {
        this.isAllowSprinting = other.isAllowSprinting
        this.isAllowDiagonal = other.isAllowDiagonal
        this.isAllowParkour = other.isAllowParkour
        this.backingMaxParkourGap = other.backingMaxParkourGap
        this.isAllowSwimming = other.isAllowSwimming
        this.isAllowClimbing = other.isAllowClimbing
        this.backingMaxDropDown = other.backingMaxDropDown
        this.backingSearchRadius = other.backingSearchRadius
        this.backingThinkTimeoutMillis = other.backingThinkTimeoutMillis
    }

    /**
     * Returns a defensive copy of this configuration.
     *
     * @return copy
     */
    fun copy(): Movements {
        return Movements(this)
    }
}
