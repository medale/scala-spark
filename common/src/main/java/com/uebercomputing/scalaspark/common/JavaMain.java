package com.uebercomputing.scalaspark.common;

/**
 * Starting a program from Java.
 */
public class JavaMain {

    private int answer = 0;

    public JavaMain(int answer) {
        this.answer = answer;
    }

    public int getAnswer() {
        return answer;
    }

    public static void main(String[] args) {
        System.out.println("Starting a Java program...");
        JavaMain jaMain = new JavaMain(42);
        int answer = jaMain.getAnswer();
        System.out.println("The answer was " + answer);
    }
}
