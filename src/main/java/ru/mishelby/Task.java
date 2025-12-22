package ru.mishelby;



public class Task {
    public static void main(String[] args) {
        int i = calculateYears(1000, 0.05, 0.18, 1100);
        System.out.println(i);
    }

    public static int calculateYears(double principal, double interest, double tax, double desired) {
        double r = 1 + interest * (1 - tax);
        double result = Math.log((desired / principal)) / Math.log(r);
        return  (int) Math.ceil(result);
    }

}
