import java.util.Scanner;

public class BowlingScore {
	
    private static final int MAX_PINS = 10;
    private static final int NUM_FRAMES = 10;
    private int[] rolls = new int[21];
    private int currentRoll = 0;
    private int[] frameScores = new int[NUM_FRAMES];

    /**
     * ピンを倒すためのメソッドです。
     *
     * @param pins 倒したピンの数
     */
    public void roll(int pins) {
        if (pins < 0 || pins > MAX_PINS) {
            throw new IllegalArgumentException("Invalid number of pins: " + pins);
        }
        // Add check to ensure the sum of pins in a frame does not exceed 10
        if(currentRoll % 2 == 1 && !isStrike(currentRoll - 1) && pins + rolls[currentRoll - 1] > MAX_PINS){
            throw new IllegalArgumentException("The total pins knocked down in a frame cannot exceed " + MAX_PINS);
        }
        rolls[currentRoll++] = pins;
    }


    public int getFrameScore(int frameNumber) {
        return frameScores[frameNumber];
    }

    public int getTotalScore() {
        int total = 0;
        for (int score : frameScores) {
            total += score;
        }
        return total;
    }
    
    public void calculateScores() {
        int frameIndex = 0;

        for (int frame = 0; frame < 10; frame++) {
            if (isStrike(frameIndex)) {
                if(frameIndex+2 >= currentRoll) {
                    frameScores[frame] = -1; // Score for this frame not yet available
                } else {
                    frameScores[frame] = 10 + strikeBonus(frameIndex);
                }
                frameIndex++;
            } else if (isSpare(frameIndex)) {
                if(frameIndex+2 >= currentRoll) {
                    frameScores[frame] = -1; // Score for this frame not yet available
                } else {
                    frameScores[frame] = 10 + spareBonus(frameIndex);
                }
                frameIndex += 2;
            } else {
                if(frameIndex+1 >= currentRoll) {
                    frameScores[frame] = -1; // Score for this frame not yet available
                } else {
                    frameScores[frame] = sumOfPinsInFrame(frameIndex);
                }
                frameIndex += 2;
            }
        }
    }

    /**
     * スコアを表示します。
     */
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

    private boolean isStrike(int frameIndex) {
        return rolls[frameIndex] == 10;
    }

    private int sumOfPinsInFrame(int frameIndex) {
        return rolls[frameIndex] + rolls[frameIndex + 1];
    }

    private int spareBonus(int frameIndex) {
        return rolls[frameIndex + 2];
    }

    private int strikeBonus(int frameIndex) {
        return rolls[frameIndex + 1] + rolls[frameIndex + 2];
    }

    private boolean isSpare(int frameIndex) {
        return rolls[frameIndex] + rolls[frameIndex + 1] == 10;
    }

    public static void main(String[] args) {
        BowlingScore game;
        Scanner scanner = new Scanner(System.in);
        String playAgain;
        do {
            game = new BowlingScore();
            int frame = 1;
            int roll = 1;
            for (int i = 0; i < 21; i++) {
                System.out.println("Frame " + frame + ", roll " + roll);
                int pins;
                do {
                    System.out.println("Enter number of pins knocked down (0-10):");
                    pins = scanner.nextInt();
                } while (pins < 0 || pins > 10);
                game.roll(pins);
                game.calculateScores();
                game.printScores();

                if (frame < 10 && roll == 1 && pins == 10) {
                    frame++;
                } else if (frame < 10 && roll == 2) {
                    frame++;
                    roll = 1;
                } else if (frame == 10) {
                    roll++;
                } else {
                    roll++;
                }

                // If we've played 10 frames, break out of the loop
                if (frame > 10 && game.frameScores[9] != -1 || frame == 10 && roll > 3) {
                    break;
                }
            }
            System.out.println("Game Over");
            System.out.println("Would you like to play again? (yes/no)");
            playAgain = scanner.next();
        } while (playAgain.equalsIgnoreCase("yes"));
        scanner.close();
        System.out.println("Thank you for playing!");
    }
}
