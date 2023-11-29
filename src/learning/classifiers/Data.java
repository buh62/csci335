package learning.classifiers;

public class Data<L> implements Comparable<Data<L>> {
    private L label;
    private double dist;


    @Override
    public int compareTo(Data<L> o) {
        if (this.dist > o.dist) {
            return 1;
        }
        else if (this.dist < o.dist) {
            return -1;
        }
        return 0;
    }
    public Data(L label, double dist) {
        this.label = label;
        this.dist = dist;
    }
    public L call() {
        return label;
    }
}
