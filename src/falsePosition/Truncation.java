/*
A class for implementing truncation in a displayed value in the screen.
 */

package falsePosition;

import java.math.RoundingMode;
import java.text.DecimalFormat;
public class Truncation {
    public float truncate(float value) {
        DecimalFormat decimalFormat = new DecimalFormat("#.#####");
        decimalFormat.setRoundingMode(RoundingMode.FLOOR);
        return Float.parseFloat(decimalFormat.format(value));
    }
}
