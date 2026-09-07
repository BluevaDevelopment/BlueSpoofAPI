package net.blueva.spoof.api.inventory

import org.bukkit.Material
import org.bukkit.inventory.ItemStack

/**
 * The fake player's own inventory: hotbar, main inventory, armor and off-hand.
 *
 * Slot layout (compatible with Bukkit's raw player-inventory slots):
 * - `0-8` — hotbar
 * - `9-35` — main inventory
 * - `36-39` — armor (boots, leggings, chestplate, helmet)
 * - `40` — off-hand
 *
 * Obtain it with `fakePlayer.inventory()`. Accessors are synchronous
 * and read/write the live inventory; call them on the fake player's owning
 * thread (inside a continuation of any API future, or anywhere on the main
 * thread on Spigot/Paper).
 *
 * @since 3.7
 */
interface BotInventory {

    /**
     * Returns the item in the given slot, or `null` when empty.
     *
     * @param slot slot index (0-40)
     * @return the item, or `null`
     * @throws IllegalArgumentException if the slot is out of range
     */
    fun getItem(slot: Int): ItemStack?

    /**
     * Sets the item in the given slot (`null` clears it).
     *
     * @param slot  slot index (0-40)
     * @param item  the item, or `null` to clear
     * @throws IllegalArgumentException if the slot is out of range
     */
    fun setItem(slot: Int, item: ItemStack?)

    /**
     * Returns the item the fake player is currently holding (main hand).
     *
     * @return the held item, or `null` when empty-handed
     */
    fun getHeldItem(): ItemStack

    /**
     * Returns the first slot containing the given material, or `-1`.
     *
     * @param material the material to find
     * @return first matching slot index, or `-1` if none
     */
    fun first(material: Material): Int

    /**
     * Returns all slots containing the given material.
     *
     * @param material the material to find
     * @return list of matching slot indices (empty if none)
     */
    fun findAll(material: Material): List<Int>

    /**
     * Returns the total amount of the given material across the inventory.
     *
     * @param material the material to count
     * @return total item count
     */
    fun count(material: Material): Int

    /**
     * Returns whether the inventory holds at least `amount` items of the
     * given material.
     *
     * @param material the material
     * @param amount   required amount
     * @return `true` if enough items are present
     */
    fun contains(material: Material, amount: Int): Boolean

    /**
     * Returns the first empty storage slot (0-35), or `-1` when full.
     *
     * @return first empty slot index, or `-1`
     */
    fun firstEmpty(): Int

    /**
     * Adds an item to the inventory, stacking with existing items first.
     *
     * @param item the item to add (its amount is fully consumed on success)
     * @return `true` if the whole stack fit, `false` otherwise
     * (nothing is added when it does not fit completely)
     */
    fun addItem(item: ItemStack): Boolean

    /**
     * Removes up to `amount` items of the given material.
     *
     * @param material the material to remove
     * @param amount   the amount to remove
     * @return `true` if the full amount was removed, `false` if
     * the inventory did not hold enough (nothing is removed then)
     */
    fun removeItem(material: Material, amount: Int): Boolean

    /** Clears the whole inventory (storage, armor and off-hand). */
    fun clear()

    companion object {
        /** Total number of slots (`41`). */
        const val SIZE: Int = 41

        /** First hotbar slot (`0`). */
        const val HOTBAR_START: Int = 0

        /** First main-inventory slot (`9`). */
        const val MAIN_START: Int = 9

        /** Boots slot (`36`). */
        const val SLOT_BOOTS: Int = 36

        /** Leggings slot (`37`). */
        const val SLOT_LEGGINGS: Int = 37

        /** Chestplate slot (`38`). */
        const val SLOT_CHESTPLATE: Int = 38

        /** Helmet slot (`39`). */
        const val SLOT_HELMET: Int = 39

        /** Off-hand slot (`40`). */
        const val SLOT_OFFHAND: Int = 40
    }
}
