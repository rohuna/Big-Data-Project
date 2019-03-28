//imports
import java.util.Scanner;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * @author Rohun Agrawal and Steven Long
 * @since 3/20/19
 */
public class ParseFiles
{
    //lines taken from the education.csv file
    ArrayList<String> eduLines;

    //lines taken from the unemploymentmed.csv file
    ArrayList<String> unemploymentLines;

    //a list of County objects to store all the data
    ArrayList<County> dataByCounty;
    
    private final String ETHNICITY_FILE = "cc-est2017-alldata.csv";

	// indeces for each ethnicity value
	private final int CTYNAME = 4;
	private final int WA_MALE = 10;
	private final int WA_FEMALE = 11;
	private final int BA_MALE = 12;
	private final int BA_FEMALE = 13;
	private final int TOTAL_INDECES = 80;
	private final int GROUPS = 19;
	private final int REPEAT = 10;

    //main method
    public static void main(String[] args)
    {   
        ParseFiles pf = new ParseFiles();
        pf.run();
    }

    //constructor
    public ParseFiles()
    {
        eduLines = new ArrayList<String>();
        unemploymentLines = new ArrayList<String>();
        dataByCounty = new ArrayList<County>();
    }

    //get data from various files
    public void run()
    {  
        getEduData();
        getUnempData();
        getRaceData();
    }

    /*
     * gets data from the education.csv file 
     */
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
                County county = new County(data[i][1], data[i][2], data[i][44], null, 0.0, 0.0);
                dataByCounty.add(county);
                //System.out.printf("\n%-20s%-20s%-20s", county.state, county.name, county.hsDiploma2012);
            }
            else dataByCounty.add(new County(null, null, null,  null, 0.0, 0.0));
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
            
            } 
        }
       
    }

    public void getRaceData()
    {
        Scanner sc = FileUtils.openToRead(ETHNICITY_FILE);
		sc.nextLine(); // skip the headers
        String[] tempData = new String[TOTAL_INDECES];
		String tempName = ""; // temp name of county
		double tempB = 0.0; // temp count of black population
		double tempW = 0.0; // temp count of white population

		int index = 0; // iterates through the list of counties
		double totalW = 0.0;
		double totalB = 0.0;

        PrintWriter out = FileUtils.openToWrite("EduUnemployment.csv");
        out.println("\"State,County Name, High School Diploma Only Rate 2012 - 2016, Average Unemployment Rate 2012 - 2016, White population, Black Population\"");
        
		while(sc.hasNext()) {

			tempB = 0; // temp count of black population
			tempW = 0; // temp count of white population

			// iterate through values for the same county and prepare aggregates
			for(int i=0; i<REPEAT; i++) {

				// reset temporary values
				tempW = 0;
				tempB = 0;
                try
                {
				for(int j=0; j<GROUPS; j++) {
					tempData = parseLine(sc.nextLine());
					tempName = tempData[4];
					tempW += Double.parseDouble(tempData[WA_MALE]) + Double.parseDouble(tempData[WA_FEMALE]);
					tempB += Double.parseDouble(tempData[BA_MALE]) + Double.parseDouble(tempData[BA_FEMALE]);
				}
                }
                catch(Exception e)
                {
                    
                }
				// aggregate
				totalW += tempW;
				totalB += tempB;
			}
            // County county = dataByCounty.get(index);
			// create county object with the averages

            for(int i = 0; i < dataByCounty.size(); i++)
            {
                if(dataByCounty.get(i).getName() != null && dataByCounty.get(i).getName().equals(tempName))
                {
                    County county = dataByCounty.get(i);
                    county.setW(totalW/REPEAT);
                    county.setB(totalB/REPEAT);
                    out.printf("\n%s,%s,%s,%s,%s,%s", county.state, county.name, county.hsDiploma2012, county.avgUnemployment, county.ba, county.wa);
                }
            }
			index++;
		}
        out.close();       
	}

	public String[] parseLine(String line) {
		//System.out.println(line);
		String[] result = new String[TOTAL_INDECES];
		int index = 0; 

		// initializing array elements
		for(int i=0; i<result.length; i++)
			result[i] = "";

		// add tokens to array, skipping commas
		for(int i=0; i<result.length; i++) {
			while(index < line.length() && line.charAt(index) != ',') {
				result[i] += line.charAt(index);
				index++;
			}
			index++;
			//System.out.println(result[i]);
		}

		return result;
	}

    

    
}
