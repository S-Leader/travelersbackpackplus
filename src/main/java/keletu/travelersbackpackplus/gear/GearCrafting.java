package keletu.travelersbackpackplus.gear;

import com.tiviacz.travelersbackpack.init.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.item.component.ItemContainerContents;

public final class GearCrafting {
    public static ItemContainerContents initialTanks(HolderLookup.Provider lookup) {
        CompoundTag item = new CompoundTag();
        item.putString("id", ModItems.TANKS_UPGRADE.getId().toString());
        CompoundTag slot = new CompoundTag();
        slot.putInt("slot", 0);
        slot.put("item", item);
        ListTag slots = new ListTag();
        slots.add(slot);
        return ItemContainerContents.CODEC.parse(lookup.createSerializationContext(NbtOps.INSTANCE), slots).getOrThrow();
    }
}