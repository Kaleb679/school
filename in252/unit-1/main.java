import java.text.DecimalFormat;

class Main {
    public static void main(String[] args) {
        String name = "John Smith";
        String address = "101 N. Main Street";
        String city = "AnyTown";
        String state = "TX";
        String zip = "11111";
        String unitsTaken = "19";
        String previousPerformance = "Good";

        // Define Constants
        final double PRICE_PER_UNIT = 100.50;
        final int BASE_DISCOUNT = 150;
        final double AI_PERFORMANCE_MULTIPLIER = 1.1;

        //Process units with ai recs
        int intUnitsTaken = Integer.parseInt (unitsTaken);
        intUnitsTaken = intUnitsTaken +1;

        //calculate initial tuition
        double tuition = PRICE_PER_UNIT * intUnitsTaken;

        double aiAdjustedDiscount = BASE_DISCOUNT;
        if (previousPerformance.equalsIgnoreCase("Good")) {
            aiAdjustedDiscount *= AI_PERFORMANCE_MULTIPLIER;
        }

        //calc finals
        double afterDiscount = tuition - aiAdjustedDiscount;
        double monthlyPayment = afterDiscount / 12.0;

        //Format for display
        DecimalFormat currencyFormat = new DecimalFormat("$#,##0.00");
        System.out.println("=== AI-Enhanced HVAC Course Registration Summary ===");
        System.out.println("Name: " + name);
        System.out.println("Address: " + address);
        System.out.println("City: " + city);
        System.out.println("State: " + state);
        System.out.println("ZIP code: " + zip);
        System.out.println("AI-Recommended units: " + intUnitsTaken);
        System.out.println("Original tuition: " + currencyFormat.format(tuition));
        System.out.println("AI-adjusted discount: " + currencyFormat.format(aiAdjustedDiscount));
        System.out.println("Final tuition after discount: " + currencyFormat.format(afterDiscount));
        System.out.println("AI-recommended monthly payment: " + currencyFormat.format(monthlyPayment));

        // System.out.prinln("Hello");
    }
}
