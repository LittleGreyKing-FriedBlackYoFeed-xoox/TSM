package com.tsm.user.mapper;

import com.tsm.auth.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 临时UserMapper - 用于解决包名冲突问题
 * 这是一个代理类，实际功能由com.tsm.auth.mapper.UserMapper提供
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    // 这个接口暂时为空，用于解决Spring Boot自动配置的包名冲突问题
}