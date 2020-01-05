import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.SparkSession;

public class TestSparkSQLJava {
    public static void main(String[] args) {

        SparkSession spark = SparkSession.builder().appName("sparkjavasql").master("local[*]").getOrCreate();

        Dataset<String> ds = spark.read().textFile("/Users/shufang/idea_project/spark244/src/main/testdata/helloworld.txt");

        ds.show();

    }
}
