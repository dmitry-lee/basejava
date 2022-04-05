package com.dmitrylee.webapp;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MainStreams {
    public static void main(String[] args) {
        int[] arr = {9, 8};
        int[] arr2 = {5, 1, 2, 1, 7};
        System.out.println(minValue(arr));
        System.out.println(minValue(arr2));

        List<Integer> integers1 = Arrays.asList(1, 5, 5, 2, 5);
        System.out.println(oddOrEven(integers1));
        List<Integer> integers2 = Arrays.asList(2, 23, 11, 12, 5);
        System.out.println(oddOrEven(integers2));
    }

    public static int minValue(int[] values) {
        long[] count = new long[1];
        count[0] = Arrays.stream(values).distinct().count() - 1;
        return Arrays.stream(values).
                distinct().
                sorted().
                reduce(0, (acc, x) -> (int) (acc + Math.pow(10, count[0]--) * x));
    }

    public static List<Integer> oddOrEven(List<Integer> integers) {
        int sum = integers.stream().mapToInt(y -> y).sum();
        System.out.println("sum = " + sum);
        return integers.stream().filter(x -> {
            if (sum % 2 == 0) {
                return x % 2 == 0;
            }
            return x % 2 != 0;
        }).collect(Collectors.toList());
    }
}
