import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class BowlingScoreTest {

    @Test
    public void 全ての投球でガーター() {
        BowlingScore game = new BowlingScore();
        rollMany(game, 20, 0);
        game.calculateScores();
        assertEquals(0, game.getTotalScore());
    }

    @Test
    public void 全ての投球で1ピンずつ倒した場合() {
        BowlingScore game = new BowlingScore();
        rollMany(game, 20, 1);
        game.calculateScores();
        assertEquals(20, game.getTotalScore());
    }

    @Test
    public void 全ての投球でスペアを取った場合() {
        BowlingScore game = new BowlingScore();
        for (int i = 0; i < 10; i++) {
            game.roll(5);
            game.roll(5);
        }
        // 最終フレームでスペアを取った場合、1回追加で投げられる
        game.roll(5);
        game.calculateScores();
        assertEquals(150, game.getTotalScore());
    }

    @Test
    public void 全ての投球でストライク_パーフェクトゲーム() {
        BowlingScore game = new BowlingScore();
        rollMany(game, 12, 10);
        game.calculateScores();
        assertEquals(300, game.getTotalScore());
    }

    
    private void rollMany(BowlingScore game, int numberOfRolls, int pins) {
        for (int i = 0; i < numberOfRolls; i++) {
            game.roll(pins);
        }
    }
}
