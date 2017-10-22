package sedykh.tradescalc;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

public class TestCalculator {

    @Test
    public void testCalcFuncMyData() throws Exception {

        final Calculator calculator = new Calculator();
        File data = new File(getClass().getResource("/TRD1.csv").getFile());
        final List<String> list = calculator.calculate(data);
    }


    @Test
    public void testCalcFunc() throws Exception {

        final Calculator calculator = new Calculator();
        File data = new File(getClass().getResource("/TRD2.csv").getFile());
        final Set<String> results = new TreeSet<>(calculator.calculate(data));
        final Set<String> corrResults = new TreeSet<>(Arrays.asList("Результаты для биржы: \"Q\" : максимальное количество сделок в течение одной секунды было между 10:02:00.184 и 10:02:01.183. В этот интервал прошло 243 сделок.",
                "Результаты для биржы: \"B\" : максимальное количество сделок в течение одной секунды было между 10:13:00.435 и 10:13:01.434. В этот интервал прошло 11 сделок.",
                "Результаты для биржы: \"Y\" : максимальное количество сделок в течение одной секунды было между 10:09:54.386 и 10:09:55.385. В этот интервал прошло 12 сделок.",
                "Результаты для биржы: \"V\" : максимальное количество сделок в течение одной секунды было между 10:12:01.057 и 10:12:02.056. В этот интервал прошло 9 сделок.",
                "Результаты для биржы: \"Z\" : максимальное количество сделок в течение одной секунды было между 10:02:00.632 и 10:02:01.631. В этот интервал прошло 43 сделок.",
                "Результаты для биржы: \"K\" : максимальное количество сделок в течение одной секунды было между 10:02:00.632 и 10:02:01.631. В этот интервал прошло 133 сделок.",
                "Результаты для биржы: \"P\" : максимальное количество сделок в течение одной секунды было между 10:14:23.576 и 10:14:24.575. В этот интервал прошло 55 сделок.",
                "Результаты для биржы: \"J\" : максимальное количество сделок в течение одной секунды было между 10:09:54.386 и 10:09:55.385. В этот интервал прошло 8 сделок.",
                "Результаты для биржы: \"X\" : максимальное количество сделок в течение одной секунды было между 10:02:00.780 и 10:02:01.779. В этот интервал прошло 9 сделок.",
                "Результаты для биржы: \"D\" : максимальное количество сделок в течение одной секунды было между 10:02:00.785 и 10:02:01.784. В этот интервал прошло 47 сделок.",
                "Результаты для всех бирж: максимальное количество сделок в течение одной секунды было между 10:02:00.735 и 10:02:01.734. В этот интервал прошло 516 сделок."));
        assertArrayEquals(results.toArray(), corrResults.toArray());
        results.forEach(System.out::println);
    }
}
