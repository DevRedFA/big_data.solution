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
        final Set<String> corrResults = new TreeSet<>(Arrays.asList("���������� ��� �����: \"Q\" : ������������ ���������� ������ � ������� ����� ������� ���� ����� 10:02:00.184 � 10:02:01.183. � ���� �������� ������ 243 ������.",
                "���������� ��� �����: \"B\" : ������������ ���������� ������ � ������� ����� ������� ���� ����� 10:13:00.435 � 10:13:01.434. � ���� �������� ������ 11 ������.",
                "���������� ��� �����: \"Y\" : ������������ ���������� ������ � ������� ����� ������� ���� ����� 10:09:54.386 � 10:09:55.385. � ���� �������� ������ 12 ������.",
                "���������� ��� �����: \"V\" : ������������ ���������� ������ � ������� ����� ������� ���� ����� 10:12:01.057 � 10:12:02.056. � ���� �������� ������ 9 ������.",
                "���������� ��� �����: \"Z\" : ������������ ���������� ������ � ������� ����� ������� ���� ����� 10:02:00.632 � 10:02:01.631. � ���� �������� ������ 43 ������.",
                "���������� ��� �����: \"K\" : ������������ ���������� ������ � ������� ����� ������� ���� ����� 10:02:00.632 � 10:02:01.631. � ���� �������� ������ 133 ������.",
                "���������� ��� �����: \"P\" : ������������ ���������� ������ � ������� ����� ������� ���� ����� 10:14:23.576 � 10:14:24.575. � ���� �������� ������ 55 ������.",
                "���������� ��� �����: \"J\" : ������������ ���������� ������ � ������� ����� ������� ���� ����� 10:09:54.386 � 10:09:55.385. � ���� �������� ������ 8 ������.",
                "���������� ��� �����: \"X\" : ������������ ���������� ������ � ������� ����� ������� ���� ����� 10:02:00.780 � 10:02:01.779. � ���� �������� ������ 9 ������.",
                "���������� ��� �����: \"D\" : ������������ ���������� ������ � ������� ����� ������� ���� ����� 10:02:00.785 � 10:02:01.784. � ���� �������� ������ 47 ������.",
                "���������� ��� ���� ����: ������������ ���������� ������ � ������� ����� ������� ���� ����� 10:02:00.735 � 10:02:01.734. � ���� �������� ������ 516 ������."));
        assertArrayEquals(results.toArray(), corrResults.toArray());
        results.forEach(System.out::println);
    }
}
