package sample.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sample.Main;
import sample.Service;

import java.io.File;
import java.io.IOException;

public class Controller {

    InputFileDialogController InputFileDialogController = new InputFileDialogController();

    private String srcFilePath;

    @FXML
    private Label filePath_Label;

    @FXML
    private void importFile() {
        //创建选择文件stage
        Stage chooseFileStage = new Stage();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("选择文件");

        //文件过滤
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("ipynb file", "*.ipynb")
        );

        //以对话框的形式显示选择文件
        File file = fileChooser.showOpenDialog(chooseFileStage);
        if (file != null) {
            String absolutePath = file.getAbsolutePath();
            srcFilePath = absolutePath;
            filePath_Label.setText(absolutePath);
            System.out.println("劳资要读文件啦");
        }
    }

    @FXML
    /**
     * 生成按钮点击事件处理
     * srcFilePath:文件的绝对路径
     * fullName：名称+后缀
     * filePkgPath：目标文件的包路径，
     *      比如：C:\test\notebook\test.ipynb。
     *      这里filePkgPath就是C:\test\notebook
     * path_arr数组：对文件绝对路径的拆分，按反斜杠 \ 进行拆分，在java里用\\
     * fullName_arr数组：对fullName按英文句号进行拆分，在java里用"\\."表示
     * texFileName：构造一个tex文件名，给后面转化pdf时用
     *
     */
    public void generatePDF() throws IOException, InterruptedException {
        if (srcFilePath != null) {                                               //在选择了文件的情况下
            String[] path_arr = srcFilePath.split("\\\\");
            String fullName = path_arr[path_arr.length - 1];

            StringBuffer filePkgPath = getStringBuffer(path_arr);
            String[] fullName_arr = fullName.split("\\.");

            Service.change2Tex(filePkgPath.toString(), fullName_arr[0]);

            File bufferFile = Service.createTexFile(filePkgPath.toString());

            Service.delay();

            String texFileName = fullName_arr[0] + ".tex";
            texFileName = filePkgPath + "\\" + texFileName;
            Service.modifyTex(bufferFile, texFileName);
            Service.change2PDF(filePkgPath.toString());
        } else {
            InputFileDialogController.loadDialog().show();            //显示提示框，请先选择所需文件
            Main.getMainStage().close();
        }
    }

    /**
     * 获取目标文件的包路径
     *
     * @param path_arr
     * @return
     */
    private StringBuffer getStringBuffer(String[] path_arr) {
        StringBuffer filePkgPath = new StringBuffer();
        for (int i = 0; i < path_arr.length - 1; i++) {
            if (i < path_arr.length - 2) {                  //除开绝对路径后面的（文件名+后缀）那一项
                filePkgPath.append(path_arr[i]).append("\\");
            } else {
                filePkgPath.append(path_arr[i]);
            }
        }
        return filePkgPath;
    }

    /**
     * 退出软件
     *
     * @throws IOException
     */
    @FXML
    public void exit() throws IOException {
        Main.getMainStage().close();
    }
}
