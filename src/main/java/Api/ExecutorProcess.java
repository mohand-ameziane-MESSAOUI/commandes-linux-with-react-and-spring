package Api;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.UUID;

import org.json.simple.JSONObject;

import Data.ExecObject;

/**
 * This class represents the process in which the command is executed and result , status  files are created  
 */
public class ExecutorProcess extends Thread{
	/**
	 * Command of this execution 
	 */
	private ExecObject commandInExec ;
	/**
	 * Command's id
	 */
	private UUID id;
	/**
	 * Create a new ExecutorProcess with the given command and id
	 * @param eo Command to execute
	 * @param i Command's id
	 */
	public ExecutorProcess(ExecObject eo , UUID i) {
		commandInExec = eo;
		id = i;
	}
	
	
	/*
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run(){
		Runtime rt = Runtime.getRuntime();
		String path = ApiController.generalPath+id+"/";
		StringBuilder log = new StringBuilder();
//		StringBuilder error = new StringBuilder();
		
		Process proc1,proc2;
		StringBuilder ccm = new StringBuilder() ;
		String cmd = commandInExec.getcmd();
		
		
		switch (cmd) {
		case "gcc":
			
			ccm.append("gcc ");
			ccm.append(commandInExec.getFilename()+" ");
			commandInExec.getopt().stream().forEach(a -> { if(!a.equals("-o")) ccm.append(a+" "); });
			ccm.append("-o "+commandInExec.getresult());
			
			try {
				proc1 = rt.exec(ccm.toString() , null ,new File(path) );
				radStdStream(proc1,  log  ,commandInExec);
				
//				if there is not errors and command's option is different of "-o" so we read the result file 
				if (!commandInExec.isError() && ( commandInExec.getopt().size() == 0 || (commandInExec.getopt().size()==1 && commandInExec.getopt().contains("-o"))) ) {
					proc2 = rt.exec("./"+commandInExec.getresult(),null, new File(path));
					radStdStream(proc2, log ,commandInExec);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
			
		case "dot":
			ccm.append(" dot ");
			ccm.append(commandInExec.getFilename()+" ");
			commandInExec.getopt().stream().forEach(a -> { ccm.append(a+" "); });
//			redirection
			ccm.append(" > "+commandInExec.getresult());
			
			try {
				proc1 = rt.exec(new String[]{"/bin/sh", "-c", ccm.toString()} , null ,new File(path) );
				radStdStream(proc1, log  ,commandInExec );
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			break;
		case "latex":
			String resultname;
			if(commandInExec.getopt().size()==0  ) {
				ccm.append(cmd+" ");
				ccm.append(commandInExec.getFilename()+" ");
				resultname = commandInExec.getFilename().replace(".tex", ".dvi");
			}else {
				
				commandInExec.getopt().stream().forEach(a -> { ccm.append(a+" "); });
				ccm.append(commandInExec.getFilename()+" ");
				resultname = commandInExec.getFilename().replace(".tex", ".pdf");
			}
			commandInExec.setresult(resultname);
			
			try {
				proc1 = rt.exec( ccm.toString() , null ,new File(path) );
				radStdStream(proc1, log  ,commandInExec );
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		default:
			ccm.append(cmd+" ");
			ccm.append(commandInExec.getFilename()+" ");
			commandInExec.getopt().stream().forEach(a -> { ccm.append(a+" "); });
			try {
				proc1 = rt.exec(new String[]{"/bin/sh", "-c", ccm.toString()} , null ,new File(path) );
				radStdStream(proc1, log ,commandInExec );
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			break;
		}
				
		createJsonfileResponse(log , path);
			
		commandInExec.setEnd(true);
				
		

		
	}
	

	/**
	 * Create a json file containing this log message and save it in directory has this path
	 * @param log Message to write in this file
	 * @param path Directory path
	 */
	private void createJsonfileResponse(StringBuilder log , String path) {
		JSONObject obj = new JSONObject();
		String filename;
		
		if(commandInExec.isError()) obj.put("Type", "error");
		else obj.put("Type", "ok");
		
		obj.put("Message", log.toString());
		
		filename = path+"status.json";
		try (FileWriter file = new FileWriter(filename)){
			file.write(obj.toJSONString());
			file.flush();
			
		} catch (Exception e) {
		}
		
	}
	

	/**
	 * Read stdout and stderr of process
	 * @param proc Process to read
	 * @param log String to save the exit
	 * @param commandInExec Command in execution
	 * @throws IOException
	 */
	public void radStdStream(Process proc ,  StringBuilder log ,ExecObject commandInExec) throws IOException {
		String s ="";
		BufferedReader stdInput;
		BufferedReader stdError;
		stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
		stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
		
		while ((s = stdInput.readLine()) != null) {
			log.append(s);
		}
		while ((s = stdError.readLine()) != null) {
			
			commandInExec.setError();
			log.append(s);
		}	
	}



}
