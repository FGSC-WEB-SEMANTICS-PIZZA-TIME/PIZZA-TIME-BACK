package esprit.fgsc.pizzatime.controllers;

import esprit.fgsc.pizzatime.controllers.utils.OwlReaderUtil;

import esprit.fgsc.pizzatime.dto.QueryDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@CrossOrigin("*")
@RequestMapping("/pizzas/query")
@RestController
public class QueryPizzas {
    // WORKS
    // http://localhost:8080/pizzas/query
    // you can select more properties by execute2colum
    @PostMapping
    public List<String> doGet(@RequestBody QueryDTO query) {
        log.info(query.dropdownValues.values().toString());
        String queryString = OwlReaderUtil.QUERY_PREFIX + " "
                + "SELECT   ?x ?y ?price "
                + "WHERE "
                + "{ "
                + (query.type    != null && !query.type.isEmpty()    ? "?x rdf:type 	      pizza:" + query.type    + " . " : "")
//                + (query.getCrust()   != null && !query.getCrust().isEmpty()   ? "?x pizza:hasCrust   pizza:" + query.getCrust()   + " . " : "")
//                + (query.getTopping() != null && !query.getTopping().isEmpty() ? "?x pizza:hasTopping pizza:" + query.getTopping() + " . " : "")
//                + (query.getSauce()   != null && !query.getSauce().isEmpty()   ? "?x pizza:hasSauce   pizza:" + query.getSauce()   + " . " : "")
//                + " ?x     pizza:Calories     ?y     . "
//                + " ?x     pizza:Price        ?price . "
//                + (query.getMinCalories() != 0 && query.getMaxCalories() != 0 ?" FILTER (?y     > "+query.getMinCalories()+") FILTER ( ?y     < "+query.getMaxCalories()+" ) ": "")
//                + (query.getMinPrice()    != 0 && query.getMaxPrice()    != 0 ?" FILTER (?price > "+query.getMinPrice()   +") FILTER ( ?price < "+query.getMaxPrice()   +" ) ": "")
                + "} "
                + (query.limit != 0 ? " LIMIT "+query.limit : "")
                + "OFFSET %s ";
        ;
        return OwlReaderUtil.executeQueryOneColumn(String.format(queryString,query.offset));
    }
}
