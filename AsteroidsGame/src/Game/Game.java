/*
 * Gregory Pellegrin
 * pellegrin.gregory.work@gmail.com
 */

/*
 * TODO
 * 
 * Refactoring
 * Multijoueurs
 * Collisions
 * Objectifs annexes
 * Map
 * IA
 * Site Web
 * Son
 */

/*
 * BUG
 * Vie des Ship = 4, apres 2 tir le vaisseau meurt
 */

package Game;

import Character.Computer;
import Entity.Entity;
import Character.Player;
import Client.Client;
import Ship.SuperSpeedShip;
import Effect.Story;
import Entity.Ship;
import Util.Clock;
import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JFrame;

public class Game extends JFrame
{
	public Story story;
	
	private static final int FRAMES_PER_SECOND = 60;
	private static final long FRAME_TIME = (long) (1000000000.0 / FRAMES_PER_SECOND);
	
	private final MenuPanel menu;
	private final WorldPanel world;
	private final KeyAdapter menuListener;
	private final KeyAdapter playerListener;
	
	private List <Entity> entities;
	private List <Entity> pendingEntities;
	private Clock logicTimer;
	private Player player;
	private Client client;
	private boolean isGameStart;
	private boolean isGameStop;
	
	private Game ()
	{
		super ("Asteroids");
		
		this.menu = new MenuPanel (this);
		this.world = new WorldPanel (this);
		
		this.menuListener = new KeyAdapter ()
		{
			@Override
			public void keyPressed (KeyEvent e)
			{
				switch (e.getKeyCode())
				{						
					case KeyEvent.VK_SPACE:
						startTheGame();
						break;

					case KeyEvent.VK_ENTER:
						startTheGame();
						break;
				}
			}
		};
		
		this.playerListener = new KeyAdapter ()
		{
			@Override
			public void keyPressed (KeyEvent e)
			{
				switch (e.getKeyCode())
				{
					case KeyEvent.VK_Z:
						player.setMove(true);
						break;

					case KeyEvent.VK_D:
						player.setRotateRight(true);
						break;

					case KeyEvent.VK_Q:
						player.setRotateLeft(true);
						break;

					case KeyEvent.VK_O:
						player.setSuperSpeed(true);
						break;

					case KeyEvent.VK_SPACE:
						player.setFiring(true);
						break;
					
					case KeyEvent.VK_ESCAPE:
						stopTheGame();
						break;
				}
			}
			
			@Override
			public void keyReleased (KeyEvent e)
			{
				switch (e.getKeyCode())
				{
					case KeyEvent.VK_Z:
						player.setMove(false);
						break;

					case KeyEvent.VK_D:
						player.setRotateRight(false);
						break;
						
					case KeyEvent.VK_Q:
						player.setRotateLeft(false);
						break;

					case KeyEvent.VK_O:
						player.setSuperSpeed(false);
						break;

					case KeyEvent.VK_SPACE:
						player.setFiring(false);
						break;
				}
			}
		};
		
		this.story = new Story ();
		
		this.setLayout(new BorderLayout ());
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setResizable(false);
		
		this.add(this.menu, BorderLayout.CENTER);
		
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	
	public List <Entity> getEntities ()
	{
		return this.entities;
	}

	public Player getPlayer ()
	{
		return this.player;
	}

	/*public boolean canDrawPlayer ()
	{
		return this.player.canDrawPlayer();
	}*/

	public int getScore ()
	{
		return this.player.getScore();
	}
	
	private boolean isGameStart ()
	{
		return isGameStart;
	}

	private boolean isGameStop ()
	{
		return isGameStop;
	}
	
	private void startTheGame ()
	{
		this.isGameStart = true;
	}
	
	private void stopTheGame ()
	{
		this.isGameStop = true;
	}
	
	private void resetEntityLists ()
	{
		this.pendingEntities.clear();
		this.entities.clear();
		this.entities.add(this.player);
	}
	
	private void removeKeyListener ()
	{
		this.removeKeyListener(this.menuListener);
		this.removeKeyListener(this.playerListener);
	}
	
	private void resetMenu ()
	{
		this.removeKeyListener();
		this.remove(this.world);
		
		this.entities = new LinkedList <> ();
		this.pendingEntities = new ArrayList <> ();
		this.logicTimer = new Clock (Game.FRAMES_PER_SECOND);
		this.isGameStart = false;
		
		this.resetEntityLists();
		this.addKeyListener(this.menuListener);
		this.add(this.menu, BorderLayout.CENTER);
		
		this.revalidate();
	}
	
	private void resetGame ()
	{
		this.removeKeyListener();
		this.remove(this.menu);
		
		this.entities = new LinkedList <> ();
		this.pendingEntities = new ArrayList <> ();
		this.logicTimer = new Clock (Game.FRAMES_PER_SECOND);
		this.player = new Player ();
		this.client = new Client (this.player);
		this.isGameStop = false;
		
		this.resetEntityLists();
		this.addKeyListener(this.playerListener);
		this.add(this.world, BorderLayout.CENTER);
		
		Thread test = new Thread (this.client);
		test.start();
		//Envoyer son Entity
		//Ajouter les Entity recu du serveur
		//for (int i = 0; i < 4 * 2; i++)
		//	this.pendingEntities.add(new SuperSpeedShip (50 + i * 50, 100, Computer.START_LEFT, Entity.COMPUTER));
		
		this.revalidate();
	}

	private void startMenu ()
	{
		this.resetMenu();
		
		while (! this.isGameStart())
		{
			long start = System.nanoTime();
			
			this.logicTimer.update();

			this.menu.repaint();

			long delta = Game.FRAME_TIME - (System.nanoTime() - start);			
			if (delta > 0)
				try
				{
					Thread.sleep(delta / 1000000L, (int) delta % 1000000);
				}
				catch (Exception e)
				{
					System.out.println(e.getMessage());
				}
		}
	}

	private void startGame ()
	{
		this.resetGame();
		
		while (! this.isGameStop())
		{
			long start = System.nanoTime();
			
			this.logicTimer.update();
			for (int i = 0; i < 5 && this.logicTimer.hasElapsedCycle(); i++)
				this.updateGame();
			
			this.world.repaint();
			
			long delta = Game.FRAME_TIME - (System.nanoTime() - start);			
			if (delta > 0)
				try
				{
					Thread.sleep(delta / 1000000L, (int) delta % 1000000);
				}
				catch (Exception e)
				{
					System.out.println(e.getMessage());
				}
		}
	}
	
	private void updateGame ()
	{
		this.player.update();
		this.entities.clear();
		this.entities.addAll(this.client.update(this.player));
		//this.updatePlayer();
	}
	
	private void updatePlayer ()
	{
		boolean find = false;
		
		for (int i = 0; ((i < this.entities.size()) && (! find)); i++)
			if (this.entities.get(i).getId() == this.player.getId())
			{
				find = true;
				
				Player player = (Player) this.entities.get(i);
				this.player = player;
				
				/*for (int j = 0; j < player.missile.size(); j++)
				{
					find = false;

					for (int k = 0; k < player.missile.size(); k++)
						if (player.missile.get(k).getId() == this.player.getId())
							find = true;

					if (find == false)
						this.entities.add(player.missile.get(j));
				}*/
			}
	}
	
	public static void main (String [] args)
	{
		Game game = new Game();
		
		while (true)
		{
			game.startMenu();
			game.startGame();
		}
	}
}