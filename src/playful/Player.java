package playful;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.particles.ParticleIO;
import org.newdawn.slick.particles.ParticleSystem;

public class Player extends Rectangle {
	private static final long serialVersionUID = 1L;
	int difficulty;
	float score;
	int health;
	int level;
	float speed, startSpeed;
	float startWidth;
	float multi, startMulti;
	Image image, startImage, walruswiz;
	float timeSurvived = 0;

	boolean cheating = false;
	boolean cheats[] = { false, false };

	Vector2f startPos;
	double spawnRate;
	int maxPenguins;

	public boolean bossed = false;
	public boolean killingBoss = false;
	public int killingBossTime = 0;
	private static Penguin bossPenguin;
	public ParticleSystem fire;
	private Sound killedBossSound;

	float bonusTime = 10000;
	int speedTime, stretchTime, multiTime;
	String multiText = "";
	int turtleTime;

	List<Fireball> fireballs, activeFireballs;
	int missedCannonballs;
	boolean timeLost;
	float timeLostCounter = 0;
	Image timeLostImage;

	float angle = 0;
	float spinRate;

	GameContainer gamecontainer;
	float inputDelta = 0;

	public Player(float x, float y, Image image, GameContainer gc) throws SlickException {
		super(x, y, image.getWidth(), image.getHeight());

		this.score = 0;
		this.health = 25;
		this.level = 0;
		this.speed = 1;
		this.startSpeed = 1;
		this.startWidth = this.width;
		this.multi = 1;
		this.startMulti = 1;

		this.gamecontainer = gc;

		this.image = image;
		this.startImage = image.copy();
		Image walruswiz2 = new Image("res/walruswiz2.png");
		this.walruswiz = walruswiz2.getScaledCopy(0.5F);
		this.walruswiz.setFilter(2);

		this.startPos = new Vector2f(x, y);

		this.speedTime = 10000;
		this.stretchTime = 10000;
		this.multiTime = 10000;
		this.turtleTime = 5000;
		this.fireballs = new ArrayList<Fireball>();
		this.activeFireballs = new ArrayList<Fireball>();

		this.timeLostImage = new Image("res/timeLostImg.png");

		killedBossSound = new Sound("res/bossKilled.wav");

		try {
			this.fire = ParticleIO.loadConfiguredSystem("res/bossSystem.xml");
		} catch (IOException e) {
			throw new SlickException("PLAYER - Failed to load particle system", e);
		}

	}

	public void reset() {
		this.score = 0.0f;
		this.level = 0;
		this.speed = 1.0f;

		this.image = this.startImage;

		setWidth(this.startWidth);
		setLocation(this.startPos);

		this.timeSurvived = 0.0f;
		this.speedTime = 10500;
		this.stretchTime = 10500;
		this.multiTime = 10500;
		this.turtleTime = 5500;
		this.fireballs.clear();
		this.activeFireballs.clear();

		this.bossed = false;

		this.inputDelta = 0.0f;
		this.angle = 0;
		this.spinRate = 0.25f;

		switch (this.difficulty) {
		case 0:
			this.health = 25;
			this.maxPenguins = 5;
			this.spawnRate = 2000;
			this.multi = 1.0f;
			this.startMulti = 1.0f;
			break;
		case 1:
			this.health = 15;
			this.maxPenguins = 10;
			this.spawnRate = 1500;
			this.multi = 1.0f;
			this.startMulti = 1.0f;
			break;
		case 2:
			this.health = 10;
			this.maxPenguins = 10;
			this.spawnRate = 1500;
			this.multi = 3.0f;
			this.startMulti = 3.0f;
			break;
		case 3:
			this.health = 5;
			this.maxPenguins = 15;
			this.spawnRate = 750;
			this.multi = 1.0f;
			this.startMulti = 1.0f;
			break;
		case 4:
			this.health = 10;
			this.maxPenguins = 10;
			this.spawnRate = 1500;
			this.multi = 1.0f;
			this.startMulti = 1.0f;
			break;
		}

		if (isCheatsOn(0)) {
			this.health = 25;
			this.maxPenguins = 3;
			this.spawnRate = 2500;
			this.multi = 1.0f;
			this.startMulti = 1.0f;
		}

		this.missedCannonballs = 0;
	}

	protected void setGameContainer(GameContainer gc) {
		this.gamecontainer = gc;
	}

	protected void setDifficulty(int diff) {
		this.difficulty = diff;
	}

	protected float getScore() {
		return this.score;
	}

	protected void setScore(float score) {
		this.score = score;
	}

	protected int getHealth() {
		return this.health;
	}

	protected void setHealth(int health) {
		this.health = health;
	}

	protected void subtractHealth(int subtract) {
		this.health -= subtract;
	}

	protected int getLevel() {
		return this.level;
	}

