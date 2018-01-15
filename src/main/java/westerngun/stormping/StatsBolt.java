package westerngun.stormping;

import java.util.Map;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
/*
 * @author: WesternGun (https://github.com/WesternGun)
 * 2018.1
 */
public class StatsBolt implements IRichBolt {
    private OutputCollector collector;
    private static int timeTotal = 0;
    
    //
    private static int counter;
    private static double timeAver;
    private static double timeMax;
    private static double timeMin;
    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
    }

    @Override
    public void execute(Tuple input) {
        // TODO Auto-generated method stub
        String bytes = input.getString(0);
        String time = input.getString(1);
        String ttl = input.getString(2);
        try {
            int bytesData = Integer.parseInt(bytes.split("=")[1]);
            int timeData = Integer.parseInt(time.split("=")[1].replace("ms", ""));
            int ttlData = Integer.parseInt(ttl.split("=")[1]);
            timeTotal += timeData;
            counter ++;
            if (counter == 1) {
                timeAver = timeData;
                timeMax = timeData;
                timeMin = timeData;
            } else {
                timeAver = Math.round(timeTotal / counter);
                if (timeMax <= timeData) timeMax = timeData;
                if (timeMin >= timeData) timeMin = timeData;
                collector.emit(new Values(bytesData, ttlData, timeData, timeAver, timeMax, timeMin));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void cleanup() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        // TODO Auto-generated method stub
        declarer.declare(new Fields("bytes", "ttl", "time", 
                "averageTime", "maxTime", "minTime"));
    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        // TODO Auto-generated method stub
        return null;
    }
    

}
