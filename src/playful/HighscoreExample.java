package playful;

import java.util.ArrayList;

//import st.mark.highscores.HighscoreBoard;
//import st.mark.highscores.HighscoreItem;

public class HighscoreExample {
//	private HighscoreBoard hs = null;

	public static void main(String[] args) {
		new HighscoreExample();
	}

	public HighscoreExample() {
//		this.hs = new HighscoreBoard("0698e5beb51ff52a27f683f25de6e2e0089e1401");

//		getTopScores();
	}

	/**
	public void getTopScores() {
		ArrayList<HighscoreItem> scores = this.hs.getTop();

		for (int i = 0; i < scores.size(); i++) {
			HighscoreItem hsi = (HighscoreItem) scores.get(i);
			System.out.println(hsi.getRank() + "\t" + hsi.getScore() + "\t" + hsi.getText1() + "\t" + hsi.getText2() + "\t" + hsi.getText3() + "\t" + hsi.getText4() + "\t" + hsi.getText5() + "\t");
		}
	}
	*/

	/**
	public void getScoreRange(int start, int end) {
		ArrayList<HighscoreItem> scores = this.hs.getScoreRange(start, end);

		for (int i = 0; i < scores.size(); i++) {
			HighscoreItem hsi = (HighscoreItem) scores.get(i);
			System.out.println(hsi.getRank() + "\t" + hsi.getScore() + "\t" + hsi.getText1() + "\t" + hsi.getText2() + "\t" + hsi.getText3() + "\t" + hsi.getText4() + "\t" + hsi.getText5() + "\t");
		}
	}
	*/

	/**
	public void addScoreItem(double score, String text1, String text2, String text3, String text4, String text5) {
		HighscoreItem hsi = this.hs.addNewScore((int) score, text1, text2, text3, text4, text5);
		System.out.println(hsi.getRank() + "\t" + hsi.getScore() + "\t" + hsi.getText1() + "\t" + hsi.getText2() + "\t" + hsi.getText3() + "\t" + hsi.getText4() + "\t" + hsi.getText5());
		System.out.println(score);
	}
	*/
}
