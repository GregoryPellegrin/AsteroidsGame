/*
 * Gregory Pellegrin
 * pellegrin.gregory.work@gmail.com
 */

package Entity;

import Util.Vector;
import java.awt.Color;
import java.awt.Graphics2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class Ship extends Entity implements Serializable
{
	public final ArrayList <Missile> missiles;
	
	protected ArrayList <Color> flamesMotorColor;
	
	private static final double SPEED_SHIP_MAX = 6.5;
	private static final double SPEED_STOP = 0.995;
	private static final double SPEED_ROTATION_DEFAULT = -Math.PI / 2.0;
	private static final int CONSECUTIVE_SHOTS_MAX = 8;
	
	private final Color MISSILE_COLOR;
	private final double SPEED_ROTATION;
	private final int MISSILE_MAX;
	private final int FIRE_RATE;
	private final int RECHARGE_COOLDOWN;
	private final int KILL_SCORE;
	
	private boolean movePressed;
	private boolean rotationRightPressed;
	private boolean rotationLeftPressed;
	private boolean firePressed;
	private double speedShip;
	private double speedMissile;
	private int consecutiveShots;
	private int fireCooldown;
	private int overheatCooldown;
	private int animationFrame;
	
	public Ship (Vector position, Vector velocity, Color shipColor, Color missileColor, double speedShip, double speedMissile, double radius, double speedRotation, int missileMax, int fireRate, int rechargeCooldown, int life, int killScore, int type)
	{
		super(position, velocity, shipColor, radius, life, type);
		
		this.MISSILE_COLOR = missileColor;
		this.MISSILE_MAX = missileMax;
		this.FIRE_RATE = fireRate;
		this.RECHARGE_COOLDOWN = rechargeCooldown;
		this.KILL_SCORE = killScore;
		
		this.flamesMotorColor = new ArrayList <> ();
		this.missiles = new ArrayList <> ();
		this.rotation = Ship.SPEED_ROTATION_DEFAULT;
		this.SPEED_ROTATION = speedRotation;
		this.movePressed = false;
		this.rotationRightPressed = false;
		this.rotationLeftPressed = false;
		this.firePressed = false;
		this.speedShip = speedShip;
		this.speedMissile = speedMissile;
		this.fireCooldown = 0;
		this.overheatCooldown = 0;
		this.animationFrame = 0;
	}
	
	public boolean isMovePressed ()
	{
		return this.movePressed;
	}
	
	public int getAnimationFrame ()
	{
		return this.animationFrame;
	}

	public void setMove (boolean state)
	{
		this.movePressed = state;
	}

	public void setRotateLeft (boolean state)
	{
		this.rotationLeftPressed = state;
	}
	
	public void setRotateRight (boolean state)
	{
		this.rotationRightPressed = state;
	}

	public void setFiring (boolean state)
	{
		this.firePressed = state;
	}
	
	public void setSpeedShip (double speed)
	{
		this.speedShip = speed;
	}
	
	public void setSpeedMissile (double speed)
	{
		this.speedMissile = speed;
	}

	@Override
	public void reset ()
	{
		super.reset();
		
		this.rotation = Ship.SPEED_ROTATION_DEFAULT;
		this.missiles.clear();
		super.speed.set(0.0, 0.0);
	}
	
	@Override
	public void update ()
	{
		super.update();
		
		this.animationFrame = this.animationFrame + 1;
		
		if (this.rotationLeftPressed != this.rotationRightPressed)
			super.rotate(this.rotationLeftPressed ? - this.SPEED_ROTATION : this.SPEED_ROTATION);
		
		if (this.movePressed)
		{
			super.speed.add(new Vector (this.rotation).scale(this.speedShip));
			
			if (super.speed.getLengthSquared() >= Ship.SPEED_SHIP_MAX * Ship.SPEED_SHIP_MAX)
				super.speed.normalize().scale(Ship.SPEED_SHIP_MAX);
		}
		
		if (super.speed.getLengthSquared() != 0.0)
			super.speed.scale(Ship.SPEED_STOP);
		
		Iterator <Missile> iter = this.missiles.iterator();
		while (iter.hasNext())
		{
			Missile bullet = iter.next();
			
			if (bullet.needsRemoval())
				iter.remove();
		}
		
		this.fireCooldown = this.fireCooldown- 1;
		this.overheatCooldown = this.overheatCooldown - 1;
		if (this.firePressed && (this.fireCooldown <= 0) && (this.overheatCooldown <= 0))
		{
			if (this.missiles.size() < this.MISSILE_MAX)
			{
				this.fireCooldown = this.FIRE_RATE;
				
				Missile bullet = new Missile(this, this.MISSILE_COLOR, this.rotation, this.speedMissile);
				
				this.missiles.add(bullet);
			}
			
			this.consecutiveShots = this.consecutiveShots + 1;
			if (this.consecutiveShots == Ship.CONSECUTIVE_SHOTS_MAX)
			{
				this.consecutiveShots = 0;
				this.overheatCooldown = this.RECHARGE_COOLDOWN;
			}
		}
		else
		{
			if (this.consecutiveShots > 0)
				this.consecutiveShots = this.consecutiveShots - 1;
		}
	}
	
	@Override
	public void checkCollision (Entity other)
	{
		if (other.getType() == Entity.MISSILE)
			if (this.getId() != ((Missile) other).idShip)
			{
				super.life = super.life - 1;
				
				if (super.life == 0)
					super.flagForRemoval();
			}
		
		if (other.getType() == Entity.COMPUTER)
			super.flagForRemoval();
	}

	@Override
	public void draw (Graphics2D g) {}
}