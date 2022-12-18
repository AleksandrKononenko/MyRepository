package test;


import main.entity.Data;
import main.propertyloader.PropertyParser;
import org.junit.Assert;
import org.junit.Test;

public class TestOur {

    @Test
    public void test1() {
        Data data = null;
        try {
            data = (Data) PropertyParser.getDataFromProperty(Object.class, "resource.properties");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Assert.assertNull(data);
    }

    @Test
    public void test2() {
        Data data = null;
        try {
            data = PropertyParser.getDataFromProperty(Data.class, "resource.properties");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(data);
    }
}
