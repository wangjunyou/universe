package com.ioi.universe.core.ssh;

import com.ioi.universe.api.common.ssh.SshdProcess;
import org.apache.sshd.client.channel.ChannelExec;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

public class SimpleSshdProcess implements SshdProcess {

    private Optional<ChannelExec> channelExec;

    public SimpleSshdProcess(Optional<ChannelExec> channelExec) {
        this.channelExec = channelExec;
    }

    public static SshdProcess of(Optional<ChannelExec> channelExec) {
        return new SimpleSshdProcess(channelExec);
    }

    @Override
    public int exitStatus() {
        return this.channelExec.isPresent() ?
                this.channelExec.get().getExitStatus() != null ?
                        this.channelExec.get().getExitStatus() : 1
                : 1;
    }

    @Override
    public InputStream getInputStream() {
        return this.channelExec.isPresent() ? this.channelExec.get().getInvertedOut() : null;
    }

    @Override
    public void close() {
        this.channelExec.ifPresent(exec -> {
            try {
                exec.close();
                ;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
