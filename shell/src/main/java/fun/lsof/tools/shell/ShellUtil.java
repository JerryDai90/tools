package fun.lsof.tools.shell;

import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import fun.lsof.tools.shell.exception.SessionException;
import fun.lsof.tools.shell.exception.ShellExcuteException;
import fun.lsof.tools.shell.vo.ShellResult;

import java.io.*;

public class ShellUtil {

    /**
     * 执行命令
     * @param username
     * @param ip
     * @param pwd
     * @param cmd
     * @return
     */
    public static ShellResult run(String username, String ip, String pwd, int port, final String cmd[]) {

        final ShellResult result = new ShellResult();
        try {

            if (cmd.length > 0) {
                createSession(username, pwd, ip, port, new ShellCallback() {
                    @Override
                    public void execute(Session session) throws InterruptedException, JSchException, IOException {
                        execCommand(session, cmd, result);
                    }
                });
            }
        } catch (Exception e) {
            result.setMsg(e.getMessage());
            result.setSucceed(false);
        }
        return result;
    }

    public static void createSession(String username, String pwd, String ip, int port, ShellCallback callback) {

        JSch jsch = new JSch();
        Session session = null;
        try {
            log("remote ip :" + ip);
            session = jsch.getSession(username, ip, port);
            //第一次访问服务器不用输入yes
            session.setConfig("StrictHostKeyChecking", "no");
            session.setPassword(pwd);

            log("session connecting...");
            session.connect();
//            Thread.sleep(1000L);
            log("session is connected：" + session.isConnected());

        } catch (Exception e) {
            throw new SessionException(e);
        }

        try {
            callback.execute(session);
        }catch (Exception e) {
            throw new ShellExcuteException(e);
        } finally {
            session.disconnect();
        }

    }

    private static void execCommand(Session session, String[] commands, ShellResult result) throws IOException, JSchException, InterruptedException {
        execCommand(session, commands, 500L, result);
    }

    private static void execCommand(Session session, String[] commands, long cmdIntervalRunTime, ShellResult result) throws IOException, JSchException, InterruptedException {

        ChannelShell channelShell = null;
        OutputStream outputStream = null;
        InputStream inputStream = null;
        try {

            //2.尝试解决 远程ssh只能执行一句命令的情况
            channelShell = (ChannelShell) session.openChannel("shell");
            //从远端到达的数据  都能从这个流读取到
            inputStream = channelShell.getInputStream();
            channelShell.setPty(true);
            channelShell.connect();

            new Thread(new ShellConsole(channelShell, inputStream, result)).start();

            //写入该流的数据  都将发送到远程端
            outputStream = channelShell.getOutputStream();
            //使用PrintWriter 就是为了使用println 这个方法
            //好处就是不需要每次手动给字符加\n
            PrintWriter printWriter = new PrintWriter(outputStream);

            //先跑一条无用语句，让控制台进入状态，还要休眠一下，否则控制台响应不过来
            printWriter.println();
            printWriter.flush();
            Thread.sleep(1000L);

            for (String _cmd : commands) {
//                log("cmd action: " + _cmd);
                printWriter.println(_cmd);
                printWriter.flush();

                if ("act".equals(_cmd) || "commit".equals(_cmd)) {
                    Thread.sleep(cmdIntervalRunTime);
                } else {
                    Thread.sleep(cmdIntervalRunTime);
                }
            }
            //为了结束本次交互
            printWriter.println("exit");
            //把缓冲区的数据强行输出
            printWriter.flush();
        } finally {
            close(outputStream);
            close(inputStream);
            try {
                channelShell.disconnect();
            } catch (Exception e1) {
            }
        }
        log("execute end");
    }


    private static void log(String msg) {
        System.out.println(Thread.currentThread().getName() + ":" + msg);
    }


    private static void close(Object... obj) {
        for (Object _obj : obj) {
            try {

                if (_obj instanceof Closeable) {
                    ((Closeable) _obj).close();
                }
            } catch (IOException e) {
                //nothing
            }
        }
    }

}
