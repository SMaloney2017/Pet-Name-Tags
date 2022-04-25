/*
 * Copyright (c) 2018, Tomas Slusny <slusnucky@gmail.com>
 * Copyright (c) 2019, Jordan Atwood <nightfirecat@protonmail.com>
 * Copyright (c) 2022, Sean Maloney <https://github.com/SMaloney2017>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.petnametags;

import com.google.inject.Inject;
import net.runelite.api.NPC;
import net.runelite.api.Point;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.OverlayUtil;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.Map;

public class PetNameTagsOverlay extends Overlay
{
    private static final int ACTOR_OVERHEAD_TEXT_MARGIN = 40;
    private final PetNameTagsConfig config;

    PetNameTagsService service;

    @Inject
    private PetNameTagsOverlay(PetNameTagsConfig config, PetNameTagsService service)
    {
        this.config = config;
        this.service = service;
        setPosition(OverlayPosition.DYNAMIC);
        setPriority(OverlayPriority.MED);
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        service.forEachPet((id) -> renderNameTagOverlay(graphics, id));
        return null;
    }

    public void renderNameTagOverlay(Graphics2D graphics, NPC actor)
    {
        final int zOffset;
        zOffset = actor.getLogicalHeight() + ACTOR_OVERHEAD_TEXT_MARGIN;

        Map<String, PetNameTag> NameTags = service.getNameTags();
        final String name = NameTags.get(actor.getName()).getLabel();
        final Color color = NameTags.get(actor.getName()).getColor();

        Point textLocation = actor.getCanvasTextLocation(graphics, name, zOffset);

        if (textLocation == null)
        {
            return;
        }

        try
        {
            if(config.rememberTagColors())
            {
                OverlayUtil.renderTextLocation(graphics, textLocation, name, color);
            }
            else
            {
                OverlayUtil.renderTextLocation(graphics, textLocation, name, config.getNameTagColor());

            }
        }
        catch(NullPointerException e)
        {
            e.printStackTrace();
        }
    }
}
