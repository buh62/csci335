package search.bestfirst;

import search.SearchNode;
import search.SearchQueue;

import java.util.*;
import java.util.function.ToIntFunction;

public class BestFirstQueue<T> implements SearchQueue<T> {
    // TODO: Implement this class
    private PriorityQueue <SearchNode<T>> queue;
    private HashMap<T, Integer> bestEstimateSoFar = new HashMap<>();
    // HINT: Use java.util.PriorityQueue. It will really help you.
    private ToIntFunction<T> heuristic;
    public BestFirstQueue(ToIntFunction<T> heuristic) {
        this.heuristic = heuristic;
        queue = new PriorityQueue<>(new Comparator<SearchNode<T>>() {
            @Override
            public int compare(SearchNode<T> o1, SearchNode<T> o2) {
                int length1 = heuristic.applyAsInt(o1.getValue()) + o1.getDepth();
                int length2 = heuristic.applyAsInt(o2.getValue()) + o2.getDepth();
                if (length1 < length2) {
                    return -1;
                }
                else if (length1 == length2) {
                    return 0;
                }
                else { return 1;}

            }
        });
    }

    @Override
    public void enqueue(SearchNode<T> node) {
        int length = heuristic.applyAsInt(node.getValue()) + node.getDepth();
        if (!bestEstimateSoFar.containsKey(node.getValue()) || bestEstimateSoFar.get(node.getValue()) > length) {
            queue.add(node);
            bestEstimateSoFar.put(node.getValue(), length);
        }
    }

    @Override
    public Optional<SearchNode<T>> dequeue() {
        if (queue.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(queue.remove());
        }
    }
}
