package net.blueva.spoof.api.control

import net.blueva.spoof.api.ControlState
import net.blueva.spoof.api.inventory.ContainerView
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector
import java.util.Optional
import java.util.concurrent.CompletableFuture

/**
 * Everything a fake player can do: the single path from a behaviour to the server.
 *
 * There is exactly one actuator per fake player, reached with
 * [net.blueva.spoof.api.FakePlayer.actuator]. Everything that goes through it is shaped by the
 * bot's [net.blueva.spoof.api.behavior.HumanProfile]: rotations are smoothed and never exact,
 * reactions are delayed, and the total rate of actions is capped. That shaping is not optional and
 * cannot be bypassed, which is what keeps bots from behaving like aimbots no matter what a
 * behaviour asks for.
 *
 * Movement is client-style input, not teleports. [moveInput] accelerates, walks and stops with
 * vanilla friction; jumping produces the real jump arc; knockback, gravity, water and ladders are
 * handled by the server's own physics.
 *
 * Actions with an observable outcome return a [CompletableFuture] that completes when the outcome
 * happens. Futures complete exceptionally with [IllegalStateException] if the fake player is no
 * longer online, and always complete on the server's main thread (or the owning region thread on
 * Folia), so it is safe to touch the Bukkit API from plain `thenAccept`/`thenRun` continuations.
 *
 * @since 3.9
 */
interface Actuator {

    // ------------------------------------------------------------------
    // Movement
    // ------------------------------------------------------------------

    /**
     * Applies one tick of analog movement input. Input does not persist: a behaviour that wants
     * the bot to keep walking calls this every tick, exactly like a client sending a movement
     * packet every tick.
     *
     * On server versions without [Capabilities.analogMovement] the input is quantised onto the
     * seven [ControlState] keys, which is accurate enough for walking but not for frame-precise
     * parkour.
     *
     * @since 3.9
     */
    fun moveInput(input: MoveInput)

    /**
     * Convenience for `moveInput(MoveInput(forward, strafe))`.
     *
     * @since 3.9
     */
    fun moveInput(forward: Float, strafe: Float) {
        moveInput(MoveInput(forward, strafe))
    }

    /**
     * Holds or releases a movement control, the older key-at-a-time model. States persist until
     * changed, unlike [moveInput]. Useful for "hold sneak for the next few seconds"; prefer
     * [moveInput] for anything that steers.
     *
     * @since 3.9
     */
    fun setControlState(state: ControlState, active: Boolean)

    /**
     * Returns whether the given control is currently held.
     *
     * @since 3.9
     */
    fun isControlStateActive(state: ControlState): Boolean

    /**
     * Releases every control state and clears any analog input. The fake player decelerates to a
     * stop with vanilla friction.
     *
     * @since 3.9
     */
    fun releaseAll()

    /**
     * Jumps once if on the ground, or swims upward in a liquid.
     *
     * @since 3.9
     */
    fun jump(): CompletableFuture<Void>

    /**
     * Performs the sprint-jump combination with the timing a real player uses: begin sprinting,
     * jump on the following tick, keep the forward input through the arc. This is the movement
     * that clears gaps, and getting the timing right by hand from [jump] and [moveInput] is
     * fiddly enough to be worth a dedicated call.
     *
     * @since 3.9
     */
    fun sprintJump(): CompletableFuture<Void>

    /**
     * Sets the fake player's velocity. Vanilla physics (gravity, friction, collisions) takes over
     * from the next tick, exactly like knockback landing on a real player.
     *
     * This is a real thing a client experiences, not a shortcut around movement: use it for
     * launches, knockback and anything else the server would do to a player anyway. Steering a bot
     * around by writing its velocity every tick produces motion no key press could ever produce,
     * and reads as such.
     *
     * @since 3.9
     */
    fun setVelocity(velocity: Vector)

