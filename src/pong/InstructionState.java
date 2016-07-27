package pong;

import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class InstructionState extends BasicGameState {
	AngelCodeFont font;
	AngelCodeFont smallFont1;
	SpriteSheet bonusSheet;
	Animation[] bonusAnims;
	Color bg;
	int frame;
	public static final int ID = 3;
	float inputDelta = 1500.0F;

	public int getID() {
		return 3;
	}

	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		this.font = new AngelCodeFont("res/pong/font2.fnt", "res/pong/font2_0.tga");
		this.smallFont1 = new AngelCodeFont("res/pong/smallFont1.fnt", "res/pong/smallFont1_0.tga");

		this.bonusSheet = new SpriteSheet("res/pong/bonuses.png", 10, 10);
		bonusAnims = new Animation[bonusSheet.getVerticalCount()];
		for (int a = 0; a < bonusSheet.getVerticalCount(); a++) {
			bonusAnims[a] = new Animation(new Image[] { bonusSheet.getSprite(0, a).getScaledCopy(2.0f), bonusSheet.getSprite(1, a).getScaledCopy(2.0f), bonusSheet.getSprite(2, a).getScaledCopy(2.0f) }, 200, true);
			bonusAnims[a].setPingPong(true);
			bonusAnims[a].setAutoUpdate(true);
		}
	}

	public void enter(GameContainer gc, StateBasedGame sbg) throws SlickException {
		gc.getInput().clearKeyPressedRecord();
		gc.getInput().clearMousePressedRecord();

		this.bg = Color.cyan.darker();

		this.frame = 0;
	}

	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		g.setBackground(this.bg);

		this.font.drawString(gc.getWidth() / 2 - 60, 15.0F, "INSTRUCTIONS", Color.white);
		this.smallFont1.drawString(gc.getWidth() / 2 - 30, 40.0F, "Page " + (this.frame + 1) + "/3");
		this.smallFont1.drawString(150.0F, gc.getHeight() - 30, "CLICK or press [SPACE] to continue");

		switch (this.frame) {
		case 0:
			this.font.drawString(130.0f, 80.0F, "Stop the Robot Penguins from invading!", Color.orange);
			this.font.drawString(155.0f, 80.0f + font.getLineHeight(), "Don't let them reach the bottom!", Color.red.darker(0.35f));
			this.font.drawString(180.0f, 175.0f, "Fear the \"Head\" Penguin!", Color.red.darker(0.35f));
			this.font.drawString(110.0F, 225.0F, "Use the [ARROW KEYS] to move the Walrus\nHit the Penguins with the Walrus to stop them\n");
			this.font.drawString(60.0F, 325.0F, "Every Robot Penguin you miss makes you LOSE ONE LIFE", Color.black);
			this.font.drawString(25.0F, 375.0F, "Yet! Each Penguin you hit, you get POINTS and survive a bit longer!");
			break;
		case 1:
			this.font.drawString(150.0f, 120.0f, "There are FIVE difficulties to play!", Color.darkGray);
			this.font.drawString(40.0f, 160.0f, "EASY", Color.yellow);
			this.smallFont1.drawString(80.0f, 185.0f, "This is the simplest of simple. You start with lots of health.\nThe Robot Penguins take a while to come en masse.");
			this.font.drawString(40.0f, 240.0f, "NORMAL", Color.yellow);
			this.smallFont1.drawString(80.0f, 265.0f, "This is the Average-Jo's style of play. Decent starting health.\nThe Penguins start stepping up their game so beware!");
			this.font.drawString(40.0f, 320.0f, "Quick", Color.yellow);
			this.smallFont1.drawString(80.0f, 345.0f, "For those who want points..uh..quickly! Average health.\nThis one is special because you get points THREE TIMES as fast\nso you get bonuses (and bosses) sooner.");
			this.font.drawString(40.0f, 400.0f, "Hardcore", Color.yellow);
			this.smallFont1.drawString(80.0f, 425.0f, "The definition of 'intense'. VERY small starting health.\nRobot Penguins have mustered their might and attack quickly!");
			this.font.drawString(40.0f, 480.06f, "Deathcore", Color.yellow);
			this.smallFont1.drawString(80.05f, 505.0f, "Crazyness galore! Although it's is the same as Normal with\nless health, I hope you don't get dizzy easily...");
			break;
		case 2:
			this.font.drawString(85.0f, 80.0f, "Don't fret! There are brilliant bonuses to help you!", Color.green);
			this.font.drawString(50.0f, 110.0f, "Bonuses fall every 50 points and offer you aids in your quest!");
			bonusAnims[0].draw(35.0f, 160.0f);
			this.smallFont1.drawString(70.0f, 165.0f, "This 'Fire' bonus allows you to send out a devastating ball.\nJust press [UP] or [SPACE]!");
			bonusAnims[1].draw(35.0f, 200.0f);
			this.smallFont1.drawString(70.0f, 205.0f, "This is a Health bonus that adds ONE life!");
			bonusAnims[2].draw(35.0f, 240.0f);
			this.smallFont1.drawString(70.0f, 245.0f, "Doubles your speed for the price of 1! What a deal!\nLasts 10 seconds.");
			bonusAnims[3].draw(35.0f, 280.0f);
			this.smallFont1.drawString(70.0f, 285.0f, "The all-consuming 'Nuke' bonus. Destroys all Robot Penguins.");
			bonusAnims[4].draw(35.0f, 320.0f);
			this.smallFont1.drawString(70.0f, 325.0f, "Time to stretch! Doubles the width of your paddle for 10 seconds.");
			bonusAnims[5].draw(35.0f, 360.0f);
			this.smallFont1.drawString(70.0f, 360.0f, "The strange and eccentric 'Rift' which reverses the\ndirection of the Robot Penguins.");
			bonusAnims[6].draw(15.0f, 400.0f);
			bonusAnims[7].draw(40.0f, 400.0f);
			this.smallFont1.drawString(70.0f, 405.0f, "These bonuses affect the rate at which you get points.\nThey DOUBLE and QUADRUPLE your point gain, respectively.");
			break;
		default:
			this.font.drawString(50.0f, 200.0f, "Whatchu doin' here? GET OUT!");
		}
	}

	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		this.inputDelta -= delta;
		Input input = gc.getInput();

		if ((input.isKeyPressed(Input.KEY_ENTER)) || (input.isKeyPressed(Input.KEY_SPACE)) || (input.isMousePressed(Input.MOUSE_LEFT_BUTTON))) {
			this.frame += 1;
			if (this.frame == 3) {
				this.bg = Color.gray;
			}
		}

		if (input.isKeyPressed(Input.KEY_ESCAPE)) {
			sbg.enterState(0);
		}

		if (this.frame > 2)
			sbg.enterState(0);
	}
}
