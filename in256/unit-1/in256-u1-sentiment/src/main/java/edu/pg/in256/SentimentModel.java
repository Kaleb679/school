package edu.pg.in256;

import weka.core.converters.CSVLoader;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

// pseudocode for negation terms
// Stack<String> modifiers = new Stack<>();

// for (String word : tokens) {
//     if (isNegation(word)) {
//         modifiers.push(word);
//         continue;
//     }

//     int delta = 0;
//     if (positiveWords.contains(word)) delta = 1;
//     if (negativeWords.contains(word)) delta = -1;

//     // Invert polarity if a negation is active
//     if (!modifiers.isEmpty()) {
//         delta *= -1;
//         modifiers.pop(); // consume one negation
//     }

//     score += delta;
// }


public class SentimentModel {
    private Instances data;

    // These words track sentiment in underlying reviews and are assigned positive
    // or negative here

    // O(1) lookup for each token -> linear-time scan over tokens per review
    // limited by negation "not good service"
    // lets add a stack to fix

    // Lexicon for sentiment math
    private final Set<String> positiveWords = new HashSet<>(Arrays.asList(
            "love", "loved", "great", "excellent", "good", "fantastic", "amazing", "wonderful","worth",
            "recommend", "satisfied", "happy", "perfect", "best", "awesome"));

    private final Set<String> negativeWords = new HashSet<>(Arrays.asList(
            "bad", "terrible", "awful", "worst", "poor", "disappoint", "hate", "horrible",
            "never", "mediocre", "problem", "slow", "broken", "dirty"));

    /** Loads CSV and processes into Weka dataset */
    public void loadCSV(File csvFile) throws Exception {
        CSVLoader loader = new CSVLoader();
        loader.setSource(csvFile);
        data = loader.getDataSet();

        if (data.numAttributes() == 0) {
            throw new Exception("CSV file has no attributes.");
        }

        // Convert text to word vectors for future ML use
        StringToWordVector filter = new StringToWordVector();
        filter.setInputFormat(data);
        data = Filter.useFilter(data, filter);
    }

    public String summarize() {
        if (data == null)
            return "No data loaded.";
        return "Loaded " + data.numInstances() + " reviews with " + data.numAttributes() + " attributes.";
    }


    public String analyzeSentiment(File csvFile) {
        try {
            CSVLoader loader = new CSVLoader();
            loader.setSource(csvFile);
            Instances raw = loader.getDataSet();

            int positiveCount = 0, negativeCount = 0;
            StringBuilder detailed = new StringBuilder("=== INDIVIDUAL REVIEW ANALYSIS ===\n\n");

            for (int i = 0; i < raw.numInstances(); i++) {
                String review = raw.instance(i).toString().toLowerCase();
                int score = computePolarity(review);
                String sentiment = (score > 0) ? "Positive" : (score < 0 ? "Negative" : "Neutral");

                detailed.append(String.format("Review %d: %s → Sentiment: %s%n", i + 1, review, sentiment));

                if (score > 0)
                    positiveCount++;
                else if (score < 0)
                    negativeCount++;
            }

            int total = raw.numInstances();
            int neutralCount = total - positiveCount - negativeCount;

            double posPct = (positiveCount * 100.0) / total;
            double negPct = (negativeCount * 100.0) / total;
            double polarity = (positiveCount - negativeCount) / (double) total;

            detailed.append("\n=== SENTIMENT SUMMARY ===\n");
            detailed.append(String.format("Total Reviews: %d%n", total));
            detailed.append(String.format("Positive: %d (%.1f%%)%n", positiveCount, posPct));
            detailed.append(String.format("Negative: %d (%.1f%%)%n", negativeCount, negPct));
            detailed.append(String.format("Neutral: %d%n", neutralCount));
            detailed.append(String.format("Polarity Score: %.2f%n", polarity));

            return detailed.toString();

        } catch (Exception e) {
            return "Error during sentiment analysis: " + e.getMessage();
        }
    }

    /** Compute basic polarity by counting positive/negative word hits */
    // private int computePolarity(String text) {
    // int score = 0;
    // for (String word : text.split("\\W+")) {
    // if (positiveWords.contains(word)) score++;
    // if (negativeWords.contains(word)) score--;
    // }
    // return score;
    // }

    // use stack to track negation of prev sentiment words


    // computePolarity will use a stack to track modifiers, and a lexicon to track sentiment type, returning a score where a positive number is a good review, zero is neutral, and negative as such.
    private int computePolarity(String text) {
        int score = 0;
        Stack<String> modifiers = new Stack<>();
        String[] tokens = text.split("\\W+");

        for (String word : tokens) {
            if (word.isEmpty())
                continue;

            // Negations and boosters
            // if we get a negation or mod, add it to the stack and go straight to the next word without changing the score right now
            if (isNegation(word)) {
                modifiers.push("NEG");
                continue;
            }

            if (isBooster(word)) {
                modifiers.push("BOOST");
                continue;
            }

            // init delta at zero, add our base value from a sentiment based word
            int delta = 0;
            //delta defines our score from the stack and sentimentWord
            if (positiveWords.contains(word))
                delta = 1;
            if (negativeWords.contains(word))
                delta = -1;

            //take our modifiers and combine them with the sentiment base from this current word
            if (!modifiers.isEmpty()) {
                String mod = modifiers.pop();
                if (mod.equals("NEG"))
                    delta *= -1;
                else if (mod.equals("BOOST"))
                    delta *= 2;
            }

            //tally running score
            score += delta;
        }

        return score;
    }

    // is a word a negation?
    private boolean isNegation(String word) {
        return word.equals("not") || word.equals("never") || word.equals("no") || word.equals("don’t");
    }

    // is a word a superlative or adjective that might modify a sentimentWord?
    private boolean isBooster(String word) {
        return word.equals("very") || word.equals("extremely") || word.equals("really");
    }
}