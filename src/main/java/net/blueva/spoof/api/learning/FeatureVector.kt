package net.blueva.spoof.api.learning

/**
 * A named vector of numbers describing one situation, the input a [Policy] decides from.
 *
 * The names matter as much as the values. A policy trained offline and a policy running on a server
 * have to agree on what position 7 means, so a vector carries its [schema] and a policy refuses a
 * vector whose schema it was not trained on. Failing loudly on a schema mismatch is the whole point:
 * silently feeding a model the wrong columns produces a bot that behaves strangely for reasons
 * nobody can trace.
 *
 * @since 3.9
 */
class FeatureVector(
    /** The schema id this vector was built against, for example `race.route.v1`. */
    @JvmField val schema: String,
    /** The feature names, in the same order as [values]. */
    @JvmField val names: List<String>,
    /** The values. */
    @JvmField val values: DoubleArray
) {
    init {
        require(names.size == values.size) {
            "feature vector has ${names.size} names but ${values.size} values"
        }
    }

    /** The number of features. */
    val size: Int
        get() = values.size

    /**
     * The value of a named feature.
     *
     * @throws IllegalArgumentException if the vector has no such feature
     * @since 3.9
     */
    operator fun get(name: String): Double {
        val index = names.indexOf(name)
        require(index >= 0) { "no feature named '$name' in schema '$schema'" }
        return values[index]
    }

    override fun toString(): String = "FeatureVector($schema, ${values.size} features)"

    /** Builds a vector one named feature at a time. */
    class Builder(private val schema: String) {
        private val names: MutableList<String> = ArrayList()
        private val values: MutableList<Double> = ArrayList()

        fun put(name: String, value: Double): Builder = apply {
            names.add(name)
            values.add(value)
        }

        fun put(name: String, value: Boolean): Builder = put(name, if (value) 1.0 else 0.0)

        fun build(): FeatureVector = FeatureVector(schema, ArrayList(names), values.toDoubleArray())
    }

    companion object {
        /** Starts building a vector against the given schema. */
        @JvmStatic
        fun builder(schema: String): Builder = Builder(schema)
    }
}
