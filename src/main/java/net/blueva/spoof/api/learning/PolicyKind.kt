package net.blueva.spoof.api.learning

/**
 * How a [Policy] arrives at its decisions, so operators and logs can tell at a glance what is
 * driving a bot.
 *
 * @since 3.9
 */
enum class PolicyKind {
    /** Hand-written rules. Not learned at all, but exposed through the same interface. */
    SCRIPTED,

    /**
     * Looks up the most similar recorded situations and reuses what the humans did in them. Needs
     * no training step, adapts to a map as soon as somebody plays it, and degrades to "no close
     * match, defer to something else" rather than to nonsense.
     */
    NEAREST_NEIGHBOUR,

    /** A linear or tile-coded function fitted to demonstrations. */
    LINEAR,

    /** A lookup table over discretised features. */
    TABULAR,

    /** A small neural network, trained offline and run through a downloaded inference runtime. */
    NEURAL
}
