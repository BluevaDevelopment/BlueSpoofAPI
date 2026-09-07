package net.blueva.spoof.api.learning

/**
 * Something that turns a situation into a decision.
 *
 * A policy is deliberately narrow: given a [FeatureVector], return a [PolicyAction]. It does not
 * touch the world, does not know about fake players, and is safe to unit test on its own. The
 * behaviour that owns it decides what to do with the answer, including ignoring it when the
 * confidence is low.
 *
 * [decide] runs on the server tick loop. Anything that cannot answer in well under a millisecond
 * belongs behind a cache or off-thread with the answer applied a tick later.
 *
 * @since 3.9
 */
interface Policy {
    /** A stable id. */
    fun id(): String

    /** How this policy decides. */
    fun kind(): PolicyKind

    /**
     * The feature schema this policy expects. Feeding it a vector built against a different schema
     * throws rather than silently producing wrong answers.
     */
    fun schema(): String

    /**
     * Decides what to do in the given situation.
     *
     * @throws IllegalArgumentException if the vector's schema does not match [schema]
     * @since 3.9
     */
    fun decide(observation: FeatureVector): PolicyAction

    /**
     * Tells the policy how a past decision turned out, so it can adapt.
     *
     * Adaptation is bounded on purpose: a policy may re-weight what it already knows, but never
     * drift far from the demonstrations it was built from. A run of bad luck must not be able to
     * teach a bot something absurd.
     *
     * @param reward positive when the decision worked out, negative when it did not
     * @since 3.9
     */
    fun observeOutcome(observation: FeatureVector, action: PolicyAction, reward: Double) {}
}
