import java.util.Arrays;
import java.util.Scanner;

public class BowlingScore {
    public static final int NUM_FRAMES = 10;
    public static final int MAX_PINS = 10;
    private final int[] rolls = new int[21];
    private final int[] frameScores = new int[NUM_FRAMES];
    private int currentRoll = 0;
    private int currentFrame = 1;

    public void roll(int pins) {
    	
    	//1投で倒せるピンの数は1～10本
        if (pins < 0 || pins > 10) {
            throw new IllegalArgumentException("Invalid number of pins: " + pins);
        }
        
        //1～9フレーム目では、2投の合計が10ピン以内であること
        if (currentFrame < 10 && 
        	currentRoll % 2 == 1 && 
        	rolls[currentRoll - 1] + pins > 10) {
            
        	throw new IllegalArgumentException("In one frame, the total number of pins cannot exceed 10.");
        }

        //10フレーム目では、3投の合計が20ピン以内であること
        if (currentFrame == 10 && 
        	currentRoll % 2 == 1  && 
        	rolls[currentRoll - 1] + rolls[currentRoll - 2] + pins > 20) {
            
        	throw new IllegalArgumentException("In one frame, the total number of pins cannot exceed 10.");
        }
       
        rolls[currentRoll++] = pins;
   
           //1～9フレーム目でストライクだった場合は2投目をスキップする
        if (pins == 10 &&
        	currentRoll % 2 == 1 && 
        	currentFrame < 10) {
           
        	currentRoll++;
        }
               
        if (currentRoll % 2 == 0) {
            currentFrame++;
        }
        
        calculateScores();
    }

    void calculateScores() {
        int rollIndex = 0;
        for (int frame = 0; frame < NUM_FRAMES; frame++) {
            if (isStrike(rollIndex)) {
                frameScores[frame] = MAX_PINS + rolls[rollIndex + 1] + rolls[rollIndex + 2];
                rollIndex++;
            } else if (isSpare(rollIndex)) {
                frameScores[frame] = MAX_PINS + rolls[rollIndex + 2];
                rollIndex += 2;
            } else {
                frameScores[frame] = rolls[rollIndex] + rolls[rollIndex + 1];
                rollIndex += 2;
            }
        }
    }

    private boolean isStrike(int rollIndex) {
        return rolls[rollIndex] == MAX_PINS;
    }

    private boolean isSpare(int rollIndex) {
        return rolls[rollIndex] + rolls[rollIndex + 1] == MAX_PINS;
    }

    public int getFrameScore(int frame) {
        return frameScores[frame];
    }

    /**
     * ゲームが終了しているかどうかを判断するメソッド。
     * @return ゲームが終了していればtrue、そうでなければfalse
     */
    public boolean isGameOver() {
        // 10フレーム終了か、または投球回数が21回以上ならゲーム終了
        return currentFrame >= 10 || currentRoll >= 21;
    }
    
    public void printScores() {
        for (int i = 0; i < NUM_FRAMES; i++) {
            if (frameScores[i] != -1) {
                System.out.printf("Frame %d: %d\n", i + 1, frameScores[i]);
            } else {
                System.out.printf("Frame %d: \n", i + 1);
            }
        }
        System.out.printf("Total Score: %d\n", getTotalScore());
    }

    public int getTotalScore() {
        return Arrays.stream(frameScores).sum();
    }

    public int getRoll(int rollIndex) {
        return rolls[rollIndex];
    }

    public static void main(String[] args) {
        BowlingScore game = new BowlingScore();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            game = new BowlingScore();
            playBowling(game, scanner);
            System.out.println("Game Over. Play again? (yes/no)");
            String playAgain = scanner.next();
            if (!playAgain.equalsIgnoreCase("yes")) {
                break;
            }
        }
        scanner.close();
    }

	private static void playBowling(BowlingScore game, Scanner scanner) {
		for (int i = 0; i < 21; i++) {
		    try {
		        System.out.printf("Frame %d, roll %d:\n", (i / 2) + 1, (i % 2) + 1);
		        game.roll(scanner.nextInt());
		        game.printScores();
		    } catch (IllegalArgumentException e) {
		        System.out.println(e.getMessage());
		        i--;
		    }
		}
	}
}
