package net.blueva.spoof.api.learning

import net.blueva.spoof.api.control.MoveInput
import java.util.Optional

/**
 * What a [Policy] decided to do.
 *
 * An action can be concrete movement, an abstract choice, or both. A route-following policy returns
 * a [moveInput] and an aim; a "fight or flee" policy returns a [label] and lets the behaviour work
 * out how to carry it out. [confidence] is what lets a behaviour fall back on something else when
 * the policy is guessing.
 *
 * @since 3.9
 */
class PolicyAction private constructor(
    /** What the policy chose, for example `follow_route`, `retreat`, `open_chest`. */
    @JvmField val label: String,
    /** How sure the policy is, 0.0 to 1.0. */
    @JvmField val confidence: Double,
    private val moveInput: MoveInput?,
    private val aimYaw: Double?,
    private val aimPitch: Double?,
    /** Any extra numbers the policy wants to hand back, interpreted by the behaviour. */
    @JvmField val params: Map<String, Double>
) {
    /** The movement input this action calls for, if it is a movement action. */
    fun moveInput(): Optional<MoveInput> = Optional.ofNullable(moveInput)

    /** The yaw this action calls for, if it aims. */
    fun aimYaw(): Optional<Double> = Optional.ofNullable(aimYaw)

    /** The pitch this action calls for, if it aims. */
    fun aimPitch(): Optional<Double> = Optional.ofNullable(aimPitch)

    override fun toString(): String = "PolicyAction($label, confidence=$confidence)"

    /** Builds an action. */
    class Builder(private val label: String) {
        private var confidence: Double = 1.0
        private var moveInput: MoveInput? = null
        private var aimYaw: Double? = null
        private var aimPitch: Double? = null
        private val params: MutableMap<String, Double> = LinkedHashMap()

        fun confidence(confidence: Double): Builder = apply { this.confidence = confidence }
        fun moveInput(moveInput: MoveInput?): Builder = apply { this.moveInput = moveInput }
        fun aim(yaw: Double, pitch: Double): Builder = apply {
            this.aimYaw = yaw
            this.aimPitch = pitch
        }
        fun param(key: String, value: Double): Builder = apply { params[key] = value }

        fun build(): PolicyAction =
            PolicyAction(label, confidence, moveInput, aimYaw, aimPitch, LinkedHashMap(params))
    }

    companion object {
        /** Starts building an action with the given label. */
        @JvmStatic
        fun of(label: String): Builder = Builder(label)

        /** An action meaning "I have nothing useful to say about this situation". */
        @JvmStatic
        fun none(): PolicyAction = Builder("none").confidence(0.0).build()
    }
}
