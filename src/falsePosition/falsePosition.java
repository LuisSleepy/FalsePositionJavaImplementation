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
        System.out.println("This is the Java implementation of FALSE POSITION method in finding a root of a " +
                "polynomial equation.");
        // Scans the input from the user (to be used all throughout the program)
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the highest degree of the equation: ");
        int highestPow = scanner.nextInt();

        // This program is intended for polynomial function with the highest exponent greater than or equal to 2.
        while (highestPow <= 1) {
            System.out.println("Not applicable. Try another function.");
            System.out.print("Enter the highest degree of the equation: ");
            highestPow = scanner.nextInt();
        }

        NumericalTerm nt = new NumericalTerm();
        // Allows the user to enter the constants of each term of the polynomial function
        ArrayList<Float> coefficients;
        coefficients = nt.inputConstants(scanner, highestPow);

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
        System.out.print("These are the coefficients of the (modified, if needed) function: " + "[ ");
        for (Float coefficient : coefficients) {        // Display the array of the coefficients (modified or not)
            System.out.print(coefficient + " ");
        }
        System.out.println("]");

        float baseValue = nt.getHighestValue(coefficients);      // Check NumericalTerm.java
        float xl = 0.f, xu = 0.f;
        float lowerEquation = 0.f, upperEquation = 0.f;
        Guesses lowerGuess = new Guesses();
        Guesses upperGuess = new Guesses();

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

            if (guessChecker == 10000) { // Too many randomization. Maybe the function could not be solved
                                            // using this method
                System.out.println(guessChecker + " pairs of guesses already done. No root found. It might be not " +
                        "solvable using FALSE POSITION method.");
                return;
            }
        }
        // Now, conditions above are satisfied. Lower and upper guess shown to the user
        System.out.println("\n\nLower Guess: " + xl + " Equivalent: " + lowerEquation);
        System.out.println("Upper Guess: " + xu + " Equivalent: " + upperEquation + "\n\n");
        // Next, the midpoint of lower and upper guess will be the next objective
        float error = 0, midPoint, newMidPoint, equationWithMidPoint;
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
                newMidPoint = midPointGuess.getMidPoint(xl, xu, lowerEquation, upperEquation);

                error = midPointGuess.getError(newMidPoint, midPoint);
                if (error > 0.00000001 && iteration <= 10000) {       // Checks the absolute error and the number of iterations
                    //System.out.println("Error of Iteration No. " + iteration + ": " + error);
                    midPoint = newMidPoint;
                } else {
                    System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                    // If the chosen base error is satisfied, the midPoint is a root of the function
                    System.out.println("\n\nA root of this equation is " + midPoint + ".");
                    break;
                }
            } else if (product < 0) {
                // If the product of f(xl) and f(midPoint) is less than 0, midPoint will be the
                // new lower guess. A new midPoint will also be solved.
                xl = midPoint;
                lowerEquation = lowerGuess.valueOfEquation(coefficients, highestPow, xl);
                newMidPoint = midPointGuess.getMidPoint(xl, xu, lowerEquation, upperEquation);

                error = midPointGuess.getError(newMidPoint, midPoint);
                if (error > 0.00000001 && iteration <= 10000) {   // Checks the absolute error and the number of iterations
                    //System.out.println("Error of Iteration No. " + iteration + ": " + error);
                    midPoint = newMidPoint;
                } else {
                    System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                    // If the chosen base error is satisfied, the midPoint is a root of the function
                    System.out.println("\n\nA root of this equation is " + midPoint + ".");
                    break;
                }
            } else {
                System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                System.out.println("\n\nA root of this equation is " + midPoint + ".");
                break;
            }
        }
    }
}
