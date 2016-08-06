/*
 * Gregory Pellegrin
 * pellegrin.gregory.work@gmail.com
 */

package Character;

import Entity.Entity;
import Entity.Missile;
import Entity.Ship;
import Game.Game;
import Util.Vector;
import java.awt.Color;
import java.awt.Graphics2D;
import java.io.Serializable;

public class Computer extends Ship implements Serializable
{
	public final static int START_RIGHT = 300;
	public final static int START_LEFT = -300;
	public final static int START_UP = 600;
	public final static int START_DOWN = 0;
	
	public int life;
	
	public Computer (Vector position, Vector shipVelocity, Color shipColor, Color missileColor, double shipSpeed, double missileSpeed, double radius, double rotationSpeed, int missileMax, int fireRate, int rechargeCooldown, int startingPosition, int life, int killScore)
	{
		super (position, shipVelocity, shipColor, missileColor, shipSpeed, missileSpeed, radius, rotationSpeed, missileMax, fireRate, rechargeCooldown, life, killScore, Entity.COMPUTER);
	
		super.rotate(startingPosition);
		
		this.life = life;
	}
	
	@Override
	public void flagForRemoval ()
	{
		if (this.life > 0)
			this.life = this.life - 1;
		
		if (this.life == 0)
		{
			super.flagForRemoval();
			
			//game.addScore(super.getKillScore());
		}
	}
	
	@Override
	public void checkCollision (Entity other)
	{
		if (other.getClass() == Missile.class)
			this.flagForRemoval();
	}
	
	@Override
	public void draw (Graphics2D g) {}
}