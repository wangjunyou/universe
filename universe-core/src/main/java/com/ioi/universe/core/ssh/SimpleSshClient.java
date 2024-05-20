package com.ioi.universe.core.ssh;

import com.ioi.universe.api.common.ssh.SshdClient;
import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.channel.ChannelExec;
import org.apache.sshd.client.session.ClientSession;
import org.apache.sshd.common.keyprovider.FileKeyPairProvider;
import org.apache.sshd.sftp.client.SftpClientFactory;
import org.apache.sshd.sftp.client.fs.SftpFileSystem;
import org.apache.sshd.sftp.client.fs.SftpPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Collections;

public class SimpleSshClient implements SshdClient, Closeable {

    public static final Logger LOG = LoggerFactory.getLogger(SimpleSshClient.class);

    public static final Duration DEFAULT_TIME_OUT = Duration.ofMinutes(1L);
    public static SshClient DEFAULT_CLIENT = SshClient.setUpDefaultClient();
    public ClientSession clientSession;
    public SftpFileSystem sftpFileSystem;

    @Override
    public boolean createSession(String userName, String password, String host, int port) {
        return createSession(userName, password, host, port, DEFAULT_TIME_OUT);
    }

    @Override
    public boolean createSession(String userName, String password, String host, int port, Duration duration) {
        if (connect(userName, host, port, duration)) {
            this.clientSession.addPasswordIdentity(password);
            return auth(duration);
        }
        return false;
    }

    @Override
    public boolean createSession(String userName, Path pairPath, String host, int port) {
        return createSession(userName, pairPath, host, port, DEFAULT_TIME_OUT);
    }

    @Override
    public boolean createSession(String userName, Path pairPath, String host, int port, Duration duration) {
        if (connect(userName, host, port, duration)) {
            FileKeyPairProvider pairProvider = new FileKeyPairProvider();
            pairProvider.setPaths(Collections.singleton(pairPath));
            this.clientSession.setKeyIdentityProvider(pairProvider);
            return auth(duration);
        }
        return false;
    }

    @Override
    public boolean closeSession() {
        try {
            this.clientSession.close();
        } catch (IOException e) {
            LOG.error("ClientSession close failed.", e);
            return false;
        }
        return true;
    }

    private boolean connect(String userName, String host, int port, Duration duration) {
        if (!DEFAULT_CLIENT.isStarted()) DEFAULT_CLIENT.start();
        try {
            this.clientSession = DEFAULT_CLIENT.connect(userName, host, port)
                    .verify(duration)
                    .getClientSession();
            return true;
        } catch (IOException e) {
            LOG.error("[userName: {}, host: {}, port: {}] connect failed.", userName, host, port, e);
            return false;
        }
    }

    private boolean auth(Duration duration) {
        try {
            this.clientSession.auth()
                    .verify(duration);
        } catch (IOException e) {
            LOG.error("Session auth failed.", e);
            return false;
        }
        return true;
    }

    @Override
    public boolean openSftpFileSystem() {
        try {
            this.sftpFileSystem = SftpClientFactory.instance().createSftpFileSystem(this.clientSession);
        } catch (IOException e) {
            LOG.error("SftpFileSystem open failed.", e);
            return false;
        }
        return true;
    }

    @Override
    public boolean closeSftpFileSystem() {
        try {
            this.sftpFileSystem.close();
        } catch (IOException e) {
            LOG.error("SftpFileSystem close failed.", e);
            return false;
        }
        return true;
    }

    @Override
    public InputStream exec(String command) {
        return exec(command, DEFAULT_TIME_OUT);
    }

    @Override
    public InputStream exec(String command, Duration duration) {
        try {
            ChannelExec execChannel = this.clientSession.createExecChannel(command);
            execChannel.setRedirectErrorStream(true);
            execChannel.open().verify(duration);
            return execChannel.getInvertedOut();
        } catch (IOException e) {
            LOG.error("Exec {} failed.", command, e);
            return null;
        }
    }

    @Override
    public boolean createDir(String path) {
        SftpPath remotePath = this.sftpFileSystem.getDefaultDir().resolve(path);
        if (!Files.exists(remotePath)) {
            try {
                Files.createDirectories(remotePath);
            } catch (IOException e) {
                LOG.error("CreateDir {} failed.", path, e);
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean deleteDir(String path) {
        SftpPath remotePath = this.sftpFileSystem.getDefaultDir().resolve(path);
        try {
            return Files.deleteIfExists(remotePath);
        } catch (IOException e) {
            LOG.error("deleteDir {} failed.", path);
            return false;
        }
    }

    @Override
    public boolean download(String remotePath, String localPath) {
        SftpPath srcPath = this.sftpFileSystem.getDefaultDir().resolve(remotePath);
        Path destPath = new File(localPath).toPath();
        try {
            Files.copy(srcPath, destPath);
            return true;
        } catch (IOException e) {
            LOG.error("Download remotePath: {} to localPath: {} failed.", remotePath, localPath, e);
        }
        return false;
    }

    @Override
    public boolean upload(String localPath, String remotePath) {
        Path srcPath = new File(localPath).toPath();
        SftpPath destPath = this.sftpFileSystem.getDefaultDir().resolve(remotePath);
        try {
            Files.copy(srcPath, destPath);
            return true;
        } catch (IOException e) {
            LOG.error("Upload localPath: {} to remotePath: {} failed.", localPath, remotePath, e);
        }
        return false;
    }

    @Override
    public void close() throws IOException {
        if (this.sftpFileSystem != null) this.sftpFileSystem.close();
        if (this.sftpFileSystem != null) this.sftpFileSystem.close();
        DEFAULT_CLIENT.close();
    }
}
