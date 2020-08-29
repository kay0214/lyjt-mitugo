package co.yixiang.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CurrUser implements Serializable {

    private  Long id;

    private  String username;

    private  String nickName;

    private  String sex;

    private  String avatar;

    private  String email;

    private  String phone;

    private  String dept;

    private  String job;

    private  Integer userRole;

    private  List<Long> childUser;
}
