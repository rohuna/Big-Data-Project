public class County
{  
    String name;
    String state;
    String hsDiploma2012;
    String avgUnemployment;

    public County(String state, String name, String hsDiploma2012, String avgUnemployment)
    {
        this.name = name;
        this.state = state;
        this.hsDiploma2012 = hsDiploma2012;
        this.avgUnemployment = avgUnemployment;
    }

    public String getState()
    {
        return state;
    }

    public String getName()
    {
        return name;
    }

    public String getHsDiploma2012()
    {
        return hsDiploma2012;
    }

    public String getAvgUnemployment()
    {
        return avgUnemployment;
    }

    public void setState(String state)
    {
        this.state = state;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setHsDiploma2012(String hsDiploma2012)
    {
        this.hsDiploma2012 = hsDiploma2012;
    }

    public void setAvgUnemployment(String avgUnemployment)
    {
        this.avgUnemployment = avgUnemployment;
    }
}