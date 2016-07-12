package customer.affeliateconsumer.dto.cj.commission;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

/**
 * Created by roman rasskazov on 28.05.2015.
 */
@XmlType
@XmlEnum(String.class)
public enum CDStatus {
    @XmlEnumValue("new") NEW("new"),
    @XmlEnumValue("extended") EXTENDED("extended"),
    @XmlEnumValue("locked") LOCKED("locked"),
    @XmlEnumValue("closed") CLOSED("closed");

    private final String name;

    private CDStatus(String name){
        this.name = name;
    }

    @Override
    public String toString(){
        return name;
    }
}