package keletu.travelersbackpackplus.menu;

import keletu.travelersbackpackplus.GearRegistry;
import keletu.travelersbackpackplus.gear.CompressorBlockEntity;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public final class CompressorMenu extends AbstractContainerMenu {
    private final CompressorBlockEntity entity;
    private final ContainerData data;

    public CompressorMenu(int id, Inventory inventory, CompressorBlockEntity entity) {
        super(GearRegistry.COMPRESSOR_MENU.get(), id);
        this.entity = entity;
        this.data = inventory.player.level().isClientSide() ? new SimpleContainerData(3) : new ContainerData() {
            @Override
            public int get(int index) {
                return index == 0 ? entity.air() : index == 1 ? entity.burn() : entity.fuel();
            }

            @Override
            public void set(int index, int value) {
            }

            @Override
            public int getCount() {
                return 3;
            }
        };
        for (int x = 0; x < 9; x++) addSlot(new Slot(inventory, x, 8 + x * 18, 142));
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 9; x++) addSlot(new Slot(inventory, x + y * 9 + 9, 8 + x * 18, 84 + y * 18));
        }
        addSlot(new Slot(entity, 0, 77, 64) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return entity.canPlaceItem(0, stack);
            }
        });
        addSlot(new Slot(entity, 1, 30, 22) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return entity.canPlaceItem(1, stack);
            }
        });
        addSlot(new Slot(entity, 2, 30, 52) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }
        });
        addSlot(new Slot(entity, 3, 126, 34) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.is(GearRegistry.SCUBA.get());
            }

            @Override
            public int getMaxStackSize() {
                return 1;
            }
        });
        addDataSlots(data);
    }

    public static CompressorMenu fromNetwork(int id, Inventory inventory, RegistryFriendlyByteBuf buffer) {
        return new CompressorMenu(id, inventory, (CompressorBlockEntity) inventory.player.level().getBlockEntity(buffer.readBlockPos()));
    }

    public int air() {
        return data.get(0);
    }

    public int burn() {
        return data.get(1);
    }

    public int fuel() {
        return data.get(2);
    }

    @Override
    public boolean stillValid(Player player) {
        return entity.stillValid(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        if (index < 0 || index >= slots.size()) return ItemStack.EMPTY;
        Slot slot = slots.get(index);
        if (!slot.hasItem()) return ItemStack.EMPTY;
        ItemStack stack = slot.getItem();
        ItemStack copy = stack.copy();
        boolean moved;
        if (index >= 36) moved = moveItemStackTo(stack, 0, 36, false);
        else if (stack.is(GearRegistry.SCUBA.get())) moved = moveItemStackTo(stack, 39, 40, false);
        else if (slots.get(37).mayPlace(stack)) moved = moveItemStackTo(stack, 37, 38, false);
        else moved = slots.get(36).mayPlace(stack) && moveItemStackTo(stack, 36, 37, false);
        if (!moved) return ItemStack.EMPTY;
        slot.setByPlayer(stack.isEmpty() ? ItemStack.EMPTY : stack);
        slot.onTake(player, stack);
        return copy;
    }
}
