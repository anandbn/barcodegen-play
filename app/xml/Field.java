package xml;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

public class Field {
	@XmlElement String dataType;
	@XmlElement String fieldText;
	@XmlElement String fieldType;
	@XmlElement String name;
	@XmlElement Boolean required;
	@XmlElement String rawValue;
	
	@XmlElements(
			@XmlElement (name = "choices", type = Choice.class) 
	)
	List choices;

	public String getDataType() {
		return dataType;
	}

	public String getFieldText() {
		return fieldText;
	}

	public String getFieldType() {
		return fieldType;
	}

	public String getName() {
		return name;
	}

	public Boolean getRequired() {
		return required;
	}

	public List getChoices() {
		return choices;
	}

	public String getRawValue() {
		return rawValue;
	}

	@Override
	public String toString() {
		return "Field [dataType=" + dataType + ", fieldText=" + fieldText
				+ ", fieldType=" + fieldType + ", name=" + name + ", required="
				+ required + ", rawValue=" + rawValue + ", choices=" + choices
				+ "]";
	}
	
}
