package Data;

import java.util.List;

/**
 * This class represents the structure of commands expected in the json file Commands.json
 */
public class Command {
	
	/**
	 * Name of command
	 */
	private String cmd;
	/**
	 * List of options come with this command
	 */
	private List<Option> opt; 
	
	
	/**
	 * Create a new Command with the given name and options list.
	 * @param c Command's name
	 * @param o Command's options list
	 */
	public Command(String c , List<Option> o) {
		cmd = c;
		opt = o;
		
		
	}
	
	protected Command() {
	}
	
	/**
	 * Get the name of the command
	 * @return This command's name
	 */
	public String getcmd() {
		return cmd;
	}
	
	/**
	 * Change the name of this command
	 * @param cmd This command's new name
	 */
	public void setcmd(String cmd) {
		this.cmd = cmd;
	}
	
	/**
	 * Get the options list of the command
	 * @return This command's option list
	 */
	public List<Option> getopt() {
		return opt;
	}
	
	/**
	 * Change The options list of this command
	 * @param opt This command's new options list
	 */
	public void setopt(List<Option> opt) {
		this.opt = opt;
	}
	

}
