package com.jojostudios.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Pause extends Menu{
	
	public Pause() {
		options = new String[] {"Continue", "Exit"};
		currentOption = 0;
		maxOption = options.length - 1;
	}
	
	public void render(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(new Color(0, 0, 0, 100));
		g2.fillRect(0, 0, Game.WIDTH*Game.SCALE, Game.HEIGHT*Game.SCALE);
		g.setColor(Color.white);
		g.setFont(new Font("arial", Font.BOLD, 66));
		g.drawString("Pause", (Game.WIDTH*Game.SCALE/2)-240, (Game.HEIGHT*Game.SCALE/2)-138);
		
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
