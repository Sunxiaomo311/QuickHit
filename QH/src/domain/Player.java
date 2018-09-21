package domain;

/**
 * 玩家类
 * 
 * @author Sun
 * @date Sep 17, 2018
 * @version V1.0
 */
public class Player {

	private String name; // 称号
	private int score; // 分数
	private int ranking; // 排名
	private String message; // 留言

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getRanking() {
		return ranking;
	}

	public void setRanking(int ranking) {
		this.ranking = ranking;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
