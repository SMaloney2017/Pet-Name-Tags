package com.petnametags;

import com.google.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.NPC;

import java.awt.Color;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static net.runelite.client.RuneLite.RUNELITE_DIR;

public class PetNameTagsService
{
    private static Client client;
    private static PetNameTagsConfig config;
    private final PetNameTagsPlugin plugin;

    private static String path;
    private static Map<String, PetNameTag> entries = new HashMap<>();

    @Inject
    private PetNameTagsService(Client client, PetNameTagsConfig config, PetNameTagsPlugin plugin)
    {
        this.config = config;
        this.client = client;
        this.plugin = plugin;
        path = RUNELITE_DIR + "/pet-name-tags/" + client.getAccountHash() + ".txt";
    }

    public static void forEachPet(final Consumer<NPC> consumer)
    {
        for (NPC npc : client.getNpcs())
        {
            Map<String, PetNameTag> NameTags = getNameTags();
            if (NameTags.containsKey(npc.getName()))
            {
                consumer.accept(npc);
            }
        }
    }

    public static void addToNameTags(String npcId, PetNameTag newNameTag)
    {
        entries.put(npcId, newNameTag);
        rebuildFile(entries);
    }

    public static void rebuildFile(Map<String, PetNameTag> entries)
    {
        File sessionFile = new File(path);
        if(sessionFileExists())
        {
            createNewUserFile();
        }
        try (
            FileOutputStream f = new FileOutputStream(sessionFile);
            ObjectOutputStream b = new ObjectOutputStream(f);
        )
        {
            b.writeObject(entries);
        } catch (IOException ignored)
        {
            /* Print Stack Trace */
        }
    }

    public static Map<String, PetNameTag> getNameTags()
    {
        Map<String, PetNameTag> entries = new HashMap<>();
        File sessionFile = new File(path);

        if (sessionFileExists() || sessionFile.length() == 0 ) {
            return entries;
        }

        try (
            FileInputStream f = new FileInputStream(sessionFile);
            ObjectInputStream b = new ObjectInputStream(f);
        )
        {
            try
            {
                entries = (HashMap<String, PetNameTag>) b.readObject();
            } catch (Exception e)
            {
                /* Exit */
            }
        } catch (IOException ignored)
        {
            /* Print Stack Trace */
        }
        return entries;
    }

    static boolean sessionFileExists()
    {
        return !new File(path).exists();
    }

    static void createNewUserFile()
    {
        File file = new File(path);
        try
        {
            file.getParentFile().mkdirs();
            file.createNewFile();
        } catch (IOException ignored)
        {
            /* Print Stack Trace */
        }
    }
}
