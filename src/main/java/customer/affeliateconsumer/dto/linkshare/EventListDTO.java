package customer.affeliateconsumer.dto.linkshare;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.ArrayList;

/**
 * Created by roman rasskazov on 30.05.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
public class EventListDTO extends ArrayList<EventDTO> {
}
