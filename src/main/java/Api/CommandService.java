package Api;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;

import Data.Command;
import Data.Option;

/**
 * This class represents a service which parse commands json file
 */
@Service
public class CommandService {
	
	/**
	 * Read file , extract commands with their options
	 * @return Commands list
	 */
	public List<Command> parsJson() {
		
		List<Command> commands=new ArrayList<>();
		JSONParser jsonParser = new JSONParser();
//		InputStream is =CommandService.class.getResourceAsStream("Commands.json");
		
		InputStream is = TypeReference.class.getResourceAsStream("/json/Commands.json");
//		if (is != null) System.out.println("not null");
		InputStreamReader isr = new InputStreamReader(is);
		
		try (BufferedReader reader = new BufferedReader(isr);)
		{
			//Read JSON file
			Object obj = jsonParser.parse(reader);
			
			JSONArray commandList = (JSONArray) obj;
			
			Iterator<JSONObject> iteratorCmd = commandList.iterator();
	            while (iteratorCmd.hasNext()) {
	            	JSONObject lineCmd =  iteratorCmd.next();
	            	
	            	String cmd = (String) lineCmd.get("cmd");  
	  
	            	JSONArray options = (JSONArray) lineCmd.get("opt");
	            	List<Option> optList = new ArrayList<>();
	            	
	            	Iterator<JSONObject> iteratorOpt = options.iterator();
	            	while(iteratorOpt.hasNext()){

	            		JSONObject lineOpt = iteratorOpt.next();

		            	String name = (String) lineOpt.get("name");
		            	String type =  (String) lineOpt.get("type");
		            	String description =  (String) lineOpt.get("description");
		            

		            	optList.add(new Option(name,type,description));
		           
 	
	            	}
	            	JSONArray opt =  (JSONArray) lineCmd.get("opt"); ;  
	            	
	            
	                 commands.add(new Command(cmd,optList ));
	            }
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return commands;
	}
	
	
	
	
	/**
	 * Get list of commands from the file that contain the description of supported commands
	 * @return Commands list
	 */
	public List<Command> getCommands (){
		
		return parsJson();
		
	}
	
	
}