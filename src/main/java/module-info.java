module it.polimi.sw.GC50 {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires java.rmi;
    exports it.polimi.sw.GC50.net.RMI;
    exports it.polimi.sw.GC50.net;
    exports it.polimi.sw.GC50.app;
    exports it.polimi.sw.GC50.net.util;
    exports it.polimi.sw.GC50.net.observ;
}