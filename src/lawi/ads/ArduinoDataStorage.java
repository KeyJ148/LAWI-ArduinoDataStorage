package lawi.ads;

import lawi.ads.arduino.Arduino;
import com.fazecast.jSerialComm.SerialPort;
import lawi.ads.arduino.Package;

import java.io.IOException;
import java.util.Scanner;

public class ArduinoDataStorage {

    /*
    Написать:
    общение через сокет или консоль, как задать в параметрах командной строки
    вывод списка портов и устройств
    открытие и закрытие устройств на разных портах, перезапуск
    соединение с БД через JDBC
    соединение с системой управления через сокеты (возможно с авторизацией)
    установка начальных значений через параметры командной строки
    отлавливать все исключения в устройстве, и в случае критичных писать в лог и выводить в консоль/сокет,
        а устройство отключать

    логировать все полученные байты и результат парсинга в консоль/сокет/файл по выбору пользователя
    В параметры командной строки: префикс для имен устройств, например: /dev/, либо настройка через сокет
    Общение через сокет по JSON

    Библиотеки:
    логирование, парсинг параметров cmd, jdbc, hibernate, мб ArrayUtils и pool-connection от Apache

    В ардуино: переписать парсинг интов по виду флоатов (в идеале общая функция)
    можно по такому принципу сделать считывание пакета

    */

    public static void main(String[] args) {
        System.out.println("List:");
        SerialPort[] ports = SerialPort.getCommPorts();
        for(int i=0; i<ports.length; i++){
            System.out.println(ports[i].getDescriptivePortName() + ": " + ports[i].getSystemPortName());
        }

        Scanner in = new Scanner(System.in);
        Arduino arduino = new Arduino(in.nextLine(), 9600, (int) 1e9+7, 13, 0xAA9999AA);
        in.close();

        boolean connected = arduino.openConnection();
        System.out.println("Соединение установлено: " + connected);
        if (!connected) return;

        long lastTime = System.currentTimeMillis();
        System.out.println("Start: " + lastTime);
        while (connected) {
            try {
                Package p = arduino.nextPackage();
                System.out.println(p.t + " " + p.h + " " + p.p + " " + p.bmeStatus);
            } catch (IOException e){
                e.printStackTrace();
            }

            if (System.currentTimeMillis() - lastTime > 10000){
                lastTime = System.currentTimeMillis();
                System.out.println(System.currentTimeMillis() + ":\n" + arduino.getStatistic() + "\n");
            }
        }
    }
}
