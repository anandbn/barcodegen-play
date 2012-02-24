package xml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

public class Choice {
	@XmlElement String label;
	@XmlElement String name;
	@XmlElement Boolean selected;
	public String getLabel() {
		return label;
	}
	public String getName() {
		return name;
	}
	public Boolean getSelected() {
		return selected;
	}
	@Override
	public String toString() {
		return "Choice [label=" + label + ", name=" + name + ", selected="
				+ selected + "]";
	}
	
}
