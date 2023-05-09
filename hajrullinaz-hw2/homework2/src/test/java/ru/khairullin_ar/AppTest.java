package ru.khairullin_ar;

import org.junit.jupiter.api.Test;
import org.assertj.core.api.Assertions;

public class AppTest {
    @Test
    void helloTest() {
        Assertions.assertThat(2 + 2).isEqualTo(4);
    }

    @Test
    void simpleTest() {
        String[] data = {
                "a1 a3 b2 c1 c3 d2 e1 e3 f2 g1 g3 h2",
                "a7 b6 b8 c7 d6 d8 e7 f6 f8 g7 h6 h8",
                "g3-f4 f6-e5", "c3-d4 e5:c3", "b2:d4 d6-c5", "d2-c3 g7-f6", "h2-g3 h8-g7",
                "c1-b2 f6-g5", "g3-h4 g7-f6", "f4-e5 f8-g7"
        };

        String answer = "a1 a3 b2 c3 d4 e1 e3 e5 f2 g1 h4 \n"
                + "a7 b6 b8 c5 c7 d8 e7 f6 g5 g7 h6 ";

        String result = App.play(data);

        Assertions.assertThat(result).isEqualTo(answer);
    }

    @Test
    void invalidMoveTest() {
        String[] data = {
                "E7 G3 b4",
                "a7 b8 h6",
                "b4-c5 b8-c7", "G3:B8 h6-g5", "E7:H4 a7-b6", "c5-d6 b6-a5"
        };

        String answer = "invalid move";

        String result = App.play(data);

        Assertions.assertThat(result).isEqualTo(answer);
    }

    @Test
    void busyCellTest() {
        String[] data = {
                "E7 G3 b4",
                "a7 b8 h6",
                "G3:B8 b8-c7"
        };

        String answer = "busy cell";

        String result = App.play(data);

        Assertions.assertThat(result).isEqualTo(answer);
    }

    @Test
    void whiteCellTest() {
        String[] data = {
                "E6 G3 b4",
                "a7 b8 h6",
                "b4-c5 b8-c7"
        };

        String answer = "white cell";

        String result = App.play(data);

        Assertions.assertThat(result).isEqualTo(answer);
    }

    @Test
    void simpleTest2() {
        String[] data = {
                "C3", "d4 f4 b4 f2",
                "C3:E5:G3:E1:A5"
        };

        String answer = "A5 \n";

        String result = App.play(data);

        Assertions.assertThat(result).isEqualTo(answer);
    }
}
