package net.blueva.spoof.api.learning

/**
 * The entry point to everything BlueSpoof knows about learning from real players.
 *
 * Reached with `BlueSpoofAPI.learning()`.
 *
 * @since 3.9
 */
interface LearningService {
    /**
     * Whether learning is enabled on this server. When it is not, the store accepts nothing and the
     * registry resolves nothing, so a behaviour that asks and gets nothing back should fall back to
     * hand-written logic rather than refusing to run.
     *
     * @since 3.9
     */
    fun isEnabled(): Boolean

    /** Where demonstrations are kept. */
    fun store(): DemonstrationStore

    /** Where policies come from. */
    fun policies(): PolicyRegistry
}
