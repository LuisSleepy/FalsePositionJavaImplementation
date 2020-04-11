/*
This is the class in relation with the guesses (low and high) for the possible
root of the function.
 */

package falsePosition;

import java.util.*;
import java.math.BigDecimal;

public class Guesses {
    ArrayList<Float> coefficients = new ArrayList<>();

    // Randomizes the guess, either the lower or the upper guess
    // Boundaries between the positive and negative value of the highest coefficient of the function
    public Float randomizeGuess(float highestValue) {
        double randomNumber = Math.random() * (highestValue + highestValue + 1) - highestValue;
        return BigDecimal.valueOf(randomNumber).setScale(5, BigDecimal.ROUND_DOWN).floatValue();
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
        return BigDecimal.valueOf(equationValue).setScale(5, BigDecimal.ROUND_DOWN).floatValue();
    }

    // Solves the midpoint between the lower guess and the upper guess
    public float getMidPoint(float lowerValue, float upperValue, float lowerEquation, float upperEquation) {
        return BigDecimal.valueOf(((upperValue * lowerEquation) - (lowerValue * upperEquation)) / (lowerEquation - upperEquation)).setScale(5, BigDecimal.ROUND_DOWN).floatValue();
    }

    public float getError(float newGuess, float oldGuess) {
        return BigDecimal.valueOf(Math.abs((newGuess - oldGuess) / newGuess)).setScale(5, BigDecimal.ROUND_DOWN).floatValue();
    }
}
