package model.world;
import java.awt.Point;
import java.util.SplittableRandom;
public class Cover implements Damageable{
	private int currentHP;
	private final Point location;
	public Cover(int x,int y)
	{
		this.location = new Point (x,y);
		SplittableRandom random = new SplittableRandom();
		this.currentHP = random.nextInt(100, 1000);
	}
	public int getCurrentHP() {
		return currentHP;
	}
	public void setCurrentHP(int currentHP) {
		this.currentHP = Math.max(currentHP, 0);
	}
	public Point getLocation() {
		return location;
	}
	public String toString() {
		return "Current Health Points of the cover: " + currentHP;
	}
	
}
