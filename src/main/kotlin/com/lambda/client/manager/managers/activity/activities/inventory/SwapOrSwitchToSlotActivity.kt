package com.lambda.client.manager.managers.activity.activities.inventory

import com.lambda.client.event.SafeClientEvent
import com.lambda.client.manager.managers.activity.Activity
import com.lambda.client.manager.managers.activity.activities.InstantActivity
import com.lambda.client.util.items.firstEmpty
import com.lambda.client.util.items.hotbarSlots
import com.lambda.client.util.items.toHotbarSlotOrNull
import net.minecraft.inventory.Slot
import net.minecraft.item.ItemStack

class SwapOrSwitchToSlotActivity(
    private val slot: Slot,
    private val predicateSlot: (ItemStack) -> Boolean = { true }
) : InstantActivity, Activity() {
    override fun SafeClientEvent.onInitialize() {
        slot.toHotbarSlotOrNull()?.let { hotbarSlot ->
            subActivities.add(SwitchToHotbarSlotActivity(hotbarSlot.hotbarSlot))
        } ?: run {
            val hotbarSlots = player.hotbarSlots
            val slotTo = hotbarSlots.firstEmpty()
                ?: hotbarSlots.firstOrNull { predicateSlot(it.stack) } ?: return

            subActivities.add(SwapWithSlotActivity(slot, slotTo))
            subActivities.add(SwitchToHotbarSlotActivity(slotTo.hotbarSlot))
        }
    }
}