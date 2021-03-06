package nl.hro.dta01.lesson.two;

import nl.hro.dta01.lesson.two.importer.UserItemDataImporter;
import nl.hro.dta01.lesson.two.matrix.*;
import nl.hro.dta01.lesson.four.model.Tuple;
import nl.hro.dta01.lesson.two.model.UserPreference;

import java.util.*;

public class Main {

    private static Map<Integer, UserPreference> userPreferences;
    private static boolean dataIsComplete;

    public static void main(String[] args) {
        dataIsComplete = true;

        userPreferences = UserItemDataImporter.ImportUserItemDataIntoUserPreferences();    // Loads the userItem.data file into the TreeMap
        //userPreferences = MovieLensDataImporter.ImportUserItemDataIntoUserPreferences();    // Loads the ml-100k/u.data file into the TreeMap

        /*
        Show the data for each user
         */
        for (Integer key : userPreferences.keySet()){
            System.out.println("User " + key + ":");

            UserPreference userPreference = userPreferences.get(key);
            for (Integer productKey : userPreference.getRatings().keySet()){
                System.out.println("\tProductId: " + productKey + ", value: " + userPreference.getRatings().get(productKey));
            }
        }

        /*
        Searches the imported data and checks if "holes" exist. If there are holes found in the data, the program saves the dataIsComplete as FALSE.
        When dataIsComplete is false the program will use the CosineSimilarity algoritm
         */
        for (int i = 1; i < userPreferences.size(); i++) {
            UserPreference a = userPreferences.get(i);
            UserPreference b = userPreferences.get(i+1);

            for (Integer productKey : a.getRatings().keySet()){
                if(!b.getRatings().keySet().contains(productKey)){
                    dataIsComplete = false;
                    break;
                }
            }
        }

        /*
        Find 3 nearest neighbours of user 7 using the PearsonCoefficient algoritm
         */
        System.out.println("Finding 3 nearest neighbours of user 7 using the PearsonCoefficient algoritm");
        List<Tuple<Integer, Double>> similaritiesToTargetUser = getNearestNeighboursUsingAlgoritm(7, 3, 0.35, PearsonCoefficient.class);
        System.out.println("The 3 nearest neighbours are:");
        for (Tuple<Integer, Double> aSimilaritiesToTargetUser : similaritiesToTargetUser) {
            System.out.println("\t" + aSimilaritiesToTargetUser.getA() + ": " + aSimilaritiesToTargetUser.getB());
        }

        /*
        Find 3 nearest neighbours of user 7 using the Euclidian algoritm
         */
        System.out.println("Finding 3 nearest neighbours of user 7 using the Euclidian algoritm");
        similaritiesToTargetUser = getNearestNeighboursUsingAlgoritm(7, 3, 0.35, EuclideanDistance.class);
        System.out.println("The 3 nearest neighbours are:");
        for (Tuple<Integer, Double> aSimilaritiesToTargetUser : similaritiesToTargetUser) {
            System.out.println("\t" + aSimilaritiesToTargetUser.getA() + ": " + aSimilaritiesToTargetUser.getB());
        }

        /*
        Find 3 nearest neighbours of user 7 using the Cosine algoritm
         */
        System.out.println("Finding 3 nearest neighbours of user 7 using the Cosine algoritm");
        similaritiesToTargetUser = getNearestNeighboursUsingAlgoritm(7, 3, 0.35, CosineSimilarity.class);
        System.out.println("The 3 nearest neighbours are:");
        for (Tuple<Integer, Double> aSimilaritiesToTargetUser : similaritiesToTargetUser) {
            System.out.println("\t" + aSimilaritiesToTargetUser.getA() + ": " + aSimilaritiesToTargetUser.getB());
        }

        /*
        Calculate similarity between user 3 and 4 using Pearson Coefficient
         */
        System.out.println("Calculating the similarity between user 3 and 4 using the Pearson Coefficient algoritm");
        double similarity = calculateSimilarityUsingGivenAlgorithm(3, 4, PearsonCoefficient.class);
        System.out.println("The similarity between user 3 and 4 is " + similarity);

        // Get 3 nearest neighbours for user 7 using the Pearson Coefficient
        similaritiesToTargetUser = getNearestNeighboursUsingAlgoritm(7, 3, 0.35, PearsonCoefficient.class);
        System.out.println("The 3 nearest neighbours are:");
        for (Tuple<Integer, Double> aSimilaritiesToTargetUser : similaritiesToTargetUser) {
            System.out.println("\t" + aSimilaritiesToTargetUser.getA() + ": " + aSimilaritiesToTargetUser.getB());
        }
        predictUserRatingForItem(7, similaritiesToTargetUser, userPreferences, 101);
        predictUserRatingForItem(7, similaritiesToTargetUser, userPreferences, 103);
        predictUserRatingForItem(7, similaritiesToTargetUser, userPreferences, 106);

        // Get 3 nearest neighbours for user 4 using the Pearson Coefficient
        similaritiesToTargetUser = getNearestNeighboursUsingAlgoritm(4, 3, 0.35, PearsonCoefficient.class);
        System.out.println("The 3 nearest neighbours are:");
        for (Tuple<Integer, Double> aSimilaritiesToTargetUser : similaritiesToTargetUser) {
            System.out.println("\t" + aSimilaritiesToTargetUser.getA() + ": " + aSimilaritiesToTargetUser.getB());
        }

        predictUserRatingForItem(4, similaritiesToTargetUser, userPreferences, 101);

        /*
        Insert rating for product 106 and user 7 into the dataset
         */
        System.out.println("Insert rating for product 106 and user 7 into the dataset");
        userPreferences.get(7).getRatings().put(106, 2.8);

        // Get 3 nearest neighbours for user 7 using the Pearson Coefficient
        similaritiesToTargetUser = getNearestNeighboursUsingAlgoritm(7, 3, 0.35, PearsonCoefficient.class);
        System.out.println("Recalculate the predicted ratings for products 101 and 103 for user 7");
        predictUserRatingForItem(7, similaritiesToTargetUser, userPreferences, 101);
        predictUserRatingForItem(7, similaritiesToTargetUser, userPreferences, 103);


        /*
        Insert rating for product 106 and user 7 into the dataset
         */
        System.out.println("Update rating for product 106 and user 7 in the dataset");
        userPreferences.get(7).getRatings().put(106, (double)5);

        // Get 3 nearest neighbours for user 7 using the Pearson Coefficient
        similaritiesToTargetUser = getNearestNeighboursUsingAlgoritm(7, 3, 0.35, PearsonCoefficient.class);
        System.out.println("Recalculate the predicted ratings for products 101 and 103 for user 7");
        predictUserRatingForItem(7, similaritiesToTargetUser, userPreferences, 101);
        predictUserRatingForItem(7, similaritiesToTargetUser, userPreferences, 103);
    }

