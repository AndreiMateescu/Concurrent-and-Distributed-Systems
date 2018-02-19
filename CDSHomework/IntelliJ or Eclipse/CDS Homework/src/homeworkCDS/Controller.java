package homeworkCDS;

import java.time.LocalDate;
import java.util.Date;

import static java.lang.Thread.sleep;

public class Controller implements Runnable {

    int capacityOfBridge;
    static int numberCarsInside;
    static int carsWaitLeft;
    static int carsWaitRight;
    static long lastRight;
    static long lastLeft;
    int[] semaphore = {0, 0};

    public Controller(int capacityOfBridge) {
        this.capacityOfBridge = capacityOfBridge;
    }

    public static synchronized void carWaitingRight() {
        carsWaitRight = carsWaitRight + 1;
    }

    public static synchronized void carWaitingLeft() {
        carsWaitLeft = carsWaitLeft + 1;
    }

    //update the lastRight and lastLeft time and decreases the value for the cars on the bridge. Direction required
    public static synchronized void carExits(String direction) {
        numberCarsInside = numberCarsInside - 1;
        if (direction.equals("RIGHT")) {
            lastRight = (new Date()).getTime() / 1000;
        } else {
            lastLeft = (new Date()).getTime() / 1000;
        }
    }

    /**
     * This function checks if the other side is not starving.
     * The other side is starving if no car has passed in the last 4 seconds.
     *
     * @return true if other side is not starving and false if the other side is starving.
     */
    public boolean areNotAvailableCars(int direction) {
        long currentTime = (new Date()).getTime() / 1000;
        if (direction == 0) {
            if (carsWaitRight == 0) {
                System.out.println("No cars on right side, keeping left semaphore green");
                return true;
            } else {
                if (currentTime - lastRight <= 4) {
                    return true;
                }
                return false;
            }
        } else {
            if (carsWaitLeft == 0) {
                System.out.println("No cars on left side, keeping right semaphore green");
                return true;
            } else {
                if (currentTime - lastLeft <= 4) {
                    return true;
                }
                return false;
            }
        }
    }

    /**
     * Function that checks if the bridge limit has been reached or not.
     *
     * @return true if bridge limit has not been reached and false otherwise.
     */
    boolean bridgeLimitNotReached() {
        return numberCarsInside < capacityOfBridge;
    }

    @Override
    public void run() {
        try {
            sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        semaphore[0] = 1;
        lastRight = (new Date()).getTime() / 1000;
        lastLeft = (new Date()).getTime() / 1000;
        System.out.println("LEFT side semaphore is GREEN");
        System.out.println("RIGHT side semaphore is RED");

        while (true) {
            if (semaphore[0] == 1) {
                while (areNotAvailableCars(0) && carsWaitLeft != 0) {
                    if (!bridgeLimitNotReached()) {
                        System.out.println("Car clearing... bridge is full. Waiting for decreasing capacity...");
                        while (!bridgeLimitNotReached()) {
                            try {
                                sleep(50);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    System.out.println("One car cleared from LEFT");
                    synchronized (this) {
                        numberCarsInside = numberCarsInside + 1;
                        carsWaitLeft = carsWaitLeft - 1;
                    }

                    Car.turnOnLeft();
                    try {
                        sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    lastLeft = (new Date()).getTime() / 1000;
                }
                semaphore[0] = 0;
                System.out.println("LEFT side semaphore is RED.");

                // WHILE we still have cars driving on the bridge, we wait
                while (numberCarsInside != 0) {
                    try {
                        sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                if (carsWaitRight != 0) {
                    semaphore[1] = 1;
                    System.out.println("RIGHT side semaphore is GREEN");
                } else {
                    if (carsWaitLeft == 0) {
                        System.out.println("No more cars on either side. Exiting...");
                        return;
                    }
                }
            }
            if (semaphore[1] == 1) {
                /**
                 * While other side is not starving and we still have cars waiting in line.
                 */
                while (areNotAvailableCars(1) && carsWaitRight != 0) {
                    if (!bridgeLimitNotReached()) {
                        System.out.println("Car clearing... bridge is full. Waiting for decreasing capacity...");
                        while (!bridgeLimitNotReached()) {
                            try {
                                sleep(50);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    System.out.println("One car cleared from RIGHT");
                    synchronized (this) {
                        numberCarsInside = numberCarsInside + 1;
                        carsWaitRight = carsWaitRight - 1;
                    }

                    Car.turnOnRight();
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    lastRight = (new Date()).getTime() / 1000;
                }
                System.out.println("RIGHT side semaphore is RED");
                semaphore[1] = 0;

                while (numberCarsInside != 0) {
                    try {
                        sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (carsWaitLeft != 0) {
                    semaphore[0] = 1;
                    System.out.println("LEFT side semaphore is GREEN");
                } else {
                    if (carsWaitLeft == 0) {
                        System.out.println("No more cars on either side. Exiting...");
                        return;
                    }
                }
            }
        }
    }
}
