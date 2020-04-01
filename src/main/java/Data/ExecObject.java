package Data;

import java.util.List;
/**
 * This class represents the structure of the command expected 
 * in the demand of execution from client , 
 * ExecObject refer to Execution object
 */
public class ExecObject {
	
	/**
	 * Name of command
	 */
	private String cmd;
	/**
	 * List of options come with the command
	 */
	private List<String> opt; 
	/**
	 * Number of files that the user want to execute
	 */
	private Long nbfile;
	/**
	 * Name of result file 
	 */
	private String result;
	/**
	 * Original name of the file sent by user
	 */
	private String filename ="";

	/**
	 * Specify status of the ExecObject
	 */
	private boolean error = false;
	
	private boolean end = false;
	
	private boolean inExec = false;
	
	

	public ExecObject() {
	}
	
	
	/**
	 * Create a new ExecObject with the given name , options list, number of files and name of result file .
	 * @param name 
	 * @param option
	 * @param number
	 * @param result
	 */
	public ExecObject( String name  ,List<String> option, Long number ,String result ){
		this.cmd = name;
		this.opt = option;
		this.nbfile = number;
		this.result = result;
		
		
	}

	/**
	 * Get number of files  
	 * @return This ExecObject's nbfile
	 */
	public Long getnbfile() {
		return nbfile;
	}

	/**
	 * Change the number of file
	 * @param nbfile This ExecObject's new nbfile
	 */
	public void setnbfile(Long nbfile) {
		this.nbfile = nbfile;
	}

	/**
	 * Get name of result file 
	 * @return This ExecObject's result
	 */
	public String getresult() {
		return result;
	}

	/**
	 * Change the name of result file
	 * @param result This ExecObject's new result
	 */
	public void setresult(String result) {
		this.result = result;
	}

	/**
	 * @return If this ExecObject finished the execution
	 */
	public boolean isEnd() {
		return end;
	}
	
	/**
	 * @param end Status of the execution 
	 */
	public void setEnd(boolean end) {
		this.end = end;
	}
	/**
	 * @return If this ExecObject has error 
	 */
	public boolean isError() {
		return error;
	}

	/**
	 * Set this ExecObject's error to true
	 */
	public void setError() {
		this.error = true;
	}

	/**
	 * @return If this ExecObject is still in execution 
	 */
	public boolean isInExec() {
		return inExec;
	}

	/**
	 * Set this ExecObject's InExec to true
	 */
	public void setInExec() {
		this.inExec = true;
	}


//	@Override
//	public String toString() {
//		// TODO Auto-generated method stub
//		return "Exec obj : "+cmd +"opt:"+ opt.toArray().toString() + " nbfile: "+nbfile+" result : "+result+ " end: "+end;
//	}

	/**
	 * Get name of the ExecObject
	 * @return This ExecObject's name
	 */
	public String getcmd() {
		return cmd;
	}

	public void setcmd(String cmd) {
		this.cmd = cmd;
	}

	/**
	 * Get the options list of the ExecObject
	 * @return This ExecObject's option list
	 */
	public List<String> getopt() {
		return opt;
	}

	/**
	 * Change The options list of this ExecObject
	 * @param opt This ExecObject's options list
	 */
	public void setopt(List<String> opt) {
		this.opt = opt;
	}
	
	/**
	 * Get name of the original file 
	 * @return This ExecObject's filename
	 */
	public String getFilename() {
		return filename;
	}
	
	/**
	 * Change name of the original file 
	 * @param filename This ExecObject's new filename
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	public String options() {
		StringBuilder str = new StringBuilder();
		opt.stream().forEach(a-> str.append("\""+a+"\""));
		return str.toString();
	}

}
