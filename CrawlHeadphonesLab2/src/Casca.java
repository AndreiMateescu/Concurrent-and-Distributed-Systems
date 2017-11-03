class Casca implements Comparable<Casca>
{
    String id;
    int price;
    String name;
    String url;

    @Override
    public String toString()
    {
        System.out.println("ID:"+id+" Price:"+price+" Name:"+name+" URL:"+url);
        return super.toString();
    }

    public int compareTo(Casca a)
    {
        return a.price - this.price;
    }
}