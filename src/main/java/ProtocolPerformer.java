import thrift.WinAPIFunction;

import java.util.List;

interface ProtocolPerformer {
    void connect(String address);

    void insert(WinAPIFunction func);
    List<WinAPIFunction> getAllFunctions();
    void update(WinAPIFunction func);
    void delete(WinAPIFunction func);
}

