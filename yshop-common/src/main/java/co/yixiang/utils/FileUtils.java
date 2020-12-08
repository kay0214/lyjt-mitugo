package co.yixiang.utils;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 文件处理工具类
 * 
 * @author monster
 */
public class FileUtils
{
    public static String FILENAME_PATTERN = "[a-zA-Z0-9_\\-\\|\\.\\u4e00-\\u9fa5]+";

    /**
     * 输出指定文件的byte数组
     * 
     * @param filePath 文件路径
     * @param os 输出流
     * @return
     */
    public static void writeBytes(String filePath, OutputStream os) throws IOException
    {
        FileInputStream fis = null;
        try
        {
            File file = new File(filePath);
            if (!file.exists())
            {
                throw new FileNotFoundException(filePath);
            }
            fis = new FileInputStream(file);
            byte[] b = new byte[1024];
            int length;
            while ((length = fis.read(b)) > 0)
            {
                os.write(b, 0, length);
            }
        }
        catch (IOException e)
        {
            throw e;
        }
        finally
        {
            if (os != null)
            {
                try
                {
                    os.close();
                }
                catch (IOException e1)
                {
                    e1.printStackTrace();
                }
            }
            if (fis != null)
            {
                try
                {
                    fis.close();
                }
                catch (IOException e1)
                {
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
     * 删除文件
     * 
     * @param filePath 文件
     * @return
     */
    public static boolean deleteFile(String filePath)
    {
        boolean flag = false;
        File file = new File(filePath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists())
        {
            file.delete();
            flag = true;
        }
        return flag;
    }

    /**
     * 文件名称验证
     * 
     * @param filename 文件名称
     * @return true 正常 false 非法
     */
    public static boolean isValidFilename(String filename)
    {
        return filename.matches(FILENAME_PATTERN);
    }

    /**
     * 下载文件名重新编码
     * 
     * @param request 请求对象
     * @param fileName 文件名
     * @return 编码后的文件名
     */
    public static String setFileDownloadHeader(HttpServletRequest request, String fileName) throws UnsupportedEncodingException
    {
        final String agent = request.getHeader("USER-AGENT");
        String filename = fileName;
        if (agent.contains("MSIE"))
        {
            // IE浏览器
            filename = URLEncoder.encode(filename, "utf-8");
            filename = filename.replace("+", " ");
        }
        else if (agent.contains("Firefox"))
        {
            // 火狐浏览器
            filename = new String(fileName.getBytes(), "ISO8859-1");
        }
        else if (agent.contains("Chrome"))
        {
            // google浏览器
            filename = URLEncoder.encode(filename, "utf-8");
        }
        else
        {
            // 其它浏览器
            filename = URLEncoder.encode(filename, "utf-8");
        }
        return filename;
    }
    /**
     * 把文件打成压缩包并保存在本地硬盘
     */
    public static void saveZipFiles(List<String> srcfiles, String zipFilePath, String zipFileName) {
        try {
            // 创建文件夹
            File file = new File(zipFilePath);
            if (!file.exists()) {
                file.mkdirs();
            }
            // 创建zip输出流
            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFilePath + File.separator + zipFileName));
            // 循环将源文件列表添加到zip文件中
            zipFile(srcfiles, zos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 把文件打成压缩包并输出到客户端浏览器中
     */
    public static void downloadZipFiles(HttpServletResponse response, List<String> srcFiles, String zipFileName) {
        try {
            response.reset(); // 重点突出
            response.setCharacterEncoding("UTF-8"); // 重点突出
            //response.setContentType("application/x-msdownload"); // 不同类型的文件对应不同的MIME类型 // 重点突出
           // response.addHeader("Content-Length", "" + data.length);
            // 不同类型的文件对应不同的MIME类型 // 重点突出
            response.setContentType("application/octet-stream; charset=UTF-8");

            // 对文件名进行编码处理中文问题
            zipFileName = new String(zipFileName.getBytes(), StandardCharsets.UTF_8);
            // inline在浏览器中直接显示，不提示用户下载
            // attachment弹出对话框，提示用户进行下载保存本地
            // 默认为inline方式
            response.setHeader("Content-Disposition", "attachment;filename=" +URLEncoder.encode(zipFileName,"UTF-8"));

            // --设置成这样可以不用保存在本地，再输出， 通过response流输出,直接输出到客户端浏览器中。
            ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());
            zipFile(srcFiles, zos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 压缩文件
     *
     * @param filePaths 需要压缩的文件路径集合
     * @throws IOException
     */
    private static void zipFile(List<String> filePaths, ZipOutputStream zos) {
        //设置读取数据缓存大小
        byte[] buffer = new byte[4096];
        try {
            //循环读取文件路径集合，获取每一个文件的路径
            for (String filePath : filePaths) {
                File inputFile = new File(filePath);
                //判断文件是否存在
                if (inputFile.exists()) {
                    //判断是否属于文件，还是文件夹
                    if (inputFile.isFile()) {
                        //创建输入流读取文件
                        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(inputFile));
                        //将文件写入zip内，即将文件进行打包
                        zos.putNextEntry(new ZipEntry(inputFile.getName()));
                        //写入文件的方法，同上
                        int size = 0;
                        //设置读取数据缓存大小
                        while ((size = bis.read(buffer)) > 0) {
                            zos.write(buffer, 0, size);
                        }
                        //关闭输入输出流
                        zos.closeEntry();
                        bis.close();
                    } else {  //如果是文件夹，则使用穷举的方法获取文件，写入zip
                        File[] files = inputFile.listFiles();
                        List<String> filePathsTem = new ArrayList<String>();
                        for (File fileTem : files) {
                            filePathsTem.add(fileTem.toString());
                        }
                        zipFile(filePathsTem, zos);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != zos) {
                try {
                    zos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 删除指定文件或文件夹下所有文件
     *
     * @param path 文件夹完整绝对路径
     * @return
     */
    public static boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return false;
        }
        if (file.isFile()) {
            return file.delete();
        }
        if (file.isDirectory()) {
            String[] fileList = file.list();
            String tempFilePath = null;
            for (String tempFileName : fileList) {
                if (path.endsWith(File.separator)) {
                    tempFilePath = path + tempFileName;
                } else {
                    tempFilePath = path + File.separator + tempFileName;
                }
                delAllFile(tempFilePath);
            }
            file.delete();
        }
        return true;
    }

    /**
     * 判断文件路径是否存在
     *
     * @param uploadDir
     * @return
     * @throws IOException
     */
    public static File getAbsoluteFile(String uploadDir) throws IOException {
        File desc = new File(uploadDir);

        if (!desc.getParentFile().exists()) {
            desc.getParentFile().mkdirs();
        }
        if (!desc.exists()) {
            desc.createNewFile();
        }
        return desc;
    }
}
