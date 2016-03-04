package nl.hro.dta01.lesson.two.matrix;

import nl.hro.dta01.lesson.two.model.UserPreference;

public class EuclideanDistance implements SimilarityStrategy{

    protected EuclideanDistance(){}

    @Override
    public double calculateSimilarity(UserPreference a, UserPreference b) {
        double sim = 0;
        for (int i = 0; i < a.getRatings().size(); i++) {
            String product = (String) a.getRatings().keySet().toArray()[i];
            sim += Math.pow(a.getRatings().get(product) - b.getRatings().get(product), 2);
        }
        sim = Math.sqrt(sim);
        return  1/(1+sim);
    }
}
