/*
 * Gregory Pellegrin
 * pellegrin.gregory.work@gmail.com
 */

package Game;

import Entity.Entity;
import Effect.Star;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JPanel;

public class WorldPanel extends JPanel
{
	public static final Color COLOR_DEFAULT = Color.WHITE;
	public static final int W_MAP_PIXEL = 900;
	public static final int H_MAP_PIXEL = 500;
	public static final int STAR_BACKGROUND_MAX = 60;
	
	private final ArrayList <Star> starBackground = new ArrayList <> ();
	private final Game game;
	
	private Font largeFont;
	private Font mediumFont;

	public WorldPanel (Game game)
	{
		this.game = game;

		this.setPreferredSize(new Dimension (WorldPanel.W_MAP_PIXEL, WorldPanel.H_MAP_PIXEL));
		this.setBackground(Color.BLACK);
		
		for (int i = 0; i < WorldPanel.STAR_BACKGROUND_MAX; i++)
			this.starBackground.add(new Star (Star.STAR_SPEED_SLOW));
		
		try
		{
			Font arcadeFont = Font.createFont(Font.TRUETYPE_FONT, new File ("ressources/arcadeClassic.ttf"));
			
			this.largeFont = arcadeFont.deriveFont(Font.PLAIN, 30);
			this.mediumFont = arcadeFont.deriveFont(Font.PLAIN, 25);
		}
		catch (FontFormatException | IOException e)
		{
			System.out.println(e.getMessage());
		}
	}
	
	private void drawEntity (Graphics2D g2d, Entity entity, double x, double y)
	{
		g2d.translate(x, y);
		
		double rotation = entity.getRotation();
		
		if (rotation != 0.0f)
			g2d.rotate(entity.getRotation());
		
		entity.draw(g2d);
	}
	
	@Override
	public void paintComponent (Graphics g)
	{
		super.paintComponent(g);
		
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setColor(WorldPanel.COLOR_DEFAULT);
		
		AffineTransform identity = g2d.getTransform();
		
		Iterator <Entity> iter = this.game.getEntities().iterator();
		while (iter.hasNext())
		{
			Entity entity = iter.next();
			
			this.drawEntity(g2d, entity, entity.getPosition().x, entity.getPosition().y);
			g2d.setTransform(identity);
		}
		
		g2d.setFont(this.largeFont);
		g2d.setColor(Color.RED);
		g2d.drawString("SCORE", 10, 25);

		g2d.setFont(this.mediumFont);
		g2d.setColor(Color.CYAN);
		g2d.drawString(String.valueOf(this.game.getScore()), 10, 50);

		g2d.setColor(WorldPanel.COLOR_DEFAULT);
		
		for (int i = 0; i < WorldPanel.STAR_BACKGROUND_MAX; i++)
		{
			this.starBackground.get(i).update();
			this.starBackground.get(i).draw(g2d);
		}
		
		g2d.setColor(WorldPanel.COLOR_DEFAULT);
	}
}