package org.example;

// фабрика для сервера

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerMulti {
    private static final int PORT = 8000;
    private static final String HOST = "127.0.0.1"; // 127.0.0.1 - локальный хост
    private static final String login = "login";         // переменная логина (задаем с конструктора всем одинаковый)
    private static final String password = "password";      // переменная пароля (задаем с конструктора всем одинаковый)
    static ExecutorService executeIt = Executors.newFixedThreadPool(1);

    public static void main(String[] args) throws IOException {
        try (ServerSocket server = new ServerSocket(PORT, 10, InetAddress.getByName(HOST))) {
//        try (ServerSocket server = new ServerSocket(PORT) {
            boolean b = true;
            while (b) {
                Socket client = server.accept();
                executeIt.execute(new ServerMono(login, password, client));
//            System.out.print("Подключение установлено");
            }
            executeIt.shutdown();
        }
    }

}
