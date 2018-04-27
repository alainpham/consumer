package techlab;

import org.springframework.stereotype.Component;

@Component(value = "counter")
public class Counter {


    private int msgCount=0;

    public int getMsgCount() {
        return msgCount;
    }

    public void setMsgCount(int msgCount) {
        this.msgCount = msgCount;
    }

    public synchronized void increment() {
        msgCount++;
    }
}
