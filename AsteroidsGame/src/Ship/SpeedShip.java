/*
 * Gregory Pellegrin
 * pellegrin.gregory.work@gmail.com
 */

package Ship;

import Entity.Ship;
import Game.WorldPanel;
import Util.Vector;
import java.awt.Color;
import java.awt.Graphics2D;
import java.io.Serializable;

public class SpeedShip extends Ship implements Serializable
{
	private static final double SPEED_SHIP = 0.0385;
	private static final double SPEED_MISSILE = 6.75;
	private static final double ROTATION_SPEED = 0.055;
	private static final int MISSILE_MAX = 4;
	private static final int FIRE_RATE = 4;
	private static final int RECHARGE_COOLDOWN = 30;
	private static final int LIFE = 1;
	private static final int KILL_SCORE = 20;
	
	public SpeedShip (int x, int y, int startingPosition, int type)
	{
		super (new Vector (x, y), new Vector (1, 0), Color.RED, Color.RED, SpeedShip.SPEED_SHIP, SpeedShip.SPEED_MISSILE, 10.0, SpeedShip.ROTATION_SPEED, SpeedShip.MISSILE_MAX, SpeedShip.FIRE_RATE, SpeedShip.RECHARGE_COOLDOWN, SpeedShip.LIFE, SpeedShip.KILL_SCORE, type);
		
		super.flamesMotorColor.add(Color.YELLOW);
		super.flamesMotorColor.add(Color.RED);
	}
	
	@Override
	public void draw (Graphics2D g)
	{
		g.setColor(super.color);
		g.drawLine(-10, -8, 10, 0);
		g.drawLine(-10, 8, 10, 0);
		g.drawLine(-10, -8, -4, 0);
		g.drawLine(-10, 8, -4, 0);

		g.setColor(WorldPanel.COLOR_DEFAULT);

		if (super.isMovePressed() && ((super.getAnimationFrame() % 6) < 3))
		{
			g.setColor(super.flamesMotorColor.get(0));
			g.drawLine(-12, -8, -6, 0);
			g.drawLine(-12, 8, -6, 0);

			g.setColor(super.flamesMotorColor.get(1));
			g.drawLine(-13, -8, -7, 0);
			g.drawLine(-13, 8, -7, 0);

			g.setColor(WorldPanel.COLOR_DEFAULT);
		}
	}
}