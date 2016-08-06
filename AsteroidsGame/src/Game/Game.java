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

package Game;

import Character.Computer;
import Entity.Entity;
import Character.Player;
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
		this.isGameStop = false;
		
		this.resetEntityLists();
		this.addKeyListener(this.playerListener);
		this.add(this.world, BorderLayout.CENTER);
		
		//Envoyer son Entity
		//Ajouter les Entity recu du serveur
		for (int i = 0; i < 4 * 2; i++)
			this.pendingEntities.add(new SuperSpeedShip (50 + i * 50, 100, Computer.START_LEFT, Entity.COMPUTER));
		
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
		for (int i = 0; i < this.getPlayer().missile.size(); i++)
		{
			boolean find = false;
			
			for (int j = 0; j < this.entities.size(); j++)
				if (this.getPlayer().missile.get(i).getId() == this.entities.get(j).getId())
					find = true;
			
			if (find == false)
				this.pendingEntities.add(this.getPlayer().missile.get(i));
		}
		
		this.entities.addAll(this.pendingEntities);
		this.pendingEntities.clear();
		
		/*if (this.getPlayer().deathCooldown > 0)
		{
			this.getPlayer().deathCooldown = this.getPlayer().deathCooldown - 1;
			
			switch (this.getPlayer().deathCooldown)
			{
				case Player.RESPAWN_COOLDOWN_LIMIT:
					this.getPlayer().reset();
					break;
			}
		}*/
		
		for (Entity entity : this.entities)
			entity.update();
		
		for (int i = 0; i < this.entities.size(); i++)
		{
			Entity a = this.entities.get(i);

			for (int j = i + 1; j < this.entities.size(); j++)
			{
				Entity b = this.entities.get(j);
				
				if ((i != j) && a.isCollision(b))
				{
					a.checkCollision(b);
					b.checkCollision(a);
				}
			}
		}
		
		Iterator <Entity> iter = this.entities.iterator();
		while (iter.hasNext())
			if (iter.next().needsRemoval())
				iter.remove();
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