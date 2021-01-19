package com.jojostudios.entities;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.jojostudios.main.Game;

public class Npc extends Entity{
	
	public String[] phrases = new String[2];
	public boolean talkRange = false;
	public int talkStep = 0;
	public int maxTalkSteps = 2;

	public Npc(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		phrases[0] = "Hello, welcome to the game!";
		phrases[1] = "Use W/S/A/D to move around";
	}
	
	public void tick() {
		int xPlayer = Game.player.getX();
		int yPlayer = Game.player.getY();
		
		if(Math.abs(xPlayer-x) < 30 && Math.abs(yPlayer-y) < 30) {
			talkRange = true;
		} else {
			talkRange = false;
			talkStep = 0;
		}
		
		if (talkStep > maxTalkSteps) {
			talkStep = 0;
		}
		
	}
	
	public void render(Graphics g) {
		super.render(g);
		
		if(talkStep > 0) {
//			g.setColor(Color.blue);
//			g.fillRect((int)x+5, (int)y-12, Game.WIDTH-150, Game.HEIGHT-150);
			g.setFont(new Font("Arial", Font.BOLD, 9));
			g.setColor(Color.black);
			g.drawString(phrases[talkStep-1], (int)x-50, (int)x-50);
		}
	}
	
}
