import java.util.concurrent.Semaphore;

public class Main {

    // variabilele alea 2 de care ti-am zis in partea cealalta. Lui target poti sa ii dai orice valoare.
    public static int count = 0;
    public static int target = 20;

    //asta e functia main
    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(1); //creezi un semafor, ca sa il dai mai departe, threadurilor

        //pe astea privestele pe fiecare pe un thread. Sunt 4 threaduri
        Incrementer incrementer1 = new Incrementer(semaphore, "thread1");
        Incrementer incrementer2 = new Incrementer(semaphore, "thread2");
        Incrementer incrementer3 = new Incrementer(semaphore, "thread3");
        Incrementer incrementer4 = new Incrementer(semaphore, "thread4");

        incrementer1.start();
        incrementer2.start();
        incrementer3.start();
        incrementer4.start();

        //incearca sa execute corect, daca intervine o eroare, o prinzi si o sa stii ce eroare e
        try {
            incrementer1.join();
            incrementer2.join();
            incrementer3.join();
            incrementer4.join();
        } catch (InterruptedException exception) {
            System.out.println(exception);
        }
    }
}
