import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import thrift.WinAPIFunction;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    private final String DEFAULT_SERVER = "0.0.0.0:12000";
    private String serverAddress = DEFAULT_SERVER;

    private static final Logger log = LogManager.getLogger(HandbookClientApp.class);
    private ProtocolPerformer performer = new RPCProtocolPerformer();

    /**
     * Таблица, отображающая содержимое справочника
     */
    @FXML
    TableView<WinAPIFunction> winAPIFunctionsTable;
    /**
     * Поле названия функции
     */
    @FXML
    TableColumn iName;
    /**
     * Поле параметров функции
     */
    @FXML
    TableColumn iParams;
    /**
     * Поле возвращаемого значения функции
     */
    @FXML
    TableColumn iReturnValue;
    /**
     * Поле описания функции
     */
    @FXML
    TableColumn iDescription;

    private final ObservableList<WinAPIFunction> data = FXCollections.observableArrayList();

    /**
     * Построение таблицы
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Callback<TableColumn, TableCell> cellFactory = p -> new EditingCell(this);

        iName.setCellValueFactory(new PropertyValueFactory<WinAPIFunction, String>("name"));
        iName.setCellFactory(cellFactory);
        iName.setOnEditCommit(
                (EventHandler<TableColumn.CellEditEvent<WinAPIFunction, String>>) t -> t.getTableView().getItems().get(
                        t.getTablePosition().getRow()).setName(t.getNewValue())
        );
        iParams.setCellValueFactory(new PropertyValueFactory<WinAPIFunction, String>("params"));
        iParams.setCellFactory(cellFactory);
        iParams.setOnEditCommit(
                (EventHandler<TableColumn.CellEditEvent<WinAPIFunction, String>>) t -> t.getTableView().getItems().get(
                        t.getTablePosition().getRow()).setParams(t.getNewValue())
        );
        iReturnValue.setCellValueFactory(new PropertyValueFactory<WinAPIFunction, String>("returnValue"));
        iReturnValue.setCellFactory(cellFactory);
        iReturnValue.setOnEditCommit(
                (EventHandler<TableColumn.CellEditEvent<WinAPIFunction, String>>) t -> t.getTableView().getItems().get(
                        t.getTablePosition().getRow()).setReturnValue(t.getNewValue())
        );
        iDescription.setCellValueFactory(new PropertyValueFactory<WinAPIFunction, String>("description"));
        iDescription.setCellFactory(cellFactory);
        iDescription.setOnEditCommit(
                (EventHandler<TableColumn.CellEditEvent<WinAPIFunction, String>>) t -> t.getTableView().getItems().get(
                        t.getTablePosition().getRow()).setDescription(t.getNewValue())
        );

        winAPIFunctionsTable.setItems(data);

        showConnectionDialog();
        getAllData();
    }

    /**
     * Получение всех функций справочника
     */
    void getAllData() {
        List<WinAPIFunction> functions = performer.getAllFunctions();
        data.clear();
        data.addAll(functions);
    }

    /**
     * Добавление новой функции в справочник
     * @param func - добавляемая функция
     */
    void addRow(WinAPIFunction func) {
        performer.insert(func);
        getAllData();
    }

    /**
     * Извлечение функции из справочника
     * @param func - извлекаемая функция
     */
    void removeRow(WinAPIFunction func) {
        performer.delete(func);
        getAllData();
    }

    /**
     * Обновление функции в справочнике
     * @param func - обновляемая функция
     */
    void updateRow(WinAPIFunction func) {
        performer.update(func);
        getAllData();
    }

    /**
     * Синхронизация содержимого таблицы с БД
     */
    @FXML
    private void handleSynchronizeAction() {
        getAllData();
    }

    /**
     * Попытка соединения с сервером
     */
    @FXML
    private void handleConnectAction() {
        showConnectionDialog();
        getAllData();
    }

    /**
     * Переключение в режим RPC
     */
    @FXML
    private void handleRPCAction() {
    }

    /**
     * Переключение в режим SOAP
     */
    @FXML
    private void handleSOAPAction() {
        performer = new RPCProtocolPerformer();
    }

    /**
     * Добавление новой записи в таблицу
     */
    @FXML
    private void handleAddAction() {
        showAddDialog();
    }

    /**
     * Открытие диалога добавления новой записи
     */
    private void showAddDialog() {
        InsertDialog dialog;
        try {
            dialog = new InsertDialog((Stage) winAPIFunctionsTable.getScene().getWindow(), this);
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            showErrorAlert(
                    "Error with add dialog",
                    "Exception occurred while loading dialog window!"
            );
        }
    }

    /**
     * Извлечение записи из таблицы
     */
    @FXML
    private void handleRemoveAction() {
        removeRow(winAPIFunctionsTable.getSelectionModel().getSelectedItem());
    }

    /**
     * Обновление записи в таблице
     */
    @FXML
    private void handleUpdateAction() {
        updateRow(winAPIFunctionsTable.getSelectionModel().getSelectedItem());
    }

    /**
     * Отображение диалога подключения к серверу
     */
    private void showConnectionDialog() {
        TextInputDialog dialog = new TextInputDialog(serverAddress);
        dialog.setTitle("Connect");
        dialog.setHeaderText("Connection to a server");
        dialog.setContentText("Enter host name:");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(address -> {
            serverAddress = address;
            performer.connect(address);
        });
    }

    /**
     * Отображение окна ошибки
     * @param header - заголовок окна
     * @param content - текст ошибки
     */
    void showErrorAlert(String header, String content) {
        RPCProtocolPerformer.showErrorAlert(header, content);
    }

}