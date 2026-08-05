package net.blueva.spoof.api.inventory

/**
 * Mouse click types for container interaction, mirroring the vanilla
 * `ClickType`s a real client sends in container-click packets.
 *
 * @since 3.7
 */
enum class ContainerClick {
    /** Left-click a slot: pick up / place / swap the cursor stack. */
    LEFT,

    /** Right-click a slot: pick up half / place one item. */
    RIGHT,

    /** Shift+left-click: quick-move the whole stack to the other inventory. */
    SHIFT_LEFT,

    /** Shift+right-click: quick-move the whole stack to the other inventory. */
    SHIFT_RIGHT,

    /** Drop one item from the slot (Q). */
    DROP_ONE,

    /** Drop the whole stack (Ctrl+Q). */
    DROP_STACK;

    /**
     * Returns `true` if this click quick-moves items to the other
     * inventory (shift-click behaviour).
     *
     * @return `true` for the shift-click variants
     */
    fun isQuickMove(): Boolean {
        return this == SHIFT_LEFT || this == SHIFT_RIGHT
    }
}
