package com.petnametags;

import com.google.inject.Inject;
import net.runelite.api.NPC;
import net.runelite.client.game.ChatIconManager;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;

public class PetNameTagsOverlay extends Overlay
{
    private final PetNameTagsService petNameTagsService;
    private final PetNameTagsConfig config;

    @Inject
    private PetNameTagsOverlay(PetNameTagsConfig config, PetNameTagsService petNameTagsService)
    {
        this.config = config;
        this.petNameTagsService = petNameTagsService;
        setPosition(OverlayPosition.DYNAMIC);
        setPriority(OverlayPriority.MED);
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        petNameTagsService.forEachPet((id, color) -> renderNameTagOverlay(graphics, id, color));
        return null;
    }

    public void renderNameTagOverlay(Graphics2D graphics, NPC npcID, Color color) {

    }
}
