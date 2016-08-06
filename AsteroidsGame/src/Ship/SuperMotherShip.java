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

public class SuperMotherShip extends Ship implements Serializable
{
	private static final double SPEED_SHIP = 0.0100;
	private static final double SPEED_MISSILE = 4.00;
	private static final double SPEED_ROTATION = 0.010;
	private static final int MISSILE_MAX = 10;
	private static final int FIRE_RATE = 4;
	private static final int RECHARGE_COOLDOWN = 10;
	private static final int LIFE = 10;
	private static final int KILL_SCORE = 1000;
	
	public SuperMotherShip (int x, int y, int startingPosition, int type)
	{
		super (new Vector (x, y), new Vector (1, 0), Color.RED, Color.RED, SuperMotherShip.SPEED_SHIP, SuperMotherShip.SPEED_MISSILE, 10.0, SuperMotherShip.SPEED_ROTATION, SuperMotherShip.MISSILE_MAX, SuperMotherShip.FIRE_RATE, SuperMotherShip.RECHARGE_COOLDOWN, SuperMotherShip.LIFE, SuperMotherShip.KILL_SCORE, type);
		
		super.flamesMotorColor.add(Color.YELLOW);
		super.flamesMotorColor.add(Color.RED);
	}
	
	@Override
	public void draw (Graphics2D g)
	{
		g.setColor(super.color);
		
		g.drawLine(80, -12, 32, -32);
		g.drawLine(32, -32, -4, -32);
		g.drawLine(-4, -32, -16, -20);
		g.drawLine(-16, -20, -16, 20);
		g.drawLine(-16, 20, -4, 32);
		g.drawLine(-4, 32, 32, 32);
		g.drawLine(32, 32, 80, 12);
		g.drawLine(80, 12, 32, 24);
		g.drawLine(32, 24, -4, 24);
		g.drawLine(-4, 24, -8, 20);
		g.drawLine(-8, 20, -8, -20);
		g.drawLine(-8, -20, -4, -24);
		g.drawLine(-4, -24, 32, -24);
		g.drawLine(32, -24, 80, -12);
		g.drawLine(-16, -16, -20, -16);
		g.drawLine(-20, -16, -20, 16);
		g.drawLine(-20, 16, -16, 16);
		g.drawLine(-8, -4, -4, -4);
		g.drawLine(-4, -4, -4, 4);
		g.drawLine(-4, 4, -8, 4);

		g.setColor(WorldPanel.COLOR_DEFAULT);

		if (super.isMovePressed() && ((super.getAnimationFrame() % 6) < 3))
		{
			g.setColor(super.flamesMotorColor.get(0));
			g.drawLine(-20, -16, -25, 0);
			g.drawLine(-20, 16, -25, 0);
			g.drawLine(-21, -16, -26, 0);
			g.drawLine(-21, 16, -26, 0);
			
			g.setColor(super.flamesMotorColor.get(1));
			g.drawLine(-22, -16, -27, 0);
			g.drawLine(-22, 16, -27, 0);
			g.drawLine(-23, -16, -28, 0);
			g.drawLine(-23, 16, -28, 0);

			g.setColor(WorldPanel.COLOR_DEFAULT);
		}
	}
}