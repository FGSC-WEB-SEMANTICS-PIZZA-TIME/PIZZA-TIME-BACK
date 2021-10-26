package esprit.fgsc.pizzatime.controllers;

import esprit.fgsc.pizzatime.controllers.utils.OwlReaderUtil;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/SweetDonuts")
public class QueryAggregatedPizzas {
//    I HAVENT TESTED OUT THIS ONE YET
    @GetMapping
    protected HashMap<String, Object> doGet(@RequestParam String value){
        String formattedQuery = String.format(queryString, value);
        //List<List<String>> rows = OwlReaderUtil.executeQueryTwoColumn(queryString);
        List<String> rows = OwlReaderUtil.executeQueryOneColumn(formattedQuery);
        HashMap<String, Object> response = new HashMap();
        response.put("results", rows);
        response.put("anotherone",value);
        return response;

    }
    public final String queryString =
            OwlReaderUtil.QUERY_PREFIX
            +" "
            +"SELECT DISTINCT ?x "
            +"WHERE "
            +"{ "
            +"?p 		rdfs:subClassOf 		?restriction."
            +"?restriction 	owl:onProperty 		pizza:hasCrust."
            +"?restriction 	owl:someValuesFrom 		pizza:%s."
            +"?x		pizza:hasTopping	?p."
            +" }"
            +"LIMIT 4";
}
// 