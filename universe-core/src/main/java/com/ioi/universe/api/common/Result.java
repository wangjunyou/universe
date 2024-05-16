package com.ioi.universe.api.common;

import java.io.Serializable;
import java.util.Objects;

public final class Result<T> implements Serializable {

    private Integer status;

    private T msg;

    public Result(Integer status, T msg) {
        this.status = status;
        this.msg = msg;
    }

    public static <T> Result<T> of(Integer status, T msg) {
        return new Result<>(status, msg);
    }

    public static <T> Result<T> of(T msg) {
        return new Result<>(200, msg);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Result<?> result = (Result<?>) o;

        if (!Objects.equals(status, result.status)) return false;
        return Objects.equals(msg, result.msg);
    }

    @Override
    public int hashCode() {
        int result = status != null ? status.hashCode() : 0;
        result = 31 * result + (msg != null ? msg.hashCode() : 0);
        return result;
    }
}
