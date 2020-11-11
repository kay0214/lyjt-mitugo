package co.yixiang.modules.esign.core;

import co.yixiang.modules.esign.exception.DefineException;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.text.MessageFormat;

/**
 * desciption 文件辅助类
 *
 * @author lsy
 * date 2019/7/4 11:22
 * @since 1.7
 */
@Slf4j
public class FileHelper {

    //--------------------------------共有方法 start-------------------------------------

    /**
     * description 获取文件字节流
     *
     * @param filePath {@link String} 文件路径
     * @return byte[]
     * {@link java.lang.reflect.Array} 节流流
     * date  2019-7-4
     * @author lsy
     **/
    public static byte[] getFileBytes(String filePath) throws DefineException {
        File file = new File(filePath);
        FileInputStream fis = null;
        byte[] buffer;

        try {
            fis = new FileInputStream(file);
            buffer = new byte[(int) file.length()];
            fis.read(buffer);
        } catch (Exception e) {
            throw new DefineException(
                    MessageFormat.format("获取文件字节流异常：{0}", e.getMessage()));
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    throw new DefineException(
                            MessageFormat.format("关闭字节流异常：{0}", e.getMessage()));
                }
            }
        }
        return buffer;
    }

    /**
     * description 文件字节流保存为本地文件
     *
     * @param bytes       {@link java.lang.reflect.Array} 文件字节流
     * @param dstFilePath {@link String} 签署后PDF文件保存路径
     * @return void
     * @author lsy
     * date  2019-7-4
     **/
    public static void saveFileByStream(byte[] bytes, String dstFilePath)
            throws DefineException {

        BufferedOutputStream bos = null;
        File dstFile = new File(dstFilePath);
        //文件目录
        File directory = new File(dstFile.getParent());
        if (!directory.exists() && directory.isDirectory()) {
            directory.mkdirs();
        }

        try {
            bos = new BufferedOutputStream(new FileOutputStream(dstFile));
            bos.write(bytes);
            bos.flush();
        } catch (Exception e) {
            throw new DefineException(
                    MessageFormat.format("文件字节流保存为本地文件异常：{0}", e.getMessage()));
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    throw new DefineException(
                            MessageFormat.format("关闭文件字节流异常：{0}", e.getMessage()));
                }
            }
        }
    }

    //--------------------------------共有方法 end---------------------------------------

}


