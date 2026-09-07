package net.blueva.spoof.api.control

/**
 * A job a fake player wants an item for, used by [Actuator.selectBest] to pick a hotbar slot
 * without the caller hard-coding material lists.
 *
 * @since 3.9
 */
enum class ToolPurpose {
    /** Breaking stone-like blocks: pickaxes. */
    MINING,

    /** Breaking soft blocks: shovels. */
    DIGGING,

    /** Breaking wood: axes. */
    CHOPPING,

    /** Melee combat: swords, then axes, then anything with damage. */
    WEAPON,

    /** Placing blocks: the largest stack of a placeable, non-valuable block. */
    BLOCK_PLACING,

    /** Ranged combat: bows, crossbows, tridents, throwables. */
    RANGED,

    /** Blocking: a shield. */
    SHIELD,

    /** Restoring health or hunger: food, then golden apples, then potions. */
    CONSUMABLE
}