    /*
    Predicts the rating for a product based on similarities from neighbours of the target user
     */
    public static void predictUserRatingForItem(int targetUser_id, List<Tuple<Integer, Double>> similaritiesToTargetUser, Map<Integer, UserPreference> userPrefrences, int product_item) {
        System.out.println("The " + similaritiesToTargetUser.size() + " nearest neighbours are:");
        for (Tuple<Integer, Double> aSimilaritiesToTargetUser : similaritiesToTargetUser) {
            System.out.println("\t" + aSimilaritiesToTargetUser.getA() + ": " + aSimilaritiesToTargetUser.getB());
        }

        // Get the ratings and similarities needed for the predicted rating calculation
        List<Tuple<Double, Double>> nearestNeighboursRatingForProduct = getRatingAndSimilarityFromNeighboursAboutProduct(similaritiesToTargetUser, userPrefrences, product_item);
        // Calculate the predicted rating
        double predictedUserRatingForProduct = getPredictedRating(nearestNeighboursRatingForProduct);
        System.out.println("Predicted rating for user " + targetUser_id + " for item " + product_item + " is " + predictedUserRatingForProduct);
    }

    /*
    Get the nearest neighbours using the strategy specified by the user
     */
    public static List<Tuple<Integer, Double>> getNearestNeighboursUsingAlgoritm(int targetUser_id, int numOfNeighbours, double threshold, Class<? extends SimilarityStrategy> strategy){
        // Setup an empty list, which we will fill later
        List<Tuple<Integer, Double>> similaritiesToTargetUser = new ArrayList<>();
        for (int i = 0; i < numOfNeighbours; i++) {
            similaritiesToTargetUser.add(new Tuple<>(0, (double)-1));
        }

        // Loop through all users, unless the user is the target user
        for (int i = 1; i <= userPreferences.size(); i++) {
            if((i <= userPreferences.size()) && (i != targetUser_id)) {
                double similarity;
                if(strategy == null)    // Use the strategy chosen by the program(Cosine for sparse data, Pearson otherwise)
                    similarity = calculateSimilarity(targetUser_id, (i));
                else    // Use the strategy chosen by the user
                    similarity = calculateSimilarityUsingGivenAlgorithm(targetUser_id, i, strategy);
                System.out.println("Similarity between user " + targetUser_id + " and " + i + ": " + similarity);

                if(similarity > threshold) {    // If the similarity is higher than the threshold, update the list
                    Optional<Tuple<Integer, Double>> firstZeroSimilarityInList =
                            similaritiesToTargetUser.stream().filter((Tuple<Integer, Double> similarityToUserOne) -> similarityToUserOne.getB() == (double)-1).findFirst();

                    try {
                        // Try to find the first zero similarity in the list and update it with the new similarity
                        Tuple<Integer, Double> sim = firstZeroSimilarityInList.get();
                        similaritiesToTargetUser.set(similaritiesToTargetUser.indexOf(sim), new Tuple<>(i, similarity));    // Tuple parameters are: i -> user_id, similarity->similarity to the target user
                    } catch (NoSuchElementException e) {    // If there is no zero in the list, find the lowest similarity
                        Optional<Tuple<Integer, Double>> lowestSimilarityInList = similaritiesToTargetUser.stream().min((Tuple<Integer, Double> one, Tuple<Integer, Double> two) -> {
                            if (one.getB() > two.getB()) {
                                return 1;
                            } else if (two.getB() > one.getB()) {
                                return -1;
                            } else {
                                return 0;
                            }
                        });
                        Tuple<Integer, Double> sim = lowestSimilarityInList.get();
                        if (similarity > sim.getB()) // Update the similarity in the list if the calculated similarity is higher
                            similaritiesToTargetUser.set(similaritiesToTargetUser.indexOf(sim), new Tuple<>(i, similarity));
                    }
                }
            }
        }
        return similaritiesToTargetUser;
    }

