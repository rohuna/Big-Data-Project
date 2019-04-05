public class County
{  
    private String name; //name of the county
    private String state; //name of the state of the county
    private String hsDiploma2012; //average rate of high school education between 2012-2016
    private String avgUnemployment; //average rate of unemployment between 2012-2016
    private double ba; //total African American population 
    private double wa; //total White population

    /** Constructor **/
    public County(String state, String name, String hsDiploma2012, String avgUnemployment, double wa, double ba)
    {
        this.name = name;
        this.state = state;
        this.hsDiploma2012 = hsDiploma2012;
        this.avgUnemployment = avgUnemployment;
        this.wa = wa;
        this.ba = ba;
    }

    /** Accessor Methods **/
    public String getState() { return state; }

    public String getName() { return name; }

    public String getHsDiploma2012() { return hsDiploma2012; }

    public String getAvgUnemployment() { return avgUnemployment; }

    public double getB() { return ba; }

    public double getW(); { return wa; }

    /** Setter Methods **/
    public void setState(String state)
    { this.state = state; }

    public void setName(String name)
    { this.name = name; }

    public void setHsDiploma2012(String hsDiploma2012)
    { this.hsDiploma2012 = hsDiploma2012; }

    public void setAvgUnemployment(String avgUnemployment)
    { this.avgUnemployment = avgUnemployment; }

    public void setB(double ba)
    { this.ba = ba; }

    public void setW(double wa)
    { this.wa = wa; }
}
