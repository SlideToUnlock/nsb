package com.nsb.dao;

import com.nsb.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_info
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_info
     *
     * @mbggenerated
     */
    int insert(User record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_info
     *
     * @mbggenerated
     */
    int insertSelective(User record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_info
     *
     * @mbggenerated
     */
    User selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_info
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(User record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_info
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(User record);

    User checkLogin(@Param("username")String username, @Param("password")String password);

    int checkAnswer(@Param("username")String username, @Param("answer")String answer);

    int resetPassword(@Param("username")String username, @Param("answer")String answer, @Param("passwordNew")String passwordNew);

    int updatePassword(@Param("username")String username, @Param("passwordOld")String passwordOld, @Param("passwordNew") String passwordNew);

    int checkUsername(String username);

    int deleteUser(String username);

    int checkRole(@Param("username") String username, @Param("role") int role);

    List<User> getUsers();
}