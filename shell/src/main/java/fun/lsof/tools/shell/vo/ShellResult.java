package fun.lsof.tools.shell.vo;

public class ShellResult {

    String msg = "";
    String consoleOutputStr = "";
    boolean succeed = true;


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getConsoleOutputStr() {
        return consoleOutputStr;
    }

    public void setConsoleOutputStr(String consoleOutputStr) {
        this.consoleOutputStr = consoleOutputStr;
    }

    public boolean isSucceed() {
        return succeed;
    }

    public void setSucceed(boolean succeed) {
        this.succeed = succeed;
    }

    public void appendMsg(String msg){
        this.msg += msg;
    }

    @Override
    public String toString() {
        return "ShellResult{" +
                "msg='" + msg + '\'' +
                ", consoleOutputStr='" + consoleOutputStr + '\'' +
                ", succeed=" + succeed +
                '}';
    }
}
