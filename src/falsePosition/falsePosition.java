/*
This is the implementation of the False Position method in finding a root of a
polynomial function.
Input: Highest degree of the variable; Coefficients of each term of the function
Output: A possible root of the function
Author: Jan Luis Antoc
Course: Numerical Methods
 */

package falsePosition;

import java.util.Scanner;
import java.util.ArrayList;

public class falsePosition {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int highestPow;
        ArrayList<Float> coefficients;  // for numerical coefficient of each term of the function
        ArrayList<Float> valuesForSolution; // contains the values for solving the root of the function
        float xl, xu, lowerEquation, upperEquation;
        boolean rootFound;


        highestPow = falsePosition.introduction(scanner);
        coefficients = falsePosition.getCoefficients(scanner, highestPow);

        // While the real root is not yet found after 10000 iterations, repeat the program
        do {
            // Result of the randomization of the guesses for the root of the function
            valuesForSolution = falsePosition.getGuesses(coefficients, highestPow);
            if (valuesForSolution != null) {
                xl = valuesForSolution.get(0);
                xu = valuesForSolution.get(1);
                lowerEquation = valuesForSolution.get(2);
                upperEquation = valuesForSolution.get(3);
            } else {
                // Some function can't be solved by false position method; in this case, the program stops
                return;
            }
            rootFound = solve(xl, xu, lowerEquation, upperEquation, coefficients, highestPow);
        } while (!rootFound);
    }

    // Asks the user about the order of the polynomial function
    // Only called once in finding the root of the function
    public static int introduction(Scanner scanner) {
        System.out.println("This is the Java implementation of FALSE POSITION method in finding a root of a " +
                "polynomial equation.");
        // Scans the input from the user (to be used all throughout the program)
        System.out.print("Enter the highest degree of the equation: ");
        int highestPow = scanner.nextInt();

        // This program is intended for polynomial function with the highest exponent greater than or equal to 2.
        while (highestPow <= 1) {
            System.out.println("Not applicable. Try another function.");
            System.out.print("Enter the highest degree of the equation: ");
            highestPow = scanner.nextInt();
        }
        return highestPow;
    }

    // Asks the user about the numerical coefficient of each term of the function
    public static ArrayList<Float> getCoefficients(Scanner scanner, int highestPow) {
        NumericalTerm numericalTerm = new NumericalTerm();
        ArrayList<Float> coefficients;
        coefficients = numericalTerm.inputCoefficients(scanner, highestPow);

        // The method used for determining the lower and upper guess in this program
        // would be difficult if one of the coefficients in the function is less than 1
        for (int i = 0; i < coefficients.size(); i++) {
            float coefficientOnCheck = Math.abs(coefficients.get(i));
            if (coefficientOnCheck < 1) {
                ArrayList<Float> newCoefficients = new ArrayList<>();
                for (float coefficient : coefficients) {
                    float newCoefficient = coefficient * 10; // Multiply each coefficients if it was found out that
                    newCoefficients.add(newCoefficient);    // one of the coefficients is less than 1
                }
                coefficients = newCoefficients;     // Modified array of the coefficients
            }
        }
        System.out.println("These are the coefficients of the (modified, if needed) function: " + coefficients);
        return coefficients;
    }

    // Randomizes the lower and upper guess for the root of the equation
    // Check Guesses.java for more information
    public static ArrayList<Float> getGuesses(ArrayList<Float> coefficients, int highestPow) {
        ArrayList<Float> values = new ArrayList<>();
        NumericalTerm numericalTerm = new NumericalTerm();
        Guesses lowerGuess = new Guesses();
        Guesses upperGuess = new Guesses();

        float baseValue = numericalTerm.getHighestValue(coefficients);
        float xl = 0.f, xu = 0.f;
        float lowerEquation = 0.f, upperEquation = 0.f;

        int guessChecker = 0;       // Checks how many randomization of the lower and upper guesses were done
        // Based on the first step of false position method, the following should be met:
        // 1. Lower guess (xl) of the root should be less than the upper guess (xu) of the root
        // 2. Substitute xl and xu individually. The product of f(xl) and f(xu) should be less than 0

        while ((!(xl < xu) ||!(lowerEquation * upperEquation < 0))) {
            xl = lowerGuess.randomizeGuess(Math.abs(baseValue));
            xu = upperGuess.randomizeGuess(Math.abs(baseValue));
            lowerEquation = lowerGuess.valueOfEquation(coefficients, highestPow, xl);
            upperEquation = upperGuess.valueOfEquation(coefficients, highestPow, xu);
            guessChecker++;
            //System.out.println(xl + " " + xu);
            if (guessChecker == 10000) { // Too many randomization. Maybe the function could not be solved
                // using this method
                System.out.println(guessChecker + " pairs of guesses already done. No root found. It might be not " +
                        "solvable using FALSE POSITION method.");
                return null;
            }
        }
        values.add(xl); values.add(xu); values.add(lowerEquation); values.add(upperEquation);
        // Now, conditions above are satisfied. Lower and upper guess shown to the user
        System.out.println("\n\nLower Guess: " + xl + " Equivalent: " + lowerEquation);
        System.out.println("Upper Guess: " + xu + " Equivalent: " + upperEquation + "\n\n");
        return values;
    }

    // Solves for the root of the function using the values obtained in getGuesses method
    public static boolean solve(float xl, float xu, float lowerEquation, float upperEquation, ArrayList<Float> coefficients, int highestPow) {
        float error = 0, midPoint, newMidPoint, equationWithMidPoint;
        Guesses lowerGuess = new Guesses();
        Guesses upperGuess = new Guesses();
        Guesses midPointGuess = new Guesses();

        midPoint = midPointGuess.getMidPoint(xl, xu, lowerEquation, upperEquation);     // Check Guesses.java
        int iteration = 0;      // Checks how many iterations are done before arriving to a root, if any.

        // Displaying a table of the computed values necessary for this method of finding a root of a function
        System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.format("%20s %20s %20s %20s %20s %20s %20s %20s", "Iteration Number", "xl", "xu", "xm", "f(xl)", "f(xm)", "f(xl)*f(xm)", "Error" + "\n");

        while (true) {
            iteration++;
            // On this step:
            // 1. The value of the function using the lower guess is still needed
            // 2. Another value of the function, now using the midpoint value, will be solved
            lowerEquation = lowerGuess.valueOfEquation(coefficients, highestPow, xl);
            equationWithMidPoint = midPointGuess.valueOfEquation(coefficients, highestPow, midPoint);
            float product = lowerEquation * equationWithMidPoint;

            if (iteration == 1) {
                // No error yet for the first iteration
                System.out.format("%20s %20s %20s %20s %20s %20s %20s %20s", iteration, xl, xu, midPoint, lowerEquation, equationWithMidPoint, product, "-----" + "\n");
            } else  {
                System.out.format("%20s %20s %20s %20s %20s %20s %20s %20s", iteration, xl, xu, midPoint, lowerEquation, equationWithMidPoint, product, error + "\n");
            }

            if (product > 0) {
                // If the product of f(xl) and f(midPoint) is greater than 0, midPoint will be the
                // new upper guess. A new midPoint will also be solved.
                xu = midPoint;
                upperEquation = upperGuess.valueOfEquation(coefficients, highestPow, xu);
            } else if (product < 0) {
                // If the product of f(xl) and f(midPoint) is less than 0, midPoint will be the
                // new lower guess. A new midPoint will also be solved.
                xl = midPoint;
                lowerEquation = lowerGuess.valueOfEquation(coefficients, highestPow, xl);
            } else {
                System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                System.out.println("\n\nA root of this equation is " + midPoint + ".");
                return true;
            }
            newMidPoint = midPointGuess.getMidPoint(xl, xu, lowerEquation, upperEquation);
            error = midPointGuess.getError(newMidPoint, midPoint);

            // To stop this iteration, the following should be met:
            // 1. If iteration is less than 10000, check the error. If the error is more than 0.1E-7,
            // enter the iteration once again. Else, display the estimated root and stop the program.
            // 2. If iteration is already 10000, check the error. If error is more than 0.1E-7, make
            // another round to find guesses for the root. Else, display the estimated root and stop
            // the program.
            if (iteration < 10000) {
                if (error > 1E-7 && Math.abs(product) > 1E-7) {
                    midPoint = newMidPoint;
                } else {
                    System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                    // If the chosen base error is satisfied, the midPoint is a root of the function
                    System.out.println("\n\nA root of this equation is " + midPoint + ".");
                    return true;
                }
            } else {
                if (error > 1E-7) {
                    return false;
                } else {
                    System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                    // If the chosen base error is satisfied, the midPoint is a root of the function
                    System.out.println("\n\nA root of this equation is " + midPoint + ".");
                    return true;
                }
            }
        }
    }
}
