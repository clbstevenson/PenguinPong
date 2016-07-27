package pong;

//import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.MouseOverArea;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

//import st.mark.highscores.HighscoreBoard;
//import st.mark.highscores.HighscoreItem;

public class MainMenuState extends BasicGameState implements ComponentListener {

	AngelCodeFont font, font1, smallFont1;
	Rectangle clickRect;
	Image titleImg;
	Image p2, p3, p4;
	Map<Image, Vector2f> imageMap;

	private MouseOverArea[] areas = new MouseOverArea[5];
	private MouseOverArea instructions, sandbox;
//	private HighscoreBoard[] boards = new HighscoreBoard[5];
	boolean[] boardsLoaded = new boolean[5];
	//private String[][] boardStrings = new String[5][5];

	private int mouseOverTimer;
	private MouseOverArea selectedArea = null;

	private MouseOverArea soundOnOff;
	private SpriteSheet soundOnOffSheet;
	int timerlast = 3000;
	int timer = timerlast;
	boolean fade;

	boolean quitting = false;

	StateBasedGame sbg;
	GameContainer gc;
	public static final int ID = 0;
	float inputDelta = 50.0f;
	Input input;

	public int getID() {
		return 0;
	}

	public void init(GameContainer gc, StateBasedGame sbg)
			throws SlickException {
		this.sbg = sbg;
		this.gc = gc;

		input = gc.getInput();

		this.font = new AngelCodeFont("res/pong/font2.fnt", "res/pong/font2_0.tga");
		this.font1 = new AngelCodeFont("res/pong/font1.fnt", "res/pong/font1_0.tga");
		this.smallFont1 = new AngelCodeFont("res/pong/smallFont1.fnt",
				"res/pong/smallFont1_0.tga");

		this.titleImg = new Image("res/pong/titleImage.png");

		SpriteSheet sheet = new SpriteSheet("res/pong/robotpenguin.png", 20, 20);
		Image p1 = sheet.getSubImage(1, 0);
		this.p2 = p1.getScaledCopy(2.0F);
		this.p3 = p1.getScaledCopy(3.0F);
		this.p4 = p1.getScaledCopy(4.0F);

		this.imageMap = new HashMap<Image, Vector2f>();

		this.clickRect = new Rectangle(gc.getWidth() / 2 - 100,
				gc.getHeight() / 2 - 105, 200.0F, 290.0F);

		SpriteSheet starticons = new SpriteSheet("res/pong/starticons.png", 145, 35);
		for (int i = 0; i < 5; i++) {
			this.areas[i] = new MouseOverArea(gc, starticons.getSprite(0, i),
					gc.getWidth() / 2 - starticons.getSprite(0, i).getWidth()
							/ 2, gc.getHeight() / 2 - 90 + i * 55, starticons
							.getSprite(0, i).getWidth(), starticons.getSprite(
							0, i).getHeight(), this);
			this.areas[i].setNormalColor(new Color(1.0f, 1.0f, 1.0f, 0.5f));
			this.areas[i].setMouseOverColor(new Color(1.0f, 1.0f, 1.0f, 0.9f));
		}

		Image instructImg = new Image("res/pong/instructions.png");
		this.instructions = new MouseOverArea(gc, instructImg, gc.getWidth()
				/ 2 - instructImg.getWidth() / 2, gc.getHeight() - 75,
				instructImg.getWidth(), instructImg.getHeight(), this);
		this.instructions.setNormalColor(new Color(1.0f, 1.0f, 1.0f, 0.8f));
		this.instructions
				.setMouseOverColor(new Color(250.0f, 1.0f, 1.0f, 1.0f));

		sandbox = new MouseOverArea(gc, new Image("res/pong/sandbox2.png"),
				gc.getWidth() - 100, gc.getHeight() / 2 - 30, 70, 70, this);
		sandbox.setNormalColor(new Color(1.0f, 1.0f, 1.0f, 0.8f));
		sandbox.setMouseOverColor(new Color(250.0f, 1.0f, 1.0f, 1.0f));

		SpriteSheet soundOnOffImg = new SpriteSheet("res/pong/turnSoundOnOff.png",
				30, 30);
		soundOnOffSheet = new SpriteSheet("res/pong/turnSoundOnOff.png", 30, 30);
		this.soundOnOff = new MouseOverArea(gc, soundOnOffImg.getSprite(0, 0),
				gc.getWidth() - 75, gc.getHeight() - 50, soundOnOffImg
						.getSprite(0, 0).getWidth(), soundOnOffImg.getSprite(0,
						0).getHeight(), this);
		//		soundOnOff.setMouseDownImage(soundOnOffImg.getSprite(1, 0));

		/**this.boards[0] = new HighscoreBoard(
				"41fee527d260221f54ddf6214426cd8c2a7acdfa");
		this.boards[1] = new HighscoreBoard(
				"e6ae3036955fbd62cdb66dbc5f3f6334817b469f");
		this.boards[2] = new HighscoreBoard(
				"0b0e3731aba080ca35d25f85e9b16fb28ac38cf5");
		this.boards[3] = new HighscoreBoard(
				"783774958810ca95f805ffe01415a245c6aaf956");
		//5bc0664fe8da8afe4ae3d49f79fe92a8ef87a4f1
		this.boards[4] = new HighscoreBoard(
				"da943b740ac000690c46bb40c500c9ad90080446");
				*/
	}

