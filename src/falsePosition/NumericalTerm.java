package falsePosition;

import java.util.*;

/*
A class in relation with the coefficients of the polynomial function
 */
public class NumericalTerm {
    ArrayList<Float> coefficients = new ArrayList<>();

    // Asks the user to input the coefficients of the polynomial function
    public ArrayList<Float> inputConstants(Scanner scanner, int highestPow) {
        System.out.println("Enter the numerical component of each term from highest to lowest degree of the exponent " +
                "(including 0).");
        for (int i = 0; i <= highestPow; i++) {
            int exponent = highestPow - i;
            System.out.print("Coefficient of the variable with " + exponent + " as the exponent: ");
            float constant = scanner.nextFloat();
            coefficients.add(constant);
        }
        return coefficients;
    }

    // Identifies the highest coefficient which is needed for determining the lower and higher guesses of the root
    public float getHighestValue(ArrayList<Float> coefficients) {
        this.coefficients = coefficients;
        float highestValue = coefficients.get(0);
        for (int i = 1; i < coefficients.size(); i++) {
            if (highestValue < coefficients.get(i)) {
                highestValue = coefficients.get(i);
            }
        }
        return highestValue;
    }
}
