package learning.classifiers;

import core.Duple;
import learning.core.Classifier;
import learning.core.Histogram;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.function.ToDoubleBiFunction;

// KnnTest.test() should pass once this is finished.
public class Knn<V, L> implements Classifier<V, L> {
    private ArrayList<Duple<V, L>> data = new ArrayList<>();
    private ToDoubleBiFunction<V, V> distance;
    private int k;

    public Knn(int k, ToDoubleBiFunction<V, V> distance) {
        this.k = k;
        this.distance = distance;
    }

    // TODO: Find the distance from value to each element of data. Use Histogram.getPluralityWinner()
    //  to find the most popular label.
    @Override
    public L classify(V value) {
        PriorityQueue<Data<L>> info = new PriorityQueue<>();
        for (int i = 0; i < data.size(); i++) {
            double x = distance.applyAsDouble(data.get(i).getFirst(), value);
            info.add(new Data<>(data.get(i).getSecond(), x));
        }
        Histogram<L> pop = new Histogram<>();
        int y = 0;
        while (y < k && info.size() > 0) {
            Data<L> label = info.remove();
            pop.bump(label.call());
        }
        return pop.getPluralityWinner();
    }

    @Override
    public void train(ArrayList<Duple<V, L>> training) {
        // TODO: Add all elements of training to data.
        for (int i = 0; i < training.size(); i++) {
            data.add(i, training.get(i));
        }
    }
}
