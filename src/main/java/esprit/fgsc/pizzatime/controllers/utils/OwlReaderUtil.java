package esprit.fgsc.pizzatime.controllers.utils;

import org.apache.jena.ontology.*;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.util.FileManager;
import org.json.simple.JSONObject;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class OwlReaderUtil {
    public static final Logger logger = Logger.getLogger(OwlReaderUtil.class.getName());
    public static final String OWL_FILE = "data/pizza.owl";
    public static final String ONTOLOGY_URI = "http://www.semanticweb.org/firas/ontologies/2021/9/pizza-ontology#";

    public static List<String> executeQueryOneColumn(String queryString) {
        List<String> values = new ArrayList<>();
        Model model = FileManager.getInternal().loadModelInternal( OWL_FILE );
        Query query = QueryFactory.create(queryString);
        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            ResultSet results = qexec.execSelect();
            while (results.hasNext()) {
                QuerySolution solution = results.nextSolution();
                Resource x = solution.getResource("x");
                values.add(x.getLocalName());
                //values.add(x.getURI());
            }
        }
        return values;
    }
    public static void propertiesFromIndividualName(String className){
        Model model = FileManager.getInternal().loadModelInternal( OWL_FILE );
        Query query = QueryFactory.create(SELECT_BY_NAME);
        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            ResultSet results = qexec.execSelect();
            while (results.hasNext()) {
                QuerySolution solution = results.nextSolution();
                logger.info("OK");
                System.out.println(solution);
                // Literal x = solution.getLiteral("x");
                //values.add(x.getDatatype().getJavaClass().getName());
            }
        }
    }
    public static List<String> executeQueryOneColumnLiteral(String queryString) {
        List<String> values = new ArrayList<>();
        Model model = FileManager.getInternal().loadModelInternal( OWL_FILE );
        Query query = QueryFactory.create(queryString);
        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            ResultSet results = qexec.execSelect();
            while (results.hasNext()) {
                QuerySolution solution = results.nextSolution();
                Literal x = solution.getLiteral("x");
                values.add(x.getDatatype().getJavaClass().getName());
            }
        }
        return values;
    }

    public static List<List<String>> executeQueryTwoColumn(String queryString) {
        List<List<String>> rows = new ArrayList<>();
        Model model = FileManager.getInternal().loadModelInternal(OWL_FILE);
        Query query = QueryFactory.create(queryString);
        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            ResultSet results = qexec.execSelect();
            while (results.hasNext()) {
                QuerySolution solution = results.nextSolution();
                logger.info(solution.toString());
                Resource x = solution.getResource("x");
                Literal y = solution.getLiteral("Calories");
                List<String> column1 = Arrays.asList(x.getLocalName(), y.getInt() + "");
                rows.add(column1);
            }
        }
        return rows;
    }

    public static OntModel readModel() {
        try (FileReader reader = new FileReader(OWL_FILE)){
            return (OntModel) ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM).read(reader,null);
        } catch (Exception e) {
//            log.info("Failed to readModel");
        }
        return null;
    }
    public static final String QUERY_PREFIX =
              "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
            + "PREFIX owl: <http://www.w3.org/2002/07/owl#>"
            + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
            + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>"
            + "PREFIX pizza: <http://www.semanticweb.org/firas/ontologies/2021/9/pizza-ontology#>";
    public static final String SELECT_BY_NAME = QUERY_PREFIX +
            "SELECT DISTINCT ?property " +
            "WHERE { " +
            "  ?s a <http://www.semanticweb.org/firas/ontologies/2021/9/pizza-ontology#Mexican_Pizza>;" +
            "     ?property ?o ." +
            "  FILTER isLiteral(?o)" +
            "}";
}
