module it.polimi.sw.GC50 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires com.google.gson;
    requires java.rmi;
    requires jdk.httpserver;
    exports it.polimi.sw.GC50.view.GUI;
    exports it.polimi.sw.GC50.net.RMI;
    exports it.polimi.sw.GC50.net;
    exports it.polimi.sw.GC50.app;
    exports it.polimi.sw.GC50.net.util;
    exports it.polimi.sw.GC50.net.observ;
    exports it.polimi.sw.GC50.controller;
    exports it.polimi.sw.GC50.trash;
    exports it.polimi.sw.GC50.view.GUI.controllers;
}