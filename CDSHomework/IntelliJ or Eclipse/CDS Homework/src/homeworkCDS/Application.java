package homeworkCDS;

import java.util.Scanner;

public class Application {
    private static int carsRight;
    private static int carsLeft;
    private static int capacityOfBridge;

    private static void readFromScanner(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Cars on the left: ");
        carsLeft = scanner.nextInt();
        System.out.println("Cars on the right: ");
        carsRight = scanner.nextInt();
        System.out.println("The number of maximum cars on bridge: ");
        capacityOfBridge = scanner.nextInt();
    }

    public static void main(String[] args) {
        readFromScanner();
        Thread[] cars = new Thread[200];
        for(int index = 0; index<carsLeft;++index){
            cars[index] = new Thread(new Car("LEFT"));
            cars[index].start();
        }
        for(int index = 0; index < carsRight; ++index) {
            cars[index] = new Thread(new Car("RIGHT"));
            cars[index].start();
        }
        Thread controller = new Thread(new Controller(capacityOfBridge));
        controller.start();
        try {
            controller.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            for(int index = 0;index < carsLeft + carsRight; ++index) {
                cars[index].join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
