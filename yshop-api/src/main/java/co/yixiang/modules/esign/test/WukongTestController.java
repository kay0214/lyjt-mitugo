/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package co.yixiang.modules.esign.test;

import co.yixiang.common.web.controller.BaseController;
import co.yixiang.modules.esign.constant.IdentificationConstants;
import co.yixiang.modules.esign.util.WukongUtil;
import co.yixiang.modules.esign.vo.SignPDFCommonVO;
import com.alibaba.fastjson.JSONObject;
import com.timevale.esign.sdk.tech.bean.*;
import com.timevale.esign.sdk.tech.bean.result.FileCreateFromTemplateResult;
import com.timevale.esign.sdk.tech.bean.result.FileDigestSignResult;
import com.timevale.esign.sdk.tech.impl.constants.LicenseQueryType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * @author PC-LIUSHOUYI
 * @version WukongTestController, v0.1 2019/12/24 15:07
 */
@Slf4j
@Controller
@Api(value = "悟空sdk接口测试", tags = " 悟空sdk接口测试")
@RequestMapping("/wukong")
public class WukongTestController extends BaseController {

    @ResponseBody
    @GetMapping("/addPersonTemplateSeal")
    @ApiOperation(value = "个人用户生成签章", notes = "个人用户生成签章")
    public String addPersonTemplateSeal() {
        String result = WukongUtil.addPersonTemplateSeal("9D46AE6F5E7E4526A8EE18B77EF7B63B", IdentificationConstants.NATURALPERSON);
        log.info(result);
        return result;
    }

    @ResponseBody
    @GetMapping("/addPersonalAcct")
    @ApiOperation(value = "添加个人用户", notes = "添加个人用户")
    public String addPersonalAcct() {
        PersonBean personBean = new PersonBean();
        personBean.setName("汪勇勇");
        personBean.setIdNo("220000199410106134");
        String result = WukongUtil.addPersonalAcct(personBean);
        log.info(result);
        return result;
    }

    @ResponseBody
    @GetMapping("/addTemplatePerson")
    @ApiOperation(value = "生成假示例个人印章", notes = "生成假示例个人印章")
    public String addTemplatePerson() {

        PersonBean personBean = new PersonBean();
        personBean.setName("锺强");
        personBean.setIdNo("140000200011043143");
        String accountId = WukongUtil.addPersonalAcct(personBean);
        if (StringUtils.isBlank(accountId)) {
            return "失败1";
        }
        String sealData = WukongUtil.addPersonTemplateSeal(accountId, IdentificationConstants.NATURALPERSON);
        if (StringUtils.isBlank(sealData)) {
            return "失败2";
        }
        return "";
    }

    @ResponseBody
    @GetMapping("/updatePersonalAcct")
    @ApiOperation(value = "更新个人用户", notes = "更新个人用户")
    public String updatePersonalAcct() {
        UpdatePersonBean personBean = new UpdatePersonBean();
        personBean.setName("汪大勇");
        boolean result = WukongUtil.updatePersonalAcct("9D46AE6F5E7E4526A8EE18B77EF7B63B", personBean);
        return String.valueOf(result);
    }

    @ResponseBody
    @GetMapping("/updatePersonalAcct1")
    @ApiOperation(value = "更新个人用户", notes = "更新个人用户")
    public String updatePersonalAcct1() {
        UpdatePersonBean personBean = new UpdatePersonBean();
        personBean.setName("汪小勇");
        boolean result = WukongUtil.updatePersonalAcct("9D46AE6F5E7E4526A8EE18B77EF7B63B", personBean);
        return String.valueOf(result);
    }

    @ResponseBody
    @GetMapping("/addOrganizeAcct")
    @ApiOperation(value = "添加企业用户", notes = "添加企业用户")
    public String addOrganizeAcct() {
        OrganizeBean personBean = new OrganizeBean();
        personBean.setName("天之雨信息科技有限公司");
        personBean.setOrganCode("52227058XT51M4AL63");
        String result = WukongUtil.addOrganizeAcct(personBean);
        log.info(result);
        return result;
    }

