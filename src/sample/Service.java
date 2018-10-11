package sample;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Service {
    /**
     * 延一个时，
     * 娘欸java建文件比命令提示符快
     *
     * @throws InterruptedException
     */
    public static void delay() throws InterruptedException {
        for (int i = 0; i < 4; i++) {
            Thread.sleep(1000);
        }
    }

    /**
     * 通过简单的命令
     * 将文件转化为tex文件
     * *   执行cmd
     * *   jupyter nbconvert --to latex yourNotebookName.ipynb
     *
     * 将文件里   \documentclass[11pt]{article}后面加上下面这两行
     * *       \\usepackage{fontspec, xunicode, xltxtra}
     * *       \\setmainfont{Microsoft YaHei}
     *
     * 将latex转化为pdf
     * *        xelatex yourNotebookName.tex
     */
    public static void change2Tex(String filePkgPath, String fileName) throws IOException {

        Runtime runtime = Runtime.getRuntime();
        String cmd = "cmd /k start  jupyter nbconvert --to latex " + filePkgPath + "\\" + fileName + ".ipynb";
        runtime.exec(cmd);
    }

    /*************************************这里可以改，改成用户自定义名称*************
     * 将tex文件转化成pdf文件
     *
     * @throws IOException
     */
    public static void change2PDF(String filePkgPath) throws IOException {
        Runtime runtime = Runtime.getRuntime();
        String cmd = "cmd /k start xelatex Result.tex";
        runtime.exec(cmd, null, new File(filePkgPath));
    }

    /**
     * 创建Tex文件
     *
     * @return  获取生成的tex文件
     * @throws IOException
     */
    public static File createTexFile(String filePkgPath) throws IOException {

        File tempFile = new File(filePkgPath);
        if (!tempFile.exists()) {
            tempFile.mkdir();
        }

        File bufferTopTex = new File(filePkgPath, "Result.tex");
        if (!bufferTopTex.exists()) {
            bufferTopTex.createNewFile();
        }

        return bufferTopTex;
    }

    /**
     * 文件读写，在文件后面添加需要添加的指令
     *
     * @throws IOException
     */
    public static void modifyTex(File file, String sourceFile) throws IOException {

        RandomAccessFile topTex = new RandomAccessFile(file, "rw");

        RandomAccessFile raf = new RandomAccessFile(sourceFile, "rw");

        String line;
        while ((line = new String(raf.readLine().getBytes("ISO-8859-1"), "utf-8")) != null) {
            topTex.write(("\n" + line).getBytes());
            if (line.equals("\\documentclass[11pt]{article}")) {
                topTex.write((" \n\\usepackage{fontspec, xunicode, xltxtra}").getBytes());
                topTex.write(("\n\\setmainfont{Microsoft YaHei}").getBytes());
                topTex.write(("\n\\usepackage{ctex} ").getBytes());
                break;
            }
        }

        //接着插入后半部分
        while (true) {
            final String temp;
            if ((temp = raf.readLine()) == null) {
                break;
            } else {
                line = new String(temp.getBytes("ISO-8859-1"), "utf-8");
                topTex.write(("\n" + line).getBytes());
            }
        }
    }

    //判断是否成功生成，成功生成对应文件弹出成功框

}