	public void enter(GameContainer gc, StateBasedGame sbg)
			throws SlickException {
		gc.getInput().clearKeyPressedRecord();
		gc.getInput().clearMousePressedRecord();
		gc.resume();

		this.inputDelta = 50.0f;

		mouseOverTimer = 500;
		selectedArea = null;

		for (int j = 0; j < 5; j++) {
			this.boardsLoaded[j] = false;
		}

		Random r = new Random();
		this.imageMap.clear();
		for (int i = 0; i < 60; i++) {
			int pType = r.nextInt(3);
			boolean facingleft = r.nextBoolean();
			switch (pType) {
			case 0:
				Image cp2;
				//        Image cp2;
				if (facingleft)
					cp2 = this.p2.getFlippedCopy(true, false);
				else {
					cp2 = this.p2.copy();
				}
				cp2.setAlpha(0.5F);
				this.imageMap.put(
						cp2,
						new Vector2f(r.nextInt(gc.getWidth() - 75) + 15, r
								.nextInt(gc.getHeight() - 110) + 15));
				break;
			case 1:
				Image cp3;
				//        Image cp3;
				if (facingleft)
					cp3 = this.p3.getFlippedCopy(true, false);
				else {
					cp3 = this.p3.copy();
				}
				cp3.setAlpha(0.5F);
				this.imageMap.put(
						cp3,
						new Vector2f(r.nextInt(gc.getWidth() - 75) + 15, r
								.nextInt(gc.getHeight() - 110) + 15));
				break;
			case 2:
				Image cp4;
				//        Image cp4;
				if (facingleft)
					cp4 = this.p4.getFlippedCopy(true, false);
				else {
					cp4 = this.p4.copy();
				}
				cp4.setAlpha(0.5F);
				this.imageMap.put(
						cp4,
						new Vector2f(r.nextInt(gc.getWidth() - 75) + 15, r
								.nextInt(gc.getHeight() - 110) + 15));
			}
		}
	}

	public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
			throws SlickException {
		g.setColor(Color.cyan.darker());
		g.fillRect(0.0F, 0.0F, gc.getWidth(), gc.getHeight());

		Set<Image> imageSet = this.imageMap.keySet();
		for (Image img : imageSet) {
			img.draw(((Vector2f) this.imageMap.get(img)).x,
					((Vector2f) this.imageMap.get(img)).y);
		}

		this.titleImg.draw(this.clickRect.getX(), this.clickRect.getY()
				- this.titleImg.getHeight() - 20.0F);
		g.setColor(Color.blue.darker(0.55F));
		g.fill(this.clickRect);

		this.instructions.render(gc, g);
		this.sandbox.render(gc, g);

		if (this.inputDelta < 0.0f) {
			//TODO: Somehow use variable selectedArea instead of looping.
			for (int i = 0; i < 5; i++) {
				this.areas[i].render(gc, g);
				if (this.areas[i].isMouseOver() && mouseOverTimer < 0) {
					g.setColor(new Color(217, 168, 33, 100));
					g.fillRect(this.areas[i].getX() + 100,
							this.areas[i].getY() - 170, 250.0f, 328.0f);
					String title = "";
					switch (i) {
					case 0:
						title = "Easy";
						break;
					case 1:
						title = "Normal";
						break;
					case 2:
						title = "Quick";
						break;
					case 3:
						title = "Hardocre";
						break;
					case 4:
						title = "Deathcore";
						break;
					}
					this.font1.drawString(this.areas[i].getX() + 200,
							this.areas[i].getY() - 160, title, Color.yellow);
					for (int k = 0; k < 20; k++) {
						this.smallFont1.drawString(this.areas[1].getX() + 110,
								this.areas[i].getY() - 120 + k
										* this.smallFont1.getLineHeight() + k
										* 2, (k + 1)
										+ "  Highscores are unavailable");
					}

					//TODO: FIX HIGHSCORES SOMEHOW. 
					/**THEY ARE BROKEN 
					 * 
					 * this.smallFont1.drawString(this.areas[i].getX() + 110, this.areas[i].getY() - 138, "Rank - Name - Time - Score", Color.yellow);
					 * for (int k = 0; k < this.boardStrings[i].length; k++) {
					 * this.smallFont1.drawString(this.areas[i].getX() + 110, this.areas[i].getY() - 120 + k * this.smallFont1.getLineHeight() + k * 2, this.boardStrings[i][k]);
					 * }
					 * 
					 * */

				}
			}
		}

		if (gc.isSoundOn()) {
			//			soundOnOff.render(gc, g);
			soundOnOff.render(gc, g);
		} else {
			soundOnOff.render(gc, g);
			soundOnOffSheet.getSprite(1, 0).draw(soundOnOff.getX(),
					soundOnOff.getY());
		}
		float timerPercent = (float) timer / timerlast;
		int alphaPercent = (int) (350 * timerPercent);
		font.drawString(clickRect.getX() - 15, clickRect.getY() - 30,
				"Mouse over for Highscores!", new Color(212, 212, 212,
						alphaPercent));

		if (quitting) {
			g.setColor(Color.red.darker(0.25f));
			g.fillRect(gc.getWidth() / 2 - 100, gc.getHeight() / 2 - 40, 200,
					80);
			font.drawString(gc.getWidth() / 2 - 15, gc.getHeight() / 2 - 30,
					"Quit?");
			smallFont1.drawString(gc.getWidth() / 2 - 65, gc.getHeight() / 2
					- 30 + font.getLineHeight(),
					"Press ESC to quit.\nLeft-Click or \n   SPACE to resume.",
					Color.orange);
		}

	}

