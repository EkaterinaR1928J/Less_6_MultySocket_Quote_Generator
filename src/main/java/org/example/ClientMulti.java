package org.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientMulti {
    private static ServerSocket server;
    private static final int PORT = 8000;
    private static final String HOST = "127.0.0.1"; // 127.0.0.1 - локальный хост
    private static final String login = "login";         // переменная логина (задаем с конструктора всем одинаковый)
    private static final String password = "password";      // переменная пароля (задаем с конструктора всем одинаковый)


    public static void main(String[] args) throws IOException, InterruptedException {

        // запустим пул нитей в которых колличество возможных нитей ограничено - 5-ю.
        ExecutorService exec = Executors.newFixedThreadPool(5);
        int j = 0;

        // стартуем цикл в котором с паузой в 10 милисекунд стартуем клиентов
        while (j < 5) {
            j++;
            exec.execute(new ClientMono(PORT, HOST, login, password, j));
//            Thread.sleep(10);
        }

        // закрываем фабрику
        exec.shutdown();
    }
}
