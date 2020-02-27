package fun.lsof.tools.shell;

import com.jcraft.jsch.ChannelShell;
import fun.lsof.tools.shell.exception.ShellExcuteException;
import fun.lsof.tools.shell.vo.ShellResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

public class ShellConsole implements Runnable {

    private static Logger LOG = LoggerFactory.getLogger(ShellConsole.class);

    ChannelShell channelShell;
    InputStream inputStream;
    ShellResult result;

    public ShellConsole(ChannelShell channelShell, InputStream inputStream, ShellResult result) {
        this.channelShell = channelShell;
        this.inputStream = inputStream;
        this.result = result;
    }

    @Override
    public void run() {
        String bk = "            ";
        while (true) {
            try {
                //不要使用  BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));，会有丢失数据的
                byte[] b = new byte[128];
                int a = 0;
                if ((a = inputStream.read(b, 0, 128)) != -1) {
                    String outputStr = new String(b, 0, a);

//                    outputStr = outputStr.replaceAll("\\r\\n", "\r\n" + bk);
//                        log("\r\n" + bk + outputStr);

                    result.appendMsg("\r\n" + outputStr);
                }
                if (channelShell.isClosed()) {
                    break;
                }
            } catch (Exception e) {
                throw new ShellExcuteException(e);
            }
        }
    }
}
