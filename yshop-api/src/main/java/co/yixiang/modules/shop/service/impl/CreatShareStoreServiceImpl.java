/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package co.yixiang.modules.shop.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import co.yixiang.common.constant.CommonConstant;
import co.yixiang.modules.image.entity.YxImageInfo;
import co.yixiang.modules.image.service.YxImageInfoService;
import co.yixiang.modules.shop.entity.YxStoreInfo;
import co.yixiang.modules.shop.mapper.YxStoreInfoMapper;
import co.yixiang.modules.shop.service.CreatShareStoreService;
import co.yixiang.modules.user.entity.YxSystemAttachment;
import co.yixiang.modules.user.service.YxSystemAttachmentService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static co.yixiang.utils.FileUtil.transformStyle;

/**
 * @author zhangqingqing
 * @version CreatShareStoreServiceImpl, v0.1 2020/8/25 17:02
 */
@Slf4j
@Service
public class CreatShareStoreServiceImpl implements CreatShareStoreService {

    @Autowired
    YxSystemAttachmentService systemAttachmentService;
    @Autowired
    YxStoreInfoMapper yxStoreInfoMapper;

    @Autowired
    YxImageInfoService yxImageInfoService;

    @Override
    public String creatProductPic(Integer id, String shareCode, String spreadPicName, String spreadPicPath, String apiUrl) throws IOException, FontFormatException {
        YxStoreInfo yxStoreInfo = yxStoreInfoMapper.selectById(id);
        YxImageInfo yxImageInfo = yxImageInfoService.selectOneImg(id, CommonConstant.IMG_TYPE_STORE, CommonConstant.IMG_CATEGORY_PIC);
        YxSystemAttachment attachmentT = systemAttachmentService.getInfo(spreadPicName);
        String spreadUrl = "";
        if(ObjectUtil.isNull(attachmentT)){
            //创建图片
            BufferedImage img = new BufferedImage(750, 1334, BufferedImage.TYPE_INT_RGB);
            //开启画图
            Graphics g = img.getGraphics();
            //背景 -- 读取互联网图片
            InputStream stream =  getClass().getClassLoader().getResourceAsStream("background.png");
            ImageInputStream background = ImageIO.createImageInputStream(stream);
            BufferedImage back = ImageIO.read(background);

            g.drawImage(back.getScaledInstance(750, 1334, Image.SCALE_DEFAULT), 0, 0, null); // 绘制缩小后的图
            //商品  banner图
            //读取互联网图片
            BufferedImage priductUrl = null;
            try {
                priductUrl = ImageIO.read(new URL(transformStyle(yxImageInfo.getImgUrl())));
            } catch (IOException e) {
                e.printStackTrace();
            }
            g.drawImage(priductUrl.getScaledInstance(750,590,Image.SCALE_DEFAULT),0,0,null);
            InputStream streamT =  getClass().getClassLoader()
                    .getResourceAsStream("Alibaba-PuHuiTi-Regular.otf");
            File newFileT = new File("Alibaba-PuHuiTi-Regular.otf");
            FileUtils.copyInputStreamToFile(streamT, newFileT);
            Font font =  Font.createFont(Font.TRUETYPE_FONT, newFileT);
            //文案标题
            g.setFont(font.deriveFont(Font.BOLD,34));
            g.setColor(new Color(29,29,29));
            int fontlenb = getWatermarkLength(yxStoreInfo.getStoreName(), g);
            //文字长度相对于图片宽度应该有多少行
            int lineb = fontlenb / (back.getWidth() +200);
            //高度
            int yb = back.getHeight() - (lineb + 1) * 30 + 100;
            //文字叠加,自动换行叠加
            int tempXb = 32;
            int tempYb = yb;
            //单字符长度
            int tempCharLenb = 0;
            //单行字符总长度临时计算
            int tempLineLenb = 0;
            StringBuffer sbb =new StringBuffer();
            for(int i=0; i < yxStoreInfo.getStoreName().length(); i++) {
                char tempChar = yxStoreInfo.getStoreName().charAt(i);
                tempCharLenb = getCharLen(tempChar, g);
                tempLineLenb += tempCharLenb;
                if(tempLineLenb >= (back.getWidth()+220)) {
                    //长度已经满一行,进行文字叠加
                    g.drawString(sbb.toString(), tempXb, tempYb + 50);
                    //清空内容,重新追加
                    sbb.delete(0, sbb.length());
                    //每行文字间距50
                    tempYb += 50;
                    tempLineLenb =0;
                }
                //追加字符
                sbb.append(tempChar);
            }
            g.drawString(sbb.toString(), tempXb, tempYb + 50);

            //------------------------------------------------文案-----------------------
            //文案
            g.setFont(font.deriveFont(Font.PLAIN,30));
            g.setColor(new Color(47,47,47));
            String storeInfo = yxStoreInfo.getStoreProvince()+yxStoreInfo.getStoreAddress();
            int fontlen = getWatermarkLength(storeInfo, g);
            //文字长度相对于图片宽度应该有多少行
            int line = fontlen / (back.getWidth() +200);
            //高度
            int y = tempYb + 50 - (line + 1) * 30 + 100;
            //文字叠加,自动换行叠加
            int tempX = 32;
            int tempY = y;
            //单字符长度
            int tempCharLen = 0;
            //单行字符总长度临时计算
            int tempLineLen = 0;
            StringBuffer sb =new StringBuffer();
            boolean tamp = true;
            for(int i=0; i < storeInfo.length(); i++) {
                char tempChar = storeInfo.charAt(i);
                tempCharLen = getCharLen(tempChar, g);
                tempLineLen += tempCharLen;
                if(tempLineLen >= (back.getWidth()+180)) {
                    //长度已经满一行,进行文字叠加
                    if(i==0){
                        g.drawString(sbb.toString(), tempXb, tempYb + 80);
                        tamp = false;
                    }
                    g.drawString(sb.toString(), tempX, tempY + 50);
                    //清空内容,重新追加
                    sb.delete(0, sb.length());
                    //每行文字间距50
                    tempY += 50;
                    tempLineLen =0;
                }
                //追加字符
                sb.append(tempChar);
            }
            //最后叠加余下的文字
            if (tamp){
                g.drawString(sbb.toString(), tempXb, tempYb + 80);
            }
            g.drawString(sb.toString(), tempX, tempY + 50);

            //生成二维码返回链接
            String url = shareCode;
            //读取互联网图片
            BufferedImage qrCode  = null;
            try {
                qrCode = ImageIO.read(new URL(url));
            } catch (IOException e) {
                log.error("二维码图片读取失败",e);
                e.printStackTrace();
            }
            // 绘制缩小后的图
            g.drawImage(qrCode.getScaledInstance(174, 174, Image.SCALE_DEFAULT), 536, 1057, null);

            //二维码字体
            g.setFont(font.deriveFont(Font.PLAIN,25));
            g.setColor(new Color(171,171,171));
            //绘制文字
            g.drawString("扫描或长按小程序码", 515, 1260);

            g.dispose();
            //先将画好的海报写到本地
            File file = new File(spreadPicPath);
            try {
                ImageIO.write(img, "jpg",file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            systemAttachmentService.attachmentAdd(spreadPicName,
                    String.valueOf(FileUtil.size(new File(spreadPicPath))),
                    spreadPicPath,"qrcode/"+spreadPicName);
            spreadUrl = apiUrl + "/file/qrcode/"+spreadPicName;
            //保存到本地 生成文件名字
        }else {
            spreadUrl = apiUrl + "/file/" + attachmentT.getSattDir();
        }

        return spreadUrl;
    }

    /**
     * 获取水印文字总长度
     *@paramwaterMarkContent水印的文字
     *@paramg
     *@return水印文字总长度
     */
    public static int getWatermarkLength(String waterMarkContent, Graphics g) {
        return g.getFontMetrics(g.getFont()).charsWidth(waterMarkContent.toCharArray(),0, waterMarkContent.length());
    }
    public static int getCharLen(char c, Graphics g) {
        return g.getFontMetrics(g.getFont()).charWidth(c);
    }
}
