import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicIntegerArray;

class MyThreads implements Runnable
{
    private int threadNumber;
    private volatile static ArrayList<Casca> results= new ArrayList<Casca>();
    private static volatile int numarCastiCel=0,numarCastiEmag=0;
    public static void printResults()
    {
        System.out.println("We have a total of: " + results.size()+ " items: " + numarCastiCel + " items from Cel and " + numarCastiEmag + " items from Emag.");
        for(Casca item:results)
        {
            System.out.println(item);
        }
    }

    public MyThreads(int thrNumber)
    {
        threadNumber = thrNumber;
    }

    public void parseCel() throws IOException
    {
        String url = "http://www.cel.ro/casti/0a-" + threadNumber;
        System.out.println("Extracting data from url:" + url);
        Document doc = Jsoup.connect(url).get();
        Elements linksAndUrls = doc.select("a[class=\"productListing-data-b product_link product_name\"]");
        Elements ids = doc.select("span[id]");
        Elements prices = doc.select("b[itemprop='price']");

        List<String>idCasti=new ArrayList<String>();
        List<String>urlCasti=new ArrayList<String>();
        List<String>preturiCasti=new ArrayList<String>();
        List<String>numeCasti=new ArrayList<String>();
        for (Element id : ids)
        {
            if (id.attr("id").endsWith("-0") && id.attr("id").startsWith("s"))
            {
                idCasti.add(id.attr("id").substring(1,id.attr("id").length()-2));
            }
        }

        for (Element link : linksAndUrls)
        {
            urlCasti.add(link.attr("abs:href"));
            numeCasti.add(link.text());
        }

        for (Element price : prices)
        {
            preturiCasti.add(price.attr("content"));
        }

        for(int i=0;i<=idCasti.size()-1;i++)
        {
            Casca item= new Casca();
            item.id=idCasti.get(i);
            item.price=Integer.parseInt(preturiCasti.get(i));
            item.name=numeCasti.get(i);
            item.url=urlCasti.get(i);
            synchronized (this)
            {
                results.add(item);
                numarCastiCel++;
            }
        }

    }

    public void parseEmag() throws IOException
    {
        String url = "https://www.emag.ro/casti-pc/p"+threadNumber+"/c";
        System.out.println("Extracting data from url:" + url);
        Document doc = Jsoup.connect(url).get();
        Elements linksAndUrls = doc.select("a[class='product-title js-product-url']");
        Elements ids = doc.select("input[name='product[]']");
        Elements prices = doc.select("p[class='product-new-price']");

        List<String>idCasti=new ArrayList<String>();
        List<String>urlCasti=new ArrayList<String>();
        List<String>preturiCasti=new ArrayList<String>();
        List<String>numeCasti=new ArrayList<String>();
        for (Element id : ids)
        {
            idCasti.add(id.attr("value"));
        }

        for (Element link : linksAndUrls)
        {
            urlCasti.add(link.attr("href"));
            numeCasti.add(link.text());
        }

        for (Element price : prices)
        {
            if(price.childNodeSize()!=0)
                preturiCasti.add(price.textNodes().get(0).toString().replaceAll("[^A-Za-z0-9]", ""));
        }

        for(int i=0;i<=idCasti.size()-1;i++)
        {
            Casca item= new Casca();
            item.id=idCasti.get(i);
            item.price=Integer.parseInt(preturiCasti.get(i));
            item.name=numeCasti.get(i);
            item.url=urlCasti.get(i);
            synchronized (this)
            {
                results.add(item);
                numarCastiEmag++;
            }
        }

    }

    @Override
    public void run()
    {
        try
        {
            parseCel();
            if(threadNumber<=11)
                parseEmag();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    public static void sort()
    {
        Collections.sort(results);
        printResults();
    }
}