    public static double calculateSimilarity(int userOne_id, int userTwo_id){
        return calculateSimilarityUsingGivenAlgorithm(userOne_id, userTwo_id, null);
    }

    public static double calculateSimilarityUsingGivenAlgorithm(int userOne_id, int userTwo_id, Class<? extends SimilarityStrategy> strategy){
        UserPreference personOne = userPreferences.get(userOne_id);
        UserPreference personTwo = userPreferences.get(userTwo_id);

        if(strategy == null)
            return SimilarityMatrix.getInstance().calculateSimilarity(dataIsComplete, personOne, personTwo);
        else
            return SimilarityMatrix.getInstance().calculateSimilarityUsingGivenAlgorithm(dataIsComplete, personOne, personTwo, strategy);
    }

    /*
    Calculates the predicted rating based on user ratings and similarity
    Tuple is in form of: <user rating, similarity>
     */
    public static double getPredictedRating(List<Tuple<Double, Double>> userRatings){
        double a = 0, b = 0;
        for (Tuple<Double, Double> userRating : userRatings) {
            a += userRating.getB() * userRating.getA();
            b += userRating.getB();
        }
        return a/b;
    }

    /*
    Gets the ratings of a product from the list of nearest neighbours
     */
    public static List<Tuple<Double, Double>> getRatingAndSimilarityFromNeighboursAboutProduct(List<Tuple<Integer, Double>> similaritiesToTargetUser, Map<Integer, UserPreference> userPrefrences, int product_id){
        List<Tuple<Double, Double>> returnList = new ArrayList<>();

        for (Tuple<Integer, Double> aSimilaritiesToTargetUser : similaritiesToTargetUser) {
            int user_id = aSimilaritiesToTargetUser.getA();
            double similarityToUser = aSimilaritiesToTargetUser.getB();
            if (userPrefrences.get(user_id).getRatings().get(product_id) != null) {
                double user_rating = userPrefrences.get(user_id).getRatings().get(product_id);

                returnList.add(new Tuple<>(user_rating, similarityToUser));
            }
        }
        return returnList;
    }
}
