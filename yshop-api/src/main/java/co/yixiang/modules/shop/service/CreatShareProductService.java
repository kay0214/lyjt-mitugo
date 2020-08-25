package co.yixiang.modules.shop.service;

import co.yixiang.modules.shop.entity.ProductInfo;

import java.awt.*;
import java.io.IOException;

public interface CreatShareProductService {

    String creatProductPic(ProductInfo product, String qrcode, String spreadPicName, String spreadPicPath, String apiUrl) throws IOException, FontFormatException;
}
