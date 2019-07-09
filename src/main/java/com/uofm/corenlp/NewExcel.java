package com.uofm.corenlp;



import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class NewExcel 
{

    private String inputFile;
    String[] data = null;
    Map<String,String> quoteNameMap = new HashMap<String, String>();
    public void setInputFile(String inputFile) 
    {
        this.inputFile = inputFile;
    }

    public String[] read()  
    {
        File inputWorkbook = new File("C:/Users/Maxim/Downloads/sp_500_stocks_old.xls");
        Workbook w;

        try 
        {
            w = Workbook.getWorkbook(inputWorkbook);
            // Get the first sheet


            Sheet sheet = w.getSheet(0);
            data = new String[sheet.getRows()];
            // Loop over first 10 column and lines
       //     System.out.println(sheet.getColumns() +  " " +sheet.getRows());
                for (int i = 0; i < sheet.getRows(); i++) 
                {
                    Cell cell = sheet.getCell(1, i);
                    data[i] = cell.getContents();
                   //System.out.println(cell.getContents());
                }

            
                for (int i = 0; i <data.length-3 ; i++) 
                {
                	
                    //System.out.println(data[i]);
            }

        } 
        catch (BiffException e) 
        {
            e.printStackTrace();
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    return data;
    }

    
    public Map<String,String> readMap()  
    {
        File inputWorkbook = new File("C:/Users/Maxim/Downloads/sp_500_stocks_old.xls");
        Workbook w;

        try 
        {
            w = Workbook.getWorkbook(inputWorkbook);
            // Get the first sheet


            Sheet sheet = w.getSheet(0);
            //data = new String[sheet.getRows()];
            // Loop over first 10 column and lines
       //     System.out.println(sheet.getColumns() +  " " +sheet.getRows());
                for (int i = 0; i < sheet.getRows() - 3; i++) 
                {
                	Cell cell1 = sheet.getCell(0, i);
                	String quote = cell1.getContents();
                    Cell cell2 = sheet.getCell(1, i);
                    String stockName = cell2.getContents();
                    stockName = stockName.split(" ")[0];
                    stockName = stockName.toLowerCase();
                   //System.out.println(cell.getContents());
                    //quoteNameMap.put(quote, stockName);
                    if(!(stockName.equalsIgnoreCase("bank") || stockName.equalsIgnoreCase("the"))) {
                    	quoteNameMap.put(stockName, quote);
                    }
                }

            
                for (Map.Entry<String, String> set : quoteNameMap.entrySet()) {
                	System.out.println(set.getKey() + " :: " + set.getValue());
                }
               

        } 
        catch (BiffException e) 
        {
            e.printStackTrace();
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    return quoteNameMap;
    }
public static void main(String[] args) {
	try {
		System.out.println(new NewExcel().readMap().size());
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
}

