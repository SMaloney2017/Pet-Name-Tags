package com.petnametags;

import com.google.common.base.Strings;
import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.MenuAction;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.chatbox.ChatboxPanelManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@Slf4j
@PluginDescriptor(
	name = "Name Tags",
	description = "Adds the ability to give your pets names.",
	tags = {"pet", "name", "tags"}
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

	@Inject
	private PetNameTagsService service;

	private String activeSessionUser = "";

	@Inject
	private ChatboxPanelManager chatboxPanelManager;

	@Override
	protected void startUp() throws Exception
	{
		overlayManager.add(nameTags);
	}

	@Override
	protected void shutDown() throws Exception
	{
		overlayManager.remove(nameTags);
	}

	@Subscribe
	public void onMenuEntryAdded(MenuEntryAdded event)
	{
		if (event.getOption().equals("Pick-up"))
		{
			client.createMenuEntry(-1)
				.setOption("Rename")
				.setTarget(event.getTarget())
				.setType(MenuAction.RUNELITE)
				.onClick(e ->
				{
					String name = event.getTarget().substring(event.getTarget().lastIndexOf('>') + 1);
					addNewNameTag(name);
				});
		}
	}

	@Subscribe
	public void onGameStateChanged(final GameStateChanged event) {
		if (event.getGameState() == GameState.LOGGED_IN)
		{
			if (!String.valueOf(client.getAccountHash()).equals(activeSessionUser))
			{
				this.service = new PetNameTagsService(client, String.valueOf(client.getAccountHash()));
				activeSessionUser = String.valueOf(client.getAccountHash());
			}
		}
	}

	@Provides
	PetNameTagsConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(PetNameTagsConfig.class);
	}

	private void addNewNameTag(String npcId)
	{

		chatboxPanelManager.openTextInput("Name-tag")
			.value("")
			.onDone((input) ->
			{
				input = Strings.emptyToNull(input);

				PetNameTag newNameTag = new PetNameTag(config.getNameTagColor(), input, npcId);
				service.addToNameTags(npcId, newNameTag);
			})
			.build();
	}

}
