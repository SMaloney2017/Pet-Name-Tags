package com.petnametags;

import com.google.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.NPC;
import java.awt.Color;
import java.util.function.BiConsumer;

public class PetNameTagsService
{
    private final Client client;
    private final PetNameTagsConfig config;

    @Inject
    private PetNameTagsService(Client client, PetNameTagsConfig config)
    {
        this.config = config;
        this.client = client;
    }

    public void forEachPet(final BiConsumer<NPC, Color> consumer) {

    }
}
