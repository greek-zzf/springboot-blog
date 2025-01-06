package com.springboot.blog;

import org.apache.http.client.HttpClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.registry.LocateRegistry;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class BlogApplication {


    public static void main(String[] args) {

        SpringApplication.run(BlogApplication.class, args);
    }

    // 写一个冒泡排序
    public void bubbleSort(int[] arr) {
        int temp = 0;
        for (int i = 0; i < arr.length -1; i++) {
            for (int j = 0; j < arr.length -1 -i; j++) {
                if (arr[j] > arr[j+1]) {
                    // 交换
                    temp = arr[j];
                    arr[j] = arr[j+1];
                    arr[j+1] = temp;
                }
            }
        }

    }



    public void caculateDaysBetweenDates(LocalDate startDate, LocalDate endDate) {
        int days = 0;
        while (startDate.isBefore(endDate)) {
            days++;
            startDate = startDate.plusDays(1);
        }
        System.out.println(days);
    }



}
