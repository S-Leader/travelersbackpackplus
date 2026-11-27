package keletu.travelersbackpackplus;

import com.tiviacz.travelersbackpack.init.ModBlockEntityTypes;
import keletu.travelersbackpackplus.compat.CurioCompat;
import keletu.travelersbackpackplus.gear.GearEvents;
import keletu.travelersbackpackplus.gear.GearFluidHandler;
import keletu.travelersbackpackplus.gear.GearItem;
import keletu.travelersbackpackplus.network.GearNetwork;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent;
import net.neoforged.neoforge.transfer.item.VanillaContainerWrapper;

@Mod(TravelersBackpackPlus.ID)
public final class TravelersBackpackPlus {
    public static final String ID = "travelersbackpackplus";

    public TravelersBackpackPlus(IEventBus bus, ModContainer container) {
        GearRegistry.register(bus);
        bus.addListener(GearNetwork::register);
        bus.addListener(TravelersBackpackPlus::validBlocks);
        bus.addListener(TravelersBackpackPlus::capabilities);
        NeoForge.EVENT_BUS.register(GearEvents.class);
        container.registerConfig(ModConfig.Type.SERVER, GearConfig.SPEC);
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(ID, path);
    }

    private static void validBlocks(BlockEntityTypeAddBlocksEvent event) {
        GearRegistry.PACKS.forEach(pack -> event.modify(ModBlockEntityTypes.BACKPACK.get(), pack.get().getBlock()));
    }

    private static void capabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.Fluid.BLOCK, GearRegistry.COMPRESSOR_ENTITY.get(), (entity, side) -> entity.airTank);
        event.registerBlockEntity(Capabilities.Item.BLOCK, GearRegistry.COMPRESSOR_ENTITY.get(), (entity, side) -> VanillaContainerWrapper.of(entity));
        if (ModList.get().isLoaded("curios")) CurioCompat.register(event);
        GearRegistry.PACKS.forEach(pack -> {
            event.registerItem(Capabilities.Fluid.ITEM, (stack, access) -> {
                GearItem item = (GearItem) stack.getItem();
                return item.type().tankCount() == 0 ? null : new GearFluidHandler(access, item);
            }, pack.get());
        });
    }
}
