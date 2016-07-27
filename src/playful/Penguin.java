package playful;

import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

public class Penguin implements Entity {
	String type;
	int team;
	int level;
	Image spriteImg;
	Animation animation;
	Image baseImg;
	int baseImgScale;
	float baseImgScaleF = 2.0f;
	Vector2f pos;
	Vector2f basePos;
	Vector2f startPos;
	Vector2f vel;
	Vector2f startVel;
	Vector2f acc;
	Vector2f startAcc;
	public boolean alive;
	public boolean dealtDamage = false;
	public double dmg;
	public boolean slowed = false;
	protected boolean bossed = false;
	public int range;
	public int power;
	public int health;
	protected int startHealth;
	protected boolean fell = false;
	protected boolean isSetFell = false;
	protected boolean caught = false;
	private int caughtMovement = 0;
	protected int fellMovement = 0;

	protected int bossShootTimer = 0;

	float inputDelta = 0.0F;

	public Penguin(String type, int health, Animation animation, Vector2f startPosition, Vector2f startVelocity, Vector2f startAcceleration) {
		this.type = type;
		this.health = health;
		this.startHealth = this.health;
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
		this.alive = true;
	}

	public Shape getShape() {
		if (this.startHealth == 1) {
			return new Rectangle(this.pos.x, this.pos.y - 3.0F, this.baseImg.getWidth() * (this.health + 1), this.baseImg.getHeight() * (this.health + 1) + 6);
		}
		return new Rectangle(this.pos.x, this.pos.y, this.baseImg.getWidth() * (this.health + 1), this.baseImg.getHeight() * (this.health + 1));
	}

	public boolean isAlive() {
		return this.alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	public boolean isFell() {
		return this.fell;
	}

	public void setFell(boolean fell) {
		this.fell = fell;
		if (fell)
			setIsSetFell(true);
	}

	public boolean isSetFell() {
		return this.isSetFell;
	}

	public void setIsSetFell(boolean isSetFell) {
		this.isSetFell = isSetFell;
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
		try {
			setSpriteImage(animation.getImage(0));
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	public void setSpriteImage(Image image) throws SlickException {
		this.spriteImg = image;
		this.spriteImg.setFilter(2);
	}

	public Image getSpriteImage() {
		return this.spriteImg;
	}

	public void setBaseImage(Image image) throws SlickException {
		this.baseImg = image;
		this.baseImg.setFilter(2);
	}

	public Image getBaseImage() {
		return this.baseImg;
	}

	public void setBaseImageScale(int imageScale) {
		this.baseImgScale = imageScale;
	}

	public void setBossed(boolean bossed) {
		this.bossed = true;
	}

	public boolean getBossed() {
		return this.bossed;
	}

	public void drawProfile(float dx, float dy, float scale) throws SlickException {
		if (this.spriteImg != null)
			this.spriteImg.draw(dx, dy, scale);
	}

	public void render(Graphics g, AngelCodeFont font, Vector2f updatePosition) {
		if (this.health > 0) {
			//			g.drawRect(pos.x, pos.y, this.baseImg.getWidth() * (this.health + 1), this.baseImg.getHeight() * (this.health + 1));
			if (!this.fell) {
				this.animation.draw(this.pos.x, this.pos.y, this.baseImg.getWidth() * (this.health + 1), this.baseImg.getHeight() * (this.health + 1));
				//				font.drawString(pos.x + 30, pos.y + 25, this.getHealth() + "");
			} else {
				GameplayState.boom.draw(this.pos.x, this.pos.y, this.baseImg.getWidth() * (this.startHealth + 1) / 20);
			}

		} else if (this.caught) {
			this.baseImg.drawFlash(this.pos.x, this.pos.y - 17.0F - this.caughtMovement * this.vel.y / 2.0F, this.baseImg.getWidth() * 2, this.baseImg.getHeight() * 2, Color.gray);
			if (!GameplayState.player.bossed) {
				font.drawString(this.pos.x - 15.0F, this.pos.y + this.baseImg.getHeight() - 10.0F - this.caughtMovement, "+" + 2 * this.startHealth * (int) GameplayState.player.multi, Color.yellow);
			} else {
				font.drawString(this.pos.x - 15.0f, this.pos.y + this.baseImg.getHeight() - 10.0f - this.caughtMovement, "+" + this.startHealth, Color.yellow);
			}
		} else if (getType().equals("Boss")) {
			baseImg.draw(pos.x, pos.y, baseImg.getWidth() * baseImgScaleF, baseImg.getHeight() * baseImgScaleF);
		}
	}

	public void update(GameContainer gc, int delta) throws SlickException {
		this.inputDelta -= delta;

		if (getType().equals("Boss") && !GameplayState.player.killingBoss) {
			this.bossShootTimer += delta;
		}

		if (this.health > 0 || (this.health < 0 && GameplayState.player.killingBoss)) {
			if (!this.fell) {
				//				this.animation.update(delta);
			} else if (this.inputDelta < 0.0f) {
				this.fellMovement += 1;
				this.inputDelta = 50.0f;
				if (this.fellMovement >= 15) {
					GameplayState.removeEnts.add(this);

					this.pos.y -= 10.0F;
					this.fellMovement = 0;
				}
			}
		} else {
			if (getType().equals("Boss")) {
				GameplayState.player.multi = GameplayState.player.startMulti;
			}
			if ((this.caught) && (this.inputDelta < 0.0F)) {
				this.caughtMovement += 1;
				this.inputDelta = 50.0F;
				if (this.caughtMovement >= 20) {
					setCaught(false);
					this.caughtMovement = 0;
					GameplayState.removeEnts.add(this);
					this.alive = false;
				}
			}
		}

		if (!this.bossed) {
			if (!this.fell) {
				if (this.inputDelta < 0.0F) {
					if (getType().equals("Boss")) {
						if (!GameplayState.player.killingBoss) {
							this.pos.add(this.vel);
							this.vel.add(this.acc);
							this.inputDelta = 50.0F;
						} else {
						}
					} else {
						this.pos.add(this.vel);
						this.vel.add(this.acc);
						this.inputDelta = 50.0F;
					}
				}

				if (this.pos.x + this.baseImg.getWidth() * this.health + 1.0F + 10.0F >= gc.getWidth()) {
					this.vel.x *= -1.0F;
					while (this.pos.x + this.baseImg.getWidth() * this.health + 10.0F + 1.0F >= gc.getWidth()) {
						this.pos.x -= 1.0F;
					}
				}
				if (this.pos.x <= 0.0F) {
					this.vel.x *= -1.0F;
					while (this.pos.x <= 0.0F) {
						this.pos.x += 1.0F;
					}

				}

				if (this.pos.y + 35.0F > gc.getHeight()) {
					setFell(true);
					GameplayState.fellSound.play(1.0f, 0.75f);
				}

				if (this.pos.y < 0.0f) {
					this.vel.y *= -1.0f;
					while (this.pos.y < 0.0f) {
						this.pos.y += 1.0f;
					}

				}

			}

		} else if (this.pos.x < gc.getWidth() / 2) {
			this.pos.x -= 4.0F;
			if (this.pos.x < 5.0F) {
				GameplayState.removeEnts.add(this);
				this.alive = false;
			}
		} else {
			this.pos.x += 4.0F;
			if (this.pos.x + this.baseImg.getWidth() * this.health + 1.0F >= gc.getWidth()) {
				GameplayState.removeEnts.add(this);
				this.alive = false;
			}
		}
	}

	public void reset() {
		this.acc = this.startAcc.copy();
		this.vel = this.startVel.copy();
		this.pos = this.startPos.copy();
	}
}
