/*
 * Gregory Pellegrin
 * pellegrin.gregory.work@gmail.com
 */

package Entity;

import Game.WorldPanel;
import Util.Vector;
import java.awt.Color;
import java.awt.Graphics2D;
import java.io.Serializable;

public abstract class Entity implements Serializable
{
	public static final int PLAYER = 0;
	public static final int COMPUTER = 1;
	public static final int MISSILE = 2;
	
	protected Vector position;
	protected Vector speed;
	protected Color color;
	protected double rotation;
	protected int life;
	
	private static int total = 0;
	
	private final double collisionRadius;
	private final int id;
	private final int lives;
	private final int type;
	
	private boolean needsRemoval;
	
	public Entity (Vector position, Vector speed, Color color, double radius, int lives, int type)
	{
		Entity.total = Entity.total + 1;
		this.id = Entity.total;
		
		this.position = position;
		this.speed = speed;
		this.color = color;
		this.collisionRadius = radius;
		this.rotation = 0.0f;
		this.lives = lives;
		this.life = this.lives;
		this.type = type;
		this.needsRemoval = false;
	}
	
	public Vector getPosition ()
	{
		return this.position;
	}

	public boolean needsRemoval ()
	{
		return this.needsRemoval;
	}

	public double getRotation ()
	{
		return this.rotation;
	}
	
	public int getId ()
	{
		return this.id;
	}
	
	public int getType ()
	{
		return this.type;
	}
	
	public void flagForRemoval ()
	{
		this.needsRemoval = true;
	}
	
	public void rotate (double amount)
	{
		this.rotation = this.rotation + amount;
		this.rotation %= Math.PI * 2;
	}
	
	public void reset ()
	{
		this.life = this.lives;
	}
	
	public void update ()
	{
		this.position.add(this.speed);
		
		if (this.position.x < 0.0f)
			this.position.x = this.position.x + WorldPanel.W_MAP_PIXEL;
		if (this.position.y < 0.0f)
			this.position.y = this.position.y + WorldPanel.H_MAP_PIXEL;
		
		this.position.x %= WorldPanel.W_MAP_PIXEL;
		this.position.y %= WorldPanel.H_MAP_PIXEL;
	}
	
	public boolean isCollision (Entity entity)
	{
		double radius = entity.collisionRadius + this.collisionRadius;
		
		return (this.position.getDistanceToSquared(entity.position) < (radius * radius));
	}
	
	public abstract void checkCollision (Entity other);

	public abstract void draw (Graphics2D g);
}