package com.tsm.user.mapper;

import com.tsm.user.entity.User;
import com.tsm.user.entity.UserRole;
import com.tsm.user.dto.UserQueryDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 用户Mapper接口
 * @author TSM
 */
@Mapper
public interface UserMapper {
    
    /**
     * 根据ID查询用户
     * @param id 用户ID
     * @return 用户信息
     */
    @Select("SELECT * FROM tsm_user WHERE id = #{id}")
    User selectById(Long id);
    
    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户信息
     */
    @Select("SELECT * FROM tsm_user WHERE username = #{username}")
    User selectByUsername(String username);
    
    /**
     * 根据邮箱查询用户
     * @param email 邮箱
     * @return 用户信息
     */
    @Select("SELECT * FROM tsm_user WHERE email = #{email}")
    User selectByEmail(String email);
    
    /**
     * 根据手机号查询用户
     * @param phone 手机号
     * @return 用户信息
     */
    @Select("SELECT * FROM tsm_user WHERE phone = #{phone}")
    User selectByPhone(String phone);
    
    /**
     * 分页查询用户列表
     * @param queryDTO 查询条件
     * @return 用户列表
     */
    @Select("<script>" +
            "SELECT * FROM tsm_user WHERE 1=1" +
            "<if test='username != null and username != \"\"\'>"+
            " AND username LIKE CONCAT('%', #{username}, '%')" +
            "</if>" +
            "<if test='realName != null and realName != \"\"\'>"+
            " AND real_name LIKE CONCAT('%', #{realName}, '%')" +
            "</if>" +
            "<if test='email != null and email != \"\"\'>"+
            " AND email LIKE CONCAT('%', #{email}, '%')" +
            "</if>" +
            "<if test='status != null\'>"+
            " AND status = #{status}" +
            "</if>" +
            " ORDER BY create_time DESC" +
            " LIMIT #{offset}, #{pageSize}" +
            "</script>")
    List<User> selectUserPage(UserQueryDTO queryDTO);
    
    /**
     * 查询用户总数
     * @param queryDTO 查询条件
     * @return 总数
     */
    @Select("<script>" +
            "SELECT COUNT(*) FROM tsm_user WHERE 1=1" +
            "<if test='username != null and username != \"\"\'>"+
            " AND username LIKE CONCAT('%', #{username}, '%')" +
            "</if>" +
            "<if test='realName != null and realName != \"\"\'>"+
            " AND real_name LIKE CONCAT('%', #{realName}, '%')" +
            "</if>" +
            "<if test='email != null and email != \"\"\'>"+
            " AND email LIKE CONCAT('%', #{email}, '%')" +
            "</if>" +
            "<if test='status != null\'>"+
            " AND status = #{status}" +
            "</if>" +
            "</script>")
    long countUserPage(UserQueryDTO queryDTO);
    
    /**
     * 新增用户
     * @param user 用户信息
     * @return 影响行数
     */
    @Insert("INSERT INTO tsm_user (username, password, email, phone, real_name, status, create_time, update_time) " +
            "VALUES (#{username}, #{password}, #{email}, #{phone}, #{realName}, #{status}, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(User user);
    
    /**
     * 更新用户
     * @param user 用户信息
     * @return 影响行数
     */
    @Update("<script>" +
            "UPDATE tsm_user SET update_time = NOW()" +
            "<if test='username != null\'>, username = #{username}</if>" +
            "<if test='password != null\'>, password = #{password}</if>" +
            "<if test='email != null\'>, email = #{email}</if>" +
            "<if test='phone != null\'>, phone = #{phone}</if>" +
            "<if test='realName != null\'>, real_name = #{realName}</if>" +
            "<if test='status != null\'>, status = #{status}</if>" +
            " WHERE id = #{id}" +
            "</script>")
    int update(User user);
    
    /**
     * 删除用户
     * @param id 用户ID
     * @return 影响行数
     */
    @Delete("DELETE FROM tsm_user WHERE id = #{id}")
    int deleteById(Long id);
    
    /**
     * 批量删除用户
     * @param ids 用户ID列表
     * @return 影响行数
     */
    @Delete("<script>" +
            "DELETE FROM tsm_user WHERE id IN" +
            "<foreach collection='list' item='id' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach>" +
            "</script>")
    int batchDeleteByIds(List<Long> ids);
    
    /**
     * 更新用户状态
     * @param id 用户ID
     * @param status 状态
     * @return 影响行数
     */
    @Update("UPDATE tsm_user SET status = #{status}, update_time = NOW() WHERE id = #{id}")
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);
    
