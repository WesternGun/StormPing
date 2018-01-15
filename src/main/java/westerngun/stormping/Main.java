package westerngun.stormping;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;
/*
 * @author: WesternGun (https://github.com/WesternGun)
 * 2018.1
 */
public class Main {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        Config config = new Config();
        config.setDebug(true);
          
        //
        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("ping-spout", new PingSpout());

        builder.setBolt("parser-bolt", new ParserBolt())
           .shuffleGrouping("ping-spout");

        builder.setBolt("stats-bolt", new StatsBolt())
           .shuffleGrouping("parser-bolt");
              
        builder.setBolt("db-bolt", new DBBolt()).shuffleGrouping("stats-bolt");
        
        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("MainTopology", config, builder.createTopology());
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
          
        //Stop the topology
        
        cluster.shutdown();
    }

}
