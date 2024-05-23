package com.ioi.universe.core.ssh;

import com.ioi.universe.api.common.ssh.SshdProcess;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;

public class SimpleSshClientTest {

    private SimpleSshClient simpleSshClient;

    @BeforeEach
    public void beforeEach() {
        this.simpleSshClient = new SimpleSshClient();
        boolean session = simpleSshClient.createSession("root", "admin", "192.168.56.8", 22);
        System.out.println("session:" + session);
    }

    @Test
    public void test1() {
        boolean dir = simpleSshClient.deleteFileOrDir("/home/hadoop/soft/wjy/DDP-1.1.1");
        System.out.println("dir:" + dir);
    }

    @Test
    public void test2() {
        boolean b = simpleSshClient.openSftpFileSystem();
        System.out.println("b:" + b);
        boolean b1 = simpleSshClient.deleteFileOrDir("/home/hadoop/soft/wjy");
        System.out.println("delete:" + b1);
        boolean dir = simpleSshClient.createDir("/home/hadoop/soft/wjy");
        System.out.println("dir:" + dir);
        boolean upload = simpleSshClient.upload("/batch/迅雷下载/ddp-1.1.1.tar.gz", "/home/hadoop/soft/wjy/ddp-1.1.1.tar.gz");
        System.out.println("upload:" + upload);
    }

    @Test
    public void test3() {
        boolean b = simpleSshClient.openSftpFileSystem();
        System.out.println("b:" + b);
        boolean download = simpleSshClient.download("/home/hadoop/soft/wjy/ddp-1.1.1.tar.gz", "/home/joker/下载/ddp-1.1.1.tar.gz");
        System.out.println("download:" + download);
    }

    @Test
    public void test4() {
        boolean b = simpleSshClient.openSftpFileSystem();
        System.out.println("b:" + b);
//        boolean delete = simpleSshClient.deleteDir("/home/hadoop/soft/wjy/ddp-1.1.1.tar.gz");
        boolean delete = simpleSshClient.deleteFileOrDir("/home/hadoop/soft/wjy");
        System.out.println("delete:" + delete);
    }

    @Test
    public void test5() throws IOException {
        boolean b = simpleSshClient.openSftpFileSystem();
        System.out.println("b:" + b);
        for (int ii = 0; ii < 4; ii++) {
            SshdProcess process1 = simpleSshClient.exec("rm -rf /home/hadoop/soft/wjy/DDP-1.1.1");
            InputStream exec1 = process1.getInputStream();
            System.out.println(new String(exec1.readAllBytes()));
            System.out.println("exitStatus:" + process1.exitStatus());
            process1.close();
            SshdProcess process2 = simpleSshClient.exec("tar -zxvf /home/hadoop/soft/wjy/ddp-1.1.1.tar.gz -C /home/hadoop/soft/wjy/");
            InputStream exec2 = process2.getInputStream();
            byte[] bt = new byte[1024];
            int i;
            while ((i = exec2.read(bt)) != -1) {
                System.out.print(new String(bt, 0, i));
            }
            System.out.println("exitStatus:" + process2.exitStatus());
            process2.close();
        }
    }

    @Test
    public void test6() throws IOException {
//        boolean b = simpleSshClient.openSftpFileSystem();
//        System.out.println("b:" + b);
//        boolean dir = simpleSshClient.createDir("/home/hadoop/soft/wjy");
//        System.out.println("dir:" + dir);
        SshdProcess process = simpleSshClient.exec("ls /home/hadoop/soft/wjy/", Duration.ofMillis(1000L));
        System.out.println(process.exitStatus());
        InputStream inputStream = process.getInputStream();
        System.out.println(new String(inputStream.readAllBytes()));
        process.close();
        SshdProcess process1 = simpleSshClient.exec("tar -zxvf /home/hadoop/soft/wjy/ddp-1.1.1.tar.gz -C /home/hadoop/soft/wjy/");
        InputStream inputStream1 = process1.getInputStream();
        byte[] bt = new byte[1024];
        int i;
        while ((i = inputStream1.read(bt)) != -1) {
            System.out.print(new String(bt, 0, i));
        }
        System.out.println(process1.exitStatus());
        process1.close();
        SshdProcess process2 = simpleSshClient.exec("rm -rf /home/hadoop/soft/wjy/DDP-1.1.1");
        System.out.println(process2.exitStatus());
        process2.close();
    }

    @AfterEach
    public void afterEach() throws IOException {
        simpleSshClient.close();
    }
}
