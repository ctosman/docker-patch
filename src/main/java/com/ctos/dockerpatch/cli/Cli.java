package com.ctos.dockerpatch.cli;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.ctos.dockerpatch.model.DockerPatch;
import com.ctos.dockerpatch.model.DockerTar;
import com.ctos.dockerpatch.model.FileInfo;
import com.ctos.dockerpatch.model.Layer;
import com.ctos.dockerpatch.util.DockerTarUtil;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cli {
    private static final String OLD_IMG_DIR = "./old-img-dir";
    private static final String NEW_IMG_DIR = "./new-img-dir";

    public static void main(String[] args) {
        Options options = new Options();
        options.addOption("d", false, "生成两个版本的docker镜像间的增量更新部分");
        options.addOption("m", false, "将老镜像和patch合并成新镜像");

        try {
            DefaultParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(options, args);
            if(cmd.hasOption("d")) {
                String[] cmdArgs = cmd.getArgs();
                if(cmdArgs.length != 2) {
                    System.out.println("入参必须有两个，第一个是旧镜像路径，第二个是新镜像路径");
                    return;
                }

                String oldImgPath = cmdArgs[0];
                String newImgPath = cmdArgs[1];
                System.out.println("-d, 开始生成增量补丁" + oldImgPath + ", " + newImgPath);
                //读取新旧tar包的内容，比对出需要更新的文件
                DockerTar oldDockerTar = DockerTarUtil.generateDockerTarInfo(oldImgPath);
                DockerTar newDockerTar = DockerTarUtil.generateDockerTarInfo(newImgPath);
                DockerPatch dockerPatch = DockerTarUtil.generateDockerPatch(oldDockerTar, newDockerTar);
                System.out.println(JSONUtil.toJsonPrettyStr(dockerPatch));

                //解压出必要文件，打包成tar包;将待更新的文件日期、新镜像名存入数据结构

                //将数据结构写入json文件

                //打tar包，删除解压后的目录
            }

            if(cmd.hasOption("m")) {
                String[] cmdArgs = cmd.getArgs();
                if(cmdArgs.length != 2) {
                    System.out.println("入参必须有两个，第一个是旧镜像路径，第二个是patch路径");
                    return;
                }

                System.out.println("-m, 开始生成新镜像" + cmdArgs[0] + ", " + cmdArgs[1]);
                //解压旧镜像tar

                //将patch内容解压，生成新目录，放入相关文件

                //修改文件日期，生成tar包
            }
        } catch (ParseException e) {
            System.out.println("解析入参失败:" + e);
        }
    }


}
