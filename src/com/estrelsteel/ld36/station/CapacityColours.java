package com.estrelsteel.ld36.station;

import java.awt.Color;

public enum CapacityColours {
	ZERO(Color.WHITE), ONE(Color.RED), TWO(Color.ORANGE), THREE(Color.YELLOW), FOUR(Color.GREEN), FIVE(Color.CYAN), SIX(Color.BLUE), 
	SEVEN(Color.MAGENTA), EIGHT(Color.PINK), NINE(Color.LIGHT_GRAY), TEN(Color.GRAY), ELEVEN(Color.DARK_GRAY), TWELEVE(Color.BLACK);
	
	private Color colour;
	
	CapacityColours(Color colour) {
		this.colour = colour;
	}
	
	public Color getColour() {
		return colour;
	}
}
