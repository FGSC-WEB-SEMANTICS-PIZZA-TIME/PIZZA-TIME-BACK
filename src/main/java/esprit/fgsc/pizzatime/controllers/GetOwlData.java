package esprit.fgsc.pizzatime.controllers;

import esprit.fgsc.pizzatime.controllers.utils.OwlReaderUtil;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntProperty;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/generic")
public class GetOwlData {
    private static final String queryString = OwlReaderUtil.QUERY_PREFIX +
    "SELECT DISTINCT  ?x WHERE { ?d     pizza:%s    ?x }";
    private static final String OBJECT_PROPERTY = "ObjectProperty";
    private static final String DATA_TYPE_PROPERTY = "DatatypeProperty";
    // CLASSNAME = Pizza
    @GetMapping(value = "/properties")
        public List<Map<String,String>> getClassProperties(@RequestParam String className) {
        ArrayList<Map<String, String>> list= new ArrayList();
        OntClass ontClass = Objects.requireNonNull(OwlReaderUtil.readModel()).getOntClass(OwlReaderUtil.ONTOLOGY_URI.concat(className) );
        Iterator<OntProperty> subIter = ontClass.listDeclaredProperties();
        while (subIter.hasNext()) {
            OntProperty property = subIter.next();
            Map<String, String> obj = new HashMap<>();
            obj.put("propertyName",property.getLocalName());
            obj.put("propertyType",property.getRDFType().getLocalName());
            if(property.getDomain()!=null) obj.put("domain", property.getDomain().getLocalName());
            if(property.getRange()!=null) obj.put("range",property.getRange().getLocalName());
            list.add(obj);
        }
        return list;
    }
    // WORKS
    // http://localhost:8080/generic?key=hasCrust&type=ObjectProperty
    @GetMapping
    public List<String> getCrustAndTopping(@RequestParam String key, @RequestParam(defaultValue = DATA_TYPE_PROPERTY) String type) {
        if(Objects.equals(type, OBJECT_PROPERTY))
            return OwlReaderUtil.executeQueryOneColumn(String.format(queryString,key));
        else
            return OwlReaderUtil.executeQueryOneColumnLiteral(String.format(queryString,key));
    }
}
