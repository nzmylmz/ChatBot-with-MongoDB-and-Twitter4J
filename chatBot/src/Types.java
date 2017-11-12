
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
public class Types extends Category {

    String typeName;

    protected Iterable<DBObject> getSubRecords(DBCollection dbcol) {
        
        Iterable<DBObject> output = (Iterable<DBObject>) dbcol.aggregate(Arrays.asList(
                (DBObject) new BasicDBObject("$match",
                        new BasicDBObject("category", categoryName)),
                (DBObject) new BasicDBObject("$group", new BasicDBObject("_id", "$type")))).results();

        return output;   
    }
}
