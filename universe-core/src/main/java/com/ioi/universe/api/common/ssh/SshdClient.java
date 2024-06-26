package com.ioi.universe.api.common.ssh;

import java.nio.file.Path;
import java.time.Duration;

public interface SshdClient {

    public boolean createSession(String userName, String password, String host, int port);
    public boolean createSession(String userName, String password, String host, int port, Duration duration);
    public boolean createSession(String userName, Path pairPath, String host, int port);
    public boolean createSession(String userName, Path pairPath, String host, int port, Duration duration);
    public boolean closeSession();
    public boolean openSftpFileSystem();
    public boolean closeSftpFileSystem();
    public SshdProcess exec(String command);
    public SshdProcess exec(String command, Duration duration);
    public boolean createDir(String path);
    public boolean deleteFileOrDir(String path);
    public boolean download(String remotePath, String localPath);
    public boolean upload(String localPath, String remotePath);

}
