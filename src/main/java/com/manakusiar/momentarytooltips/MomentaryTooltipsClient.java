package com.manakusiar.momentarytooltips;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

import java.awt.*;
import java.util.ArrayList;
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
            List<Component> tooltip = event.getToolTip();

            if (tooltip.isEmpty()) return;

            Component itemName = tooltip.getFirst();
            List<Component> attributes = new ArrayList<>();
            List<Component> enchantments = new ArrayList<>();
            List<Component> lore = new ArrayList<>();
            List<Component> description = new ArrayList<>();
            List<Component> appliesTo = new ArrayList<>();
            List<Component> ingredients = new ArrayList<>();
            List<Component> upgrade = new ArrayList<>();

            for (Component line : tooltip) {
                if (Config.SHOW_ATTRIBUTES.getAsBoolean() &&
                        checkForTranslationKey(line, "attribute.modifier", true) ||
                        checkForTranslationKey(line, "attribute.name.", true)) {
                    attributes.add(line);
                }
                if (Config.SHOW_ATTRIBUTES.getAsBoolean() && checkForTranslationKey(line, "item.modifiers.", true)) {
                    attributes.add(Component.empty());
                    attributes.add(line);
                }
                if (Config.SHOW_ENCHANTS.getAsBoolean() && checkForTranslationKey(line, "enchantment.", true)) {
                    enchantments.add(line);
                }
                if (Config.SHOW_UPGRADE.getAsBoolean() && checkForTranslationKey(line, ".upgrade", false)) {
                    upgrade.add(line);
                }
                if (Config.SHOW_LORE.getAsBoolean() && isLoreLine(line)) {
                    lore.add(line);
                }
                if (Config.SHOW_DESCRIPTION.getAsBoolean() && checkForTranslationKey(line, ".description", false)) {
                    description.add(line);
                }
                if (Config.SHOW_APPLIES_TO.getAsBoolean() && checkForTranslationKey(line, ".applies_to", false)) {
                    attributes.add(Component.empty());
                    appliesTo.add(line);
                }
                if (Config.SHOW_INGREDIENTS.getAsBoolean() && checkForTranslationKey(line, ".ingredients", false)) {
                    ingredients.add(line);
                }
            }

            int initialSize = tooltip.size();

            tooltip.clear();
            tooltip.add(itemName);
            tooltip.addAll(lore);
            tooltip.addAll(description);
            tooltip.addAll(upgrade);
            tooltip.addAll(enchantments);
            tooltip.addAll(attributes);
            tooltip.addAll(appliesTo);
            tooltip.addAll(ingredients);

            if (Config.SHOW_TOOLTIP_ICON.getAsBoolean() && initialSize > tooltip.size()) {
                MutableComponent finalName = itemName.copy();
                finalName.append(Component.literal(Config.TOOLTIP_ICON.get()));

                tooltip.removeFirst();
                tooltip.addFirst(finalName);
            }
        }


    }

    // Check for all translation keys in a component
    public static boolean checkForTranslationKey(Component component, String keyPrefix, boolean startsWith) {
        // Check translatable contents
        if (component.getContents() instanceof TranslatableContents translatable) {
            if ((startsWith && translatable.getKey().startsWith(keyPrefix)) || (!startsWith && translatable.getKey().endsWith(keyPrefix))) {
                return true;
            }

            // Check internal components
            for (Object arg : translatable.getArgs()) {
                if (arg instanceof Component argComponent) {
                    if (checkForTranslationKey(argComponent, keyPrefix, startsWith)) return true;
                }
            }
        }

        // Check the components siblings
        for (Component sibling : component.getSiblings()) {
            if (checkForTranslationKey(sibling, keyPrefix, startsWith)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isLoreLine(Component component) {
        TextColor color = component.getStyle().getColor();
        if (color == null) return false;

        boolean isDarkPurple = color.getValue() == ChatFormatting.DARK_PURPLE.getColor();

        return component.getStyle().isItalic() && isDarkPurple;
    }
}