	public void update(GameContainer gc, StateBasedGame sbg, int delta)
			throws SlickException {

		input.setDoubleClickInterval(500);

		this.inputDelta -= delta;

		if (fade) {
			timer += delta;
			if (timer > timerlast) {
				fade = !fade;
			}
		} else {
			timer -= delta;
			if (timer < 0) {
				fade = !fade;
			}
		}

		if (quitting) {
			if (input.isKeyPressed(Input.KEY_ESCAPE)) {
				gc.exit();
			}
			if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)
					|| input.isKeyPressed(Input.KEY_SPACE)
					|| input.isKeyPressed(Input.KEY_ENTER)) {
				quitting = false;
			}
		} else {
			if (input.isKeyPressed(Input.KEY_ESCAPE)) {
				quitting = true;
			}
			for (int i = 0; i < 5; i++) {
				if ((this.areas[i].isMouseOver())) {
					mouseOverTimer -= delta;
					if (mouseOverTimer < 0) {
						selectedArea = areas[i];
						if (this.boardsLoaded[i] == false) {
							/**HIGHSCORES ARE BROKEN
							 * this.boardStrings[i] = getScoreRange(
									this.boards[i], 1, 20);
							 * */

							this.boardsLoaded[i] = true;
						}
					}
				}
			}

			if (selectedArea != null && !selectedArea.isMouseOver()) {
				mouseOverTimer = 500;
				selectedArea = null;
			}

			if (input.isKeyPressed(Input.KEY_R)) {
				sbg.enterState(0);
			}
			if ((input.isKeyPressed(Input.KEY_SPACE))
					|| (input.isKeyPressed(Input.KEY_ENTER))) {
				GameplayState.setDifficulty(1);
				sbg.enterState(1);
			}
		}
	}

	/**
	public String[] getScoreRange(HighscoreBoard hs, int start, int end) {
		ArrayList<HighscoreItem> scores = hs.getScoreRange(start, end);

		String[] uber = new String[20];
		for (int i = 0; i < uber.length; i++) {
			if (i < scores.size()) {
				if (scores.get(i) != null) {
					HighscoreItem hsi = (HighscoreItem) scores.get(i);
					uber[i] = (hsi.getRank() + " - " + hsi.getText1() + " - "
							+ hsi.getScore() + " - " + hsi.getText2());
				}
			} else
				uber[i] = (i + 1) + "";

		}

		return uber;
	}
	*/

	public void componentActivated(AbstractComponent source) {
		for (int i = 0; i < 5; i++) {
			if (source == this.areas[i]) {
				GameplayState.setDifficulty(i);
				this.sbg.enterState(1);
			}
		}
		if (source == this.instructions)
			this.sbg.enterState(3);
		if (source == sandbox) {
			GameplayState.setDifficulty(2);
			GameplayState.player.setCheatsOn(0, true);
			this.sbg.enterState(1);
		}
		if (source == soundOnOff) {
			if (gc.isSoundOn()) {
				gc.setSoundOn(false);
			} else {
				gc.setSoundOn(true);
			}
		}
	}
}
