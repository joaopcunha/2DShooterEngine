package com.jojostudios.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

import com.jojostudios.entities.Bullet;
import com.jojostudios.entities.Enemy;
import com.jojostudios.entities.Entity;
import com.jojostudios.entities.Npc;
import com.jojostudios.entities.Player;
import com.jojostudios.graphics.Spritesheet;
import com.jojostudios.graphics.UI;
import com.jojostudios.world.Camera;
import com.jojostudios.world.World;

public class Game extends Canvas implements Runnable, KeyListener, MouseListener{
	
	private static final long serialVersionUID = 1L;
	public static JFrame frame;
	private Thread thread;
	private boolean isRunning;
	public static final int WIDTH = 240; 
	public static final int HEIGHT = 160;
	public static final int SCALE = 3;
	
	private static BufferedImage image;
	
	public static List<Enemy> enemies;
	public static List<Entity> entities;
	public static List<Bullet> bullets;
	public static Spritesheet spritesheet;
	
	public static World world;
	
	public static Player player;
	public static Npc tutorialNpc1, tutorialNpc2, tutorialNpc3;
	
	public static Random rand;
	
	public static UI ui;
	public static Menu menu;
	public static Pause pause;
	public static String gameState = "menu";
	
	public static int CUR_LEVEL = 0, MAX_LEVEL = 2;
	
	public static int[] pixels;
	
	public static BufferedImage minimap;
	public static int[] minimapPixels;
	
	public Game() {
		rand = new Random();
		addKeyListener(this);
		addMouseListener(this);
		this.setPreferredSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
		initFrame();
		restart();
//		Sound.music.play();
	}
	
	public static void restart() {
		initGame();
		world = new World("/level0.png");
		minimap = new BufferedImage(World.width, World.height, BufferedImage.TYPE_INT_RGB);
		minimapPixels = ((DataBufferInt)minimap.getRaster().getDataBuffer()).getData();
	}
	
	public static void changeLevel(int nextLevel) {
		initGame();
		String newWorld = "level"+nextLevel+".png";
		world = new World("/"+newWorld);
		minimap = new BufferedImage(World.width, World.height, BufferedImage.TYPE_INT_RGB);
		minimapPixels = ((DataBufferInt)minimap.getRaster().getDataBuffer()).getData();
		
		CUR_LEVEL=nextLevel;
	}
	
	private static void initGame() {
		ui = new UI();
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
		entities = new ArrayList<Entity>();
		enemies = new ArrayList<Enemy>();
		spritesheet = new Spritesheet("/Spritesheet.png");
		player = new Player(0, 0, 16, 16, spritesheet);
		tutorialNpc1 = new Npc(0, 0, 16, 16, Entity.NPC_EN, Npc.TUTORIAL_PHRASES_1);
		tutorialNpc2 = new Npc(0, 0, 16, 16, Entity.NPC_EN, Npc.TUTORIAL_PHRASES_2);
		tutorialNpc3 = new Npc(0, 0, 16, 16, Entity.NPC_EN, Npc.TUTORIAL_PHRASES_3);
		
		entities.add(tutorialNpc1);
		entities.add(tutorialNpc2);
		entities.add(tutorialNpc3);
		entities.add(player);
		bullets = new ArrayList<Bullet>();
		menu = new Menu();
		pause = new Pause();
	}

	public void initFrame() {
		frame = new JFrame("Game #1");
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		frame.setAlwaysOnTop(true);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
	}
	
	public synchronized void start() {
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	}
	
	public synchronized void stop() {
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Game game = new Game();
		game.start();
	}
	
	public void tick() {
		if (gameState == "normal") {
			for(int i=0; i < entities.size(); i++) {
				Entity e = entities.get(i);
				e.tick();
			}
			
			for(int i=0; i<bullets.size();i++) {
				Bullet b = bullets.get(i);
				b.tick();
			}
			
		} else if (gameState == "game_over") {
		} else if (gameState == "menu") {
			menu.tick();
		} else if (gameState == "pause") {
			pause.tick();
		}
		
	}
	
	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		
		Graphics g = image.getGraphics();
		g.setColor(new Color(0, 100, 0));
		g.fillRect(0, 0, WIDTH, HEIGHT);

		world.render(g);
		Collections.sort(entities, Entity.nodeSorter);
		for(int i=0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.render(g);
		}
		
		for(int i=0; i<bullets.size();i++) {
			Bullet b = bullets.get(i);
			b.render(g);
		}
		
		

		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, WIDTH*SCALE, HEIGHT*SCALE, null);
		ui.render(g);
		tutorialNpc1.renderDialog(g, (tutorialNpc1.getX()-Camera.x)*SCALE, (tutorialNpc1.getY()-Camera.y)*SCALE);
		tutorialNpc2.renderDialog(g, (tutorialNpc2.getX()-Camera.x-70)*SCALE, (tutorialNpc2.getY()-Camera.y)*SCALE);
		tutorialNpc3.renderDialog(g, (tutorialNpc3.getX()-Camera.x)*SCALE, (tutorialNpc3.getY()-Camera.y)*SCALE);
		
		if (gameState == "game_over") {
			ui.renderGameOver(g);
		} else if (gameState == "menu") {
			menu.render(g);
		} else if (gameState == "pause") {
			pause.render(g);
		}
		
		World.renderMinimap();
		g.drawImage(minimap, 610, 370, world.width*5, world.height*5,null);
		
		bs.show();
	}
	
	public void run() {
		requestFocus();
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000/amountOfTicks;
		double delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();
		
		
		while(isRunning) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			
			if (delta >= 1) {
				tick();
				render();
				frames++;
				delta--;
			}
			
			if(System.currentTimeMillis() - timer >= 1000) {
				frames = 0;
				timer+=1000;
			}
		}
		
		stop();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (gameState == "normal") {
			if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
				player.right = true;
			} else if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
				player.left = true;
			}
			
			if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
				player.up = true;
			} else if(e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
				player.down = true;
			}
			
			if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				player.shoot();
			}
			
			if (e.getKeyCode() == KeyEvent.VK_Q) {
				player.interact();
			}
			
			if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				gameState = "pause";
			}
			
			if (e.getKeyCode() == KeyEvent.VK_E) {
				player.dodge = true;
			}
		}
		
		if (gameState == "game_over" && e.getKeyCode() == KeyEvent.VK_SPACE) {
			changeLevel(CUR_LEVEL);
			gameState = "normal";
		}
		
		if (gameState == "menu") {
			if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
				menu.down = true;
			}
			else if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
				menu.up = true;
			}
			
			if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				menu.enter = true;
			}
		}
		
		if (gameState == "pause") {
			if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
				pause.down = true;
			}
			else if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
				pause.up = true;
			}
			
			if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				pause.enter = true;
			}
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			player.right = false;
		} else if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			player.left = false;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			player.up = false;
		} else if(e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
			player.down = false;
		}
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {

		
	}

	@Override
	public void mouseExited(MouseEvent e) {

		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		player.mouseShoot((e.getX()/SCALE), (e.getY()/SCALE));
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {

		
	}

}	
