package pong;

import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

public abstract interface Entity {
	
	public abstract Shape getShape();

	public abstract boolean isAlive();

	public abstract void setAlive(boolean paramBoolean);

	public abstract boolean isFell();

	public abstract void setFell(boolean paramBoolean);

	public abstract boolean isCaught();

	public abstract void setCaught(boolean paramBoolean);

	public abstract String getType();

	public abstract void setType(String paramString);

	public abstract int getHealth();

	public abstract void setHealth(int paramInt);

	public abstract int getStartHealth();

	public abstract void setStartHealth(int paramInt);

	public abstract Vector2f getPosition();

	public abstract void setPosition(Vector2f paramVector2f);

	public abstract void setBasePosition(Vector2f paramVector2f);

	public abstract Vector2f getVelocity();

	public abstract void setVelocity(Vector2f paramVector2f);

	public abstract Vector2f getAcceleration();

	public abstract void setAcceleration(Vector2f paramVector2f);

	public abstract Animation getAnimation();

	public abstract void setAnimation(Animation paramAnimation);

	public abstract void render(Graphics paramGraphics, AngelCodeFont paramAngelCodeFont, Vector2f paramVector2f);

	public abstract void update(GameContainer paramGameContainer, int paramInt) throws SlickException;

	public abstract void reset();
}
