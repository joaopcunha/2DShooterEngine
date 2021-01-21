package com.jojostudios.entities;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.jojostudios.main.Game;

public class Npc extends Entity{
	
	public String[] phrases = new String[5];
	public boolean talkRange = false;
	public int talkStep = 0;
	public int maxTalkSteps;
	
	public int curIndex = 0;

	public Npc(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		phrases[0] = "Hello, welcome to the game!";
		phrases[1] = "Use W/S/A/D to move around";
		phrases[2] = "Use your mouse to aim and shoot";
		phrases[3] = "Use E to dodge, pay atention to your stamina";
		phrases[4] = "Good luck!";
		maxTalkSteps = phrases.length;
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
		} else if (talkStep > 0) {
			if(curIndex < phrases[talkStep-1].length()) {
				curIndex++;
			}
		}
		
	}
	
	public void render(Graphics g) {
		super.render(g);
	}
	 
	public void renderDialog(Graphics g) {
		if(talkStep > 0) {
			g.setFont(new Font("Arial", Font.BOLD, 19));
			g.setColor(Color.black);
			g.drawString(phrases[talkStep-1].substring(0, curIndex), (int)x+120, (int)y+50);
		}
	}
	
}
