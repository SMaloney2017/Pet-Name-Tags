package com.petnametags;

import com.google.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.NPC;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static net.runelite.client.RuneLite.RUNELITE_DIR;

public class PetNameTagsService
{
    private static Client client;

    private static String path;

    private static Map<String, PetNameTag> entries = new HashMap<>();

    @Inject
    PetNameTagsService(Client user, String accountName)
    {
        client = user;
        path = RUNELITE_DIR + "/pet-name-tags/" + accountName + ".txt";
        entries = getNameTags();
    }

    public void forEachPet(final Consumer<NPC> consumer)
    {
        for (NPC npc : client.getNpcs())
        {
            if (entries.containsKey(npc.getName()))
            {
                consumer.accept(npc);
            }
        }
    }

    public void addToNameTags(String npcId, PetNameTag newNameTag)
    {
        entries.put(npcId, newNameTag);
        rebuildFile(entries);
    }

    public void rebuildFile(Map<String, PetNameTag> entries)
    {
        File sessionFile = new File(path);
        if(!sessionFileExists())
        {
            createNewUserFile();
            try (
                FileOutputStream f = new FileOutputStream(sessionFile);
                ObjectOutputStream b = new ObjectOutputStream(f)
            )
            {
                b.writeObject(entries);
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        } else {
            try (
                FileOutputStream f = new FileOutputStream(sessionFile);
                ObjectOutputStream b = new ObjectOutputStream(f)
            )
            {
                b.writeObject(entries);
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public Map<String, PetNameTag> getNameTags()
    {
        Map<String, PetNameTag> entries = new HashMap<>();
        File sessionFile = new File(path);

        if (!sessionFileExists() || sessionFile.length() == 0 ) {
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
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return entries;
    }

    boolean sessionFileExists()
    {
        return new File(path).exists();
    }

    void createNewUserFile()
    {
        File file = new File(path);
        try
        {
            file.getParentFile().mkdirs();
            file.createNewFile();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
