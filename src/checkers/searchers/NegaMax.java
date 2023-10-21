package checkers.searchers;

import checkers.core.Checkerboard;
import checkers.core.CheckersSearcher;
import checkers.core.Move;
import core.Duple;

import java.util.Optional;
import java.util.function.ToIntFunction;

public class NegaMax extends CheckersSearcher {
    private int numNodes = 0;

    public NegaMax(ToIntFunction<Checkerboard> e) {
        super(e);
    }

    @Override
    public int numNodesExpanded() {
        return numNodes;
    }
    public int depthTracking(Checkerboard c, int depth) {
        if (c.gameOver()) {
            if (c.playerWins(c.getCurrentPlayer())) {
                return Integer.MAX_VALUE;
            }
            return -Integer.MAX_VALUE;
        }
        else if (depth == 0) {
            return getEvaluator().applyAsInt(c);
        }
        else {
            int best = -Integer.MAX_VALUE;
            for (Checkerboard n: c.getNextBoards()) {
                numNodes++;
                int negation = c.getCurrentPlayer() != n.getCurrentPlayer() ? -1 : 1;
                int score = depthTracking(n, depth - 1) * negation;
                if (best < score) {
                    best = score;
                }
            }
            return best;
        }
    }

    @Override
    public Optional<Duple<Integer, Move>> selectMove(Checkerboard board) {
        Optional<Duple<Integer, Move>> best = Optional.empty();
        for (Checkerboard alternative: board.getNextBoards()) {
            numNodes += 1;
            int negation = board.getCurrentPlayer() != alternative.getCurrentPlayer() ? -1 : 1;
            int scoreFor = negation * depthTracking(board, getDepthLimit() - 1);
            if (best.isEmpty() || best.get().getFirst() < scoreFor) {
                best = Optional.of(new Duple<>(scoreFor, alternative.getLastMove()));
            }
        }
        return best;
    }
}
