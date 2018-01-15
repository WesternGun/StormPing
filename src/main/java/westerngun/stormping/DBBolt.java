package westerngun.stormping;

import java.util.Iterator;
import java.util.Map;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;


/*
 * @author: WesternGun (https://github.com/WesternGun)
 * 2018.1
 */
public class DBBolt implements IRichBolt {
    private OutputCollector collector;
    private MongoCollection<Document> pingColl;
    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        // TODO Auto-generated method stub
        this.collector = collector;
        MongoClient client = new MongoClient("localhost", 27017);
        MongoDatabase database = client.getDatabase("ping");
        pingColl = database.getCollection("pingData");
    }

    @Override
    public void execute(Tuple input) {
        // TODO Auto-generated method stub
        Document doc = new Document();
        try {
            Fields fields = input.getFields();
            Iterator<String> it = fields.iterator();
            while (it.hasNext()) {
                String i = it.next();
                System.out.println("Field: " + i);
                Object value = input.getValueByField(i);
                System.out.println(value.toString());
                if (value instanceof Double) {
                    doc.put(i, (Double)value);
                } else if (value instanceof Integer) {
                    doc.put(i, (Integer)value);
                } else {
                    doc.put(i, (String)value);
                }
                
            }
            System.out.println(doc.toJson());
            pingColl.insertOne(doc);
            collector.ack(input);
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
        }
    }

    @Override
    public void cleanup() {
        // TODO Auto-generated method stub

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        // TODO Auto-generated method stub

    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        // TODO Auto-generated method stub
        return null;
    }

}
