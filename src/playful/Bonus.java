package playful;

import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

public class Bonus implements Entity {
	String type;
	Image spriteImg;
	Animation animation;
	Vector2f pos;
	Vector2f startPos;
	Vector2f basePos;
	Vector2f vel;
	Vector2f startVel;
	Vector2f acc;
	Vector2f startAcc;
	public int health;
	protected int startHealth;
	protected boolean alive;
	protected boolean fell = false;
	protected boolean caught = false;

	private int caughtMovement = 0;

	float inputDelta = 0.0F;
	float caughtScale = 1.0F;

	public Bonus(String type, int health, Animation animation, Vector2f startPosition, Vector2f startVelocity, Vector2f startAcceleration) {
		this.type = type;
		this.health = health;
		this.startHealth = health;
		this.animation = animation;

		this.pos = startPosition.copy();
		this.startPos = this.pos.copy();
		this.vel = startVelocity.copy();
		this.startVel = this.vel.copy();
		this.acc = startAcceleration.copy();
		this.startAcc = this.acc.copy();
		try {
			setSpriteImage(animation.getImage(0));
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	public Bonus(int typeInteger, int health, Animation animation, Vector2f startPosition, Vector2f startVelocity, Vector2f startAcceleration) {
		this.health = health;
		this.startHealth = health;
		this.animation = animation;

		switch (typeInteger) {
		case 0:
			setType("Fire");
			break;
		case 1:
			setType("Health");
			break;
		case 2:
			setType("Speed");
			break;
		case 3:
			setType("Nuke");
			break;
		case 4:
			setType("Stretch");
			break;
		case 5:
			setType("Rift");
			break;
		case 6:
			setType("x2");
			break;
		case 7:
			setType("x4");
			break;
		case 8:
			setType("Turtle");
			break;
		case 9:
		}

		this.pos = startPosition.copy();
		this.startPos = this.pos.copy();
		this.vel = startVelocity.copy();
		this.startVel = this.vel.copy();
		this.acc = startAcceleration.copy();
		this.startAcc = this.acc.copy();
		try {
			setSpriteImage(animation.getImage(0));
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	public Shape getShape() {
		return new Circle(this.pos.x + this.spriteImg.getWidth() / 2, this.pos.y + this.spriteImg.getHeight() / 2, this.spriteImg.getWidth());
	}

	public boolean isAlive() {
		if ((!isFell()) && (!isCaught())) {
			return true;
		}
		return false;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	public boolean isFell() {
		return this.fell;
	}

	public void setFell(boolean fell) {
		this.fell = fell;
	}

	public boolean isCaught() {
		return this.caught;
	}

	public void setCaught(boolean caught) {
		this.caught = caught;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getHealth() {
		return this.health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public int getStartHealth() {
		return this.startHealth;
	}

	public void setStartHealth(int startHealth) {
		this.startHealth = startHealth;
	}

	public Vector2f getPosition() {
		return this.pos;
	}

	public void setPosition(Vector2f position) {
		this.pos = position.copy();
	}

	public void setBasePosition(Vector2f basePosition) {
		this.basePos = basePosition.copy();
	}

	public Vector2f getVelocity() {
		return this.vel;
	}

	public void setVelocity(Vector2f velocity) {
		this.vel = velocity.copy();
	}

	public Vector2f getAcceleration() {
		return this.acc;
	}

	public void setAcceleration(Vector2f acceleration) {
		this.acc = acceleration.copy();
	}

	public Animation getAnimation() {
		return this.animation;
	}

	public void setAnimation(Animation animation) {
		this.animation = animation;
	}

	private void setSpriteImage(Image image) throws SlickException {
		this.spriteImg = image;
		this.spriteImg.setFilter(2);
	}

	public Image getSpriteImage() {
		return this.spriteImg;
	}

	public void render(Graphics g, AngelCodeFont font, Vector2f updatePosition) {
		if (this.health > 0) {
			this.animation.draw(this.pos.x, this.pos.y);
		} else if (this.caught) {
			this.spriteImg.setAlpha(1.0F / (1.5F * this.caughtScale));
			this.spriteImg.draw(this.pos.x, this.pos.y, this.caughtScale);
		}
	}

	public void update(GameContainer gc, int delta) {
		this.inputDelta -= delta;

		if (this.health > 0) {
			if (!this.fell) {
				this.animation.update(delta);
			} else {
				setFell(false);
				GameplayState.removeEnts.add(this);
			}
		} else if ((this.caught) && (this.inputDelta < 0.0F)) {
			this.caughtMovement += 1;
			this.caughtScale += 0.1F;
			this.inputDelta = 50.0F;
			if (this.caughtMovement >= 20) {
				setCaught(false);
				this.caughtMovement = 0;
				GameplayState.removeEnts.add(this);
			}

		}

		if (!this.fell) {
			if (this.inputDelta < 0.0F) {
				this.pos.add(this.vel);
				this.vel.add(this.acc);
				this.inputDelta = 50.0F;
			}

			if (this.pos.x + this.animation.getWidth() >= gc.getWidth()) {
				this.vel.x *= -1.0F;
			}
			if (this.pos.x <= 0.0F) {
				this.vel.x *= -1.0F;
			}

			if (this.pos.y + 15.0F > gc.getHeight()) {
				setFell(true);
			}
			if (this.pos.y < 0.0F)
				this.vel.y *= -1.0F;
		}
	}

	public void reset() {
		this.acc = this.startAcc.copy();
		this.vel = this.startVel.copy();
		this.pos = this.startPos.copy();
	}

	public void doBonusEffect(Player player) throws SlickException {
		if (this.type.equals("Fire")) {
			player.addFireball();
		} else if (this.type.equals("Health")) {
			player.health += 1;
		} else if (this.type.equals("Speed")) {
			player.speed = (player.startSpeed * 2.0F);
			player.setBonus(this);
		} else if (this.type.equals("Nuke")) {
			for (Penguin p : GameplayState.penguins) {
				if (!p.getType().equals("Boss")) {
					p.setFell(true);
					p.setIsSetFell(false);

					player.score += 1.0F;
				}
			}
			player.checkScore();
		} else if (this.type.equals("Stretch")) {
			player.setWidth(2.0F * player.startWidth);
			player.image = player.image.getScaledCopy(200, 10);
			player.setBonus(this);
		} else if (this.type.equals("Rift")) {
			for (Penguin p : GameplayState.penguins)
				p.setVelocity(new Vector2f(p.getVelocity().copy().x * -1.0F, p.getVelocity().copy().y * -1.0F));
		} else if (this.type.equals("x2")) {
			player.multi = (player.startMulti * 2.0F);
			player.setBonus(this);
		} else if (this.type.equals("x4")) {
			player.multi = (player.startMulti * 4.0F);
			player.setBonus(this);
		} else if (this.type.equals("Turtle")) {
			for (Penguin p : GameplayState.penguins) {
				if (!p.slowed) {
					p.setVelocity(new Vector2f(p.getVelocity().copy().x / 2.0F, p.getVelocity().copy().y / 2.0F));
					p.slowed = true;
				}
			}

			player.setBonus(this);
		}
	}
}
