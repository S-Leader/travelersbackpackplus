package keletu.travelersbackpackplus.compat;

import com.tiviacz.travelersbackpack.compat.curios.TravelersBackpackCurio;
import keletu.travelersbackpackplus.GearRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;

import java.util.List;

public final class CurioCompat {
    public static void register(RegisterCapabilitiesEvent event) {
        GearRegistry.PACKS.forEach(pack -> event.registerItem(CuriosCapability.ITEM,
                (stack, context) -> new GearCurio(new TravelersBackpackCurio(stack)), pack.get()));
    }

    private record GearCurio(TravelersBackpackCurio delegate) implements ICurio {
        @Override
        public ItemStack getStack() {
            return delegate.getStack();
        }

        @Override
        public boolean canEquip(SlotContext context) {
            return delegate.canEquip(context);
        }

        @Override
        public boolean canEquipFromUse(SlotContext context) {
            return delegate.canEquipFromUse(context);
        }

        @Override
        public void curioTick(SlotContext context) {
            delegate.curioTick(context);
        }

        @Override
        public List<Component> getSlotsTooltip(List<Component> tooltips, Item.TooltipContext context) {
            return List.of();
        }
    }
}