    /**
     * 重置用户密码
     * @param id 用户ID
     * @param password 新密码
     * @return 影响行数
     */
    @Update("UPDATE tsm_user SET password = #{password}, update_time = NOW() WHERE id = #{id}")
    int resetPassword(@Param("id") Long id, @Param("password") String password);
    
    /**
     * 查询用户角色关联
     * @param userId 用户ID
     * @return 角色关联列表
     */
    @Select("SELECT * FROM tsm_user_role WHERE user_id = #{userId}")
    List<UserRole> selectUserRoles(Long userId);
    
    /**
     * 删除用户角色关联
     * @param userId 用户ID
     * @return 影响行数
     */
    @Delete("DELETE FROM tsm_user_role WHERE user_id = #{userId}")
    int deleteUserRoles(Long userId);
    
    /**
     * 批量插入用户角色关联
     * @param userRoles 用户角色关联列表
     * @return 影响行数
     */
    @Insert("<script>" +
            "INSERT INTO tsm_user_role (user_id, role_id, create_time) VALUES" +
            "<foreach collection='list' item='item' separator=','>" +
            "(#{item.userId}, #{item.roleId}, NOW())" +
            "</foreach>" +
            "</script>")
    int batchInsertUserRoles(List<UserRole> userRoles);
    
    /**
     * 检查用户名是否存在（排除指定ID）
     * @param username 用户名
     * @param excludeId 排除的用户ID
     * @return 数量
     */
    @Select("<script>" +
            "SELECT COUNT(*) FROM tsm_user WHERE username = #{username}" +
            "<if test='excludeId != null\'> AND id != #{excludeId}</if>" +
            "</script>")
    int countByUsername(@Param("username") String username, @Param("excludeId") Long excludeId);
    
    /**
     * 检查邮箱是否存在（排除指定ID）
     * @param email 邮箱
     * @param excludeId 排除的用户ID
     * @return 数量
     */
    @Select("<script>" +
            "SELECT COUNT(*) FROM tsm_user WHERE email = #{email}" +
            "<if test='excludeId != null\'> AND id != #{excludeId}</if>" +
            "</script>")
    int countByEmail(@Param("email") String email, @Param("excludeId") Long excludeId);
    
    /**
     * 检查手机号是否存在（排除指定ID）
     * @param phone 手机号
     * @param excludeId 排除的用户ID
     * @return 数量
     */
    @Select("<script>" +
            "SELECT COUNT(*) FROM tsm_user WHERE phone = #{phone}" +
            "<if test='excludeId != null\'> AND id != #{excludeId}</if>" +
            "</script>")
    int countByPhone(@Param("phone") String phone, @Param("excludeId") Long excludeId);
    
    /**
     * 获取用户统计信息
     * @return 统计信息
     */
    @Select("SELECT " +
            "COUNT(*) as totalUsers, " +
            "SUM(CASE WHEN status = 1 THEN 1 ELSE 0 END) as enabledUsers, " +
            "SUM(CASE WHEN status = 0 THEN 1 ELSE 0 END) as disabledUsers " +
            "FROM tsm_user")
    @Results({
            @Result(property = "totalUsers", column = "totalUsers"),
            @Result(property = "enabledUsers", column = "enabledUsers"),
            @Result(property = "disabledUsers", column = "disabledUsers")
    })
    java.util.Map<String, Object> getUserStatistics();
}