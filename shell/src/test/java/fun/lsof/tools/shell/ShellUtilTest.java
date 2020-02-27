package fun.lsof.tools.shell;

import fun.lsof.tools.shell.vo.ShellResult;
import org.junit.Test;


public class ShellUtilTest {

    @Test
    public void testSSH() {

        ShellResult root = ShellUtil.run("root", "192.168.29.194", "1qaz", 22, new String[]{"ll", "ls", "pwd"});

        System.out.println(root);

    }

}
