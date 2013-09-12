import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import javax.swing.text.html.HTMLDocument.Iterator;

import edu.fudan.example.nlp.DepParser;

public class TextParser {
	private static String inputfilePath = "input/input1.txt";
	private static String outputPath = "output/out.txt";
	public static Pattern pattern;
	
	private static HashMap<String, ArrayList<String>> mHash = new HashMap<>();
	private static String lastSubString;
	private static DepParser depParser;
	
	public static void main(String[] args) throws Exception {
		// System.out.println("123");
		depParser = new DepParser();
		pattern = Pattern.compile(",|\\.|;|\"|!|~|，|。|；|”|“|！|\t|？|?");
		if (args.length >= 3) {
			inputfilePath = args[1];
			outputPath = args[2];
		} else {
			System.out.println("将使用默认路径");
		}
		reader();
		writeToFiles();
	}

	public static void reader() throws IOException {
		File inputFile = new File(inputfilePath);
		
		FileInputStream fins;
		try {
			fins = new FileInputStream(inputFile);
			InputStreamReader inReader = new InputStreamReader(fins);
			BufferedReader bufferedReader = new BufferedReader(inReader);
			String s;

			while ((s = bufferedReader.readLine()) != null) {
				String contents[] = split(s);
				for (String subString : contents) {
					if (subString != null && subString.length() > 0) {
						ProcessString(subString);
					}

				}

			}
			bufferedReader.close();
			fins.close();
			
			
			

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void ProcessString(String s) {
		if (!s.endsWith("\n")) {
			if(lastSubString !=null){
				s = lastSubString +s;
				lastSubString = null;
			}
			String [] result = depParser.getResult(s);
//			mHash.put(result[1], result[0]);
			if(result ==null){
				return;
			}
			if(mHash.containsKey(result[1])){
				mHash.get(result[1]).add(result[0]);
			}else {
				ArrayList<String> list = new ArrayList<>();
				list.add(result[0]);
				mHash.put(result[1], list);
			}
			if(mHash.containsKey(result[3])){
				mHash.get(result[1]).add(result[0]);
			}else{
				ArrayList<String> list = new ArrayList<>();
				list.add(result[0]);
				mHash.put(result[3], list);
			}
			
			
		}else{
			lastSubString = s.substring(0, s.length()-1);
		}
	}
	
	
	public static void writeToFiles() throws IOException{
		File  outFile = new File(outputPath);
		if(!outFile.exists()){
			outFile.createNewFile();
		}
		FileOutputStream fos = new FileOutputStream(outFile);
		OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fos);
		
		
		java.util.Iterator<Entry<String, ArrayList<String>>>  e=  mHash.entrySet().iterator();
		while(e.hasNext()){
			Entry<String, ArrayList<String>> s= e.next();
			outputStreamWriter.write(s.getKey());
			outputStreamWriter.write(" || ");
			for(String value:s.getValue()){
				outputStreamWriter.write(value);
				outputStreamWriter.write("|");
			}
			outputStreamWriter.write("\n");
			
		}
		
		outputStreamWriter.close();
	}

	public static String[] split(String s) {
		System.out.println("123");

		return pattern.split(s);
	}
}
