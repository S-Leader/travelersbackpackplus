package keletu.travelersbackpackplus;

import com.tiviacz.travelersbackpack.block.TravelersBackpackBlock;
import keletu.travelersbackpackplus.gear.*;
import keletu.travelersbackpackplus.menu.CompressorMenu;
import keletu.travelersbackpackplus.menu.GearMenu;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.*;

import java.util.List;

public final class GearRegistry {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(TravelersBackpackPlus.ID);
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(TravelersBackpackPlus.ID);
    public static final DeferredRegister<DataComponentType<?>> COMPONENTS = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, TravelersBackpackPlus.ID);
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(Registries.SOUND_EVENT, TravelersBackpackPlus.ID);
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TravelersBackpackPlus.ID);
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, TravelersBackpackPlus.ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, TravelersBackpackPlus.ID);
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(Registries.FLUID, TravelersBackpackPlus.ID);
    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.FLUID_TYPES, TravelersBackpackPlus.ID);
    public static final DeferredHolder<FluidType, FluidType> AIR_TYPE = FLUID_TYPES.register("air", () -> new FluidType(FluidType.Properties.create().descriptionId("fluid.travelersbackpackplus.air").density(-100).canSwim(false).canDrown(false)));
    public static final DeferredHolder<Fluid, AirFluid> AIR = FLUIDS.register("air", AirFluid::new);
    public static final DeferredHolder<MenuType<?>, MenuType<GearMenu>> GEAR_ITEM_MENU = MENUS.register("gear_item", () -> IMenuTypeExtension.create(GearMenu::fromItem));
    public static final DeferredHolder<MenuType<?>, MenuType<GearMenu>> GEAR_BLOCK_MENU = MENUS.register("gear_block", () -> IMenuTypeExtension.create(GearMenu::fromBlock));
    public static final DeferredHolder<MenuType<?>, MenuType<CompressorMenu>> COMPRESSOR_MENU = MENUS.register("air_compressor", () -> IMenuTypeExtension.create(CompressorMenu::fromNetwork));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<CustomData>> STATE = COMPONENTS.register("gear_state", () -> DataComponentType.<CustomData>builder().persistent(CustomData.CODEC).networkSynchronized(CustomData.STREAM_CODEC).build());
    public static final DeferredItem<GearItem> COPTER = pack(GearType.COPTER);
    public static final DeferredItem<GearItem> JETPACK = pack(GearType.JETPACK);
    public static final DeferredItem<GearItem> FLAMETHROWER = pack(GearType.FLAMETHROWER);
    public static final DeferredItem<GearItem> SCUBA = pack(GearType.SCUBA);
    public static final List<DeferredItem<GearItem>> PACKS = List.of(COPTER, JETPACK, FLAMETHROWER, SCUBA);
    public static final DeferredItem<NozzleItem> NOZZLE = ITEMS.registerItem("flamethrower", properties -> new NozzleItem(properties.stacksTo(1)));
    public static final DeferredItem<Item> COPTER_ENGINE = component("copter_engine");
    public static final DeferredItem<Item> COPTER_BLADES = component("copter_blades");
    public static final DeferredItem<Item> AIR_TANK = component("air_tank");
    public static final DeferredItem<Item> DIVING_MASK = component("diving_mask");
    public static final DeferredItem<Item> PYRO_MASK = component("pyro_mask");
    public static final List<DeferredItem<Item>> COMPONENT_ITEMS = List.of(COPTER_ENGINE, COPTER_BLADES, AIR_TANK, DIVING_MASK, PYRO_MASK);
    public static final DeferredBlock<CompressorBlock> COMPRESSOR = BLOCKS.registerBlock("air_compressor", CompressorBlock::new, properties -> properties.strength(3.5F).sound(SoundType.METAL));
    public static final DeferredItem<BlockItem> COMPRESSOR_ITEM = ITEMS.registerSimpleBlockItem(COMPRESSOR);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CompressorBlockEntity>> COMPRESSOR_ENTITY = BLOCK_ENTITIES.register("air_compressor", () -> new BlockEntityType<>(CompressorBlockEntity::new, COMPRESSOR.get()));
    public static final DeferredHolder<SoundEvent, SoundEvent> COPTER_SOUND = sound("helicopter");
    public static final DeferredHolder<SoundEvent, SoundEvent> JET_SOUND = sound("jetpack");
    public static final DeferredHolder<SoundEvent, SoundEvent> FLAME_SOUND = sound("flamethrower");

    static {
        TABS.register("gear", () -> CreativeModeTab.builder().title(Component.translatable("itemGroup.travelersbackpackplus")).icon(COPTER::toStack).displayItems((parameters, output) -> {
            PACKS.forEach(pack -> output.accept(pack.get()));
            output.accept(NOZZLE.get());
            output.accept(COMPRESSOR_ITEM.get());
            COMPONENT_ITEMS.forEach(item -> output.accept(item.get()));
        }).build());
    }

    private static DeferredItem<GearItem> pack(GearType type) {
        DeferredBlock<TravelersBackpackBlock> block = BLOCKS.registerBlock(type.path(), GearBlock::new, properties -> properties.strength(1.0F).sound(SoundType.METAL).noOcclusion());
        return ITEMS.registerItem(type.path(), properties -> new GearItem(properties, block.get(), type));
    }

    private static DeferredHolder<SoundEvent, SoundEvent> sound(String path) {
        return SOUNDS.register(path, () -> SoundEvent.createVariableRangeEvent(TravelersBackpackPlus.id(path)));
    }

    private static DeferredItem<Item> component(String path) {
        return ITEMS.registerItem(path, properties -> new Item(properties.stacksTo(16)));
    }

    public static void register(IEventBus bus) {
        COMPONENTS.register(bus);
        BLOCKS.register(bus);
        ITEMS.register(bus);
        SOUNDS.register(bus);
        TABS.register(bus);
        MENUS.register(bus);
        BLOCK_ENTITIES.register(bus);
        FLUID_TYPES.register(bus);
        FLUIDS.register(bus);
    }

}
