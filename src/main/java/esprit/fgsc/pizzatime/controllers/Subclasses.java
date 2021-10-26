package esprit.fgsc.pizzatime.controllers;

import esprit.fgsc.pizzatime.controllers.utils.OwlReaderUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@Slf4j
@CrossOrigin("*")
@RestController
public class Subclasses {
    // Return the types of pizza or whatever class
    // http://localhost:8080/subclasses?type=Salty_Topping, http://localhost:8080/subclasses?type=Pizza
    @GetMapping("/subclasses")
    protected Map<String, Object> getSubclasses(@RequestParam String type) {
        Map<String, Object> response = new HashMap<>();
        response.put("subclasses", getTypeSubclasses(type));
        return response;
    }
    private List<String> getTypeSubclasses(String type) {
        String queryString = OwlReaderUtil.QUERY_PREFIX + "SELECT DISTINCT ?x WHERE { "
        + "?x		rdfs:subClassOf 	pizza:%s }";
        return OwlReaderUtil.executeQueryOneColumn(String.format(queryString, type));
    }
    // http://localhost:8080/namedindividuals?class=Pizza
    //
    @GetMapping("/namedindividuals")
    public List<Map<String, String>> getIndividus(@RequestParam(name = "class") String className) {
        List<Map<String, String>> response= new ArrayList<>();
        Iterator<Individual> individus = Objects.requireNonNull(OwlReaderUtil.readModel()).listIndividuals();
        while (individus.hasNext()) {
            Individual sub = individus.next();
            OntClass sc = sub.getOntClass();
            if(sc != null){
                if(sc.getLocalName().endsWith(className)){
                    Map<String , String> obj = new HashMap<>();
                    obj.put("value",sub.getLocalName());
                    obj.put("uri",sub.getURI());
                    response.add(obj);
                }
            }
        }
        return response;
    }


}
