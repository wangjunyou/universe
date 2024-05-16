package com.ioi.universe.util;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class ShellUtilTest {

    @Test
    public void test1() {
//        Process exec = ShellUtil.exec("cat /home/joker/opt/workspace/dev/universe/pom.xml");
        Process exec = ShellUtil.exec("/home/joker/下载/", null, "tar -zxvf /home/joker/下载/ddp-1.1.1.tar.gz");
//        Process exec = ShellUtil.exec("/home/joker/下载/", null, "tar -zcvf ddp-cop.tar.gz DDP-1.1.1");
        InputStream inputStream = exec.getInputStream();
        String data = null;
        int i = 0;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            loop:
            while ((data = br.readLine()) != null) {
                System.out.println(data);
                /*if (++i == 10) {
                    ShellUtil.destroy(exec);
                    break loop;
                }*/
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        int i1 = 0;
        try {
            System.out.println("==========");
            i1 = exec.waitFor();
            System.out.println("======end====");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(i1);

        Process ls = ShellUtil.exec("/home/joker/下载/DDP-1.1.1", null, "ls");
        byte[] datas = new byte[0];
        try {
            datas = ls.getInputStream().readAllBytes();
            ls.waitFor();
            System.out.println(ls.exitValue());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        String sd = new String(datas);
        for (String s : sd.split(System.lineSeparator())) {
            if (s.substring(s.length() - 3).equals(".gz")) {
                Process tar = ShellUtil.exec("/home/joker/下载/DDP-1.1.1", null, "tar -zxvf " + s);
                try {
                    InputStream inputStream1 = tar.getInputStream();
                    while (inputStream1.read() != -1) {
                    }
                    int si = tar.waitFor();
                    System.out.println(si);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } finally {
                    tar.destroy();
                }
            }
        }
        System.out.println(ls.exitValue());

        Process rm = ShellUtil.exec("/home/joker/下载/", null, "rm -rf DDP-1.1.1");
        try {
            boolean b = rm.waitFor(10L, TimeUnit.SECONDS);
            System.out.println(b);
            int rmi = rm.exitValue();
            System.out.println(rmi);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void test2() {
        Process process = ShellUtil.exec("/home/joker", null, "cat root.key");
        InputStream inputStream = process.getInputStream();
        String data = null;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            loop:
            while ((data = br.readLine()) != null) {
                System.out.println(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(process.exitValue());
    }

}
