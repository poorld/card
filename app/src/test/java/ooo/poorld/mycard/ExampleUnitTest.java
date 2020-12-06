package ooo.poorld.mycard;

import org.junit.Test;

import ooo.poorld.mycard.common.DataType;
import ooo.poorld.mycard.utils.Constans;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testEnum() {
        DataType dataType = DataType.valueOfName(Constans.DATA_PATH_DATA_DOCUMENT);
        System.out.println(dataType.getName());
    }
}