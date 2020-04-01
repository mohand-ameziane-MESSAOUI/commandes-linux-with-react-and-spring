package Api;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;
import java.util.UUID;
import java.util.List;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import Data.Command;
import Data.ExecObject;

/**
 * This class represents a service which manage commands in execution , 
 *  bind each of them with id , register them , 
 *  and create thread for each execution 
 */
@Service
public class ExecutionService {
	/**
	 * Map to save commands in execution with their id 
	 * Hashtable is a synchronized map 
	 */
	private Map<UUID , ExecObject> commandInExecution = new Hashtable<UUID, ExecObject>();

	
	/**
	 * Add new command and id to map
	 * @param id Command's id
	 * @param newExecObject New command
	 */
	public void addTask( UUID id , ExecObject newExecObject) {
		commandInExecution.put(id,newExecObject);
	}

	/**
	 * Check if this id is already saved
	 * @param id Command's id
	 * @return Result of check
	 */
	public boolean isIdCorrect(UUID id) {
		return commandInExecution.containsKey(id);
	}
	
	/**
	 * Check if the command of this id has error 
	 * @param id Command's id
	 * @return Result of check
	 */
	public boolean isCommandHasError(UUID id) {
		return commandInExecution.get(id).isError();
	}
	
	/**
	 * Check if the command of this id is in execution 
	 * @param id Command's id
	 * @return Result of check
	 */
	public boolean isCommandInExec(UUID id) {
		return commandInExecution.get(id).isInExec();
	}
	
	/**
	 * Check if the command of this id has finished the execution
	 * @param id Command's id
	 * @return Result of check
	 */
	public boolean isEndCommand(UUID id) {
		return commandInExecution.get(id).isEnd();
	}
	
	/**
	 * Change command status
	 * @param id Command's id
	 */
	public void setCommandInExec(UUID id) {
		 commandInExecution.get(id).setInExec();
	}
	
	/**
	 * Set the name of result file 
	 * @param id Command's id
	 * @param fileName New name 
	 */
	public void setFileToExecute(UUID id, String fileName) {
		commandInExecution.get(id).setFilename(fileName);
	}
	
	/**
	 * Create thread for executing command and start execution 
	 * @param id Command's id
	 * @throws IOException
	 */
	@Async
	public void enterInService(UUID id ) throws IOException {
		
		setCommandInExec(id);
		ExecutorProcess executorProcess = new ExecutorProcess(commandInExecution.get(id), id);
		executorProcess.run();
	}
	

	/**
	 * Get result file from the directory has this id
	 * @param id Command's id
	 * @return Result file
	 */
	public File getResult(UUID id) {
		String path = ApiController.generalPath+id+"/";
		ExecObject command = commandInExecution.get(id);
		
		File resultFile = new File(path+command.getresult());
		if(resultFile.exists())return resultFile;
		else return null;
	}
	
	
	/**
	 * Get status file from the directory has this id
	 * @param id Command's id
	 * @return Status file
	 */
	public File getLog(UUID id) {
		String path = ApiController.generalPath+id+"/";
		File resultFile = new File(path+"status.json");
		if(resultFile.exists())return resultFile;
		else return null;
	}

	/**
	 * Remove this id from map and delete it's directory 
	 * @param id Command's id
	 */
	public void clearTask(UUID id) {
		
		File commandFolder = new File(ApiController.generalPath+id);
		String[]entries = commandFolder.list();
		for(String s: entries){
		    File currentFile = new File(commandFolder.getPath(),s);
		    currentFile.delete();
		}
		commandFolder.delete();
		
		commandInExecution.remove(id);
	}

	
	
	
	
}


