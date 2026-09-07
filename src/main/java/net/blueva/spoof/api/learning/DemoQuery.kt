package net.blueva.spoof.api.learning

/**
 * A filter over stored [Demonstration]s.
 *
 * @since 3.9
 */
class DemoQuery private constructor(
    @JvmField val gameId: String?,
    @JvmField val mapKey: String?,
    @JvmField val source: String?,
    @JvmField val requiredTags: Map<String, String>,
    @JvmField val minFrames: Int,
    @JvmField val limit: Int
) {
    /** Builder for a query. */
    class Builder {
        private var gameId: String? = null
        private var mapKey: String? = null
        private var source: String? = null
        private val requiredTags: MutableMap<String, String> = LinkedHashMap()
        private var minFrames: Int = 0
        private var limit: Int = 100

        fun gameId(gameId: String?): Builder = apply { this.gameId = gameId }
        fun mapKey(mapKey: String?): Builder = apply { this.mapKey = mapKey }
        fun source(source: String?): Builder = apply { this.source = source }
        fun tag(key: String, value: String): Builder = apply { requiredTags[key] = value }
        fun minFrames(minFrames: Int): Builder = apply { this.minFrames = minFrames }
        fun limit(limit: Int): Builder = apply { this.limit = limit }

        fun build(): DemoQuery =
            DemoQuery(gameId, mapKey, source, LinkedHashMap(requiredTags), minFrames, limit)
    }

    companion object {
        /** Starts building a query. */
        @JvmStatic
        fun builder(): Builder = Builder()

        /** A query for every demonstration recorded on one map of one game. */
        @JvmStatic
        fun forMap(gameId: String, mapKey: String): DemoQuery =
            builder().gameId(gameId).mapKey(mapKey).build()
    }
}
