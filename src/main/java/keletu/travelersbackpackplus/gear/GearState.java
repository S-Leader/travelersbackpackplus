package keletu.travelersbackpackplus.gear;

import keletu.travelersbackpackplus.GearRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

public final class GearState {
    private final CompoundTag data;

    public GearState(ItemStack stack) {
        data = stack.getOrDefault(GearRegistry.STATE.get(), CustomData.EMPTY).copyTag();
    }

    public boolean enabled() {
        return data.getBooleanOr("enabled", false);
    }

    public boolean hover() {
        return data.getBooleanOr("hover", false);
    }

    public boolean active() {
        return data.getBooleanOr("active", false);
    }

    public int steam() {
        return data.getIntOr("steam", 0);
    }

    public int temperature() {
        return data.getIntOr("temperature", 25);
    }

    public int burn() {
        return data.getIntOr("burn", 0);
    }

    public int burnTotal() {
        return data.getIntOr("burn_total", Math.max(1, burn()));
    }

    public int air() {
        return data.getIntOr("air", 0);
    }

    public void enabled(boolean value) {
        data.putBoolean("enabled", value);
    }

    public void hover(boolean value) {
        data.putBoolean("hover", value);
    }

    public void active(boolean value) {
        data.putBoolean("active", value);
    }

    public void steam(int value) {
        data.putInt("steam", Math.clamp(value, 0, 12000));
    }

    public void temperature(int value) {
        data.putInt("temperature", Math.clamp(value, 25, 200));
    }

    public void burn(int value) {
        data.putInt("burn", Math.max(0, value));
    }

    public void burnTotal(int value) {
        data.putInt("burn_total", Math.max(1, value));
    }

    public void air(int value) {
        data.putInt("air", Math.clamp(value, 0, 6000));
    }

    public void save(ItemStack stack) {
        stack.set(GearRegistry.STATE.get(), CustomData.of(data));
    }
}
