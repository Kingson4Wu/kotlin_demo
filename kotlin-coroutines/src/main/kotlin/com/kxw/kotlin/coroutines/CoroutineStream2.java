package com.kxw.kotlin.coroutines;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Request;

public class CoroutineStream2 {

    public static void main(String[] args) {

        OkHttpClient httpClient = new Builder()
            .connectTimeout(100, TimeUnit.MILLISECONDS)
            .readTimeout(3000, TimeUnit.MILLISECONDS)
            .writeTimeout(1000, TimeUnit.MILLISECONDS)
            .retryOnConnectionFailure(true)
            .build();

        List<Supplier<String>> inputList = new ArrayList<>();
        IntStream.range(0, 10).forEach(i -> inputList.add(() -> {

            try {
                Request.Builder builder = new Request.Builder()
                    .url("http://baidu.com")
                    .get();

                Request request = builder.build();

                httpClient.newCall(request).execute();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return String.valueOf(i);
        }));

        //inputList.forEach(v->v.get());

        long startTime = System.currentTimeMillis();
        List<String> resultList = CoroutineStream.Companion.execute(inputList);
        resultList.forEach(System.out::println);
        long endTime = System.currentTimeMillis();
        System.out.println("spend:" + (endTime - startTime));

        //spend:785

        //execute(inputList);

        startTime = System.currentTimeMillis();
        resultList = execute(inputList);
        resultList.forEach(System.out::println);
        endTime = System.currentTimeMillis();
        System.out.println("spend2:" + (endTime - startTime));

        //spend2:272

        //没看到kotlin并发的优势；测试的方式不对？？

        //JMH测试 todo

    }

    public static <T> List<T> execute(List<Supplier<T>> inputList) {
        return inputList.parallelStream().map(v -> v.get()).collect(Collectors.toList());
    }

}
