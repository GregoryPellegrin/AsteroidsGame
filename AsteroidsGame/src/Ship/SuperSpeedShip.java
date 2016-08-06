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

public class SuperSpeedShip extends Ship implements Serializable
{
	private static final double SPEED_SHIP = 0.0785;
	private static final double SPEED_MISSILE = 7.75;
	private static final double ROTATION_SPEED = 0.045;
	private static final int MISSILE_MAX = 6;
	private static final int FIRE_RATE = 4;
	private static final int RECHARGE_COOLDOWN = 30;
	private static final int LIFE = 4;
	private static final int KILL_SCORE = 50;
	
	public SuperSpeedShip (int x, int y, int startingPosition, int type)
	{
		super (new Vector (x, y), new Vector (1, 0), Color.RED, Color.RED, SuperSpeedShip.SPEED_SHIP, SuperSpeedShip.SPEED_MISSILE, 10.0, SuperSpeedShip.ROTATION_SPEED, SuperSpeedShip.MISSILE_MAX, SuperSpeedShip.FIRE_RATE, SuperSpeedShip.RECHARGE_COOLDOWN, SuperSpeedShip.LIFE, SuperSpeedShip.KILL_SCORE, type);
		
		super.flamesMotorColor.add(Color.YELLOW);
		super.flamesMotorColor.add(Color.RED);
	}
	
	@Override
	public void draw (Graphics2D g)
	{
		g.setColor(super.color);
		g.drawLine(-25, -15, 10, 0);
		g.drawLine(-25, 15, 10, 0);
		g.drawLine(-25, -15, -10, -3);
		g.drawLine(-25, 15, -10, 3);
		g.drawLine(-20, 0, -10, -3);
		g.drawLine(-20, 0, -10, 3);

		g.setColor(WorldPanel.COLOR_DEFAULT);

		if (super.isMovePressed() && ((super.getAnimationFrame() % 6) < 3))
		{
			g.setColor(super.flamesMotorColor.get(0));
			g.drawLine(-28, -15, -13, -3);
			g.drawLine(-28, 15, -13, 3);
			g.drawLine(-23, 0, -13, -3);
			g.drawLine(-23, 0, -13, 3);

			g.setColor(super.flamesMotorColor.get(1));
			g.drawLine(-29, -15, -14, -3);
			g.drawLine(-29, 15, -14, 3);
			g.drawLine(-24, 0, -14, -3);
			g.drawLine(-24, 0, -14, 3);

			g.setColor(WorldPanel.COLOR_DEFAULT);
		}
	}
}