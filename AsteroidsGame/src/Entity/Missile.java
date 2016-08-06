/*
 * Gregory Pellegrin
 * pellegrin.gregory.work@gmail.com
 */

package Entity;

import Character.Player;
import Game.WorldPanel;
import Util.Vector;
import java.awt.Color;
import java.awt.Graphics2D;
import java.io.Serializable;

public class Missile extends Entity implements Serializable
{
	private static final int LIFESPAN_MAX = 60;
	
	public final int shipId;
	
	private int lifeSpan;

	public Missile (Entity owner, Color color, double direction, double speed)
	{
		super(new Vector(owner.getPosition()), new Vector(direction).scale(speed), color, 2.0, 1, Entity.MISSILE);
		
		this.shipId = owner.getId();
		this.lifeSpan = LIFESPAN_MAX;
	}

	@Override
	public void update ()
	{
		super.update();

		this.lifeSpan = this.lifeSpan - 1;
		
		if (lifeSpan <= 0)
			super.flagForRemoval();
	}
	
	@Override
	public void checkCollision (Entity other)
	{
		if (other.getClass() != Player.class)
			super.flagForRemoval();
		
		if (other.getClass() == Player.class)
			if (other.getId() != this.shipId)
				super.flagForRemoval();
	}
	
	@Override
	public void draw (Graphics2D g)
	{
		g.setColor(super.color);	
		g.drawOval(-1, -1, 2, 2);
		
		g.setColor(WorldPanel.COLOR_DEFAULT);
	}
}