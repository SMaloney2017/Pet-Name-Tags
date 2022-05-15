/*
 * Copyright (c) 2018, Tomas Slusny <slusnucky@gmail.com>
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

	static Map<String, PetNameTag> entries = new HashMap<>();

	@Inject
	PetNameTagsService(Client user, String accountHash)
	{
		client = user;
		path = RUNELITE_DIR + "/pet-name-tags/" + accountHash + ".txt";
		entries = getNameTags();
	}

	public void forEachPet(final Consumer<NPC> consumer)
	{
		for (NPC npc : client.getNpcs())

		{
			if (npc.getInteracting() != null && !npc.getInteracting().equals(client.getLocalPlayer()))
			{
				continue;
			}
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
		if (!sessionFileExists())
		{
			createNewUserFile();
		}
		try (
			FileOutputStream f = new FileOutputStream(sessionFile);
			ObjectOutputStream b = new ObjectOutputStream(f)
		)
		{
			b.writeObject(entries);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public Map<String, PetNameTag> getNameTags()
	{
		Map<String, PetNameTag> entries = new HashMap<>();
		File sessionFile = new File(path);

		if (!sessionFileExists() || sessionFile.length() == 0)
		{
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
			}
			catch (Exception e)
			{
				/* Exit */
			}
		}
		catch (IOException e)
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
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
