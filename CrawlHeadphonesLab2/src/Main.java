import java.io.IOException;

public class Main
{
    public static void main(String[] args) throws IOException
    {
        Thread myThreads[] = new Thread[26];
        try
        {
            for (int i = 1; i <= 25; i++)
            {
                myThreads[i] = new Thread(new MyThreads(i));
                myThreads[i].start();
            }

            for (int i = 1; i <= 25; i++)
            {
                myThreads[i].join();
            }

        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        MyThreads.sort();
        MyThreads.printResults();
    }
}