    @ResponseBody
    @GetMapping("/updateOrganizeAcct")
    @ApiOperation(value = "更新企业用户", notes = "更新企业用户")
    public String updateOrganizeAcct() {
        UpdateOrganizeBean personBean = new UpdateOrganizeBean();
        personBean.setName("地之云信息科技有限公司");
        boolean result = WukongUtil.updateOrganizeAcct("0CF4268E78F3444490F7F16A55780B13", personBean);
        return String.valueOf(result);
    }

    @ResponseBody
    @GetMapping("/updateOrganizeAcct1")
    @ApiOperation(value = "更新企业用户", notes = "更新企业用户")
    public String updateOrganizeAcct1() {
        UpdateOrganizeBean personBean = new UpdateOrganizeBean();
        personBean.setName("人之云信息科技有限公司");
        boolean result = WukongUtil.updateOrganizeAcct("0CF4268E78F3444490F7F16A55780B13", personBean);
        return String.valueOf(result);
    }

    @ResponseBody
    @GetMapping("/addOrganizeTemplateSeal")
    @ApiOperation(value = "企业用户生成签章", notes = "企业用户生成签章")
    public String addOrganizeTemplateSeal() {
        String result = WukongUtil.addOrganizeTemplateSeal("0CF4268E78F3444490F7F16A55780B13", IdentificationConstants.LEGALPERSON);
        log.info(result);
        return result;
    }

    @ResponseBody
    @GetMapping("/getAccountInfoByIdNo/{idNo}/{idType}")
    @ApiOperation(value = "获取账户信息", notes = "获取账户信息")
    public String getAccountInfoByIdNo(@PathVariable String idNo, @PathVariable String idType) {
        if ("0".equals(idType)) {
            // 个人大陆身份证
            AccountProfile result = WukongUtil.getAccountInfoByIdNo(idNo, LicenseQueryType.MAINLAND);
            log.info(JSONObject.toJSONString(result));
            if (null == result) {
                return "未查询到数据！";
            }
            return JSONObject.toJSONString(result);
        } else {
            // 企业三证合一
            AccountProfile result = WukongUtil.getAccountInfoByIdNo(idNo, LicenseQueryType.MERGE);
            log.info(JSONObject.toJSONString(result));
            if (null == result) {
                return "未查询到数据！";
            }
            return JSONObject.toJSONString(result);
        }
    }

    @ResponseBody
    @GetMapping("/localSignPDF")
    @ApiOperation(value = "个人用户签章", notes = "用户签章")
    public String localSignPDF() {
        SignPDFCommonVO signPDFCommonVO = new SignPDFCommonVO();
        signPDFCommonVO.setAccountId("9D46AE6F5E7E4526A8EE18B77EF7B63B");
        signPDFCommonVO.setSealData("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");
        signPDFCommonVO.setSrcPdfFile("D:/pdfsign/after/SignAfter111.pdf");
        signPDFCommonVO.setDstPdfFile("D:/pdfsign/after/SignAfter222.pdf");
        signPDFCommonVO.setKey("自然人（签字）");
        signPDFCommonVO.setPosX(150);
        signPDFCommonVO.setPosY(20);
        signPDFCommonVO.setIsPerson(1);
        FileDigestSignResult result = WukongUtil.localSignPDF(signPDFCommonVO);
//        String result = WukongUtil.addPersonTemplateSeal("9D46AE6F5E7E4526A8EE18B77EF7B63B");
        log.info(JSONObject.toJSONString(result));
        return result.getSignServiceId();
    }


