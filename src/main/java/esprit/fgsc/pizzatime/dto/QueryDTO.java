package esprit.fgsc.pizzatime.dto;

import esprit.fgsc.pizzatime.dto.datatypes.Dropdown;
import esprit.fgsc.pizzatime.dto.datatypes.Slider;

import java.util.Dictionary;
import java.util.List;
import java.util.Map;

public class QueryDTO {
    public String search;
    public List<Dropdown> dropdownValues;
    public List<Slider> sliderValues;
    public int limit;
    public int offset;
}


//{
//    "dropdownValues": [
//        [
//            "Type",
//            "Vegan_Pizza"
//        ],
//        [
//            "hasSauce",
//            "Tomato"
//        ],
//        [
//            "hasTopping",
//            "Garlic"
//        ],
//        [
//            "hasCrust",
//            "Regular_Thin_Crust"
//        ]
//    ],
//    "sliderValues": {},
//}