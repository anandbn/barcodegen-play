package xml;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="FlowResponse")
public class FlowResponse {
	
	@XmlElement(name = "interview", type = Interview.class) Interview interview;
	@XmlElement String state;
	
	public Interview getInterview() {
		return interview;
	}
	public String getState() {
		return state;
	}
	@Override
	public String toString() {
		return "FlowResponse [interview=" + interview + ", state=" + state
				+ "]";
	}
}
