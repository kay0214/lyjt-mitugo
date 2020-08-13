package co.yixiang.modules.manage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import co.yixiang.common.entity.BaseEntity;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * <p>
 * 商户详情
 * </p>
 *
 * @author hupeng
 * @since 2020-08-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="YxMerchantsDetail对象", description="商户详情")
public class YxMerchantsDetail extends BaseEntity {

    private static final long serialVersionUID = 1L;

@TableId(value = "id", type = IdType.AUTO)
private Integer id;

@ApiModelProperty(value = "用户id")
private Integer uid;

@ApiModelProperty(value = "审批状态：0->待审核,1->通过,2->驳回")
private Integer examineStatus;

@ApiModelProperty(value = "商户地址")
private String address;

@ApiModelProperty(value = "联系人")
private String contacts;

@ApiModelProperty(value = "联系电话")
private String contactMobile;

@ApiModelProperty(value = "邮箱")
private String mailbox;

@ApiModelProperty(value = "认证类型：0->个人,1->企业,2->个体商户")
private Integer merchantsType;

@ApiModelProperty(value = "手持证件照")
private String personIdcardImg;

@ApiModelProperty(value = "证件照人像面")
private String personIdcardImgFace;

@ApiModelProperty(value = "证件照国徽面")
private String personIdcardImgBack;

@ApiModelProperty(value = "银行账号")
private String bankNo;

@ApiModelProperty(value = "开户省市")
private String openAccountProvince;

@ApiModelProperty(value = "银行卡信息：0->对私账号,1->对公账号")
private Integer bankType;

@ApiModelProperty(value = "开户名称")
private String openAccountName;

@ApiModelProperty(value = "开户行")
private String openAccountBank;

@ApiModelProperty(value = "开户支行")
private String openAccountSubbranch;

@ApiModelProperty(value = "企业所在省市区")
private String companyProvince;

@ApiModelProperty(value = "企业所在详细地址")
private String companyAddress;

@ApiModelProperty(value = "公司名称")
private String companyName;

@ApiModelProperty(value = "法定代表人")
private String companyLegalPerson;

@ApiModelProperty(value = "公司名称")
private String companyPhone;

@ApiModelProperty(value = "经营类目")
private Integer businessCategory;

@ApiModelProperty(value = "主体资质类型")
private Integer qualificationsType;

@ApiModelProperty(value = "营业执照")
private String businessLicenseImg;

@ApiModelProperty(value = "银行开户证明")
private String bankOpenProveImg;

@ApiModelProperty(value = " 法人身份证头像面")
private String legalIdcardImgFace;

@ApiModelProperty(value = "法人身份证国徽面")
private String legalIdcardImgBack;

@ApiModelProperty(value = "门店照及经营场所")
private String storeImg;

@ApiModelProperty(value = "医疗机构许可证")
private String licenceImg;

@ApiModelProperty(value = "是否删除（0：未删除，1：已删除）")
private Integer delFlag;

@ApiModelProperty(value = "创建人")
private Integer createUserId;

@ApiModelProperty(value = "修改人")
private Integer updateUserId;

@ApiModelProperty(value = "创建时间")
private Date createTime;

@ApiModelProperty(value = "更新时间")
private Date updateTime;

}
