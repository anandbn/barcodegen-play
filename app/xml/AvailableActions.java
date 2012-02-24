package xml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

public class AvailableActions {
	@XmlElement String id;
	@XmlElement String label;
	public String getId() {
		return id;
	}
	public String getLabel() {
		return label;
	}

	@Override
	public String toString() {
		return "AvailableActions [id=" + id + ", label=" + label + "]";
	}

}
