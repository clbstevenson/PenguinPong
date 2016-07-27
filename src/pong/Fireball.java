package pong;

import java.io.IOException;

import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.particles.ParticleIO;
import org.newdawn.slick.particles.ParticleSystem;

public class Fireball implements Entity {
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
	protected boolean active = false;
	
	private Sound missedCannonballSound;

	private int caughtMovement = 0;
	private float scale;
	private ParticleSystem fire;
	float inputDelta = 0.0F;

	public Fireball(String type, int health, Animation animation, Vector2f startPosition, Vector2f startVelocity, Vector2f startAcceleration) throws SlickException {
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
		try {
			this.fire = ParticleIO.loadConfiguredSystem("res/pong/system.xml");
		} catch (IOException e) {
			throw new SlickException("FIREBALL - Failed to load particle system", e);
		}
		
		missedCannonballSound = new Sound("res/pong/missedCannonball.wav");
	}

	public Shape getShape() {
		if (getType().equals("Boss Fire")) {
			return new Circle(this.pos.x + 19.0F * this.scale, this.pos.y + 19.0F * this.scale, 20.0F * this.scale);
		}
		float[] points = { this.pos.x + 2.0F * this.scale, this.pos.y - 1.0F * this.scale, this.pos.x + 8.0F * this.scale, this.pos.y - 1.0F * this.scale, this.pos.x + 11.0F * this.scale, this.pos.y + 3.0F * this.scale, this.pos.x + 11.0F * this.scale, this.pos.y + 10.0F * this.scale,
				this.pos.x + 7.0F * this.scale, this.pos.y + 21.0F * this.scale, this.pos.x + 4.0F * this.scale, this.pos.y + 21.0F * this.scale, this.pos.x + 0.0F, this.pos.y + 10.0F * this.scale, this.pos.x + 0.0F, this.pos.y + 3.0F * this.scale };
		return new Polygon(points);
	}

	public void setScale(float scale) {
		this.scale = scale;
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
		if ((this.active) && (this.health > 0)) {
			if (!getType().equals("Boss Fire")) {
				this.fire.setPosition(this.pos.x + 19.0f, this.pos.y + 15.0f);
				this.fire.render();
			}
			this.animation.draw(this.pos.x, this.pos.y);
		}
	}

	public void update(GameContainer gc, int delta) {
		this.inputDelta -= delta;

		if (this.active) {
			this.fire.update(delta);
			if (this.health > 0) {
				if (!this.fell) {
					this.animation.update(delta);
				} else {
					setFell(false);
					GameplayState.removeEnts.add(this);
				}
			} else if ((this.caught) && (this.inputDelta < 0.0F)) {
				this.caughtMovement += 1;
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
					while (this.pos.x + this.animation.getWidth() >= gc.getWidth()) {
						this.pos.x -= 1.0F;
					}
				}
				if (this.pos.x <= 0.0F) {
					this.vel.x *= -1.0F;
					while (this.pos.x <= 0.0F) {
						this.pos.x += 1.0F;
					}
				}
				if (this.pos.y + this.spriteImg.getWidth() / 2 < 0.0F) {
					setFell(true);
					this.active = false;
					GameplayState.removeEnts.add(this);
				}
				if (this.pos.y + 35.0f > gc.getHeight()) {
					setFell(true);
					GameplayState.removeEnts.add(this);
					if (this.type.equals("Boss Fire")) {
						missedCannonballSound.play();
						GameplayState.player.missedCannonballs += 1;
						GameplayState.player.timeSurvived -= 5000.0f;
						GameplayState.player.timeLost = true;
					}
				}
			}
		}
	}

	public void reset() {
		this.acc = this.startAcc.copy();
		this.vel = this.startVel.copy();
		this.pos = this.startPos.copy();
	}
}