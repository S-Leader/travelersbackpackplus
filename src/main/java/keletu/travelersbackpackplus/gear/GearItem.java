package keletu.travelersbackpackplus.gear;

import com.tiviacz.travelersbackpack.attachment.AttachmentUtils;
import com.tiviacz.travelersbackpack.item.TravelersBackpackItem;
import keletu.travelersbackpackplus.GearRegistry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.block.Block;

public final class GearItem extends TravelersBackpackItem {
    private final GearType type;

    public GearItem(Properties properties, Block block, GearType type) {
        super(properties.component(GearRegistry.STATE.get(), CustomData.EMPTY), block);
        this.type = type;
    }

    public GearType type() {
        return type;
    }

    @Override
    public boolean overrideStackedOnOther(ItemStack stack, Slot slot, ClickAction action, Player player) {
        return false;
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack stack, ItemStack other, Slot slot, ClickAction action, Player player, SlotAccess access) {
        return false;
    }

    @Override
    public void inventoryTick(ItemStack stack, ServerLevel level, Entity entity, EquipmentSlot slot) {
        super.inventoryTick(stack, level, entity, slot);
        GearTanks.migrate(stack);
        if (!(entity instanceof Player player) || AttachmentUtils.getWearingBackpack(player) == stack) return;
        GearState state = new GearState(stack);
        if (state.enabled() || state.active()) {
            state.enabled(false);
            state.active(false);
            state.save(stack);
        }
    }
}
