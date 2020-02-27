package fun.lsof.tools.shell;

import fun.lsof.tools.shell.vo.ShellResult;
import org.junit.Test;


public class ShellUtilTest {

    @Test
    public void testSSH() {

//        if (null != args && args.length >= 1) {
//            cmd = "conf###rule id 103###enable###exit".split("###");
//        } else {
//            cmd = "conf###rule id 103###disable###exit".split("###");
//        }

//        t.sshKeyblock("hillstone", "192.168.254.5", "gdgm*&&)6270", cmd);

        ShellResult root = ShellUtil.run("root", "192.168.29.194", "1qaz@WSX@2019", 22, new String[]{"ll", "ls", "pwd"});

        System.out.println(root);

    }

}
