package com.jojostudios.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Menu {
	
	public String[] options;
	public int currentOption;
	public int maxOption;
	public boolean up, down, enter;
	
	public Menu() {
		options = new String[] {"New game", "Load game", "Exit"};
		currentOption = 0;
		maxOption = options.length - 1;
	}
	
	public void tick() {
		if (up) {
			currentOption--;
			if (currentOption < 0) {
				currentOption = maxOption;
			}
			up = false;
		}
		
		if (down) {
			currentOption++;
			if (currentOption > maxOption) {
				currentOption = 0;
			}
			down = false;
		}
		
		if (enter) {
			if(options[currentOption] == "New game" || options[currentOption] == "Continue") {
				Game.gameState = "normal";
			}
			enter = false;
			
			if(options[currentOption] == "Exit") {
				System.exit(1);;
			}
			enter = false;
		}
	}
	
	public void render(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, Game.WIDTH*Game.SCALE, Game.HEIGHT*Game.SCALE);
		g.setColor(Color.green);
		g.setFont(new Font("arial", Font.BOLD, 66));
		g.drawString("Ghost Hunter", (Game.WIDTH*Game.SCALE/2)-240, (Game.HEIGHT*Game.SCALE/2)-138);
		g.setColor(Color.white);
		g.setFont(new Font("arial", Font.BOLD, 34));

		for (int i=0; i<=maxOption; i++) {
			int yOffeset = 80*i;
			g.drawString(options[i], (Game.WIDTH*Game.SCALE/2)-200, (Game.HEIGHT*Game.SCALE/2)+yOffeset);
			if (currentOption == i) {
				g.drawString(">", (Game.WIDTH*Game.SCALE/2)-230, (Game.HEIGHT*Game.SCALE/2)+yOffeset);
			}
		}
	}
	

}
