package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientMono implements Runnable{
    private final int port;             // переменная с номером порта
    private final String host;          // переменная с номером хоста
    private final String login;         // переменная логина (задаем с конструктора всем одинаковый)
    private final String password;      // переменная пароля (задаем с конструктора всем одинаковый)
    private int numberOfClient;   // номер клиента
    private int count = 0;                  // переменная счетчик цитат

    public ClientMono(int port, String host, String login, String password, int numberOfClient) {
        this.port = port;
        this.host = host;
        this.login = login;
        this.password = password;
        this.numberOfClient = numberOfClient;
    }

    public void run() {
        try (Socket client = clientInit())  // попытка инициализации клиента
        {
//            numberOfClient = Integer.valueOf(receiveMessageFromServer(client));
//            System.out.println("Клиент> получил номер клиента на стороне сервера #_" + numberOfClient);
            sendMessage(client, "OK");

            // проверка логина и пароля
            boolean b = true;   // служебный флаг работы в цикле
            while (b) {
                String s = receiveMessageFromServer(client);
                if (s.equalsIgnoreCase("соединение не подтверждено")) {
                    System.out.println("Клиент #_" + numberOfClient + "> получил отказ в соединении");
                    b = false;
                } else if (s.equalsIgnoreCase("запрос логина")) {
                    System.out.println("Клиент #_" + numberOfClient + "> получил запрос логина");
                    System.out.println("Клиент #_" + numberOfClient + "> отправил логин");
                    sendMessage(client, login);

                } else if (s.equalsIgnoreCase("запрос пароля")) {
                    System.out.println("Клиент #_" + numberOfClient + "> получил запрос пароля");
                    System.out.println("Клиент #_" + numberOfClient + "> отправил пароль");
                    sendMessage(client, password);

                } else if (s.equalsIgnoreCase("соединение подтверждено")) {
                    receiveQuote(client, count);
                    System.out.println("Клиент #_" + numberOfClient + "> получил все цитаты");
//                    sendMessage(client, "выход");
                    b = false;
                }
            }

        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    // Метод запроса цитат
    private void receiveQuote(Socket client, int count) throws IOException {
        String s = receiveMessageFromServer(client);    // сброс значения полученного сообщения
        while (count < 10) {
            System.out.println("Клиент #_" + numberOfClient + "> запрос цитаты № " + (count + 1));
            sendMessage(client, "запрос цитаты");
            System.out.println("Клиент #_" + numberOfClient + "> получена цитата №_" + (count + 1) + ": " + receiveMessageFromServer(client));
            count = count + 1;
        }
    }

    // Метод инициализации сокета клиента  +
    private Socket clientInit() {
        System.out.println("Клиент> инициализация...");
        Socket client = null;
        try {
            client = new Socket(host, port);
            System.out.println("Клиент #_" + numberOfClient + " подключен");
        } catch (IOException e) {
            System.out.println("Не удалось создать подключение для клиента #_" + numberOfClient);
            throw new RuntimeException(e);
        }
        return client;
    }

    // Метод получения сообщения от Сервера  +
    private String receiveMessageFromServer(Socket client) {
        BufferedReader in = null;
        try {
            in = new BufferedReader(
                    new InputStreamReader(client.getInputStream()));
        } catch (IOException e) {
            System.out.println("Не удалось создать поток ввода от сервера для клиента #_" + numberOfClient);
            throw new RuntimeException(e);
        }

        try {
            String s = in.readLine();
            System.out.println("Сообщение от сервера для клиента #_" + numberOfClient + " получено");
            System.out.println("Сообщение: " + s);
            return s;
        } catch (IOException e) {
            System.out.println("Не удалось получить сообщение от сервера для клиента #_" + numberOfClient);
            throw new RuntimeException(e);
        }
    }

    // Метод отправки сообщения Серверу  +
    private void sendMessage(Socket client, String message) throws IOException {
        PrintWriter out = new PrintWriter(
                new BufferedWriter(
                        new OutputStreamWriter(client.getOutputStream())), true);
//        System.out.printf("\tКлиент> отправил сообщение: %s\n", message);
        out.println(message);
//        out.flush();
    }

}
