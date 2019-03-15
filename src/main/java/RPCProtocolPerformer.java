import javafx.scene.control.Alert;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import thrift.WinAPIHandbookService;
import thrift.WinAPIFunction;

import java.util.ArrayList;
import java.util.List;

public class RPCProtocolPerformer implements ProtocolPerformer {
    private static final Logger log = LogManager.getLogger(RPCProtocolPerformer.class);
    private TTransport transport;
    private TProtocol protocol;
    private WinAPIHandbookService.Client client;

    public void connect(String address) {
        try {
            String[] tokens = address.split(":");
            if(tokens.length != 2) {
                throw new Exception("Wrong address");
            }
            transport = new TSocket(tokens[0], Integer.parseInt(tokens[1]));
            transport.open();

            protocol = new TBinaryProtocol(transport);
            client = new WinAPIHandbookService.Client(protocol);

        } catch (Exception e) {
            log.error(e.getMessage());
            showErrorAlert("Server error", e.getMessage());
        }
    }

    public List<WinAPIFunction> getAllFunctions() {
        List<WinAPIFunction> returnedFunctions = new ArrayList<>();

        try {
            returnedFunctions = client.getAllFunctions();
        } catch (Exception e) {
            log.error(e.getMessage());
            showErrorAlert("Server error", e.getMessage());
        }
        return returnedFunctions;
    }

    @Override
    public void insert(WinAPIFunction func) {
        try {
            client.addFunction(func);
        } catch (Exception e) {
            log.error(e.getMessage());
            showErrorAlert("Server error", e.getMessage());
        }
    }

    @Override
    public void delete(WinAPIFunction func) {
        try {
            client.removeFunction(func);
        } catch (Exception e) {
            log.error(e.getMessage());
            showErrorAlert("Server error", e.getMessage());
        }
    }

    @Override
    public void update(WinAPIFunction func) {
        try {
            client.updateFunction(func);
        } catch (Exception e) {
            log.error(e.getMessage());
            showErrorAlert("Server error", e.getMessage());
        }
    }

    static void showErrorAlert(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText(content);

        alert.showAndWait();
    }
}
