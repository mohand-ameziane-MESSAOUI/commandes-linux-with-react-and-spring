package Api;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import Data.Command;
import Data.ExecObject;




/**
 *This class is the entry point of server Api , here we receive all urls and treat them
 *
 */
@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class ApiController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	/**
	 *  Directory where user uploads are stored  
	 */
	private boolean folder = new File(System.getProperty("user.dir")+"/Folder").mkdir();
	/**
	 * Path of the directory where user uploads are stored  
	 */
	public static String generalPath = System.getProperty("user.dir")+"/Folder/";
	/**
	 * Instance of CommandService
	 */
	@Autowired
	private CommandService commandService ;
	
	/**
	 * Commands list supported by server
	 */
	private List<Command> commands = new ArrayList<Command>();
	/**
	 * Instance of ExecutionService
	 */
	@Autowired
	private ExecutionService executionService;
	
	/**
	 * Get commands list supported by the server .
	 * This method is called when receiving http get request with "@/command" url 
	 * @return server's commands list
	 * @throws FileNotFoundException 
	 */
	@RequestMapping("/commands")
	public List<Command> getCommands() {
		if (commands.size() == 0)commands =commandService.getCommands();
		return commands;
		
	}
	
	
	/**
	 * Receiving a command to execute, creating an id and directory in where uploads file by user and execution result will stored . 
	 * This method is called when receiving http post request with "@/execute" url  
	 * @param newExecObject Command to execute
	 * @return Json file containing command's id
	 */
	@RequestMapping(value ="/execute" , method = RequestMethod.POST , consumes = {MediaType.APPLICATION_JSON_VALUE} ,
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	ResponseEntity<?> createIdAndTask( @RequestBody  ExecObject newExecObject) {
		UUID uniqueID  = UUID.randomUUID();
//		bind id with the command 
		executionService.addTask(uniqueID , newExecObject);
		logger.info("id "+uniqueID+" created for "+newExecObject.getcmd()+" command with"+newExecObject.options()+" options.");
//		creation of directory for this command
		new File(generalPath+uniqueID).mkdir();
	    return new ResponseEntity<>(
	    	      "{\"id\" : \"" +uniqueID.toString()+"\"}", 
	    	      HttpStatus.OK);
	}
	
	/**
	 * Receiving files to execute with sent command ,storing file in command directory and start the execution .
	 * This method is called when receiving http post request with "@/execute/id" url 
	 * @param files Files to execute
	 * @param id1 Command's id
	 * @return Http response
	 */
	@RequestMapping(value ="/execute/{id1}"  , method = RequestMethod.POST , produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<String>  handleFileUpload(@RequestParam(value = "file",  required=false) MultipartFile []files , @PathVariable String id1 ) {
		UUID id = UUID.fromString(id1);
		String fileName = "";
		
		if(!executionService.isIdCorrect(id) ) return new ResponseEntity<>( "{ \"error\" : \"UNKNOWN ID\" }",  HttpStatus.BAD_REQUEST);
		if(executionService.isCommandInExec(id)) return new ResponseEntity<>( "{ \"error\" : \"YOUR COMMAND IS ALREADY IN EXECUTION\" }",  HttpStatus.BAD_REQUEST);
		 try {
        	
        	String path = generalPath+id+"/";
//        	move files to command's directory
        	if (files != null) {
        		for (MultipartFile file : files) {
        			fileName = file.getOriginalFilename();
        			File fileToSave = new File(path + fileName);
        			file.transferTo(fileToSave);
        			logger.info("file recived "+ fileName+" for id "+ id);
        		}
        		
        	}
            
            executionService.setFileToExecute(id , fileName);
            executionService.enterInService(id );
            
        } catch (IOException ioe) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        
        return ResponseEntity.ok().build();
    }
	
	
	/**
	 * Get status of command has this id , check if execution of command has finished
	 * This method is called when receiving http get request with "@/getstatus/id1" url 
	 * @param id1 Command's id
	 * @return Http response
	 */
	@RequestMapping(value ="/getstatus/{id1}"  , method = RequestMethod.GET )
	public ResponseEntity<String> getCommandStatus(@PathVariable String id1 ){
		UUID id = UUID.fromString(id1);
		if(!executionService.isEndCommand(id)) 
			 return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		else {
			logger.info("execution of command has id :"+id+" is finished");
			return ResponseEntity.ok().build();
		}
	}
	
		/**
		 * Get status of result has this id , check if execution of command has error or not
		 * This method is called when receiving http get request with "@/resultstatus/id1" url
		 * @param request Http request
		 * @param response Http response 
		 * @param id1 Command's id
		 * @throws IOException
		 */
		@RequestMapping(value ="/resultstatus/{id1}"  , method = RequestMethod.GET )
		public void getResultStatus(HttpServletRequest request, HttpServletResponse response ,@PathVariable String id1 ) throws IOException{
			UUID id = UUID.fromString(id1);
			File jsonFile = executionService.getLog(id);
			if (jsonFile!= null) {
				creatResultResponse(jsonFile, response);
				if(executionService.isCommandHasError(id)) executionService.clearTask(id);
			}
			else response.sendError(404);
		}
	
	
	
	/**
	 * Get execution result of the command has this id , searching the result file in command directory
	 * This method is called when receiving http get request with "@/resultat/id1" url 
	 * @param request Http request
	 * @param response Http response
	 * @param id1 Command's id
	 * @throws IOException 
	 */
	@RequestMapping(value ="/resultat/{id1}"  , method = RequestMethod.GET )
		public void downloadPDFResource(HttpServletRequest request, HttpServletResponse response ,@PathVariable String id1) throws IOException {
		UUID id = UUID.fromString(id1);
		
		if(!executionService.isIdCorrect(id) ) jsonResponse("UNKNOWN ID", response);
		else if(!executionService.isEndCommand(id)) jsonResponse("EXECUTION IS NOT FINISHED!", response);
		else {
			if (executionService.isCommandHasError(id)) {
				File jsonFile = executionService.getLog(id);
				if (jsonFile!= null) {

					creatResultResponse(jsonFile, response);
					executionService.clearTask(id);
					
				}else response.sendError(404);
			}else {
				File file = executionService.getResult(id);

				if (file != null) {
					
					creatResultResponse(file, response);
					logger.info("result of coomand has id :"+id+" is sent");
					executionService.clearTask(id);
					
				}else {
					executionService.clearTask(id);
					response.sendError(404);
				}
			}
		}
	}
	/**
	 * Create json file
	 * @param message Message to write in this file
	 * @param response Http response contain the json file
	 * @throws IOException
	 */
	public void jsonResponse(String message , HttpServletResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		JSONObject json = new JSONObject();
		json.put("error", message);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.print(json);
        out.flush();   	
	}
	
	/**
	 * Put file in the Http reponse
	 * @param file File to send with Http response
	 * @param response Http response
	 * @throws IOException
	 */
	public void creatResultResponse(File file,HttpServletResponse response ) throws IOException {
		String mimeType = URLConnection.guessContentTypeFromName(file.getName());
		if (mimeType == null) mimeType = "application/octet-stream";
		response.setContentType(mimeType);
		response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));
		response.setContentLength((int) file.length());
		InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
		FileCopyUtils.copy(inputStream, response.getOutputStream());
		
	}
}

