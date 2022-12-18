import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class Main {

    public static Map <String, Double> getSum(String fileName, Map<String, Double> mapBuffer) throws ParserConfigurationException, IOException, SAXException {
        double temp = 0D;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new File(fileName));
        document.getDocumentElement().normalize();
        NodeList persons = document.getElementsByTagName("person");

        for (int i = 0; i < persons.getLength(); i++) {
            Element person = (Element) persons.item(i);
            if(mapBuffer.containsKey(person.getAttribute("type"))) {
                temp = mapBuffer.getOrDefault(person.getAttribute("type"), 0D);
                temp += Double.parseDouble(person.getAttribute("fine_amount"));
                mapBuffer.put(person.getAttribute("type"), temp);
            } else {
                mapBuffer.put(person.getAttribute("type"), Double.parseDouble(person.getAttribute("fine_amount").trim()));
            }
        }

        return mapBuffer;
    }

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        long start = System.currentTimeMillis();
        Map<String, Double> map = new HashMap<>();
       ExecutorService executor = Executors.newFixedThreadPool(8);
       for (int i = 0; i < 12; i++) {
           String fileArray[] = new String[] { "Person.xml", "Person2.xml", "Person3.xml", "Person4.xml",
                   "Person5.xml", "Person6.xml", "Person7.xml", "Person8.xml", "Person9.xml",
                   "Person10.xml", "Person11.xml", "Person12.xml" };
           for (String file: fileArray) {
               map = getSum(file, map);
               Map<String, Double> finalMap = map;
               CompletableFuture<Void> Future = CompletableFuture.runAsync(() -> {
                   try {

                       Map<String, Double> result = finalMap.entrySet()
                               .stream()
                               .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                               .collect(Collectors.toMap(
                                       Map.Entry::getKey,
                                       Map.Entry::getValue,
                                       (oldValue, newValue) -> oldValue, LinkedHashMap::new));

                       String json = "{" + result.entrySet().stream()
                               .map(e -> "\""+ e.getKey() + "\" : " + e.getValue())
                               .collect(Collectors.joining(", "))+"}";

                       FileWriter writer = new FileWriter("result.json");
                       writer.write(json);
                       writer.flush();
                       writer.close();



                   } catch (IOException e) {
                       throw new RuntimeException(e);
                   }

               }, executor);
           }
       }

       long stop = System.currentTimeMillis();
        System.out.println(stop - start);
    }
}