    /**
     * Teleports the fake player, like a real player being teleported by the server. Fires
     * `PlayerTeleportEvent`.
     *
     * Teleporting is not movement and is never shaped by the human profile: it is visible to
     * everyone as a teleport. Use it for setup and recovery, never as a way to get somewhere.
     *
     * @since 3.9
     */
    fun teleport(location: Location): CompletableFuture<Void>

    // ------------------------------------------------------------------
    // Looking
    // ------------------------------------------------------------------

    /**
     * Turns the fake player's head towards the given rotation in the given style.
     *
     * @param yaw target yaw in degrees, Minecraft convention where 0 is south
     * @param pitch target pitch in degrees, where -90 is straight up and 90 straight down
     * @return future completing once the turn has finished
     * @since 3.9
     */
    fun aim(yaw: Double, pitch: Double, style: AimStyle): CompletableFuture<Void>

    /**
     * Turns the fake player's head to face the given point, at eye height.
     *
     * @since 3.9
     */
    fun aimAt(target: Location, style: AimStyle): CompletableFuture<Void>

    // ------------------------------------------------------------------
    // Items and interaction
    // ------------------------------------------------------------------

    /**
     * Selects a hotbar slot, broadcasting the held-item change.
     *
     * @param slot hotbar slot index, 0 to 8
     * @throws IllegalArgumentException if the slot is outside 0 to 8
     * @since 3.9
     */
    fun selectSlot(slot: Int): CompletableFuture<Void>

    /**
     * Selects the hotbar slot holding the best item this fake player has for the given job, and
     * returns whether anything suitable was found. Does not move items out of the main inventory
     * into the hotbar; a behaviour that wants that does it through
     * [net.blueva.spoof.api.inventory.BotInventory] first.
     *
     * @since 3.9
     */
    fun selectBest(purpose: ToolPurpose): CompletableFuture<Boolean>

    /**
     * Equips an item in the given slot: main hand, off hand or armor.
     *
     * @param item the item to equip, or `null` to clear the slot
     * @since 3.9
     */
    fun equip(slot: EquipmentSlot, item: ItemStack?)

    /**
     * Swings the fake player's arm. Animation only, no interaction.
     *
     * @throws IllegalArgumentException if [hand] is not a hand slot
     * @since 3.9
     */
    fun swingArm(hand: EquipmentSlot): CompletableFuture<Void>

    /**
     * Attacks the given entity once with the held item, producing real damage, knockback and swing
     * animation through the vanilla combat path.
     *
     * @since 3.9
     */
    fun attack(target: Entity): CompletableFuture<Void>

    /**
     * Drops items from the selected hotbar slot in front of the fake player.
     *
     * @param fullStack `true` to drop the whole stack, `false` for a single item
     * @since 3.9
     */
    fun dropItem(fullStack: Boolean): CompletableFuture<Void>

    /**
     * Opens the container block at the given location and returns a view of the session. The fake
     * player must be within vanilla interaction range.
     *
     * Reading the contents works on every server version; clicking inside the view needs
     * [Capabilities.containerClicks].
     *
     * @since 3.9
     */
    fun openContainer(block: Location): CompletableFuture<ContainerView>

    // ------------------------------------------------------------------
    // Chat
    // ------------------------------------------------------------------

    /**
     * Sends a chat message as the fake player, through the normal chat pipeline, so other plugins
     * see it exactly like a real player's message.
     *
     * @since 3.9
     */
    fun chat(message: String): CompletableFuture<Void>

    /**
     * Runs a command as the fake player, through the normal command pipeline.
     *
     * @param command the command without a leading slash
     * @since 3.9
     */
    fun command(command: String): CompletableFuture<Void>

    // ------------------------------------------------------------------
    // Version-gated actions
    // ------------------------------------------------------------------

    /**
     * The actions that need a native server hook, or an empty optional when this server version
     * has none. See [AdvancedActuator] and [Capabilities].
     *
     * @since 3.9
     */
    fun advanced(): Optional<AdvancedActuator>
}
