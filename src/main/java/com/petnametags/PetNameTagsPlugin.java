package com.petnametags;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@Slf4j
@PluginDescriptor(
	name = "Name Tags",
	description = "Adds the ability to give your pets names."
)
public class PetNameTagsPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private PetNameTagsConfig config;

	@Inject
	private ConfigManager configManager;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private PetNameTagsOverlay nameTags;

	@Override
	protected void startUp() throws Exception
	{
		/* overlayManager.add(nameTags); */
	}

	@Override
	protected void shutDown() throws Exception
	{
		/* overlayManager.remove(nameTags); */
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{

	}

	@Subscribe
	public void onConfigChanged(ConfigChanged event)
	{

	}

	@Provides
	PetNameTagsConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(PetNameTagsConfig.class);
	}

	private void addNameTag()
	{

	}

	private void redrawNameTags()
	{

	}

}
