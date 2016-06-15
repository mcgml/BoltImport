package nhs.genetics.cardiff;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A POJO for Ensembl VEP JSON output
 *
 * @author  Matt Lyon
 * @version 1.0
 * @since   2016-05-09
 */

public class Domain {
    private String name, db;

    public Domain(){

    }

    public static HashMap<String, ArrayList<String>> convertToMap(Domain[] domains){
        HashMap<String, ArrayList<String>> map = new HashMap<>();

        if (domains == null) {
            return map;
        }

        for (Domain domain : domains){

            if (!map.containsKey(domain.getDb())){
                map.put(domain.getDb(), new ArrayList<>());
            }

            map.get(domain.getDb()).add(domain.getName());
        }

        return map;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDb() {
        return db;
    }
    public void setDb(String db) {
        this.db = db;
    }
}
