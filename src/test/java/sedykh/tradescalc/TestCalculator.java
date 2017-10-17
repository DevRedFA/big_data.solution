package sedykh.tradescalc;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;

public class TestCalculator {

    @Test
    public void testCalcFunc() throws Exception {
        final Calculator calculator = new Calculator();
        File data = new File(getClass().getResource("/TRD1.csv").getFile());
        final List<String> list = calculator.calculate(data);
    }
}
