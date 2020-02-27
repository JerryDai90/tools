package fun.lsof.tools.shell;

import com.jcraft.jsch.Session;

public interface ShellCallback {
    /**
     * 执行运行相关操作
     *
     * @param session
     * @throws Exception
     */
    void execute(Session session) throws Exception;
}
