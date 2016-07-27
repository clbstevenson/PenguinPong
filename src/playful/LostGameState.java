package playful;

import java.io.IOException;
import java.util.Map;

import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.MouseOverArea;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.particles.ParticleIO;
import org.newdawn.slick.particles.ParticleSystem;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

//import st.mark.highscores.HighscoreBoard;
//import st.mark.highscores.HighscoreItem;

public class LostGameState extends BasicGameState implements ComponentListener {
	AngelCodeFont font;
	protected int life;
	protected int score;
	protected int penguins;
	private static Player player;
	Image penguin;
	private ParticleSystem fire;
	float rx;
	float ry = 500.0F;
	Image rocket;
	TextField name;
	MouseOverArea[] areas = new MouseOverArea[2];
	//	HighscoreBoard board;
	//	HighscoreItem rank;
	Map<Image, Vector2f> flamingPenguins;

	String rankString;

	StateBasedGame sbg;
	public static final int ID = 2;
	float inputDelta = 1500.0F;

	public int getID() {
		return 2;
	}

	public static void setPlayer(Player setPlayer) {
		player = setPlayer;
	}

	public void init(GameContainer gc, StateBasedGame sbg)
			throws SlickException {
		this.sbg = sbg;

		this.font = new AngelCodeFont("res/font2.fnt", "res/font2_0.tga");

		SpriteSheet sheet = new SpriteSheet("res/robotpenguin.png", 20, 20);
		this.penguin = sheet.getSubImage(1, 0).getScaledCopy(3.0F);
		try {
			this.fire = ParticleIO.loadConfiguredSystem("res/system.xml");
		} catch (IOException e) {
			throw new SlickException("Failed to load particle systems", e);
		}
		this.rocket = new Image("res/fireball.png").getScaledCopy(3.0F);
		this.ry = 500.0F;
		this.rx = ((float) (Math.random() * 400.0D + 100.0D));

		this.name = new TextField(gc, this.font, gc.getWidth() / 2 - 28,
				gc.getHeight() / 2 + 50, 56, 25, this);
		this.name.setBackgroundColor(Color.white);
		this.name.setTextColor(Color.red);
		this.name.setMaxLength(3);
		this.name.setFocus(true);

		SpriteSheet losticons = new SpriteSheet("res/losticons.png", 60, 20);
		for (int j = 0; j < 2; j++) {
			Image lostimg = losticons.getSprite(0, j).getScaledCopy(2.0F);
			this.areas[j] = new MouseOverArea(gc, lostimg,
					gc.getWidth() / 2 - 50, gc.getHeight() - 110 + j * 45,
					lostimg.getWidth(), lostimg.getHeight(), this);
			this.areas[j].setNormalColor(new Color(1.0F, 1.0F, 1.0F, 0.5F));
			this.areas[j].setMouseOverColor(new Color(1.0F, 1.0F, 1.0F, 0.9F));
		}
		//		this.board = null;

		rankString = "";
	}

	public void enter(GameContainer gc, StateBasedGame sbg)
			throws SlickException {
		this.inputDelta = 1500.0F;

		gc.getInput().clearKeyPressedRecord();
		gc.getInput().clearMousePressedRecord();
		gc.setMusicOn(false);
		gc.resume();
		try {
			this.fire = ParticleIO.loadConfiguredSystem("res/system.xml");
		} catch (IOException e) {
			throw new SlickException("Failed to load particle systems", e);
		}
		this.rocket = new Image("res/fireball.png").getScaledCopy(4.0F);
		this.ry = 500.0F;
		this.rx = ((float) (Math.random() * 400.0D + 100.0D));

		this.name.setText("");
		this.name.setBackgroundColor(Color.white);
		this.name.setAcceptingInput(true);
		this.name.setFocus(true);

		//		this.rank = null;

		/**
		switch (player.difficulty) {
		case 0: //EASY
			this.board = new HighscoreBoard(
					"41fee527d260221f54ddf6214426cd8c2a7acdfa");
			break;
		case 1: //NORMAL
			this.board = new HighscoreBoard(
					"e6ae3036955fbd62cdb66dbc5f3f6334817b469f");
			break;
		case 2: //QUICK
			this.board = new HighscoreBoard(
					"0b0e3731aba080ca35d25f85e9b16fb28ac38cf5");
			break;
		case 3: //HARDCORE
			this.board = new HighscoreBoard(
					"783774958810ca95f805ffe01415a245c6aaf956");
			break;
		case 4: //DEATHCORE
			this.board = new HighscoreBoard(
					"da943b740ac000690c46bb40c500c9ad90080446");
			break;
		default:
			this.board = new HighscoreBoard(
					"610c68d265de755d7de5e7052e181400b5a1956b");
			break;
		}
		*/
	}

