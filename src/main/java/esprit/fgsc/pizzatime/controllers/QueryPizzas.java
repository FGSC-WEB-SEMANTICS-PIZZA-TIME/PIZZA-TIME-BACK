package esprit.fgsc.pizzatime.controllers;

import esprit.fgsc.pizzatime.controllers.utils.OwlReaderUtil;
import esprit.fgsc.pizzatime.dto.QueryDTO;
import esprit.fgsc.pizzatime.dto.datatypes.Dropdown;
import esprit.fgsc.pizzatime.dto.datatypes.Slider;
import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@CrossOrigin("*")
@RequestMapping("/pizzas/query")
@RestController
public class QueryPizzas {
    // WORKS
    // http://localhost:8080/pizzas/query
    @PostMapping
    public List<String> doGet(@RequestBody QueryDTO query) {
        StringBuilder DROPDOWN_VALUES = new StringBuilder();
        StringBuilder DROPDOWN_KEYS = new StringBuilder();
        for (Dropdown dropdown : query.dropdownValues){
            DROPDOWN_KEYS.append("?").append(dropdown.propertyName).append(" ");
            if (Objects.equals(dropdown.propertyName, "Type")){
                DROPDOWN_VALUES.append("?x rdf:type  pizza:").append(dropdown.selected).append(" . ");
            } else {
                DROPDOWN_VALUES.append("?x pizza:").append(dropdown.propertyName).append("  pizza:").append(dropdown.selected).append(" . ");
            }
        }
        StringBuilder SELECT_FIELDS = new StringBuilder();
        StringBuilder SLIDER_FILTERS = new StringBuilder();
        for (Slider slider: query.sliderValues){
            SELECT_FIELDS.append("?").append(slider.propertyName).append(" ");
            SLIDER_FILTERS.append(String
                    .format(" FILTER ( ?%s     > %s) FILTER ( ?%s     < %s ) ",
                            slider.propertyName, slider.min, slider.propertyName, slider.max));
        }
        String queryString = OwlReaderUtil.QUERY_PREFIX + " "
                + "SELECT   ?x %s "+ DROPDOWN_KEYS + SELECT_FIELDS
                + "WHERE "
                + "{ "
                + DROPDOWN_VALUES
                + " ?x pizza:Calories ?Calories . "
                + " ?x pizza:Price    ?Price . "
                + SLIDER_FILTERS
                + "} "
                + (query.limit != 0 ? " LIMIT "+query.limit : "")
                + " OFFSET %s ";
        return OwlReaderUtil.executeQueryOneColumn(String.format(queryString,SELECT_FIELDS,query.offset));
    }

}