    @ResponseBody
    @GetMapping("/localSignPDF2")
    @ApiOperation(value = "企业用户签章", notes = "用户签章")
    public String localSignPDF2() {
        SignPDFCommonVO signPDFCommonVO = new SignPDFCommonVO();
        signPDFCommonVO.setAccountId("0CF4268E78F3444490F7F16A55780B13");
        signPDFCommonVO.setSealData("iVBORw0KGgoAAAANSUhEUgAAAcQAAAHECAMAAACQgaotAAAADFBMVEX/////AAAAAP8AAABvxgj3AAAAAXRSTlMAQObYZgAAGGNJREFUeNrtneua46gORb2J3/+V9znTna6KY7C5SEJg8mO+mZrECC22EBcDtpk/+Pk3PqOaj6sLF8RZKsEFcRrrudwwjd1czpjDZC6PzGEul1fmMJXLM3OYyeWdOWzkctAc9nE5aQbjuPzUyTIqPW1BVLOL+jXngqhmFMco6zkQ0cOjGBojRjWHXYvngthqDKc3YlSIcOQ7jIURI9lBbxZxQSyygj7t4oKYaQOXcc4hYnwn8eEQMUbu4BsjHJc+1GCMD4U4EkLXBsNnyWMuGPBhEDEiQa8YsRCObz58lTnKDjNfVYCnIofa7emoGlgIx68KFkKN6nBiiJgKoZsaYSFUqhVnhDgnQhcY8aAGO23l0LWY6V+b5zwQ/agQnBEjpm6lJ2bQLBbdqtmrCJsWGoG4zSdGzCvDGDEol92nwb6mlWG0aExZaujAkI42AIt/onUDxq1qRxn+lE3DPvGy5SoWuM9Ume4yfFcRWUnWELWFB4Tc7JVo3n4xnwxxURq6xgIOBrGXDG9OOLWDaNmKw0wM8c4CXUzJRnNwHc3sM8kwUQw6QY0lOCq2vOZiGLXgY5RmnbXCZMgYZmSoE8WkTmXBABCR2T0YhrXZKQYLhg7SDPR6AqlPURginDKUcRxqA4EyxaAuQwcMKeA4NBavSTFoM9zWJ9aQ4RWiX4bUqFubAfAJEcKhFPAvKB8Ud02G5b//Xv0b5eCFHIo4PZPelNjKMJ59QMgqCXdR/PdwBhHidfb0gUjAV6MY3DKUGdhBUUhutBh0GNbkAPyspqusBsk/wwVFCYhnh1Piie1DdPw+gA6EqEZx12inFHggExuOsg1pzol4L8Ty9cFznQRWGIMjhvxQIcVaKkQCjFiapqFFhZV9Sqgw0WzvW/lPEKZEjOGNECtKUdDiLt7QmxmyvoqSwxriY0BOwfIiw/5Gu1+eGJ4RQsiq2ufhN8eCYJcrPKcRHDB8b1L7758qS6gtq0iXfXN1g6VsHeFBhxeTY1W923EaFsd/r7+NiMkiOkew4IDh3QRB+zKQzswBO/1WFCIEpmmua9R/3gZbck2Xoi0AfSBCs3EdEHaXIiU2eOhRDGIMmyuECELWqFvwZd0fxZHfrYp+KAYxhpSs0Me8DSvrB9lZuC+MrrQYXDD8fiawCcxZo9m22NwD5N6tkqIYlFpVy++/ELK6esJpESmtRiGKQUaIMkkN0ipEsW3cRPaaMp6B0xPF4IUh0zm95WWzxZlqoy5FKAY/OtzeE3CkDA62uirR9eETnngrgRFEHYZsnDEo8gkEjAUkukeJ6gZHOkw8rLDNI+sraBIi3wMOJ91icNGULh6E+nyeqr2lWMhopxhahSg82YbUmL/t5m/WLYVcL6vQS4oaGr2sG0oP0zZN9vGcmeQnW1BWo/X2DP1Zbya1AMHR6I3hSLWIb5so0qq/9t2UVnTv24Yu6nJ0V8meKX78iGVmV5wgxK07xb1JiBrvWyC9Xy2jbsibysn+Na+Dg077LaW4u2JIJH2VL0VGf9Ry9AWcvx20O9Phdrl95baBJrpsVATlr1yIqkJsk+Luq00xnXXkAkjuJGMBvVhGQ5Oq11Dc3QlxuzggsTxBLX/LlOek6j+C2LQP/6unuDtjyKbJTsQGhEWmMv0/vB0hUDPYN9NhhFjl3Nt7d0yBChkfoCbmC3QDEBQgWvWKsTmTv/5lxhD9tBIps9HquFlKaydlLcXgUIjHwtr228j+Tn6zlAjFlz+GqA2kOA0pqrpWXP+hwyUUQko01eFveTK73tolQau610nRXZ/4ffhCPkKhG9mgIhZVisGlEH9fDCw69FVnfdowmlbWIRiVU/z8N8JoFhFf4ZNcqMobimwWjVkKIqx1+OspRhZlUykiDdQAdQ9QR4nmUxU8ja2PzLipZfo3C4rlDNFIEVJK7CLEr1OEzhSNxtxo0iFEy6+H2BJMUedoRv4Dd3/T74KqYqlBHNMfYgCQObb3c+yoRnEr3btxT1A/oAZVIVb/iJe78NWiOu7/Z1m5Nmls0GVIGT3E9pdRSYpMTCFULifSQIoWMzas/A1+I/HXm0hKWoSwDq0oBlUh1i4GnUcStEqPKcxQ8QgWRSUe0xhIOZRCLr1oqFDQYepnd8lekRR3SSGeNxxCVTgUuscLvKlic6MB4x1tbt2uN9wEpWD6Pq5N8FANRuKTzO7rf5uwdHR4DqhwfeEXz/tZ2CCQr39hZJOLdESlig4PFFG19RLV4bRGiD/24nN/WPm+M1wETOJnH6jISSQXL522H6NyqIdOhqMxxODv1CYbXPAzM6P0ynbRYEjktqmyOJovxaDSI6YOCjE8RJgiD5Y6y/9PgCo9U5ziSmSFGr8Hd5SSInUYMjEOED0tK5o6tM1E7HXzF2YzNyZhMf4GCMTsRm00ykSwawGQGCH+GR39STsolC7mDwPFmjCk2nF6rLjrCZFtTAmrCHE2V4eg2mzGLiNEbJdbxD4fUfJaRUSKckEuUcFj+gEpgHXn3hxfgGcpxIIogPtgijNiNjlG5QSkRDpeceBDao8nbJdBS0L5qU/BVTDN7dF+SeO7JVPdE/zOkOsdyqZEIYfDLhHMeX4B9K5DzH294qMNy12FKJ1CI/dphIoWISDEk7rugylzTaNshtcwb1Te2onYaVGUCBBZg31UtV8kg6mgUtxfc/s71cjUcUbCkeElNbj5WM3BXZJT/ETRC561BwnRHSUf/7flVkgUKBG17Q9b7MX59vUp9lUhir9LJsYorBV2sXlVfRB+reZFL4CNeXkqOmmvyYzv2VaBbvG+V3wJzjNEd8pe3TKnsN+6czyNHdEA0YBakNhU6Z+lHeL9yXdushjUqjZ29qrcPFDu34pHusyfuhHeZKHKj7VfROwvRTW/692CnBBjWsy7shPbAB/kfiFnVNHQepEFscWlvP5TKnEdguKNb3+zUtyFT+mXIV5Cqennr6/mYyOxFZuHixJv2n7mlluWKVForBhsIw+TMcW1GJnHkIyPDVNHeFJIihAW4tXIIDkLMER+k5/RILXTg9Xjp2so8hCTU2RXMzkYmeLtSlziiL/qFS7ehdP2BYPsXUqnN52GzG/Or22lpMJm1+YmNhBrmrh8cHRqBxhUhqepRej4tHbGpl6LuGYY1SXGZHieIIHiMBg3EEWXXz+FxduoOyDF5ODw4w/QX0Z76Uye3L23GZ/JxzZWSP3o6nDtAcrLLzOcNpTM66JRa67jrDSOUSftLkg3KFMO7xOB337SyfqTwOAQW+E0d/0gQ+30DBb3cvi5XHIgGTKey3z+QeNqN+RCFHglL7ZX7YIsx5FhxuBQPZ9JJDYQbqvvt4WBVGdxm+soj7D0B4c0qIjiYURMDRwYO+pypKFFPEzGBodqvQOSEOX36CJ+rOWZLLZhhIhUxsmM76h8NI8FY7oXjJClsnB0ZfiuAjRlyGKIlCkVlyE15zAn9xmN1eAw3TJfJhnAdpPfKB48Zds88O8aAZUK5bw4o/DayvfCb/y1bc3Dw4xnD3QnShOEgu7ohhe5zPfbhzN9bOsTlAMPv071ustSx46mnTaaBPPmGctvrIJcnwGkusCDdjjgaSrcWoxmzcN2cPhR9Eu/uSKRkh7/hCEliEiaZh9Tgn596SCkQv/JHTfsBevkKp3fDP1Bz02XHe5PNDxLwaKrhUVGc1270CEX7xFSMYkMo2XsPeT/XmW0u+XVpH30q0HoM99gM7CwHLj02FeCfn1icsg4HlAYtvuLz6tDo00PGUcdJbKD/Ppmp2Yh1aphdt+hF1uVtV64MRpgTLJSEhmbha4GTfPGft9P2Lr6kVZNdp5PzREo8/QnmF2JHfsObutT3dz+fnPvbzKxhHe0urRd7w4qvLRYxODc6PcnuYlj4Cs2cxfWBbzoCsOKj8UNc5c2asXGhiZWeYrmXmsDB2z7HuIp2vOCU6e4V7gf+bataCqKL1eJrDRlzZ1J0YNAYlM5quMIqYR1PK0QX40ayvvE5AEXnsQIp+VTpV1WJDZJWCukFokP1cO0b0fXZKe86BidjzDQ4X1klgeLQj/u24zT+3BRIC9MOd0m2BJ2d/WgfarqjNMBTO2FxiXCxBFDdRPgpo0VPTlqxdPECWBIeoJp6eoPMSqjFVpi/gi5KTMNuY1GFZ7ZBU3eCu7jxvxzrEj/KWNGJds///fkrt8e4WLc3a/c0wl3zEDPkjHGbt8c+7yI2THZybx7q37X6G62fZebo9XGfmSzGl2hl3ZFjXTZ5e4onvJ2+7LQwsbeqK/G4D5xND1iZD6+cvt3saBwiv0rdm4Fa+esDhbgrmeyV4Td8uK0WnmRemakp/PtdnvUmr4GRGSNgXrTqHwwBYuSDQa78Ettgh2hs+VJSuBLvAbWGOF38YZ8jbHEXGI8hKh4QHM9d4UaQe6gTYzFUGp+CtSHeFsDOYwYWIZlqwQNld5lKwtpjB4olsuQd1+RHebIr2JA1s7+IZUVTfhqipTiifeuUGfMJEZWNWEwTVD+o/G6988r3EJnDfU8YoTFtsZGEf8ckajJOdqUea5ciShS4+BiZN1vcKh5yVJA7k6kT49ALpwiilEopvahyPof4ntBv/wdFyoqMbfOPxhlKHbIb9jW6vDRR/IOX9PC8K5YN/6zr36ZpaMY2dzqrtJV3K0clhS/q1aOomLEQAw/7CVi3crlq94V2/hVXQG5ntEypMpdx83vM3oz5uba9tjoYRxKjJIIf53wTjgJ+bKElqJwuxmPIpdvYUCGB7sps/0I1hBFj97HkAyzplZr3WBzQB+dPis+56LCUHPe6SW1sA9DAcF5e0uFS8lX++yVGO8dHIqRmgz//kXB+OCrDXcuipsmwz9/0zA9mDmA7hSjytDS7t3SAaL5DXzKu8d9Na+BT12AY4amlQ/jMpQVtvBN2Kbr2CNDFL00V6412O9DGBqiuH6GS9NngCjjMHHtcLO+DRM925CHXGLMtygxkRI3gSX44T8TQGzbCzPD28wzQGwgMccL6VNAfPpnQVwQ12dB7D7KmOO83t3W1etkm9GVyHXz7AThlO93tiAv8afF08M82961+PUZMLFZXeJE2Sn8PA0L4pLi04YYD/igS2O1DqeUD2Lo+vN4pMHUENdHHeJK/5XDA6AD0TjXoJG7OsfTkotehg2nc2s+eaqCzsxj6FLByT9Up4i5Ehv4jAq0i6hc2al1RKVCqwl9qmfYKdruZzteb2lEMSRPB5gkmrJbL8zEkEKe4uThlFlvWEOP4mZBcW6I7BJSbylKN5zQ0bXa0fQTXZ8DBRIU2UoRj1FiwXFpMNYiBQtlT4gw1nufkPoXFzQpniGOlZ6iaGBBE4OYNaASpRiaKwchpRh0u2SHsHBJESpKrGDoULzs0nIqKMo4L7THj/+3p04csRUKritFiFFE7L/r3/iu+yWE3IkqTlCki8sLoqRO7Pv2epDzIpApSWhK4j4JpXloUBCjXDiNlJ5NUsll1qeTFfbSOhTPlzKwvd3dPAab/OG7hcZDBy0yTl+On8rP2or/98NXc1NAfMyTVqQUw1TC13GqARn/E/c6Ki1G+JRF6OASZyh6PUO6moexf+rmJZbeOhFvBk0HEmG7vbeDJkKk8e8zVR1drmVOpM0eEpwhVjiDBS6SU2fz9RIQgnhFMdm+Wd8zJiA2SpH5jd0Rw03uqof8oWdqZFgiRh2IZes8Qr2kyJl0ahRZ6ixkW3G2+SWQrEHkKw2lUtX20iex7QFAceuNQISSI35ujIYfhpunxTfkWYSsH5bluiw42kTwqh1IPUjwSSKWFIVTNkLEZ7ZSPc3T3JtJTv04oMgyB/79erBuv8cJahcM/bweUrqF5P31UNlL4Fgs6iylcaXdU6z7hLpqHO+NZ6XP6Mb3lN25ZPypmzttvcWxedAvPytre2NxfTIFMYhnA1AczeHs7G4Lirj4C+tdgJq5i3MqDPNLBDjMU4uGoxUDjA8lljbErrkAdErXEiMKv8rCR70qSop0SFgHb5Q5npX9ZAFE3BnFhsb2cIosde1tNK1JbKQPEJ7/QGJmJ6Os6ihwA1mdhMSd84NIkVqalnorqnH95QHn2ii209CcT9lncGNypErLEFXi5uu0Q59a1HJxSJSDCivRZhUWw8pP30Nrf4a2UBzCz98ugmBzYf2vOL0WNTusINZFVfTcH1M/i6J8dmrVsH7JL4qVg8RviDRneCp8URRVImwZjksRjiFaM1wUa6PpF0T2ZLgoKigR1gwXxcrRXKdwytRxQYtiRRHhgjDMmlKvBHmSiLr3rXakXxxOhh0s5nWfaKUEpHYEDThQNNl4jNohBrqYNF5M7b19HPmjEflSP9OYj9N6BuwXod32LrGESxVY7MM8MXSIKO/l3W7G773Vf2ZIr0pLBie8sxu9BdHr+Bg6OYQcieF2y7Brvxg263j6jxTHZogjw67ZTeijw0NfMwxDxk6Hwue9QhZnxjEHoqoUP0h9vWrqmCGj3sORYUcthn46HIZhSgHYTkfwbdpH9kR9FDZLKR5IjdYfxt4Qje1OwNxKHJZh9iFfKhRv51/CZifFgXXIlDOjuxOsbydAHXsZhsf/iL86BCdwkWjdUifsNwgx62I4GjLMD2WPoXjP4lUiUE2GWnMLsjU4H4+dOhEDKgyLvgJpMWQxjFlEXxTttZgRFINNDMtnOGB2g7jbYSXEzLvgacfwXyt2+Rp4lhZhnNYk+0TZ7qmCodMuMqNfPDFsurIn66d5ENGF4ZDZDVL3JegJ8aIImVHG97mkgzO8jajRW0KkLmtIPScIqvouJ5iB4U12I8wwM9sM5T95NMNritIM0fw1kQT1sDllBoZXEVWXIatYi98BMwfDNEXpWOoIImOPGplh9tyNEcOsG/8aKIIXJQ3L0GjUn+3+l2ZOGq82IiOswRhGxotIBSF9hvcnmwrlq5EF4Y+nDscwNW/PHgyvlSg3bxPNSzFoLE3N3RyW9GF4h9VL7EnFDL9cMdxrNLjyjzBDtkCUkeIk48MCiu3zNCW+DmpPvmYIDM/wYmsbmt+fLcpGQo6hKrEUozNMU0zpMH9RqiyjDFsRRYgxPIwwRj1uIU4R6euEdSqqvnn4oj/E6AzjFNF+Nkjh0A7iTyyJpdvgDBN5GltznUKXh8zWVhlQc/ZEjX3e8HebhMCYo1Q2wa6ZHv6DJGdgGJ1KNGaYJ6zagHreFjzF+PC2mmwbNxa7O1TanPO9c/iMvGk7wSm1/Mq1s/NVKcmEbCvrWN8wpP0rRJoUyeacpqZVh62cIgqrd8WQU4jx8xAJUpIh5SC2N5ZkfzjHXRiH7IasZ1gVA0PVw1BhQyqnmZBiA8MqfQTRJlHBcHaKTQwpTL748bjZdT7dnULRuQwLhvl9YnlAfRjD0nlUwej3qm0maGqwc95cGts9xa1aiOJ94iZ7BeCc97NF5lEbGFIDokizmZlhZB7VhGERRLZTnJvhac8CG5+kocRmirMz/JpH5VYvxE0L4uoP8yk2MtTk31AUphrbS1e06eCXUNHOmkLA/PcGU19LjeG0leIT7n5mtXgrn9DaJ6Ksag+6vxt2rSC0FoClQ2nexV56NRdZMIW+dKjQITaN2ItbDpYONTpEIYgrSvZlWCflRdEVw7rsdDFzxbC2U8XC6odh28u/i6ILhtWDfQo1hsWw63OWFsV9X+vC+mm3pUXpD3soemnRQ4fYqp9F0YfzRCEuin1ch21RHJ1hazqyKHpwG7ZFcXSGCmtZi6K5yyBv0sJo3ezb951SoWE8iqGPRy4tdnYWtkVx7Fi66V3ytygapoHYFsWhQ6lkN7sodvQRtkVxdIaSCe/C2Ms92BbF0RnKDj0XxQI3c/MJUdnWxdAE4qLYxSvYFsXBESpMx8YeyMVQ1R/YFsXRGVotjHAhVPQFxjV9MVSFuCjaugHboriN7gOMX4XHM1TcDxN9Mp+MUK322BbF4auOieryVIbK2wsfSdG+0uhQoakx9qgxZqzUwxga7NZ+EsVOdUWvmk2IsVtFMXXtnsHQ6OWXB1DsWUU8oI4d3chtJoi96zl11dC/qqNj7N864aG6I2P0UCc8r8rz1Qdeaj0iRi9NEn5qPhpGPxWBp8oPhNFVJfB4D0xQAbjzgn+O7oyHR0945ujRbix3iHmLPs16pk/qXEW3lj3XMSNZCufO8cHRuZFYHmp2Eb170IkRXJZ5h5hnBh0axWG858gQujKHQ/nOlSledgByOM85s4bTt6RxIZYZxG6e4LA+82oTRyvvERCrrKLfYh4Ksc0wqtWY0/nqabZxOWpw67jcNLiFXC4a20ou94xtKJdvxjaWyy9jG8zlk7GN5vLHyKZzeWLkGnC5YOB68MmVH7lOE56g8z/5q1ykK2ih2wAAAABJRU5ErkJggg==");
        signPDFCommonVO.setSrcPdfFile("D:/pdfsign/after/SignAfter222.pdf");
        signPDFCommonVO.setDstPdfFile("D:/pdfsign/after/SignAfter333.pdf");
        signPDFCommonVO.setKey("挂牌方（盖章）");
        signPDFCommonVO.setPosX(200);
        signPDFCommonVO.setPosY(20);
        signPDFCommonVO.setIsPerson(0);
        FileDigestSignResult result = WukongUtil.localSignPDF(signPDFCommonVO);
//        String result = WukongUtil.addPersonTemplateSeal("9D46AE6F5E7E4526A8EE18B77EF7B63B");
        log.info(JSONObject.toJSONString(result));
        return result.getSignServiceId();
    }

    @ResponseBody
    @GetMapping("/createFileFromTemplate")
    @ApiOperation(value = "模板填充", notes = "模板填充")
    public String createFileFromTemplate() {
        String srcPdfFile = "D:/pdfsign/汇富亨通5号认购协议1.pdf";
        String dstPdfFile = "D:/pdfsign/after/SignAfter111.pdf";
        Map<String, Object> txtFields = new HashMap<>();// 模板中包含待填充文本域时，文本域Key-Value组合
        txtFields.put("客户姓名1", "某某某");
        txtFields.put("开户行", "中国某一个银行某一个支银行");
        txtFields.put("银行账户", "6666225258888888");
        txtFields.put("客户姓名2", "某某二");
        txtFields.put("身份证号码", "370902199911213358");
        txtFields.put("认购金额", "214552.00");

        FileCreateFromTemplateResult result = WukongUtil.createFileFromTemplate(srcPdfFile, dstPdfFile, txtFields);
        log.info(JSONObject.toJSONString(result));
        return result.getDstPdfFile();
    }
}
