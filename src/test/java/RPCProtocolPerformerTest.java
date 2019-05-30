import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.*;
import thrift.WinAPIFunction;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RPCProtocolPerformerTest {
    private static ProtocolPerformer performer;
    private static WinAPIFunction function;

    @BeforeClass
    public static void init(){
        function = new WinAPIFunction();
        performer = new RPCProtocolPerformer();
        function.setName("some name");
        function.setParams("some params");
        function.setReturnValue("some return value");
        function.setDescription("some description");
    }

    @Before
    public void start() throws SQLException {
        //performer.insert(function);
    }

    @After
    public void close() throws SQLException {
       // performer.delete(function);
    }

    @Test
    public void connect() {
        //performer.connect("0.0.0.0:12000");
    }

    @Test
    public void getAllFunctions() {
        WinAPIFunction function1 = new WinAPIFunction();

        function1.setName("some name 1");
        function1.setParams("some params 1");
        function1.setReturnValue("some return value 1");
        function1.setDescription("some description 1");

        /*performer.insert(function1);

        List<WinAPIFunction> actual = performer.getAllFunctions();

        List<WinAPIFunction> expected = new ArrayList<>();
        actual.add(function);
        actual.add(function1);

        Assert.assertTrue(containsAll(actual,expected));

        performer.delete(function1);*/
    }

    @Test
    public void insert() {
        //Assert.assertTrue(containsOne(performer.getAllFunctions(), function));
    }

    @Test
    public void delete() {
        /*Assert.assertTrue(containsOne(performer.getAllFunctions(), function));
        performer.delete(function);
        Assert.assertFalse(containsOne(performer.getAllFunctions(), function));*/
    }

    @Test
    public void update() {
        function.setDescription("updated description");
        /*performer.update(function);
        Assert.assertFalse(containsOne(performer.getAllFunctions(), function));*/
    }

    @Test
    public void showErrorAlert() {
    }

    private static boolean containsAll(List<WinAPIFunction> list, List<WinAPIFunction> sublist){
        int elementsNumber = 0;
        for (WinAPIFunction function: list) {
            for (WinAPIFunction function1: sublist) {
                if(function.equals(function1)) elementsNumber++;
            }
        }
        return elementsNumber == sublist.size();
    }

    private static boolean containsOne(List<WinAPIFunction> list, WinAPIFunction function){
        for (WinAPIFunction func: list) {
            if (function.getName().equals(func.getName())
                    && function.getParams().equals(func.getParams())
                    && function.getReturnValue().equals(func.getReturnValue())
                    && function.getDescription().equals(func.getDescription())) return true;
        }
        return false;
    }
}