	public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
			throws SlickException {
		g.setBackground(Color.black);

		this.font.drawString(gc.getWidth() - 90, 10.0F,
				player.getDifficulty(player.difficulty));
		this.font.drawString(gc.getWidth() / 2 - 50, 100.0F, "GAME OVER");
		if (player != null)
			this.font.drawString(gc.getWidth() / 2 - 35, 200.0F, "Score: "
					+ player.getScore() + "\nLevel: " + player.getLevel()
					+ "\nTime: " + player.timeSurvived / 1000.0F + "s");
		else {
			this.font.drawString(gc.getWidth() / 2 - 35, 200.0F,
					"Score: n/a\nLevel: n/a\nTime: 0.00s");
		}

		if (!player.isCheatsOn(0)) {
			this.font.drawString(this.name.getX() - 55, this.name.getY()
					- this.font.getLineHeight(), "Enter your initials:",
					Color.orange);
			this.name.render(gc, g);
			this.font.drawString(this.name.getX() - 70, this.name.getY()
					+ this.font.getLineHeight(), "Press [ENTER] to submit!",
					Color.orange);
		} else {
			this.font.drawString(this.name.getX() - 125, this.name.getY()
					- this.font.getLineHeight(),
					"Sorry, no Highscores in Sandbox mode.", Color.orange);
		}

		this.fire.setPosition(this.rx + 27.5F, this.ry + 25.0F);
		this.fire.render();
		this.penguin.draw((int) this.rx, (int) this.ry);

		for (int i = 0; i < 2; i++) {
			this.areas[i].render(gc, g);
		}
		/**
		if (this.rank != null) {
			this.font
					.drawString(this.name.getX() - 5, this.name.getY() + 2.5F
							* this.font.getLineHeight(),
							"Rank: " + this.rank.getRank());
		} else if (!name.isAcceptingInput()) {
			//Highscores are broken\nSorry for inconvenience
			this.font.drawString(this.name.getX() - 5, this.name.getY() + 2.5f
					* this.font.getLineHeight(),
					"Highscores are unavailable\nSorry for inconvenience");
		}
		*/
		if (!name.isAcceptingInput()) {
			this.font.drawString(this.name.getX() - 5, this.name.getY() + 2.5f
					* this.font.getLineHeight(),
					"Highscores are unavailable\nSorry for inconvenience");
		}

	}

	public void update(GameContainer gc, StateBasedGame sbg, int delta)
			throws SlickException {
		Input input = gc.getInput();

		this.inputDelta -= delta;

		this.fire.update(delta);

		this.ry -= delta * 0.1F;
		if (this.ry < -100.0F) {
			this.ry = 500.0F;
			this.rx = ((float) (Math.random() * 450.0D + 50.0D));
		}

		if (this.inputDelta < 0.0F) {
			if ((input.isKeyPressed(57)) && (!this.name.hasFocus())) {
				sbg.enterState(0);
			}
			if (input.isMousePressed(1))
				enter(gc, sbg);
		}
	}

	public void mousePressed(int button, int x, int y) {
		super.mousePressed(button, x, y);
	}

	public void keyPressed(int key, char c) {
		if (key == Input.KEY_ESCAPE)
			System.exit(0);
	}

	public void componentActivated(AbstractComponent source) {
		if (source == this.areas[0]) {
			this.sbg.enterState(1);
		}
		if (source == this.areas[1]) {
			this.sbg.enterState(0);
		}
		if (source == this.name) {
			if (!player.isCheatsOn(0)) {
				this.name.setBackgroundColor(Color.gray);
				this.name.setAcceptingInput(false);
				this.name.setFocus(false);

				/** TODO:
				 * HIGHSCORES ARE BROKEN. TO BE FIXED.
				 * 
				 * HighscoreItem hsi = this.board.addNewScore(
				 *			(int) player.timeSurvived / 10,
				 *			this.name.getText(), (int) player.getScore() + "",
				 *			"nada", "nada", "nada");
				 *	this.rank = hsi;
				 * 
				 * */

			}
		}
	}
}
