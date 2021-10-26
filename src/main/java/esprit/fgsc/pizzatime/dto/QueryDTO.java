package esprit.fgsc.pizzatime.dto;

import java.util.List;
import java.util.Map;

public class QueryDTO {
    public String type;
    public String search;
    public Map<String, String> dropdownValues;
    public Map<String, List<Integer>> sliderValues;
    public int limit;
    public int offset;
}
