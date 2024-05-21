package com.ioi.universe.api.common.ssh;

import java.io.InputStream;

public interface SshdFutre {

    public int exitStatus();
    public InputStream getInputStream();
    public void close();
}
