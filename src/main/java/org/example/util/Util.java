package org.example.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class Util {

    private static final ObjectMapper M = new ObjectMapper();

    private static final MysqlDataSource DS = new MysqlDataSource();

    //TODO 单例模式改造
    static {
        DS.setURL("jdbc:mysql://127.0.0.1:3306/image_system");
        DS.setUser("root");
        DS.setPassword("000524");
        DS.setUseSSL(false);
        DS.setCharacterEncoding("UTF-8");
    }

    /**
     * 吧java对象序列化为json字符串(Servlet响应输出的body需要json字符串)
     */
    public static String serialize(Object o){
        try {
            return M.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw  new RuntimeException("序列化Java对象失败: "+o,e);
        }
    }

    public static Connection getConnection(){
        try {
            return DS.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("数据库连接失败: ",e);
        }
    }
    //统一的关闭方法
    public static void close(Connection connection,
                             PreparedStatement statement,
                             ResultSet resultSet)  {
        try {
            if(resultSet!=null) resultSet.close();
            if(statement!=null) statement.close();
            if(connection!=null) connection.close();
        } catch (SQLException e) {
            throw new RuntimeException("释放数据库资源失败: ",e);
        }
    }
    //没有结果集的关闭方法
    public static void close(Connection connection, PreparedStatement statement)  {
        close(connection,statement,null);
    }

}
