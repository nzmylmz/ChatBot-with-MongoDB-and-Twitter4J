
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import java.util.Arrays;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author hopeful
 */
public class Model extends Brand {

    String modelName;

    
    protected Iterable<DBObject> getSubModel(DBCollection dbcol) {
        
        Iterable<DBObject> output = (Iterable<DBObject>) dbcol.aggregate(Arrays.asList(
                (DBObject) new BasicDBObject("$match",
                        new BasicDBObject("brand", brandName)),
                (DBObject) new BasicDBObject("$group", new BasicDBObject("_id", "$model")))).results();

        return output;
        
    }
}
