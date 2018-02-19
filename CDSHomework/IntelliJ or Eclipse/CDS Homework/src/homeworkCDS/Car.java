package homeworkCDS;

import static java.lang.Thread.sleep;

public class Car implements Runnable {

    private String direction;
    private final static Object WAIT_RIGHT = new Object();
    private final static Object WAIT_LEFT = new Object();

    public Car(String direction) {
        synchronized (WAIT_RIGHT) {
            this.direction = direction;
        }
    }

    //when the car stays in vector/queue at Left side
    public static void turnOnLeft() {
        synchronized (WAIT_LEFT) {
            WAIT_LEFT.notify();
        }
    }

    //when the car stays in vector/queue at right side
    public static void turnOnRight() {
        synchronized (WAIT_RIGHT) {
            WAIT_RIGHT.notify();
        }
    }

    @Override
    public void run() {
        // for loop -> car does the same process a couple of times
        for (int iterator = 0; iterator < 3; ++iterator) {

            //car is turn on / ready
            if (direction.equals("RIGHT")) {
                synchronized (this) {
                    Controller.carWaitingRight();
                }
            } else {
                synchronized (this) {
                    Controller.carWaitingLeft();
                }
            }

            if (direction.equals("RIGHT")) {
                try {
                    synchronized (WAIT_RIGHT) {
                        WAIT_RIGHT.wait();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //Enters in the tunnel after the process is notified
                System.out.println("Entering in the tunnel car from RIGHT direction");
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Controller.carExits(direction);
                System.out.println("Existing from tunner car from RIGHT direction");
            } else {
                try {
                    synchronized (WAIT_LEFT) {
                        WAIT_LEFT.wait();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //Enters in the tunnel after the process is notified
                System.out.println("Entering in the tunnel car from LEFT direction");
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Controller.carExits(direction);
                System.out.println("Existing from tunner car from LEFT direction");
            }
        }
    }
}
