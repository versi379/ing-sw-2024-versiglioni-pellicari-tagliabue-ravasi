module it.polimi.sw.GC50 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires com.google.gson;
    requires java.rmi;
    requires jdk.httpserver;
    exports it.polimi.sw.GC50.view.GUI;
    exports it.polimi.sw.GC50.net.RMI;
    exports it.polimi.sw.GC50.app;
    exports it.polimi.sw.GC50.controller;
    exports trash;
    exports it.polimi.sw.GC50.view.GUI.controllers;
    opens it.polimi.sw.GC50.view.GUI.controllers;
    exports it.polimi.sw.GC50.model.lobby;
    exports it.polimi.sw.GC50.model;
    exports it.polimi.sw.GC50.view;
    exports it.polimi.sw.GC50.net.requests;
    exports it.polimi.sw.GC50.net.messages;
    exports it.polimi.sw.GC50.net.client;
    exports it.polimi.sw.GC50.net;
}