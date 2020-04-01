package Data;

/**
 * This class represents the structure of the option expected in the json file Commands.json
 */
public class Option {

	/**
	 * Name of the option
	 */
	String name;
	/**
	 * Type that it returns
	 */
	String type;
	/**
	 * Description of the option
	 */
	String description;
	
	public Option() {
	}
	
	/**
	 * Create a new Option with the given name , type and description .
	 * @param name Option's name
	 * @param type Option's tye
	 * @param description Option's description
	 */
	public Option(String name, String type, String description) {
		this.name = name;
		this.type = type;
		this.description = description;
	}
	
	/**
	 * Get the name of the option
	 * @return This option's name
	 */
	public String getname() {
		return name;
	}
	
	/**
	 * Change The name of this option
	 * @param name This option's new name
	 */
	public void setname(String name) {
		this.name = name;
	}
	/**
	 * Get the type of the option
	 * @return This option's type
	 */
	public String gettype() {
		return type;
	}
	/**
	 * Change The type of this option
	 * @param type This option's new type
	 */
	public void settype(String type) {
		this.type = type;
	}
	
	/**
	 * Get the description of the option
	 * @return This option's description
	 */
	public String getdescription() {
		return description;
	}
	
	/**
	 * Change The description of this option
	 * @param description This option's new description
	 */
	public void setdescription(String description) {
		this.description = description;
	}
	
}