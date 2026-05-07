package com.manakusiar.momentarytooltips;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

import java.util.List;

// This class will not load on dedicated servers. Accessing client side code from here is safe.
@Mod(value = MomentaryTooltips.MODID, dist = Dist.CLIENT)
// You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
@EventBusSubscriber(modid = MomentaryTooltips.MODID, value = Dist.CLIENT)
public class MomentaryTooltipsClient {
    public MomentaryTooltipsClient(ModContainer container) {
        // Allows NeoForge to create a config screen for this mod's configs.
        // The config screen is accessed by going to the Mods screen > clicking on your mod > clicking on config.
        // Do not forget to add translations for your config options to the en_us.json file.
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        if (event.getEntity() == null) return;

        // Mod working conditions
        if (!Screen.hasShiftDown() && Config.MOD_ENABLED.getAsBoolean()) {
            // Basic
            ItemStack stack = event.getItemStack();
            List<Component> tooltip = event.getToolTip();
            int initialSize = tooltip.size();
            Component itemName = tooltip.getFirst();

            event.getToolTip().clear();
            event.getToolTip().add(itemName);

            // Show enchant setting
            boolean enchantsEnabled = Config.SHOW_ENCHANTS.getAsBoolean();
            if (enchantsEnabled) {
                ItemEnchantments enchantments = stack.getOrDefault(
                        DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
                for (var entry : enchantments.entrySet()) {
                    tooltip.add(Enchantment.getFullname(entry.getKey(), entry.getIntValue()));
                }

                ItemEnchantments storedEnchantments = stack.getOrDefault(
                        DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY);
                for (var entry : storedEnchantments.entrySet()) {
                    tooltip.add(Enchantment.getFullname(entry.getKey(), entry.getIntValue()));
                }
            }

            int statusNumber = enchantsEnabled? 1 : 0;
            if (event.getToolTip().size() < initialSize - 2 * statusNumber
                    && Config.SHOW_TOOLTIP_ICON.getAsBoolean()) {
                Component extraText = Component.literal(Config.TOOLTIP_ICON.get()).withStyle(ChatFormatting.DARK_GRAY);
                MutableComponent extendedItemName = itemName.copy().append(extraText);

                tooltip.removeFirst();
                tooltip.addFirst(extendedItemName);
            }
        }
    }
}
