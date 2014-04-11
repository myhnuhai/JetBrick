package org.myhnuhai.pageModel;

import java.io.Serializable;

/**
 * Created by 马英虎 on 14-3-15.
 */
public class Json implements Serializable{
    private boolean success = false;
    private String msg;
    private Object data = null;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Json)) return false;

        Json json = (Json) o;

        if (success != json.success) return false;
        if (data != null ? !data.equals(json.data) : json.data != null) return false;
        if (msg != null ? !msg.equals(json.msg) : json.msg != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (success ? 1 : 0);
        result = 31 * result + (msg != null ? msg.hashCode() : 0);
        result = 31 * result + (data != null ? data.hashCode() : 0);
        return result;
    }
}
