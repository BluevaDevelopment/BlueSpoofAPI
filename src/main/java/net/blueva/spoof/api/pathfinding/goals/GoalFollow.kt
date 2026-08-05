package net.blueva.spoof.api.pathfinding.goals

import net.blueva.spoof.api.pathfinding.Goal
import net.blueva.spoof.api.pathfinding.PathNode
import org.bukkit.entity.Entity
import java.lang.ref.WeakReference
import kotlin.math.max
import kotlin.math.sqrt

/**
 * Dynamic goal that follows an entity, staying within a horizontal range of it.
 *
 * Being [dynamic][Goal.isDynamic], the pathfinder periodically recomputes the
 * path as the target moves. The goal is never reported as permanently
 * reached while the target keeps moving; navigation ends when the fake
 * player stands within range, when the entity is removed, or when the
 * navigation is cancelled.
 *
 * @since 3.7
 */
class GoalFollow(entity: Entity, range: Double) : Goal {
    private val entityRef: WeakReference<Entity>

    /** Desired horizontal distance in blocks. */
    val range: Double

    /**
     * Creates a follow goal for the given entity.
     *
     * @param entity target entity (held via weak reference; navigation ends
     * when the entity is garbage collected or removed)
     * @param range  desired horizontal distance in blocks
     */
    init {
        this.entityRef = WeakReference(entity)
        this.range = max(0.0, range)
    }

    /**
     * Returns the followed entity, or `null` if it is gone.
     */
    val entity: Entity?
        get() = entityRef.get()

    override fun isDynamic(): Boolean {
        return true
    }

    override fun isEnd(node: PathNode): Boolean {
        val entity = entityRef.get()
        if (entity == null || !entity.isValid) {
            return true
        }
        val location = entity.location
        val dx = node.x - location.blockX
        val dz = node.z - location.blockZ
        return sqrt((dx * dx + dz * dz).toDouble()) <= range
    }

    override fun heuristic(node: PathNode): Double {
        val entity = entityRef.get()
        if (entity == null) {
            return 0.0
        }
        val location = entity.location
        val dx = node.x - location.blockX
        val dy = node.y - location.blockY
        val dz = node.z - location.blockZ
        return max(0.0, sqrt((dx * dx + dy * dy + dz * dz).toDouble()) - range)
    }

    override fun toString(): String {
        val entity = entityRef.get()
        return "GoalFollow(" + (if (entity == null) "gone" else entity.type) + ", range=" + range + ")"
    }
}
