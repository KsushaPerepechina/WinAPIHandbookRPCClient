import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
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
    private static boolean option;

    /**
     * Попытка соединения с сервером
     * @param address - сетевой адрес сервера
     */
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

    /**
     * Получение всех функций
     * @return список функций
     */
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

    /**
     * Добавление новой функции
     * @param func - добавляемая функция
     */
    @Override
    public void insert(WinAPIFunction func) {
        try {
            client.addFunction(func);
        } catch (Exception e) {
            log.error(e.getMessage());
            showErrorAlert("Server error", e.getMessage());
        }
    }

    /**
     * Удаление функции
     * @param func - удаляемая функция
     */
    @Override
    public void delete(WinAPIFunction func) {
        try {
            client.removeFunction(func);
        } catch (Exception e) {
            log.error(e.getMessage());
            showErrorAlert("Server error", e.getMessage());
        }
    }

    /**
     * Обновление функции
     * @param func - обновляемая функция
     */
    @Override
    public void update(WinAPIFunction func) {
        try {
            client.updateFunction(func);
        } catch (Exception e) {
            log.error(e.getMessage());
            showErrorAlert("Server error", e.getMessage());
        }
    }

    /**
     * Отображение окна ошибки
     * @param header - заголовок ошибки
     * @param content - текст ошибки
     */
    static void showErrorAlert(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
