import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BowlingScoreTest {
	
	private BowlingScore game;
	
    @BeforeEach
    public void setUp() {
        game = new BowlingScore();
    }

    @Test
    public void 一投で11ピン以上倒す入力があった場合に例外が発生する() {
        assertThrows(IllegalArgumentException.class, () -> game.roll(11));
    }
    
    @Test
    public void 一つのフレームで合計ピンが11以上になる場合に例外が発生する() {
        game.roll(6);
        assertThrows(IllegalArgumentException.class, () -> game.roll(5));
    }
	
    @Test
    public void 全ての投球でガーター() {
        rollMany(20, 0);
        assertEquals(0, game.getTotalScore());
    }

    @Test
    public void 全ての投球で1ピンずつ倒した場合() {
        rollMany(20, 1);
        assertEquals(20, game.getTotalScore());
    }

    @Test
    public void 全ての投球でスペアを取った場合() {
        for (int i = 0; i < 10; i++) {
            game.roll(5);
            game.roll(5);
        }
        // 最終フレームでスペアを取った場合、1回追加で投げられる
        game.roll(5);
        assertEquals(150, game.getTotalScore());
    }

    @Test
    public void 全ての投球でストライク_パーフェクトゲーム() {
        rollMany(12, 10);
        assertEquals(300, game.getTotalScore());
    }
   
    @Test
    public void フレームの最初の投球で全てのピンを倒さなかった場合_そのフレームのスコアは2回の投球で倒したピンの数となる() {
    	game.roll(5);
    	game.roll(3);
        rollMany(0, 18);  // 2-10フレーム目は全て0ピン
        assertEquals(8, game.getFrameScore(0));
    }
    
    @Test
    public void フレームの1投目で全てのピンを倒した場合_そのフレームの得点は10点に加え次の2回の投球で倒したピンの数が加算される() {
        game.roll(10);  // ストライク
        game.roll(3);
        game.roll(4);
        rollMany(0, 16);  // 残りのフレームは全て0ピン
        assertEquals(17, game.getFrameScore(0));
    }

    @Test
    public void フレームの2投目で全てのピンを倒した場合_そのフレームの得点は10点に加え次の1投のピンの数が加算される() {
        game.roll(5);
        game.roll(5);  // スペア
        game.roll(3);
        rollMany(0, 17);  // 残りのフレームは全て0ピン
        assertEquals(13, game.getFrameScore(0));
    }

    @Test
    public void 最終フレームでストライクまたはスペアを投じた場合_追加の投球で倒したピンの数も10フレーム目の得点に加算される() {
        rollMany(0, 18);  // 1-9フレーム目は全て0ピン
        game.roll(10);  // ストライク
        game.roll(3);
        game.roll(4);
        assertEquals(17, game.getFrameScore(9));
    }

    @Test
    public void 最終フレームで2投なげて全ピン倒せなかった場合にゲームが終了する() {
        rollMany(18, 0);
        game.roll(4);
        game.roll(5);
        Assertions.assertTrue(game.isGameOver());
    }
    
    @Test
    public void ゲームの最終スコアは_10フレーム全てのスコアの合計となる() {
        rollMany(20, 1);  // 全ての投球で1ピンずつ倒す
        assertEquals(20, game.getTotalScore());
    }

    @Test
    public void 入力されるスコアの値は0から10の範囲でなければならない() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            game.roll(11);
        });
    }

    @Test
    public void フレームの1投目と2投目で合計で倒せるピンの数も最大10ピンでなければならない() {
        game.roll(5);
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            game.roll(6);
        });
    }
    
    private void rollMany(int numberOfRolls, int pins) {
        for (int i = 0; i < numberOfRolls; i++) {
            game.roll(pins);
        }
    }
}
