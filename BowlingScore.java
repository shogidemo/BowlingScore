import java.util.Arrays;
import java.util.Scanner;

public class BowlingScore {
    public static final int MAX_FRAMES = 10;
    public static final int MAX_PINS = 10;
    public static final int MAX_THROWS = 21;
    private final int[] pinsDownList = new int[MAX_THROWS];
    private final int[] frameScores = new int[MAX_FRAMES];
    private int currentThrowIndex = 0;
    private int currentFrameIndex = 0;

    public void throwBall(int pins) {
    	
    	//1投で倒せるピンの数は1～10本
        if (pins < 0 || pins > MAX_PINS) {
            throw new IllegalArgumentException("Invalid number of pins: " + pins);
        }
        
        //1～9フレーム目では、2投の合計が10ピン以内であること
        if (currentFrameIndex < MAX_FRAMES - 1 && 
        	currentThrowIndex % 2 == 1 && 
        	pinsDownList[currentThrowIndex - 1] + pins > 10) {
            
        	throw new IllegalArgumentException("In one frame, the total number of pins cannot exceed 10.");
        }
       
        pinsDownList[currentThrowIndex] = pins;
        
        //スコア計算
        calculateScores();
        
        //1～9フレーム目でストライクだった場合は2投目をスキップする
        if (currentFrameIndex < MAX_FRAMES - 1 &&
        	isStrike(currentThrowIndex)) {
           
        	currentThrowIndex++;
        }
        
        //1～9フレーム目で2投目の場合は次のフレームへ
        if (currentFrameIndex < MAX_FRAMES - 1) {
        	if (currentThrowIndex % 2 == 1) {
        		currentFrameIndex++;
            }
        }
        
        currentThrowIndex++;
    }

    void calculateScores() {
        int rollIndex = 0;
        for (int frame = 0; frame <= currentFrameIndex; frame++) {
            if (isStrike(rollIndex)) {
                if (frame == MAX_FRAMES - 1) {
                	frameScores[frame] = MAX_PINS + pinsDownList[rollIndex + 1] + pinsDownList[rollIndex + 2];
                } else {
                	if(isStrike(rollIndex + 2)) {
                		//2連続ストライク（ダブル）だった場合
                		if (frame == MAX_FRAMES - 2) {
                			frameScores[frame] = MAX_PINS + pinsDownList[rollIndex + 2] + pinsDownList[rollIndex + 3];
                		} else {
                			frameScores[frame] = MAX_PINS + pinsDownList[rollIndex + 2] + pinsDownList[rollIndex + 4];                			
                		}
                	} else {
                		frameScores[frame] = MAX_PINS + pinsDownList[rollIndex + 2] + pinsDownList[rollIndex + 3];
                	}                	
                }
            } else if (isSpare(rollIndex)) {
                frameScores[frame] = MAX_PINS + pinsDownList[rollIndex + 2];
            } else {
                frameScores[frame] = pinsDownList[rollIndex] + pinsDownList[rollIndex + 1];
            }
            
            rollIndex += 2;
        }
    }

    private boolean isStrike(int throwIndex) {
        //フレームの2投目ならストライクではない
    	if (throwIndex % 2 == 1) {
    		return false;
    	}
    	
    	//倒したピンの数が10ピンでなければストライクではない
    	if (pinsDownList[throwIndex] != 10) {
    		return false;
    	}
    	
    	return true;
    }

    private boolean isSpare(int throwIndex) {    	
        return pinsDownList[throwIndex] + pinsDownList[throwIndex + 1] == MAX_PINS;
    }

    public int getFrameScore(int frameIndex) {
        return frameScores[frameIndex];
    }

    /**
     * ゲームが終了しているかどうかを判断するメソッド。
     * @return ゲームが終了していればtrue、そうでなければfalse
     */
    public boolean isGameOver() {
    	
    	//最大投球回数以上投げているなら終了している
    	if (currentThrowIndex >= MAX_THROWS - 1) {
    		return true;
    	}
    	
    	//最終フレームに達していないなら終了していない
    	if (currentFrameIndex < MAX_FRAMES - 1) {
    		return false;
    	}
    	
    	//以降は最終フレーム目を前提とした判断ロジック
    	
    	//2投目まで投げ終わっている（=20投目以降である）、かつ1投目と2投目の合計が10本未満なら終了している
    	if (currentThrowIndex >= MAX_THROWS - 2 && 
    		pinsDownList[19] + pinsDownList[20] < MAX_PINS) {
    		return true;
    	}
    	                      
    	return false;
    }
    
    public int getTotalScore() {
        return Arrays.stream(frameScores).sum();
    }

    public int getPinsDown(int throwIndex) {
        return pinsDownList[throwIndex];
    }
    
    public void printScores() {
    	for (int i = 0; i <= currentFrameIndex; i++) {
    		if (frameScores[i] != -1) {
    			System.out.printf("Frame %d: %d\n", i + 1, frameScores[i]);
    		} else {
    			System.out.printf("Frame %d: \n", i + 1);
    		}
    	}
    	System.out.printf("Total Score: %d\n", getTotalScore());
    }

    public static void main(String[] args) {
        BowlingScore game;
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
		for (int i = 0; i < MAX_THROWS; i++) {
		    try {
		    	System.out.printf("Frame %d, roll %d:\n", game.currentFrameIndex + 1, game.currentThrowIndex % 2 + 1);
		        game.throwBall(scanner.nextInt());
		        game.printScores();		 
		        System.out.printf("───────────\n");
		    } catch (IllegalArgumentException e) {
		        System.out.println(e.getMessage());
		        i--;
		    }
		}
	}
}
