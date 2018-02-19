import java.util.concurrent.Semaphore;

//clasa Incrementer care mosteneste/extinde clasa Thread care e implementata de Java. Clasa asta are tot ce vrei despre threaduri
// prin intermediul ei poti sa lucrezi cu threaduri
public class Incrementer extends Thread {

    private String _threadName; //e un string, numele
    private Semaphore _semaphore; //semaforul previne erorile care pot aparea. De ex..mai multe thread-uri vor sa schimbe valoarea in acelasi timp. Nu se poate
    // semaforul asta e o clasa, deja implementata de java.

    //asta e contructorul clasei, care iti initializeaza semaforul si numele. super adica apeleaza prima data constructorul clasei de baza (Thread)
    public Incrementer(Semaphore semaphore, String threadName)
    {
        super(threadName);
        _threadName = threadName;
        _semaphore = semaphore;
    }
    /*
    metoda run e o metoda suprascrisa care exista deja in clasa Thread. Cu ea, threadu-urile stiu sa ruleze
    blocul ala de try si catch e pentru tratarea exceptiilor. Adica daca da o eroare undeva, poti s-o "prinzi" si sa stii ce eroare e
    Main.count si Main.target, adica count si target sunt 2 variabile statice din clasa Main. Si ele asa se acceseaza in alte clase, cu Main.variabila
    si cresti count pana cand devine target, ceea ce ti se cere
    */
    @Override
    public void run() {
        System.out.println(_threadName + " is waiting for a permit.");
        try {
            _semaphore.acquire();
            System.out.println(_threadName + " gets a permit.");

            while(Main.count < Main.target) {

                Main.count++;

                System.out.println("Thread: " + _threadName + ". Count: " + Main.count);
                System.out.println(_threadName + " releases the permit.");
                _semaphore.release();
            }
        } catch (InterruptedException exception) {
            System.out.println(exception);
        }
    }
}
