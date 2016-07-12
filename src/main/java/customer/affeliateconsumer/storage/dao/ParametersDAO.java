package customer.affeliateconsumer.storage.dao;

import customer.affeliateconsumer.service.MongoService;
import org.jongo.MongoCollection;

import java.text.MessageFormat;

/**
 * Created by roman rasskazov on 30.05.2015.
 */
public class ParametersDAO {
    private static ParametersDAO instance;

    public static ParametersDAO getInstance() {
        if (instance == null){
            instance = new ParametersDAO();
        }
        return instance;
    }

    private static final String PARAMETERS_SELECTOR = "'{parameter: '''{0}'''}";
    private static final String COLLECTION_PARAMETERS = "parameters";


    private MongoCollection parametersCollection;

    private ParametersDAO() {
        parametersCollection = MongoService.getJongo().getCollection(COLLECTION_PARAMETERS);
    }

    public ParametersDTO getParameter(String parameterName){
        String selector = MessageFormat.format(PARAMETERS_SELECTOR, parameterName);
        return parametersCollection.findOne(selector).as(ParametersDTO.class);
    }

    public void save(ParametersDTO parametersDTO) {
        parametersCollection.save(parametersDTO);
    }

}
