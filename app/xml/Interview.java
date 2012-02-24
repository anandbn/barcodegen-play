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
	List availableActions;
	@XmlElements(
			@XmlElement (name = "fields", type = Field.class) 
	)
	List fields;
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
	@Override
	public String toString() {
		return "Interview [locationId=" + locationId + ", status=" + status
				+ ", title=" + title + ", availableActions=" + availableActions
				+ ", fields=" + fields + "]";
	}
	
	
}
