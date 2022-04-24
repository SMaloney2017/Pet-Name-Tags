package com.petnametags;

import net.runelite.client.config.Alpha;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import java.awt.Color;

@ConfigGroup("name-tags")
public interface PetNameTagsConfig extends Config
{
    @Alpha
    @ConfigItem(
            keyName = "tagColor",
            name = "Name-tag color",
            description = "Configures the color of name-tags"
    )
    default Color tagColor()
    {
        return Color.YELLOW;
    }
    @ConfigItem(
            keyName = "rememberTagColors",
            name = "Remember color per name-tag",
            description = "Color name-tags using the color from time of creation"
    )
    default boolean rememberTagColors()
    {
        return false;
    }
}
