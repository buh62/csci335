package learning.markov;

import learning.core.Histogram;

import java.util.*;

public class MarkovChain<L,S> {
    private LinkedHashMap<L, HashMap<Optional<S>, Histogram<S>>> label2symbol2symbol = new LinkedHashMap<>();

    public Set<L> allLabels() {return label2symbol2symbol.keySet();}

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (L language: label2symbol2symbol.keySet()) {
            sb.append(language);
            sb.append('\n');
            for (Map.Entry<Optional<S>, Histogram<S>> entry: label2symbol2symbol.get(language).entrySet()) {
                sb.append("    ");
                sb.append(entry.getKey());
                sb.append(":");
                sb.append(entry.getValue().toString());
                sb.append('\n');
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    // Increase the count for the transition from prev to next.
    // Should pass SimpleMarkovTest.testCreateChains().
    public void count(Optional<S> prev, L label, S next) {
        if (!label2symbol2symbol.containsKey(label)) {
            label2symbol2symbol.put(label, new HashMap<>());
        }
        HashMap<Optional<S>, Histogram<S>> temp = label2symbol2symbol.get(label);
        if (!temp.containsKey(prev)) {
            temp.put(prev, new Histogram<>());
        }
        temp.get(prev).bump(next);
    }

    // HashMap methods:
    // .containsKey()
    // .put()
    // .get()

    // Histogram methods:
    // .bump()
    // .getCountFor()
    // .getTotalCounts()

    // Optional
    // Optional.empty()
    // Optional.of(value)

    // Returns P(sequence | label)
    // Should pass SimpleMarkovTest.testSourceProbabilities() and MajorMarkovTest.phraseTest()
    //
    // HINT: Be sure to add 1 to both the numerator and denominator when finding the probability of a
    // transition. This helps avoid sending the probability to zero.
    public double probability(ArrayList<S> sequence, L label) {
        double prob = 1.0;
        Optional<S> prevLetter = Optional.empty();
        for (int i = 0; i < sequence.size(); i++) {
            if (label2symbol2symbol.get(label).containsKey(prevLetter)) {
                double n = label2symbol2symbol.get(label).get(prevLetter).getCountFor(sequence.get(i)) + 1.0;
                double d = label2symbol2symbol.get(label).get(prevLetter).getTotalCounts() + 1.0;
                prob = prob * (n / d);
            }
            prevLetter = Optional.of(sequence.get(i));
        }
        return prob;
    }

    // Return a map from each label to P(label | sequence).
    // Should pass MajorMarkovTest.testSentenceDistributions()
    public LinkedHashMap<L, Double> labelDistribution(ArrayList<S> sequence) {
        LinkedHashMap<L, Double> distribution = new LinkedHashMap<>();
        double overall = 0.0;
        for (L language : label2symbol2symbol.keySet()) {
            double odds = probability(sequence, language);
            distribution.put(language, odds);
            overall += odds;
        }
        for (L language : distribution.keySet()) {
            double chance = distribution.get(language) / overall;
            distribution.put(language, chance);
        }
        return distribution;
    }

    // Calls labelDistribution(). Returns the label with the highest probability.
    // Should pass MajorMarkovTest.bestChainTest()
    public L bestMatchingChain(ArrayList<S> sequence) {
        LinkedHashMap<L, Double> bestChoice = labelDistribution(sequence);
        return Collections.max(bestChoice.entrySet(), Map.Entry.comparingByValue()).getKey();
    }
}
