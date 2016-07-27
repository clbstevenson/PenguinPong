package playful;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Vector2f;

public class PenguinLoader {

	SpriteSheet pSheet, pSheetBoss;
	private static Player player;
	private static List<Entity> ents;
	private static List<Penguin> penguins;
	static Map<Integer, Animation> animationMap;
	private static GameContainer gc;

	static Random rand;

	public PenguinLoader(String res, int imgw, int imgh, String res2, Player player, List<Entity> ents, List<Penguin> penguins, GameContainer gc) throws SlickException {
		pSheet = new SpriteSheet(res, imgw, imgh);
		pSheetBoss = new SpriteSheet(res2, 25, 20);
		setPlayer(player);
		setEnts(ents);
		setPenguins(penguins);
		setGc(gc);

		animationMap = new HashMap<Integer, Animation>(7);

		animationMap.put(1, new Animation(new Image[] { pSheet.getSprite(0, 0), pSheet.getSprite(1, 0), pSheet.getSprite(2, 0) }, 500, true));
		animationMap.put(11, new Animation(new Image[] { pSheet.getSprite(0, 0).getFlippedCopy(true, false), pSheet.getSprite(1, 0).getFlippedCopy(true, false), pSheet.getSprite(2, 0).getFlippedCopy(true, false) }, 500, true));
		animationMap.put(10, new Animation(new Image[] { pSheetBoss.getSprite(0, 0), pSheetBoss.getSprite(1, 0), pSheetBoss.getSprite(2, 0) }, 500, true));

		animationMap.get(1).setPingPong(true);
		animationMap.get(11).setPingPong(true);
		animationMap.get(10).setPingPong(true);

		animationMap.get(1).setAutoUpdate(true);
		animationMap.get(11).setAutoUpdate(true);
		animationMap.get(10).setAutoUpdate(true);

		rand = new Random();
	}

	public static void setPlayer(Player player) {
		PenguinLoader.player = player;
	}

	public static Player getPlayer() {
		return player;
	}

	public static void setGc(GameContainer gc) {
		PenguinLoader.gc = gc;
	}

	public static GameContainer getGc() {
		return gc;
	}

	public static void setEnts(List<Entity> ents) {
		PenguinLoader.ents = ents;
	}

	public static List<Entity> getEnts() {
		return ents;
	}

	public static void setPenguins(List<Penguin> penguins) {
		PenguinLoader.penguins = penguins;
	}

	public static List<Penguin> getPenguins() {
		return penguins;
	}

	public void makePenguin(boolean isABoss) throws SlickException {
		Penguin newPenguin = null;

		float posx = rand.nextInt(getGc().getWidth() - 100) + 20;
		float posy = 50.0f;
		float velx;
		float vely;

		int health = 0;

		if (isABoss) {
			health = 10;
			velx = 3.0f;
			vely = 0.0f;

			newPenguin = new Penguin("Boss", health, animationMap.get(10), new Vector2f(posx, posy), new Vector2f(velx, vely), new Vector2f(0, 0));
			Image baseImg = animationMap.get(10).getImage(1);
			newPenguin.setBaseImage(baseImg);
			newPenguin.setSpriteImage(baseImg.getScaledCopy(health));
		} else {
			boolean facingLeft = rand.nextBoolean();
			if (!getPlayer().bossed) {
				if (getPlayer().level == 0 || getPlayer().level == 1 || getPlayer().level == 2) {
					health = 2;
				} else if (getPlayer().level == 3 || getPlayer().level == 4 || getPlayer().level == 5) {
					health = rand.nextInt(2) + 2;
				} else if (getPlayer().level == 6 || getPlayer().level == 7 || getPlayer().level == 8) {
					health = rand.nextInt(2) + 1;
				} else {
					health = rand.nextInt(3) + 1;
				}
				velx = rand.nextInt(5) - 2;
				vely = rand.nextInt(4 / health) + 3;
			} else {
				health = rand.nextInt(2) + 1;
				velx = rand.nextInt(5) - 2;
				vely = rand.nextInt(4 / health) + 5;
			}
			if (facingLeft) {
				newPenguin = new Penguin("Normal", health, animationMap.get(1), new Vector2f(posx, posy), new Vector2f(velx, vely), new Vector2f(0, 0));
				Image baseImg = animationMap.get(1).getImage(1);
				newPenguin.setBaseImage(baseImg);
				newPenguin.setSpriteImage(baseImg.getScaledCopy(health));
			} else {
				newPenguin = new Penguin("Normal", health, animationMap.get(11), new Vector2f(posx, posy), new Vector2f(velx, vely), new Vector2f(0, 0));
				Image baseImg = animationMap.get(11).getImage(1);
				newPenguin.setBaseImage(baseImg);
				newPenguin.setSpriteImage(baseImg.getScaledCopy(health));
			}
		}

		getEnts().add(newPenguin);
		getPenguins().add(newPenguin);
	}
}
