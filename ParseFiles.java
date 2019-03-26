//imports
import java.util.Scanner;
import java.io.PrintWriter;
import java.util.ArrayList;


public class ParseFiles
{
    ArrayList<String> eduLines;
    ArrayList<String> unemploymentLines;
    ArrayList<County> dataByCounty;
    public static void main(String[] args)
    {   
        ParseFiles pf = new ParseFiles();
        pf.run();
    }

    public ParseFiles()
    {
        eduLines = new ArrayList<String>();
        unemploymentLines = new ArrayList<String>();
        dataByCounty = new ArrayList<County>();
    }

    public void run()
    {  
        getEduData();
        getUnempData();
    }
    public void getEduData()
    {
        int lineCount = 0;

        Scanner reader = FileUtils.openToRead("Education.csv");
        while(reader.hasNext())
        {
            String line = reader.nextLine();
            lineCount++;
            eduLines.add(line);
        }

        String[][] data = new String[lineCount][lineCount];
        int dataNum = 0;
        for(int i = 0; i < eduLines.size(); i++)
        {
            String oneDataPiece = "";
            boolean inQuotation = false;
            String str = eduLines.get(i);
            for(int j = 0; j < eduLines.get(i).length(); j++)
            {
                if(str.charAt(j) == '\"' && inQuotation == true)
                {
                    inQuotation = false;
                }
                else if(str.charAt(j) == '\"')
                {
                    inQuotation = true;
                }
                    if(!(str.charAt(j) ==','))
                    {
                        oneDataPiece += str.charAt(j);
                    }
                    else if(inQuotation == false)
                    {
                        data[i][dataNum] = oneDataPiece;
                        dataNum++;
                        oneDataPiece = "";
                    }
            }   
            dataNum = 0;
        }

        for(int i = 0; i < eduLines.size(); i++)
        {
            if(data[i][3] != "" && i > 0)
            {
                County county = new County(data[i][1], data[i][2], data[i][44], null);
                dataByCounty.add(county);
                //System.out.printf("\n%-20s%-20s%-20s", county.state, county.name, county.hsDiploma2012);
            }
            else dataByCounty.add(new County(null, null, null,  null));
        }
    }
    
    public void getUnempData()
    {
        int lineCount = 0;

        Scanner reader = FileUtils.openToRead("UnemploymentMed.csv");
        while(reader.hasNext())
        {
            String line = reader.nextLine();
            lineCount++;
            unemploymentLines.add(line);
        }

        String[][] data = new String[lineCount][lineCount];
        int dataNum = 0;
        for(int i = 0; i < unemploymentLines.size(); i++)
        {
            String oneDataPiece = "";
            boolean inQuotation = false;
            String str = unemploymentLines.get(i);
            for(int j = 0; j < unemploymentLines.get(i).length(); j++)
            {
                if(str.charAt(j) == '\"' && inQuotation == true)
                {
                    inQuotation = false;
                }
                else if(str.charAt(j) == '\"')
                {
                    inQuotation = true;
                }
                    if(!(str.charAt(j) ==','))
                    {
                        oneDataPiece += str.charAt(j);
                    }
                    else if(inQuotation == false)
                    {
                        data[i][dataNum] = oneDataPiece;
                        dataNum++;
                        oneDataPiece = "";
                    }
            }   
            dataNum = 0;
        }

        PrintWriter out = FileUtils.openToWrite("EduUnemployment.csv");
        out.println("\"State,County Name, High School Diploma Only Rate 2012 - 2016, Average Unemployment Rate 2012 - 2016\"");
        
        for(int i = 0; i < unemploymentLines.size(); i++)
        {
            if(data[i][3] != "" && !data[i][1].equals("State") && i > 0 )
            {
                double avg = (Double.parseDouble(data[i][29]) +
                              Double.parseDouble(data[i][33]) +
                              Double.parseDouble(data[i][37]) + 
                              Double.parseDouble(data[i][41]) + 
                              Double.parseDouble(data[i][45]))/5.0;
                avg = Math.round(avg * 100.0) / 100.0;
                if(dataByCounty.get(i-3).getState() != null) dataByCounty.get(i-3).setAvgUnemployment(Double.toString(avg));
                
                County county = dataByCounty.get(i-3);

                
                if(county.getState() != null)
                    out.printf("\n%s,%s,%s,%s", county.state, county.name, county.hsDiploma2012, county.avgUnemployment);
                
            } 
        }
        out.close();
    }

    
}