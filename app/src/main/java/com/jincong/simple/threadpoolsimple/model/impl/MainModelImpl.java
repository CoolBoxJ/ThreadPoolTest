package com.jincong.simple.threadpoolsimple.model.impl;

import android.util.Log;

import com.jincong.simple.threadpoolsimple.model.IMainModel;
import com.jincong.simple.threadpoolsimple.model.bean.ThreadPoolProxy;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainModelImpl implements IMainModel {

    @Override
    public Future startThread() {
        Runnable a = new AddRunnable();
        ThreadPoolProxy proxy = ThreadPoolProxy.getInstance();
        if (proxy != null && a != null) {
            return proxy.submit(a);
        }
        return null;
    }

    @Override
    public boolean endThread(Future a) {
        ThreadPoolProxy proxy = ThreadPoolProxy.getInstance();
        if (proxy != null && a != null) {
            return proxy.remove(a);
        }
        return false;
    }

    @Override
    public int getMaxThreadNum() {
        return ThreadPoolProxy.MAX_THREAD_NUM;
    }

    @Override
    public void shutDownNow() {
        ThreadPoolProxy proxy = ThreadPoolProxy.getInstance();
        if (proxy != null) {
            proxy.shutdownNow();
        }
    }

    static class AddRunnable implements Runnable {
        @Override
        public void run() {
            int i = 0;
            while (true) {
                i++;
                if (i >= Integer.MAX_VALUE) {
                    i = Integer.MIN_VALUE;
                }
            }
        }
    }

    @Override
    public void startLogThread() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (true) {
//                    Log.d("CPUTest", getCPURateDesc());
//                }
//            }
//        }).start();
    }

    public static String getCPURateDesc(){
        String path = "/proc/stat";// 系统CPU信息文件
        long totalJiffies[]=new long[2];
        long totalIdle[]=new long[2];
        int firstCPUNum=0;//设置这个参数，这要是防止两次读取文件获知的CPU数量不同，导致不能计算。这里统一以第一次的CPU数量为基准
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        Pattern pattern=Pattern.compile(" [0-9]+");
        for(int i=0;i<2;i++) {
            totalJiffies[i]=0;
            totalIdle[i]=0;
            try {
                fileReader = new FileReader(path);
                bufferedReader = new BufferedReader(fileReader, 8192);
                int currentCPUNum=0;
                String str;
                while ((str = bufferedReader.readLine()) != null&&(i==0||currentCPUNum<firstCPUNum)) {
                    if (str.toLowerCase().startsWith("cpu")) {
                        currentCPUNum++;
                        int index = 0;
                        Matcher matcher = pattern.matcher(str);
                        while (matcher.find()) {
                            try {
                                long tempJiffies = Long.parseLong(matcher.group(0).trim());
                                totalJiffies[i] += tempJiffies;
                                if (index == 3) {//空闲时间为该行第4条栏目
                                    totalIdle[i] += tempJiffies;
                                }
                                index++;
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    if(i==0){
                        firstCPUNum=currentCPUNum;
                        try {//暂停50毫秒，等待系统更新信息。
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        double rate=-1;
        if (totalJiffies[0]>0&&totalJiffies[1]>0&&totalJiffies[0]!=totalJiffies[1]){
            rate=1.0*((totalJiffies[1]-totalIdle[1])-(totalJiffies[0]-totalIdle[0]))/(totalJiffies[1]-totalJiffies[0]);
        }

        return String.format("cpu:%.2f",rate);
    }
}
