package esprit.fgsc.pizzatime.controllers;

import esprit.fgsc.pizzatime.controllers.utils.OwlReaderUtil;
import org.apache.jena.ontology.*;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.json.simple.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RestController
public class PizzaOntologyRestApi {
    List<JSONObject> listVoitures=new ArrayList();
    OntModel model = null;
    public OntModel readModel() {
        try (FileReader reader = new FileReader(OwlReaderUtil.OWL_FILE)) {
            OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
            model.read(reader,null);
            return model;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @RequestMapping(value = "/ontologies",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public   List<JSONObject> getontologies() {
        List<JSONObject> list=new ArrayList();
        String fileName = "data/pizza.owl";
        try {
            File file = new File(fileName);
            FileReader reader = new FileReader(file);
            OntModel model = ModelFactory
                    .createOntologyModel(OntModelSpec.OWL_DL_MEM);
            model.read(reader,null);
            Iterator ontologiesIter = model.listOntologies();
            while (ontologiesIter.hasNext()) {
                Ontology ontology = (Ontology) ontologiesIter.next();

                JSONObject obj = new JSONObject();
                obj.put("name",ontology.getLocalName());
                obj.put("uri",ontology.getURI());
                list.add(obj);

            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/classesList",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public   List<JSONObject> getClasses() {
        List<JSONObject> list=new ArrayList();
        String fileName = "data/pizza.owl";
        try {
            File file = new File(fileName);
            FileReader reader = new FileReader(file);
            OntModel model = ModelFactory
                    .createOntologyModel(OntModelSpec.OWL_DL_MEM);
            model.read(reader,null);
            Iterator classIter = model.listClasses();
            while (classIter.hasNext()) {
                OntClass ontClass = (OntClass) classIter.next();
                JSONObject obj = new JSONObject();
                obj.put("name",ontClass.getLocalName());
                obj.put("uri",ontClass.getURI());
                list.add(obj);

            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/subClasses",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public   List<String> getSubClasses(@RequestParam("classname") String className) {
        List<String> list=new ArrayList();
        this.model = this.readModel();
        String classURI = "http://www.semanticweb.org/firas/ontologies/2021/9/pizza-ontology#".concat(className);
        System.out.println(classURI);
        OntClass personne = this.model.getOntClass(classURI );
        Iterator subIter = personne.listSubClasses();
        while (subIter.hasNext()) {
            OntClass sub = (OntClass) subIter.next();
            list.add(sub.getLocalName());
        }
        return list;
    }

    @RequestMapping(value = "/Individus",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public   List<JSONObject> getIndividus() {
        List<JSONObject> list=new ArrayList();
        try {
            this.model = this.readModel();

            Iterator individus = this.model.listIndividuals();
            while (individus.hasNext()) {
                Individual   sub = (Individual) individus.next();
                JSONObject obj = new JSONObject();
                obj.put("name",sub.getLocalName());
                obj.put("uri",sub.getURI());
                list.add(obj);

            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/superClasses",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public   List<JSONObject> getSuperClasses(@RequestParam("classname") String className) {
        List<JSONObject> list=new ArrayList();
        try {
            this.model = this.readModel();
            String classURI = "http://www.semanticweb.org/firas/ontologies/2021/9/pizza-ontology#".concat(className);
            System.out.println(classURI);
            OntClass personne = this.model.getOntClass(classURI );
            Iterator subIter = personne.listSuperClasses();
            while (subIter.hasNext()) {
                OntClass sub = (OntClass) subIter.next();
                JSONObject obj = new JSONObject();
                obj.put("URI",sub.getURI());
                list.add(obj);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/getClasProperty",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public List<JSONObject> getClasProperty(@RequestParam String className) {
        List<JSONObject> list=new ArrayList();
        this.model = this.readModel();
        String classURI = "http://www.semanticweb.org/firas/ontologies/2021/9/pizza-ontology#".concat(className);
        OntClass ontClass = this.model.getOntClass(classURI );
        Iterator<OntProperty> subIter = ontClass.listDeclaredProperties();
        while (subIter.hasNext()) {
            OntProperty property = subIter.next();
            JSONObject obj = new JSONObject();
            obj.put("propertyName",property.getLocalName());
            obj.put("propertyType",property.getRDFType().getLocalName());
            if(property.getDomain()!=null)
                obj.put("domain", property.getDomain().getLocalName());
            if(property.getRange()!=null)
                obj.put("range",property.getRange().getLocalName());
            list.add(obj);
        }
        return list;
    }

    @RequestMapping(value = "/equivClasses",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public   List<JSONObject> getequivClasses(@RequestParam("classname") String className) {
        List<JSONObject> list=new ArrayList();
        try {
            this.model = this.readModel();
            String classURI = "http://www.semanticweb.org/firas/ontologies/2021/9/pizza-ontology#".concat(className);
            System.out.println(classURI);
            OntClass personne = this.model.getOntClass(classURI );
            Iterator subIter = personne.listEquivalentClasses();
            while (subIter.hasNext()) {
                OntClass sub = (OntClass) subIter.next();
                JSONObject obj = new JSONObject();
                obj.put("URI",sub.getURI());
                list.add(obj);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/instances")
    public   List<JSONObject> getInstancesClasses(@RequestParam("classname") String className) {
        List<JSONObject> list=new ArrayList();
        try {
            this.model = this.readModel();
            String classURI = "http://www.semanticweb.org/firas/ontologies/2021/9/pizza-ontology#".concat(className);
            System.out.println(classURI);
            OntClass personne = this.model.getOntClass(classURI );
            Iterator<? extends OntResource> subIter = personne.listInstances();
            JSONObject obj = new JSONObject();
            while (subIter.hasNext()) {
                Individual   sub = (Individual) subIter.next();
                StmtIterator it = sub.listProperties();
                while ( it.hasNext()) {
                    Statement s =  it.next();
                    if (s.getObject().isLiteral()) {
                        obj.put("name",sub.getLocalName());
                        //Sobj.put("name",sub.getProperty(null));
                        obj.put("type",s.getPredicate().getLocalName());
                        obj.put("value",s.getLiteral().getLexicalForm().toString());
                        obj.put("uri",sub.getURI());
                        //System.out.println(""+s.getLiteral().getLexicalForm().toString()+" type = "+s.getPredicate().getLocalName());
                    }
                    else   {
                        obj.put("name",sub.getLocalName());
                        //Sobj.put("name",sub.getProperty(null));obj.put("value",s.getLiteral().getLexicalForm().toString());

                        obj.put("uri",sub.getURI());
                    } //System.out.println(""+s.getObject().toString().substring(53)+" type = "+s.getPredicate().getLocalName());
                }
                System.out.println(sub);
                list.add(obj);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @RequestMapping(value = "/add",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public List<JSONObject> AddInstance(@RequestParam("name") String name,@RequestParam("couleur") String couleur,@RequestParam("nbPorte") int nbPorte ,
                                        @RequestParam("marque") String marque,@RequestParam("cylindree") String cylindree,@RequestParam("boite") String boite,@RequestParam("type") String type,@RequestParam("consomme") String consomme) {

        String NS= "http://www.semanticweb.org/firas/ontologies/2021/9/pizza-ontology#";
        List<JSONObject> list=new ArrayList();
        try {
            this.model = this.readModel();

            OntClass classe = this.model.getOntClass(NS+type);

            // create individual ex:jack
            Individual ind = this.model.createIndividual( NS + name, classe );

            // create some properties - probably better to use FOAF here really
            DatatypeProperty couleurP = this.model.getDatatypeProperty(NS + "couleur" );
            DatatypeProperty nbPorteP = this.model.getDatatypeProperty( NS + "nombreDePortes" );
            ObjectProperty marqueP = this.model.getObjectProperty(NS + "est_fabrique_par");
            ObjectProperty consommeP = this.model.getObjectProperty(NS + "consomme");
            ObjectProperty composeP = this.model.getObjectProperty(NS + "est_compose_de");

            HasValueRestriction marqueR =
                    this.model.createHasValueRestriction( marque, marqueP, ind );
            HasValueRestriction consommeR =
                    this.model.createHasValueRestriction( consomme, consommeP, ind );
            HasValueRestriction composeR =
                    this.model.createHasValueRestriction( boite, composeP, ind );
            HasValueRestriction compose1R =
                    this.model.createHasValueRestriction( cylindree, composeP, ind );
            IntersectionClass ukIndustrialConf =
                    this.model.createIntersectionClass( NS + "name",
                            this.model.createList( new RDFNode[] {marqueR, consommeR,composeR,compose1R} ) );
            ind.addProperty( couleurP, this.model.createLiteral( couleur ) )
                    .addProperty( nbPorteP, this.model.createTypedLiteral( nbPorte) );

            String output = "<!-- http://www.semanticweb.org/firas/ontologies/2021/9/pizza-ontology#clio -->\r\n"
                    + "\r\n"
                    + "    <owl:NamedIndividual rdf:about=\"http://www.semanticweb.org/firas/ontologies/2021/9/pizza-ontology#"+name+"\">\r\n"
                    + "        <rdf:type rdf:resource=\"http://www.semanticweb.org/firas/ontologies/2021/9/pizza-ontology#"+type+"\"/>\r\n"
                    + "        <vehicules:consomme rdf:resource=\"http://www.semanticweb.org/firas/ontologies/2021/9/pizza-ontology#"+consomme+"\"/>\r\n"
                    + "        <vehicules:est_compose_de rdf:resource=\"http://www.semanticweb.org/firas/ontologies/2021/9/pizza-ontology#"+boite+"\"/>\r\n"
                    + "        <vehicules:est_compose_de rdf:resource=\"http://www.semanticweb.org/firas/ontologies/2021/9/pizza-ontology#"+cylindree+"\"/>\r\n"
                    + "        <vehicules:est_fabrique_par rdf:resource=\"http://www.semanticweb.org/firas/ontologies/2021/9/pizza-ontology#"+marque.toUpperCase()+"\"/>\r\n"
                    + "        <vehicules:couleur>"+couleur+"</vehicules:couleur>\r\n"
                    + "        <vehicules:nombreDePortes rdf:datatype=\"http://www.w3.org/2001/XMLSchema#integer\">"+nbPorte+"</vehicules:nombreDePortes>\r\n"
                    + "    </owl:NamedIndividual>\r\n"
                    + "";
            Path path = Paths.get("data/pizza.owl");
            List<String> lines = Files.readAllLines(path);
            lines.add(12, output); // index 3: between 3rd and 4th line
            Files.write(path, lines);
            System.out.println(output);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}