	protected void setLevel(int level) {
		this.level = level;
	}

	protected String getDifficulty(int diff) {
		if (isCheatsOn(0)) {
			return "Sandbox";
		} else {
			switch (diff) {
			case 0:
				return "Easy";
			case 1:
				return "Normal";
			case 2:
				return "Quick";
			case 3:
				return "Hardcore";
			case 4:
				return "Deathcore";
			}
			return "Zuh??";
		}
	}

	protected int getDifficulty(String diff) {
		if (diff.equals("Easy")) {
			return 0;
		} else if (diff.equals("Normal")) {
			return 1;
		} else if (diff.equals("Quick")) {
			return 2;
		} else if (diff.equals("Hardcore")) {
			return 3;
		} else if (diff.equals("Deathcore")) {
			return 4;
		} else {
			return -1;
		}
	}

	protected void setBonus(Bonus bonus) {
		if (bonus.getType().equals("Speed")) {
			this.speedTime = 0;
		} else if (bonus.getType().equals("Stretch")) {
			this.stretchTime = 0;
		} else if ((bonus.getType().equals("x2")) || (bonus.getType().equals("x4"))) {
			this.multiTime = 0;
			this.multiText = bonus.getType();
		} else if (bonus.getType().equals("Turtle")) {
			this.turtleTime = 0;
		}
	}

	public Penguin getBossPenguin() {
		return bossPenguin;
	}

	public void setBossPenguin(Entity ent) {
		bossPenguin = (Penguin) ent;
	}

	public boolean isCheatsOn(int index) {
		return cheats[index];
	}

	public void setCheatsOn(int index, boolean cheating) {
		this.cheats[index] = cheating;
		//		GameplayState.
	}

	protected void checkScore() throws SlickException {
		int startLevel = this.level;

		int newScore2 = (int) this.score / 50;
		this.level = newScore2;

		if (startLevel != this.level) {
			if (this.isCheatsOn(0)) {
				this.maxPenguins += 2;
			} else {
				this.maxPenguins += 5;
			}
			if (this.difficulty == 2)
				this.spawnRate *= 0.9300000000000001;
			else {
				this.spawnRate *= 0.97;
			}
			if (difficulty == 4) {
				spinRate *= 1.05f;
			}
			if (this.spawnRate < 20) {
				this.spawnRate = 20;
			}
			GameplayState.madeBonus = true;
			if ((this.level % 10 == 0) && (this.level > 0)) {
				this.bossed = true;
				for (Penguin p : GameplayState.penguins) {
					p.setBossed(true);
				}
				//				GameplayState.makeNewPenguin3("res/robotbosspenguin.png", 25, 20, this, this.gamecontainer);
				GameplayState.penguinLoader.makePenguin(true);
			}
		}
	}

	public void moveLeft(GameContainer gc, int delta) {
		setX(getX() - 1 * delta / (2.5F / this.speed));
		if (this.x < 5.0F) {
			setX(getX() + 1 * delta / (2.5F / this.speed));
		}
	}

	public void moveRight(GameContainer gc, int delta) {
		setX(getX() + 1 * delta / (2.5F / this.speed));
		if (this.x + this.width > gc.getWidth() - 4) {
			setX(getX() - 1 * delta / (2.5F / this.speed));
		}
	}

	public void render(GameContainer gc, Graphics g, AngelCodeFont font) {
		if (this.speedTime < 10500) {
			font.drawString(gc.getWidth() / 2 + 15, this.y + 10.0F, 10 - this.speedTime / 1000 + "", Color.green.brighter(2.0F));
		}
		if (this.stretchTime < 10500) {
			font.drawString(gc.getWidth() / 2 - 15, this.y + 10.0F, 10 - this.stretchTime / 1000 + "", Color.cyan.brighter());
		}
		if (this.multiTime < 10500) {
			font.drawString(gc.getWidth() / 2 + 45, this.y + 10.0F, 10 - this.multiTime / 1000 + "", Color.orange.brighter());
			if (this.multiTime < 10000) {
				if (this.multiTime / 150 % 2 == 1)
					font.drawString(gc.getWidth() / 2 - 5, this.y - 75.0F, this.multiText + " !!!", new Color(Color.orange.brighter().r, Color.red.brighter().g, Color.orange.brighter().b, 0.75F));
				else {
					font.drawString(gc.getWidth() / 2 - 5, this.y - 75.0F, this.multiText + " !!!", new Color(Color.orange.brighter().r, Color.orange.brighter().g, Color.orange.brighter().b, 0.75F));
				}
			}
		}
		if (this.turtleTime < 5500) {
			font.drawString(gc.getWidth() / 2 - 45, this.y + 10.0F, 5 - this.turtleTime / 1000 + "", Color.blue);
		}

		if (this.speedTime < 10000) {
			g.setColor(Color.green);
			this.walruswiz.draw(this.x, this.y - this.walruswiz.getHeight() + 5.0F);
			this.image.drawFlash(this.x, this.y, this.image.getWidth(), this.image.getHeight(), Color.green.darker(0.55F));
		} else {
			this.walruswiz.draw(this.x, this.y - this.walruswiz.getHeight() + 5.0F);
			this.image.draw(this.x, this.y);
		}

		if (this.fireballs.size() > 0) {
			g.setColor(Color.red);
			for (int f = 0; f < this.fireballs.size(); f++) {
				g.fillOval(this.x + f * 15, this.y + 10.0F, 10.0F, 10.0F);
			}
		}

		if (this.timeLost) {
			float posx = gc.getWidth() / 2 - this.timeLostImage.getWidth() / 2;
			float posy = gc.getHeight() / 2 + 50;
			this.timeLostImage.draw(posx, posy, 1.0F, new Color(1.0F, 1.0F, 1.0F, 100.0F));
			font.drawString(posx - 15.0F, posy + 40.0F, "- 5 seconds");
		}

		if (killingBoss) {
			//draw fire
			fire.render();
			if (killingBossTime < 1000) {
				fire.render(fire.getPositionX() + 25, fire.getPositionY() + 47);
			} else if (killingBossTime < 2000) {
				fire.render(fire.getPositionX() - 12, fire.getPositionY() - 25);
			} else {
				fire.render(fire.getPositionX() - 45, fire.getPositionY() + 30);
			}
		}
	}

