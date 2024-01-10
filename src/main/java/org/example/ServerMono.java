package org.example;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ServerMono implements Runnable {
    private final String login;         // переменная логина (задаем с конструктора всем одинаковый)
    private final String password;      // переменная пароля (задаем с конструктора всем одинаковый)
    private final Socket client;
    int index;                  // переменная рандомного номера цитаты с массиве
    int numberOfClient = 0;   // номер клиента

    // конструктор
    public ServerMono(String login, String password, Socket client) {
        this.login = login;
        this.password = password;
        this.client = client;
    }

    public void run() {
//        numberOfClient = numberOfClient + 1;                            // обновление номера для присвоения номера клиенту

        // установка соединения и последующая проверка правильности логина и пароля
        System.out.println("Сервер> установка предварительного соединения с клиентом #_. Время подключения: " + getCurrentDateAntTime());
        // передача клиенту его номера на стороне сервера
//        System.out.println("Сервер> отправка номера клиента на стороне сервера #_" + numberOfClient);
//        try {
//            sendMessage(client, String.valueOf(numberOfClient));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

        try {
            if (receiveMessage(client).equalsIgnoreCase("OK")) {
                // проверка логина и пароля
                if (testPassword(client)) {
                    System.out.println("Сервер> Доступ подтвержден");
                    sendMessage(client, "соединение подтверждено");
                    // основной метод выдачи цитат
                    while (receiveMessage(client) != null) {
                        System.out.println("Сервер> Поступил запрос на цитату");
                        sendQuote(client);
                    }
                    System.out.println("Сервер> Сеанс завершен. Время отключения: " + getCurrentDateAntTime());
                    client.close();
                } else {
                    System.out.println("Сервер> Неверные учетные данные. Соединение прекращено. Время отключения: " + getCurrentDateAntTime());
                    sendMessage(client, "соединение не подтверждено");
                    client.close();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Метод отправки цитат
    private void sendQuote (Socket client) throws IOException {
//        if (count < 10) {
        String s = getQuote();
        System.out.println("Сервер> сформирована и отправлена цитата: " + s);
        sendMessage(client, s);
//        }
    }

    private String getQuote() {
        int i = (int) ((Math.random() * (16 - 0)) + 0);
        String s = Temp.QUOTEARRAY[i];
//        System.out.println(s);
        return s;
    }

    private int getCount (Socket client) throws IOException {
        sendMessage(client, "Сервер> запрос счетчика цитат");
        return Integer.valueOf(receiveMessage(client));
    }

    // Метод определения текущей деты и времени
    private String getCurrentDateAntTime() {
        Date date = new Date();     // текущий день и время
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return formatter.format(date);
    }

    // Метод проверки пароля
    private boolean testPassword (Socket client) throws IOException {
        boolean b;
//        sendMessage(client, "проверка подключения");
        System.out.println("Сервер> запрос логина");
        sendMessage(client, "запрос логина");
        String loginTemp = receiveMessage(client);
        System.out.println("Сервер> получен логин");
        System.out.println("Сервер> запрос пароля");
        sendMessage(client, "запрос пароля");
        String passwordTemp = receiveMessage(client);
        System.out.println("Сервер> получен пароль");
        if (login.equalsIgnoreCase(loginTemp) && password.equalsIgnoreCase(passwordTemp)) {
            b = true;
            sendMessage(client, "соединение подтверждено");
        } else {
            b = false;
            sendMessage(client, "соединение не подтверждено");
        }
        return b;
    }

    // Метод инициализации сервера  +
//    private ServerSocket serverInit() throws IOException {
//        System.out.println("Сервер> инициализация...");
//        return new ServerSocket(port, 10, InetAddress.getByName(host));  //создаем объект класса ServerSocket
//        // и сразу возвращаем его.
//        // В конструкторе объекта: номер Порта; число подключения; номер Хоста,
//        // который получается из статического метода InetAddress.getByName(__)
//    }

    // Метод создания клиентского подключения  +
//    private Socket getClientConnection(ServerSocket server) throws IOException {
//        Socket client = server.accept();     //ожидаем подключения клиента
//        return client;
//    }

    // Метод отправки сообщения от Сервера Клиенту  +
    private void sendMessage(Socket client, String message) throws IOException {
        PrintWriter out = new PrintWriter(
                new BufferedWriter(
                        new OutputStreamWriter(client.getOutputStream())
                ),true);
//        System.out.printf("\tСервер> отправил сообщение: %s\n", message);
        out.println(message);
//        out.flush();
    }

    // Метод получения сообщения от Клиента   +
    private String receiveMessage(Socket client) throws IOException {
        BufferedReader in = new BufferedReader(
                new InputStreamReader(client.getInputStream()));
        return in.readLine();
    }
}






