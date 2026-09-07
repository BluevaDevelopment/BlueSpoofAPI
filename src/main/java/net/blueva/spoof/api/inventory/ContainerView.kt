package net.blueva.spoof.api.inventory

import org.bukkit.inventory.ItemStack
import java.util.concurrent.CompletableFuture

/**
 * An open container session between the fake player and a container block
 * (chest, barrel, furnace, dispenser, ...), obtained with
 * `fakePlayer.openContainer(location)`.
 *
 * The container was opened through the real server code path, so other
 * plugins see the usual `InventoryOpenEvent` and — on Minecraft 26.1+ —
 * clicks are driven through the exact vanilla menu-click code,
 * indistinguishable from a real client.
 *
 * Slot indices in this view address the container part only
 * (`0` to `containerSlots - 1`); use `fakePlayer.inventory()` for the
 * fake player's own inventory.
 *
 * Clicking requires a native server hook (Minecraft 26.1+); on older
 * versions the view still opens and can be read, but the mutating methods
 * throw [UnsupportedOperationException]. Guard with
 * `fakePlayer.supportsAdvancedActions()` when your plugin must run on
 * older servers.
 *
 * @since 3.7
 */
interface ContainerView {

    /**
     * Returns the container title (e.g. `"Chest"`, `"Large Chest"`).
     *
     * @return display title
     */
    fun getTitle(): String

    /**
     * Returns the number of slots in the container part of this view.
     *
     * @return container slot count
     */
    fun getContainerSlots(): Int

    /**
     * Returns the item in the given container slot, or `null` when empty.
     *
     * @param slot container slot index
     * @return the item, or `null`
     */
    fun getItem(slot: Int): ItemStack?

    /**
     * Returns a snapshot of the container contents (size [getContainerSlots],
     * `null` entries for empty slots).
     *
     * @return contents snapshot
     */
    fun getContents(): Array<ItemStack?>

    /**
     * Clicks a container slot with the given click type, exactly like a real
     * client would (`InventoryClickEvent` fires for other plugins).
     *
     * @param slot  container slot index
     * @param click the click type
     * @return future completing `true` once the click was processed,
     * `false` if the slot was out of range or no container is open
     * @throws UnsupportedOperationException on server versions without the
     * native hook (older than Minecraft 26.1)
     */
    fun click(slot: Int, click: ContainerClick): CompletableFuture<Boolean>

    /**
     * Quick-moves (shift-clicks) a container slot into the fake player's
     * inventory.
     *
     * @param slot container slot index
     * @return future completing with the amount of items moved
     * @throws UnsupportedOperationException on server versions without the
     * native hook (older than Minecraft 26.1)
     */
    fun moveToInventory(slot: Int): CompletableFuture<Int>

    /**
     * Closes the container (`InventoryCloseEvent` fires).
     *
     * @return future completing once the container is closed
     */
    fun close(): CompletableFuture<Void>
}
