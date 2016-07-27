package pong;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class GameplayState extends BasicGameState {

	AngelCodeFont font;
	AngelCodeFont font1;
	AngelCodeFont smallFont1;
	Image penguin;
	public static Image boom;
	Sound blip, bonusSound, fireballSound, nukeSound, cannonballSound, hitBossSound;
	//	Sound bonusSound;
	//	Sound fireballSound;
	//	Sound nukeSound;
	//	Sound cannonballSound;
	static Sound fellSound;
	Music music1;

	protected static List<Entity> ents;
	protected static List<Penguin> penguins;
	protected static List<Entity> removeEnts;
	Vector2f mapPos;
	private boolean paused = false;
	protected static boolean cheatPaused = false;

	static PenguinLoader penguinLoader;

	public static boolean madeBonus = false;
	static int difficulty;
	static Player player;
	private float angle = 0.0F;
	public static final int ID = 1;
	public static int inputDelta = 0;
	private static double makeEntityDelta = 1500.0D;
	private boolean bossIsShooting = false;
	private Penguin boss;
	private int missedcannonballs = 10;
	private int firedCannonballs = 0;
	
	Input input;

	String str = "hey";

	public int getID() {
		return 1;
	}

	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		this.font = new AngelCodeFont("res/pong/font2.fnt", "res/pong/font2_0.tga");
		this.font1 = new AngelCodeFont("res/pong/font1.fnt", "res/pong/font1_0.tga");
		this.smallFont1 = new AngelCodeFont("res/pong/smallFont1.fnt", "res/pong/smallFont1_0.tga");

		input = gc.getInput();
		
		ents = new ArrayList<Entity>();
		penguins = new ArrayList<Penguin>();
		removeEnts = new ArrayList<Entity>();

		boom = new Image("res/pong/boom.png");
		boom.setFilter(2);
		this.penguin = new Image("res/pong/penguin.png");
		this.penguin.setFilter(2);

		this.blip = new Sound("res/pong/blip2.wav");
		this.bonusSound = new Sound("res/pong/coinbonus.wav");
		this.fireballSound = new Sound("res/pong/fireball.wav");
		this.nukeSound = new Sound("res/pong/nuke.wav");
		this.cannonballSound = new Sound("res/pong/cannonballSound2.wav");
		this.hitBossSound = new Sound("res/pong/hitBoss.wav");
		fellSound = new Sound("res/pong/penguinfell.wav");
		this.music1 = new Music("res/pong/penguinmusic1.wav");

		this.mapPos = new Vector2f(0.0F, 0.0F);

		Image playerImage = new Image("res/pong/iceblock.png");

		player = new Player(gc.getWidth() / 2 - 50, gc.getHeight() - 35, playerImage, gc);

		penguinLoader = new PenguinLoader("res/pong/robotpenguin.png", 20, 20, "res/pong/robotbosspenguin.png", player, ents, penguins, gc);
	}

	public void enter(GameContainer gc, StateBasedGame sbg) throws SlickException {
		input.clearKeyPressedRecord();

		ents.clear();
		penguins.clear();
		removeEnts.clear();

		this.angle = 0;

		makeNewBonus2(gc);

		inputDelta = 0;
		this.paused = false;
		gc.resume();

		firedCannonballs = 0;
		missedcannonballs = 0;

		player.setDifficulty(difficulty);
		player.reset();
		makeEntityDelta = 1500;

		if (player.isCheatsOn(0)) {
			cheatPaused = true;
		} else {
			cheatPaused = false;
		}
		player.setCheatsOn(1, false);
	}

	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {

		if (!player.bossed)
			g.setColor(Color.cyan.darker());
		else {
			g.setColor(Color.red.darker());
		}
		g.fillRect(0, 0, gc.getWidth(), gc.getHeight());
		
		if (cheatPaused) {
			g.setColor(Color.orange.darker(0.25f));
			Rectangle cheatPauser = new Rectangle(gc.getWidth() / 2 - 70, gc.getHeight() / 2 - 50, 140, 100);
			g.fill(cheatPauser);

			this.smallFont1.drawString(cheatPauser.getMinX() + 5, cheatPauser.getMinY() + 10, "Before you start,\n\nRead the cheats\n\nIn the top left.\n\n(SPACE to start)");
		}

		if (this.paused) {
			g.setColor(Color.magenta.darker(0.25f));
			Rectangle pauser = new Rectangle(gc.getWidth() / 2 - 75, gc.getHeight() / 2 - 100, 150.0F, 200.0F);
			g.fill(pauser);

			this.font1.drawString(pauser.getCenterX() - 30, pauser.getMinY() + 5, "PAUSED", Color.white);
			this.smallFont1.drawString(pauser.getCenterX() - 65, pauser.getY() + 50, "To RESUME, Press \n [P] or [ENTER]\n\nTo RESTART,\n  Press [R]\n\nFor Main Menu, \n Press [M]\n\nTo QUIT, Press \n [Q] or [Esc]", Color.white);
		}

		if (player.difficulty == 4 || player.isCheatsOn(1)) {
			g.scale(0.9f, 0.9f);
			g.rotate(gc.getWidth() / 2, gc.getHeight() / 2, player.angle);
			g.setColor(Color.black);
			g.drawRect(0, 0, gc.getWidth(), gc.getHeight());
		}

		g.setColor(Color.orange);

		for (Entity ent : ents) {
			ent.render(g, this.smallFont1, this.mapPos);
		}

		if (player.isCheatsOn(0)) {
			this.smallFont1.drawString(10, 50, "Press 'Z' to make Bonuses.");
			this.smallFont1.drawString(10, 50 + font1.getLineHeight(), "Hold 'X' to spawn Penguins.");
			this.smallFont1.drawString(10, 50 + 2 * font1.getLineHeight(), "Press 'C' to level up ONCE.");
			this.smallFont1.drawString(10, 50 + 3 * font1.getLineHeight(), "Press 'V' to level up 5 times.");
			this.smallFont1.drawString(10, 50 + 4 * font1.getLineHeight(), "Press 'B' for 25 health.");
			this.smallFont1.drawString(10, 50 + 5 * font1.getLineHeight(), "Press 'S' to turn on spinning.");
		}

		g.setColor(Color.darkGray);
		g.fillRect(gc.getWidth() / 2 - 115, 2.0F, 250.0F, this.font.getLineHeight() + 5);
		this.font1.drawString(gc.getWidth() / 2 - 110, 5.0F, "Life = " + player.getHealth() + "   Score = " + (int) player.getScore(), Color.yellow);
		this.font1.drawString(gc.getWidth() / 2 - 35, 35.0F, "Level " + player.getLevel(), Color.orange);
		this.font1.drawString(gc.getWidth() - 90, 10.0F, player.getDifficulty(player.difficulty));
		this.font1.drawString(gc.getWidth() - 135, 10 + this.font1.getLineHeight(), "Time: " + player.timeSurvived / 1000.0F);

		player.render(gc, g, this.font1);

		g.setColor(Color.white);

	}

	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		
		inputDelta -= delta;
		makeEntityDelta -= delta;

		this.angle = ((float) (this.angle + 8.0D * delta / 100.0D));

		if (!gc.hasFocus()) {
			this.paused = true;
			gc.pause();
			for (Entity ent : ents) {
				ent.getAnimation().stop();
			}
		}

		if (player.getHealth() <= 0) {
			LostGameState.setPlayer(player);
			sbg.enterState(2);
		}

		if ((player.difficulty == 4 || player.isCheatsOn(1)) && !paused) {
			player.angle += player.spinRate;
		}

		for (Entity ent : ents) {
			if (!cheatPaused) {
				ent.update(gc, delta);
			}

			if (!ent.isAlive()) {
				removeEnts.add(ent);
				break;
			}
			if ((ent.getType().equals("Boss")) && (((Penguin) ent).bossShootTimer >= 2500)) {
				this.bossIsShooting = true;
				this.boss = ((Penguin) ent);
				((Penguin) ent).bossShootTimer = 0;
			}

			if (((ent instanceof Penguin)) && (((Penguin) ent).isSetFell())) {
				player.subtractHealth(1);
				((Penguin) ent).setIsSetFell(false);
			}

			if ((ent.getType().equals("Boss Fire")) && (ent.getShape().intersects(player))) {
				ent.setPosition(new Vector2f(ent.getPosition().x, player.getY() - ent.getShape().getHeight() - 1.0F));
				ent.setVelocity(new Vector2f(ent.getVelocity().copy().x, ent.getVelocity().copy().y * -1.0F));
				player.activeFireballs.add((Fireball) ent);
				//				cannonballSound.play(1.0f, 1.f);
				//				cannonballSound.play();
				cannonballSound.play(1.0f, 0.5f);
			}

			if (ent.getType().equals("Boss Fire") && firedCannonballs == -1) {
				((Fireball) ent).active = false;
				removeEnts.add(ent);
			}

			if ((ent instanceof Penguin)) {
				for (Fireball f : player.activeFireballs) {
					if ((f.active) && (ent.getShape().intersects(f.getShape()))) {
						if (ent.getType().equals("Boss")) {
							ent.setHealth(ent.getHealth() - 1);
							hitBossSound.play();
							removeEnts.add(f);
							if (ent.getHealth() <= 0) {
								player.killingBoss = true;
								player.setBossPenguin(ent);
								player.fire.setPosition(ent.getPosition().x + 20, ent.getPosition().y + 10);
								//								player.bossed = false;
								this.firedCannonballs = -1;
								//								removeEnts.add(ent);
								//								makeEntityDelta = player.spawnRate;
								//								for (Fireball fire : player.activeFireballs) {
								//									System.out.println("removed?");
								//									fire.active = false;
								//									removeEnts.add(fire);
								//								}
							}
						} else if (f.getType().equals("Fire")) {
							ent.setFell(true);
							((Penguin) ent).setIsSetFell(false);
							removeEnts.add(ent);
							if (!player.bossed)
								player.score += 1 * ent.getStartHealth();
							else {
								player.score += 1 * ent.getStartHealth() * player.multi;
							}
						}
					}
				}
			}

			if ((ent.getPosition().y > gc.getHeight() - 200) && (ent.getShape().intersects(player))) {
				if (((ent instanceof Penguin)) && (!ent.isFell())) {
					ent.setHealth(ent.getHealth() - 1);

					this.blip.play(1.0F, 0.6F);
					if (ent.getHealth() <= 0) {
						ent.setCaught(true);
						ent.setPosition(new Vector2f(ent.getPosition().x, player.getY() - ent.getShape().getHeight() - 1.0F));
						player.score += 2 * ent.getStartHealth() * player.multi;
					} else {
						player.score += 1.0F * player.multi;

						ent.setPosition(new Vector2f(ent.getPosition().x, player.getY() - ent.getShape().getHeight() - 1.0F));

						ent.setVelocity(new Vector2f(ent.getVelocity().copy().x, ent.getVelocity().copy().y * -1.0F));
					}
				} else if ((ent instanceof Bonus)) {
					if (ent.getType().equals("Nuke"))
						this.nukeSound.play(1.0F, 0.75F);
					else {
						this.bonusSound.play(1.0F, 0.85F);
					}
					ent.setHealth(ent.getHealth() - 1);
					ent.setCaught(true);
					((Bonus) ent).doBonusEffect(player);
					ent.setPosition(new Vector2f(ent.getPosition().x, player.getY() - ent.getShape().getHeight() - 1.0F));
				}
			}
		}

		if (this.bossIsShooting) {
			shootFireball(this.boss);
			this.firedCannonballs += 1;
			this.bossIsShooting = false;
		}

		if ((player.bossed) && (this.firedCannonballs >= 15)) {
			if (makeEntityDelta < 0) {
				makeNewPenguin2(player, gc);
				makeEntityDelta = 400;
				this.missedcannonballs -= 1;
			}
			if (this.missedcannonballs <= 0) {
				player.missedCannonballs = 0;
				this.firedCannonballs = 0;
				this.missedcannonballs = 10;
			}
		}

		if (removeEnts.size() > 0) {
			ents.removeAll(removeEnts);
			penguins.removeAll(removeEnts);
			player.activeFireballs.removeAll(removeEnts);
		}

		if (!cheatPaused) {

			if (madeBonus) {
				makeNewBonus2(gc);
				madeBonus = false;
			}

			if (!player.bossed) {
				if ((penguins.size() < player.maxPenguins) && (makeEntityDelta < 0)) {
					//TODO:				makeNewPenguin3("res/robotpenguin.png", 20, 20, player, gc);
					penguinLoader.makePenguin(false);
					makeEntityDelta = player.spawnRate;
				}

			} else {
				if (player.difficulty != player.getDifficulty("Hardcore")) {
					if ((penguins.size() < 6 * (player.level / 10)) && (makeEntityDelta < 0)) {
						penguinLoader.makePenguin(false);
						makeEntityDelta = player.spawnRate * 3;
						if (makeEntityDelta < 50) {
							makeEntityDelta = 50;
						}
					}
				} else {
					if ((penguins.size() < 10 * (player.level / 10)) && (makeEntityDelta < 0)) {
						penguinLoader.makePenguin(false);
						makeEntityDelta = player.spawnRate * 3;
						if (makeEntityDelta < 50) {
							makeEntityDelta = 50;
						}
					}
				}
			}
		}

		//		if ((penguins.size() < 6 * (player.level / 10)) && (makeEntityDelta < 0)) {
		//
		//			//			makeNewPenguin2(player, gc);
		//			penguinLoader.makePenguin(false);
		//			if (player.difficulty == player.getDifficulty("Hardcore")) {
		//				penguinLoader.makePenguin(false);
		//			}
		//
		//			makeEntityDelta = player.spawnRate * 3;
		//			if (makeEntityDelta < 50) {
		//				makeEntityDelta = 50;
		//			}
		//
		//		}

		if (this.paused) {
			if ((input.isKeyPressed(Input.KEY_ESCAPE)) || (input.isKeyPressed(Input.KEY_Q))) {
				gc.exit();
			}
			if (input.isKeyPressed(Input.KEY_M)) {
				sbg.enterState(0);
				for (Entity ent : ents) {
					ent.getAnimation().start();
				}
			}
			if (input.isKeyPressed(Input.KEY_R)) {
				sbg.enterState(1);
				for (Entity ent : ents) {
					ent.getAnimation().start();
				}
			}
			if ((input.isKeyPressed(25)) || (input.isKeyPressed(Input.KEY_ENTER))) {
				input.clearKeyPressedRecord();
				gc.resume();
				for (Entity ent : ents) {
					ent.getAnimation().start();
				}

				this.paused = false;
			}
		} else if (cheatPaused) {
			//			if (inputDelta < 0) {
			if (input.isKeyPressed(Input.KEY_SPACE)) {
				cheatPaused = false;
			}
			//				if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
			//					cheatPaused = false;
			//					inputDelta = 10;
			//				}
			//			}
		} else {
			player.update(gc, delta);
			if (input.isKeyPressed(Input.KEY_ENTER)) {
				sbg.enterState(getID());
			}

			if ((input.isKeyPressed(Input.KEY_P)) || (input.isKeyPressed(Input.KEY_ESCAPE))) {
				input.clearKeyPressedRecord();
				gc.pause();
				for (Entity ent : ents) {
					ent.getAnimation().stop();
				}
				this.paused = true;
			}

			if (inputDelta < 0) {
				if (player.isCheatsOn(0)) {
					if (input.isKeyPressed(Input.KEY_Z)) {
						for (int i = 0; i < 5; i++) {
							makeNewBonus2(gc);
						}
					}
					if (input.isKeyDown(Input.KEY_X)) {
						//TODO:					makeNewPenguin3("res/robotpenguin.png", 20, 20, player, gc);
						penguinLoader.makePenguin(false);
						inputDelta = 10;
					}
					if (input.isKeyPressed(Input.KEY_C)) {
						player.score += 50;
						player.checkScore();
					}
					if (input.isKeyPressed(Input.KEY_V)) {
						System.out.println("leveld");
						player.score += 250.0f;
						player.checkScore();
					}
					if (input.isKeyPressed(Input.KEY_B)) {
						player.health += 25;
					}
					if (input.isKeyPressed(Input.KEY_S)) {
						player.setCheatsOn(1, !player.isCheatsOn(1));
						player.angle = 0;
					}
				}

				if (input.isKeyDown(Input.KEY_LEFT)) {
					player.moveLeft(gc, delta);
				}
				if (input.isKeyDown(Input.KEY_RIGHT)) {
					player.moveRight(gc, delta);
				}
				if (((input.isKeyPressed(Input.KEY_UP)) || (input.isKeyPressed(Input.KEY_SPACE))) && (player.fireballs.size() > 0)) {
					this.fireballSound.play(1.0f, 1.0f);
					player.shootFireball();
				}
			}
		}
	}

	public List<Entity> getEnts() {
		return ents;
	}

	public List<Penguin> getPenguins() {
		return penguins;
	}

	public static void makeNewPenguin2(Player player, GameContainer gc) throws SlickException {
		Entity newEntity = null;
		Animation animation = new Animation();
		SpriteSheet sheet = new SpriteSheet("res/pong/robotpenguin.png", 20, 20);

		int health = 0;
		switch (player.level) {
		case 0:
			health = new Random().nextInt(1) + 2;
			break;
		case 1:
			health = new Random().nextInt(1) + 2;
			break;
		case 2:
			health = new Random().nextInt(2) + 2;
			break;
		case 3:
			health = new Random().nextInt(2) + 2;
			break;
		case 4:
			health = new Random().nextInt(2) + 1;
			break;
		case 5:
			health = new Random().nextInt(2) + 1;
			break;
		default:
			health = new Random().nextInt(3) + 1;
		}

		health = new Random().nextInt(2) + 1;

		boolean facingleft = new Random().nextBoolean();

		for (int frame = 0; frame < 3; frame++) {
			if (facingleft)
				animation.addFrame(sheet.getSprite(frame, 0).getScaledCopy(health + 1).getFlippedCopy(true, false), 1000);
			else {
				animation.addFrame(sheet.getSprite(frame, 0).getScaledCopy(health + 1), 1000);
			}
		}
		animation.setPingPong(true);
		Image baseImg = sheet.getSprite(1, 0);

		float posx = new Random().nextInt(gc.getWidth() - 100) + 20;
		float posy = 50.0F;

		boolean slowed = false;
		float velx;
		float vely;

		//		if (player.turtleTime < 5000) {
		//			float velx = new Random().nextInt(5) - 2;
		//			velx /= 2.0F;
		//			float vely = new Random().nextInt(6 / health) + health;
		//			if (vely % 2.0F == 1.0F) {
		//				vely += 1.0F;
		//			}
		//			vely /= 2.0F;
		//			slowed = true;
		//		} else {
		velx = new Random().nextInt(5) - 2;
		//			float vely;
		if (!player.bossed)
			vely = new Random().nextInt(4 / health) + 3;
		else {
			vely = new Random().nextInt(3 / health) + 6;
		}
		if (vely % 2.0F == 1.0F) {
			vely += 1.0F;
		}
		vely /= health;
		slowed = false;
		//		}

		float accx = 0.0F;
		float accy = 0.0F;

		newEntity = new Penguin("Normal", health, animation, new Vector2f(posx, posy), new Vector2f(velx, vely), new Vector2f(accx, accy));
		((Penguin) newEntity).slowed = slowed;
		((Penguin) newEntity).setBaseImage(baseImg);

		ents.add(newEntity);
		penguins.add((Penguin) newEntity);
	}

	public static void makeNewPenguin3(String resource, int imgw, int imgh, Player player, GameContainer gc) throws SlickException {
		Entity newEntity = null;
		Animation animation = new Animation();
		SpriteSheet sheet = new SpriteSheet(resource, imgw, imgh);

		int health = 0;
		if (!player.bossed)
			switch (player.level) {
			case 0:
				health = new Random().nextInt(1) + 2;
				break;
			case 1:
				health = new Random().nextInt(1) + 2;
				break;
			case 2:
				health = new Random().nextInt(2) + 2;
				break;
			case 3:
				health = new Random().nextInt(2) + 2;
				break;
			case 4:
				health = new Random().nextInt(2) + 1;
				break;
			case 5:
				health = new Random().nextInt(2) + 1;
				break;
			default:
				health = new Random().nextInt(3) + 1;
				break;
			}
		else {
			health = 10;
		}

		boolean facingleft = new Random().nextBoolean();

		for (int frame = 0; frame < 3; frame++) {
			if (facingleft)
				animation.addFrame(sheet.getSprite(frame, 0).getScaledCopy(health + 1).getFlippedCopy(true, false), 1000);
			else {
				animation.addFrame(sheet.getSprite(frame, 0).getScaledCopy(health + 1), 1000);
			}
		}
		animation.setPingPong(true);
		Image baseImg = sheet.getSprite(1, 0);

		float posx = new Random().nextInt(gc.getWidth() - 100) + 20;
		float posy = 50.0F;

		boolean slowed = false;
		float velx;
		float vely;
		if (!player.bossed) {
			velx = new Random().nextInt(5) - 2;
			vely = new Random().nextInt(4 / health) + 3;
			if (vely % 2.0f == 1.0f) {
				vely += 1.0f;
			}
			slowed = false;
		} else {
			velx = 3.0f;
			vely = 0;
		}
		float accx = 0.0F;
		float accy = 0.0F;

		if (!player.bossed)
			newEntity = new Penguin("Normal", health, animation, new Vector2f(posx, posy), new Vector2f(velx, vely), new Vector2f(accx, accy));
		else {
			newEntity = new Penguin("Boss", health, animation, new Vector2f(posx, posy), new Vector2f(velx, vely), new Vector2f(accx, accy));
		}
		((Penguin) newEntity).slowed = slowed;
		((Penguin) newEntity).setBaseImage(baseImg);

		ents.add(newEntity);
		penguins.add((Penguin) newEntity);
	}

	public static void makeNewBonus2(GameContainer gc) throws SlickException {
		Entity newEntity = null;
		Animation animation = new Animation();
		SpriteSheet sheet = new SpriteSheet("res/pong/bonuses.png", 10, 10);

		int typeInteger = new Random().nextInt(8);
		//		typeInteger = 0;

		for (int frame = 0; frame < 3; frame++) {
			animation.addFrame(sheet.getSprite(frame, typeInteger).getScaledCopy(2.0F), 300);
		}

		animation.setPingPong(true);

		float posx = new Random().nextInt(gc.getWidth() - 100) + 20;
		float posy = 50.0F;
		float velx = 0.0F;
		float vely = 4.0F;
		float accx = 0.0F;
		float accy = 0.0F;

		newEntity = new Bonus(typeInteger, 1, animation, new Vector2f(posx, posy), new Vector2f(velx, vely), new Vector2f(accx, accy));

		ents.add(newEntity);
	}

	public static void shootFireball(Penguin p) throws SlickException {
		Fireball fireball = null;
		Animation animation = new Animation();
		SpriteSheet fireSheet = new SpriteSheet("res/pong/bowlingball.png", 40, 40);

		for (int frame = 0; frame < 1; frame++) {
			animation.addFrame(fireSheet.getSprite(frame, 0), 750);
		}

		animation.setPingPong(true);

		float posx = p.pos.x + p.getBaseImage().getWidth() * (p.getHealth() + 1) / 2 - 25.0F;
		float posy = p.pos.y + p.getBaseImage().getHeight() * (p.getHealth() + 1) / 2;
		float velx = (float) Math.random() * 10.0F - 5.0F;
		float vely = 5.0F;
		float accx = 0.0F;
		float accy = 0.0F;

		fireball = new Fireball("Boss Fire", 1, animation, new Vector2f(posx, posy), new Vector2f(velx, vely), new Vector2f(accx, accy));

		fireball.setScale(1.0F);

		fireball.active = true;
		ents.add(fireball);
	}

	public static void shootMultipleFireball(int n, Penguin p) throws SlickException {
		Animation animation = new Animation();
		SpriteSheet fireSheet = new SpriteSheet("res/pong/bowlingball.png", 40, 40);

		for (int frame = 0; frame < 1; frame++) {
			animation.addFrame(fireSheet.getSprite(frame, 0), 750);
		}

		animation.setPingPong(true);

		float posx = 0.0f;
		float posy = p.pos.y;
		float velx = 0.0f;
		float vely = 5.0f;
		float accx = 0.0f;
		float accy = 0.0f;

		List<Fireball> newfireballs = new ArrayList<Fireball>(n);
		Fireball fball;
		for (int i = 0; i < n; i++) {
			posx = p.pos.x - 40 * (n / 2) + 40 * i;
			fball = new Fireball("Boss Fire", 1, animation, new Vector2f(posx, posy), new Vector2f(velx, vely), new Vector2f(accx, accy));
			ents.add(fball);
			fball.active = true;
			newfireballs.add(fball);
		}

		System.out.println(newfireballs.size());

		for (Fireball f : newfireballs) {
			f.active = true;
			ents.add(f);
		}
	}

	public static void setDifficulty(int diff) {
		difficulty = diff;
		player.setCheatsOn(0, false);
	}
}