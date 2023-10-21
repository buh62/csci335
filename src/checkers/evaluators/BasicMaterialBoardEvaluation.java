package checkers.evaluators;

import checkers.core.Checkerboard;
import checkers.core.PlayerColor;

import java.util.function.ToIntFunction;
import java.util.function.ToDoubleFunction;

public class BasicMaterialBoardEvaluation implements ToIntFunction<Checkerboard> {
    public int applyAsInt(Checkerboard c) {
        return c.numPiecesOf(c.getCurrentPlayer()) - c.numPiecesOf(c.getCurrentPlayer().opponent());
    }
}
