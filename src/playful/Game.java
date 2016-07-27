package playful;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class Game extends StateBasedGame {
	private static final String title = "Penguin Pong";

	public Game(String title) {
		super(title);
	}

	public static void main(String[] args) {
		try {
			AppGameContainer agc = new AppGameContainer(new Game(title));
			String[] refs = { "res/icon32x32.png", "res/icon16x16.png" };
			agc.setIcons(refs);
			agc.setDisplayMode(600, 600, false);
			agc.setMinimumLogicUpdateInterval(20);
			agc.setMaximumLogicUpdateInterval(20);
			agc.setTargetFrameRate(60);
			agc.setUpdateOnlyWhenVisible(true);
			agc.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	public void initStatesList(GameContainer gc) throws SlickException {
		MainMenuState menuState = new MainMenuState();
		addState(menuState);
		GameplayState state = new GameplayState();
		addState(state);
		LostGameState lostState = new LostGameState();
		addState(lostState);
		InstructionState instructionState = new InstructionState();
		addState(instructionState);
	}
}