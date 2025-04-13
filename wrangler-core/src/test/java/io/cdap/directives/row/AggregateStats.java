package io.cdap.wrangler.directives.row;

import io.cdap.wrangler.api.Row;
import io.cdap.wrangler.test.RecipeTester;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class AggregateStatsTest {
    @Test
    public void testAggregateStats() throws Exception {
        List<Row> input = Arrays.asList(
            new Row("data_transfer_size", "10KB").add("response_time", "150ms"),
            new Row("data_transfer_size", "512KB").add("response_time", "850ms")
        );

        String[] recipe = {
            "aggregate-stats :data_transfer_size :response_time :total_size_mb :total_time_sec"
        };

        List<Row> output = RecipeTester.run(recipe, input);

        Assert.assertEquals(1, output.size());

        double expectedMB = (10 * 1024 + 512 * 1024) / (1024.0 * 1024.0);
        double expectedSec = (150 + 850) / 1000.0;

        Assert.assertEquals(expectedMB, (double) output.get(0).getValue("total_size_mb"), 0.001);
        Assert.assertEquals(expectedSec, (double) output.get(0).getValue("total_time_sec"), 0.001);
    }
}
