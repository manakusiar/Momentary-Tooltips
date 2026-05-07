package com.manakusiar.momentarytooltips;


import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue MOD_ENABLED = BUILDER
            .comment("Toggle the mod.")
            .define("modEnabled", true);

    public static final ModConfigSpec.BooleanValue SHOW_ENCHANTS = BUILDER
            .comment("Toggle if enchantment tooltips should always be shown.")
            .define("showEnchants", false);

    public static final ModConfigSpec.BooleanValue SHOW_ATTRIBUTES = BUILDER
            .comment("Toggle if attributes (Attack damage, Attack speed) tooltips should always be shown.")
            .define("showAttributes", false);

    public static final ModConfigSpec.BooleanValue SHOW_LORE = BUILDER
            .comment("Toggle if lore tooltips should always be shown.")
            .define("showLore", false);

    public static final ModConfigSpec.BooleanValue SHOW_DESCRIPTION = BUILDER
            .comment("Toggle if descriptions tooltips should always be shown.")
            .define("showDescription", false);

    public static final ModConfigSpec.BooleanValue SHOW_INGREDIENTS = BUILDER
            .comment("Toggle if ingredient tooltips mostly on smithing templates should always be shown.")
            .define("showIngredients", false);

    public static final ModConfigSpec.BooleanValue SHOW_UPGRADE = BUILDER
            .comment("Toggle if upgrade tooltips on smithing upgrades should always be shown.")
            .define("showUpgrade", false);

    public static final ModConfigSpec.BooleanValue SHOW_APPLIES_TO = BUILDER
            .comment("Toggle if 'Applies to:' mostly on smithing templates should always be shown.")
            .define("showAppliesTo", false);

    public static final ModConfigSpec.BooleanValue SHOW_TOOLTIP_ICON = BUILDER
            .comment("Toggle the icon that appears when a item has a tooltip to show.")
            .define("showTooltipIcon", false);

    public static final ModConfigSpec.ConfigValue<String> TOOLTIP_ICON = BUILDER
            .comment("The icon that appears when a item has a tooltip to show. Remember about the optional space in front!")
            .define("tooltipIcon", " ⊽");

    static final ModConfigSpec SPEC = BUILDER.build();
}
