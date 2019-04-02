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
     * Gets data from the education.csv file and updates a list
     * of counties with the data.
     */
    public void getEduData()
    {
        //the number of lines in the file
        int lineCount = 0;

        //uses FileUtils to read the file
        Scanner reader = FileUtils.openToRead("Education.csv");

        //lines taken from the education.csv file
        ArrayList<String> eduLines = new ArrayList<String>();;

        //add all the lines of the file into a list
        while(reader.hasNext())
        {
            String line = reader.nextLine();
            lineCount++;
            eduLines.add(line);
        }

        //an array of each tokenized piece of data for every line
        String[][] data = new String[lineCount][lineCount];

        //the number of pieces of data that have occured in the line
        int dataNum = 0;

        //loop through each line and get each piece of data
        for(int i = 0; i < eduLines.size(); i++)
        {
            //the current data piece
            String oneDataPiece = "";

            //true if the data is in between two quotation marks
            boolean inQuotation = false;
            
            //the current line being read
            String str = eduLines.get(i);

            //loop through every character of the line and collect data
            for(int j = 0; j < eduLines.get(i).length(); j++)
            {
                //determine if the reader is in between quotations
                if(str.charAt(j) == '\"' && inQuotation == true)
                    inQuotation = false;
                else if(str.charAt(j) == '\"')
                    inQuotation = true;
                
                //add to the data piece if not at the end of the data piece
                if(!(str.charAt(j) ==','))
                    oneDataPiece += str.charAt(j);

                //if not in a quotation and is a comma, add data piece to data
                else if(inQuotation == false)
                {
                    data[i][dataNum] = oneDataPiece;
                    dataNum++;
                    oneDataPiece = "";
                }
            }

            //reset the number of data pieces for the current line   
            dataNum = 0;
        }

        //for every line, save the nesscary data into County objects
        for(int i = 0; i < eduLines.size(); i++)
        {  
            //if the line contains a county, add the county to the list of counties
            if(data[i][3] != "" && i > 0)
            {
                County county = new County(data[i][1], data[i][2], data[i][44], null, 0.0, 0.0);
                dataByCounty.add(county);
            }

            //if the line is not valid, add an empty county
            else dataByCounty.add(new County(null, null, null,  null, 0.0, 0.0));
        }
    }
    
    /*
     * Gets data from the UnemploymentMed.csv file and updates a list
     * of counties with the data.
     */
    public void getUnempData()
    {
        //the number of lines in the file
        int lineCount = 0;

        //uses FileUtils to read the file
        Scanner reader = FileUtils.openToRead("UnemploymentMed.csv");
        
        //lines taken from the unemploymentmed.csv file
        ArrayList<String> unemploymentLines = new ArrayList<String>();

        //add all the lines of the file into a list
        while(reader.hasNext())
        {
            String line = reader.nextLine();
            lineCount++;
            unemploymentLines.add(line);
        }

        //an array of each tokenized piece of data for every line
        String[][] data = new String[lineCount][lineCount];

        //the number of pieces of data that have occured in the line
        int dataNum = 0;

        //loop through each line and get each piece of data
        for(int i = 0; i < unemploymentLines.size(); i++)
        {
            //the current data piece
            String oneDataPiece = "";

            //true if the data is in between two quotation marks
            boolean inQuotation = false;

            //the current line being read
            String str = unemploymentLines.get(i);

            //loop through every character of the line and collect data
            for(int j = 0; j < unemploymentLines.get(i).length(); j++)
            {
                //determine if the reader is in between quotations
                if(str.charAt(j) == '\"' && inQuotation == true)
                    inQuotation = false;
                else if(str.charAt(j) == '\"')
                    inQuotation = true;

                //add to the data piece if not at the end of the data piece
                if(!(str.charAt(j) ==','))
                    oneDataPiece += str.charAt(j);

                //if not in a quotation and is a comma, add data piece to data
                else if(inQuotation == false)
                {
                    data[i][dataNum] = oneDataPiece;
                    dataNum++;
                    oneDataPiece = "";
                }
            }   

            //reset the number of data pieces for the current line   
            dataNum = 0;
        }

        //for every line, save the nesscary data into County objects
        for(int i = 0; i < unemploymentLines.size(); i++)
        {
            //if the line contains a county, update the county with the correct average
            if(data[i][3] != "" && !data[i][1].equals("State") && i > 0 )
            {
                //get the average unemployment rate between 2012 and 2016
                double avg = (Double.parseDouble(data[i][29]) +
                              Double.parseDouble(data[i][33]) +
                              Double.parseDouble(data[i][37]) + 
                              Double.parseDouble(data[i][41]) + 
                              Double.parseDouble(data[i][45]))/5.0;
                
                //round the average
                avg = Math.round(avg * 100.0) / 100.0;

                //set the unemployment rate of the corresponding county
                if(dataByCounty.get(i-3).getState() != null) 
                    dataByCounty.get(i-3).setAvgUnemployment(Double.toString(avg));
            } 
        }
    }

    /*
     * Gets data from the cc-est2017-alldata.csv file about the
     * population of certain races in each county and updates a list 
     * of counties with the data.
     */
    public void getRaceData()
    {
        //uses FileUtils to read the file
        Scanner sc = FileUtils.openToRead(ETHNICITY_FILE);

        //skip the headers
		sc.nextLine(); 

        //the data to take from the line
        String[] tempData = new String[TOTAL_INDECES];

        // temp name of county
		String tempName = ""; 

        // name of the state
        String stateName = ""; 

        // temp count of black population
		//double tempB = 0.0; 

        // temp count of white population
		//double tempW = 0.0; 

        // iterates through the list of counties
		int index = 0; 

        //total amount of white population
		double totalW = 0.0;

        //total amount of black population
		double totalB = 0.0;

        //create a file to print all the information to
        PrintWriter out = FileUtils.openToWrite("EduUnemployment.csv");
        out.println("\"State,County Name, High School Diploma Only Rate 2012 - 2016, " +
        "Average Unemployment Rate 2012 - 2016, Black population, White Population\"");
        
        //loops throught the whole file
		while(sc.hasNext()) {
            //parse the current line and get the data
            tempData = parseLine(sc.nextLine());
            double tempW = 0;
            double tempB = 0;
            if(Integer.parseInt(tempData[6]) == 0 && Integer.parseInt(tempData[5]) == 10 )
            {
                tempW = Double.parseDouble(tempData[WA_MALE]) 
                + Double.parseDouble(tempData[WA_FEMALE]);
                tempB = Double.parseDouble(tempData[BA_MALE]) 
                + Double.parseDouble(tempData[BA_FEMALE]);

                //set the name of the county
                tempName = tempData[4];

                //get the state of the county
                stateName = getStateAbbr(tempData[3].toUpperCase());
            
                //loops through every county and determines if 
                //the county matches the current county being read
                for(int i = 0; i < dataByCounty.size(); i++)
                {
                    if(dataByCounty.get(i).getName() != null && 
                        dataByCounty.get(i).getName().equals(tempName) &&
                        dataByCounty.get(i).getState().equals(stateName) && 
                        dataByCounty.get(i).getAvgUnemployment() != null)
                    {
                        County county = dataByCounty.get(i);

                        //set the white and black population of the county
                        county.setW(tempW);
                        county.setB(tempB);

                        //print the information of the countyto the output file
                        out.printf("\n%s,%s,%s,%s,%s,%s", county.state, county.name, 
                        county.hsDiploma2012, county.avgUnemployment, county.ba, county.wa);
                    }
                }
            }
			index++;
		}

        //close the printwriter object
        out.close();       
	}

    /**
     * Used to get the abbreviation of a state based on
     * its given name
     * @param input         the full name of the state
     * @return              the abbreviated name
     */
    public String getStateAbbr(String input)
    {
        switch (input.toUpperCase())
        {
            case "ALABAMA":
                return "AL";

            case "ALASKA":
                return "AK";

            case "AMERICAN SAMOA":
                return "AS";

            case "ARIZONA":
                return "AZ";

            case "ARKANSAS":
                return "AR";

            case "CALIFORNIA":
                return "CA";

            case "COLORADO":
                return "CO";

            case "CONNECTICUT":
                return "CT";

            case "DELAWARE":
                return "DE";

            case "DISTRICT OF COLUMBIA":
                return "DC";

            case "FEDERATED STATES OF MICRONESIA":
                return "FM";

            case "FLORIDA":
                return "FL";

            case "GEORGIA":
                return "GA";

            case "GUAM":
                return "GU";

            case "HAWAII":
                return "HI";

            case "IDAHO":
                return "ID";

            case "ILLINOIS":
                return "IL";

            case "INDIANA":
                return "IN";

            case "IOWA":
                return "IA";

            case "KANSAS":
                return "KS";

            case "KENTUCKY":
                return "KY";

            case "LOUISIANA":
                return "LA";

            case "MAINE":
                return "ME";

            case "MARSHALL ISLANDS":
                return "MH";

            case "MARYLAND":
                return "MD";

            case "MASSACHUSETTS":
                return "MA";

            case "MICHIGAN":
                return "MI";

            case "MINNESOTA":
                return "MN";

            case "MISSISSIPPI":
                return "MS";

            case "MISSOURI":
                return "MO";

            case "MONTANA":
                return "MT";

            case "NEBRASKA":
                return "NE";

            case "NEVADA":
                return "NV";

            case "NEW HAMPSHIRE":
                return "NH";

            case "NEW JERSEY":
                return "NJ";

            case "NEW MEXICO":
                return "NM";

            case "NEW YORK":
                return "NY";

            case "NORTH CAROLINA":
                return "NC";

            case "NORTH DAKOTA":
                return "ND";

            case "NORTHERN MARIANA ISLANDS":
                return "MP";

            case "OHIO":
                return "OH";

            case "OKLAHOMA":
                return "OK";

            case "OREGON":
                return "OR";

            case "PALAU":
                return "PW";

            case "PENNSYLVANIA":
                return "PA";

            case "PUERTO RICO":
                return "PR";

            case "RHODE ISLAND":
                return "RI";

            case "SOUTH CAROLINA":
                return "SC";

            case "SOUTH DAKOTA":
                return "SD";

            case "TENNESSEE":
                return "TN";

            case "TEXAS":
                return "TX";

            case "UTAH":
                return "UT";

            case "VERMONT":
                return "VT";

            case "VIRGIN ISLANDS":
                return "VI";

            case "VIRGINIA":
                return "VA";

            case "WASHINGTON":
                return "WA";

            case "WEST VIRGINIA":
                return "WV";

            case "WISCONSIN":
                return "WI";

            case "WYOMING":
                return "WY";
            
        }
        //if the state does not match, return null
        return null;
    }
    
    /** 
     * Parses a line into an array of Strings to be used
     * as a dataset
     * @param line          the line to parse
     * @return              a String array of each data piece
     */
	public String[] parseLine(String line) {
        
        //the array of Strings to be returned
		String[] result = new String[TOTAL_INDECES];

        //the current index in the line
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
		}
		return result;
    }
}
