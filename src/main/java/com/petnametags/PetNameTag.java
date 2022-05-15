package com.petnametags;

import java.awt.Color;
import java.io.Serializable;

public class PetNameTag implements Serializable
{
	String npcId;
	String label;
	Color color;

	public PetNameTag(Color color, String label, String npcId)
	{
		this.color = color;
		this.label = label;
		this.npcId = npcId;
	}

	public Color getColor()
	{
		return this.color;
	}

	public String getLabel()
	{
		return this.label;
	}
}
