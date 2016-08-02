/*
 * Gregory Pellegrin
 * pellegrin.gregory.work@gmail.com
 */

package Entity;

import Game.Game;
import Game.WorldPanel;
import Util.Vector;
import java.awt.Color;
import java.awt.Graphics2D;
import java.io.Serializable;

public abstract class Entity implements Serializable
{
	protected Vector position;
	protected Vector speed;
	protected Color color;
	protected double rotation;
	protected double collisionRadius;
	protected int life;
	
	private static int total = 0;
	
	private final int id;
	private final int lives;
	
	private boolean needsRemoval;
	
	public Entity (Vector position, Vector speed, Color color, double radius, int lives)
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

	public double getCollisionRadius ()
	{
		return this.collisionRadius;
	}
	
	public int getId ()
	{
		return this.id;
	}
	
	public void flagForRemoval ()
	{
		this.needsRemoval = true;
	}
	
	public void resetLife ()
	{
		this.life = this.lives;
	}
	
	public void rotate (double amount)
	{
		this.rotation = this.rotation + amount;
		this.rotation %= Math.PI * 2;
	}
	
	public void update (Game game)
	{
		this.position.add(this.speed);
		
		if (this.position.x < 0.0f)
			this.position.x = this.position.x + WorldPanel.W_MAP_PIXEL;
		if (this.position.y < 0.0f)
			this.position.y = this.position.y + WorldPanel.H_MAP_PIXEL;
		
		this.position.x %= WorldPanel.W_MAP_PIXEL;
		this.position.y %= WorldPanel.H_MAP_PIXEL;
	}
	
	public boolean checkCollision (Entity entity)
	{
		double radius = entity.getCollisionRadius() + this.collisionRadius;
		
		return (this.position.getDistanceToSquared(entity.position) < (radius * radius));
	}

	public abstract void checkCollision (Game game, Entity other);

	public abstract void draw (Graphics2D g);
}