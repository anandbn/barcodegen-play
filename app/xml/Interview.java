package xml;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

public class Interview {
	@XmlElement String locationId;
	@XmlElement String status;
	@XmlElement String title;
	@XmlElements(
		@XmlElement (name = "availableActions", type = AvailableActions.class) 
	)
	List<AvailableActions> availableActions;
	@XmlElements(
			@XmlElement (name = "fields", type = Field.class) 
	)
	List<Field> fields;
	public String getLocationId() {
		return locationId;
	}
	public String getStatus() {
		return status;
	}
	public String getTitle() {
		return title;
	}
	public List getAvailableActions() {
		return availableActions;
	}
	public List getFields() {
		return fields;
	}
	
	public Boolean getHasNext(){
		return hasAction("NEXT");
	}
	
	public Boolean getHasPrevious(){
		return hasAction("PREVIOUS");
	}
	
	public Boolean getHasChoices(){
		for(Field fld : this.fields){
			if(fld.fieldType.equalsIgnoreCase("RADIO_BUTTONS")){
				return true;
			}
		}
		return false;
	}
	
	public Boolean getHasTextInput(){
		for(Field fld : this.fields){
			if(fld.fieldType.equalsIgnoreCase("TEXT_INPUT")){
				return true;
			}
		}
		return false;
	}
	
	private Boolean hasAction(String actionId){
		for(AvailableActions action : this.availableActions){
			if(action.getId().equalsIgnoreCase(actionId)){
				return true;
			}
		}
		return false;
	}
	@Override
	public String toString() {
		return "Interview [locationId=" + locationId + ", status=" + status
				+ ", title=" + title + ", availableActions=" + availableActions
				+ ", fields=" + fields + "]";
	}
	
	
}