	public void update(GameContainer gc, int delta) throws SlickException {

		if (killingBoss) {
			fire.update(delta);
			fire.setPosition(bossPenguin.pos.x + bossPenguin.baseImg.getWidth() * bossPenguin.baseImgScaleF / 2, bossPenguin.pos.y + bossPenguin.baseImg.getHeight() * bossPenguin.baseImgScaleF / 2);
			killingBossTime += delta;
			bossPenguin.baseImgScaleF += .03f;
			if (killingBossTime % 600 == 0) {
				killedBossSound.play();
			}
			if (killingBossTime >= 3000) {
				bossPenguin.baseImgScaleF = 0;
				this.bossed = false;
				killingBoss = false;
				killingBossTime = 0;
				GameplayState.removeEnts.add(bossPenguin);
			}
		} else {
			this.timeSurvived += delta;
		}

		checkScore();

		if (this.bossed) {
			this.multi = 0.5f;
		} else if (this.multi == 0.5d) {
			this.multi = this.startMulti;
		}

		if (this.speedTime < 11000) {
			this.speedTime += delta;
		}
		if (this.stretchTime < 11000) {
			this.stretchTime += delta;
		}
		if (this.multiTime < 11000) {
			this.multiTime += delta;
		}
		if (this.turtleTime < 6000) {
			this.turtleTime += delta;
		}

		if (this.speedTime == 10000) {
			this.speed = 1.0F;
		}
		if (this.stretchTime == 10000) {
			setWidth(this.startWidth);
			this.image = this.image.getScaledCopy(100, 10);
		}
		if (this.multiTime == 10000) {
			this.multi = this.startMulti;
			this.multiText = "";
		}
		if (this.turtleTime == 5000) {
			for (Penguin p : GameplayState.penguins) {
				p.setVelocity(new Vector2f(p.getVelocity().copy().x * 2.0F, p.getVelocity().copy().y * 2.0F));
				p.slowed = false;
			}
		}

		if (this.timeLost) {
			this.timeLostCounter += delta;
			if (this.timeLostCounter >= 2000.0f) {
				this.timeLost = false;
				this.timeLostCounter = 0.0F;
			}
		}
	}

	public void addFireball() throws SlickException {
		Fireball fireball = null;
		Animation animation = new Animation();
		SpriteSheet fireSheet = new SpriteSheet("res/fireball.png", 11, 20);

		for (int frame = 0; frame < 1; frame++) {
			animation.addFrame(fireSheet.getSprite(frame, 0).getScaledCopy(5.0F), 250);
		}
		animation.setPingPong(true);

		float posx = this.x;
		float posy = this.y;
		float velx = 0.0F;
		float vely = -5.0F;
		float accx = 0.0F;
		float accy = 0.0F;

		fireball = new Fireball("Fire", 1, animation, new Vector2f(posx, posy), new Vector2f(velx, vely), new Vector2f(accx, accy));

		fireball.setScale(5.0F);
		this.fireballs.add(fireball);
	}

	public void shootFireball() {
		Fireball fireball = (Fireball) this.fireballs.get(0);
		fireball.getPosition().x = (this.x + this.width / 2.0F);
		this.fireballs.remove(fireball);
		this.activeFireballs.add(fireball);
		fireball.active = true;
		GameplayState.ents.add(fireball);
	}
}
