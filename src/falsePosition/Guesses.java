/*
This is the class in relation with the guesses (low and high) for the possible
root of the function.
 */

package falsePosition;

import java.util.*;

public class Guesses {
    ArrayList<Float> coefficients = new ArrayList<>();
    // Randomizes the guess, either the lower or the upper guess
    // Boundaries between the positive and negative value of the highest coefficient of the function
    public Float randomizeGuess(float highestValue) {
        double randomNumber = Math.random() * (highestValue + highestValue + 1) - highestValue;
        return (float) randomNumber;
    }

    public Float valueOfEquation(ArrayList<Float> coefficients, int highestPow, float value) {
        this.coefficients = coefficients;
        float equationValue = 0;
        for (int i = 0; i < coefficients.size(); i++) {
            if (i == coefficients.size() - 1) {
                equationValue = equationValue + coefficients.get(i);
            } else {
                double equationValueInDouble = equationValue + coefficients.get(i) * Math.pow(value, highestPow - i);
                equationValue = (float) equationValueInDouble;
            }
        }
        return equationValue;
    }

    // Solves the midpoint between the lower guess and the upper guess
    public float getMidPoint(float lowerValue, float upperValue, float lowerEquation, float upperEquation) {
        return ((upperValue * lowerEquation) - (lowerValue * upperEquation)) / (lowerEquation - upperEquation);
    }

    public float getError(float newGuess, float oldGuess) {
        return Math.abs((newGuess - oldGuess) / newGuess);
    }
}
