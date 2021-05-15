package org.example.dao;

import org.example.model.Image;
import org.example.util.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ImageDAO {
    public static int queryCount(String md5) {
        Connection c = null;
        PreparedStatement ps =null;
        ResultSet rs = null;
        try {
            //1.获取数据库连接Connection
            c = Util.getConnection();
            //2.获取操作命令对象Statement （Connection+sql）
            String sql = "select count(0) c from image_table where md5=?";
            ps = c.prepareStatement(sql);
            //3.执行sql:执行前替换占位符
            ps.setString(1,md5);
            rs = ps.executeQuery();
            //4.如果是查询语句，需要处理结果集ResultSet
            rs.next();
            return rs.getInt("c");
        } catch (SQLException e) {
            throw new RuntimeException("查询上传图片md5失败!"+md5,e);
        }finally {
            //5.释放资源
            Util.close(c,ps,rs);
        }
    }

    //插入图片操作
    public static int insert(Image image) {
        Connection c = null;
        PreparedStatement ps =null;
        try {
            //1.获取数据库连接Connection
            c = Util.getConnection();
            //2.获取操作命令对象Statement （Connection+sql）
            String sql = "insert into image_table values(null,?,?,?,?,?,?)";
            ps = c.prepareStatement(sql);
            //3.执行sql:执行前替换占位符
            ps.setString(1,image.getImageName());
            ps.setLong(2,image.getSize());
            ps.setString(3,image.getUploadTime());
            ps.setString(4,image.getMd5());
            ps.setString(5,image.getContentType());
            ps.setString(6,image.getPath());
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("新增上传图片出错!",e);
        }finally {
            //5.释放资源
            Util.close(c,ps);
        }
    }

    //查询所有图片
    public static List<Image> queryAll() {
        Connection c = null;
        PreparedStatement ps =null;
        ResultSet rs = null;
        try {
            //1.获取数据库连接Connection
            c = Util.getConnection();
            //2.获取操作命令对象Statement （Connection+sql）
            String sql = "select *from image_table";
            ps = c.prepareStatement(sql);
            //3.执行sql:执行前替换占位符
            rs = ps.executeQuery();
            //4.如果是查询语句，需要处理结果集ResultSet
            List<Image> list = new ArrayList<>();
            while (rs.next()){
                Image image = new Image();
                image.setImageId(rs.getInt("image_id"));
                image.setImageName(rs.getString("image_name"));
                image.setContentType(rs.getString("content_type"));
                image.setMd5(rs.getString("md5"));
                list.add(image);
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException("查询所有图片出错!",e);
        }finally {
            //5.释放资源
            Util.close(c,ps,rs);
        }
    }

    //查询单个图片
    public static Image queryOne(int id) {
        Connection c = null;
        PreparedStatement ps =null;
        ResultSet rs = null;
        try {
            //1.获取数据库连接Connection
            c = Util.getConnection();
            //2.获取操作命令对象Statement （Connection+sql）
            String sql = "select *from image_table where image_id=?";
            ps = c.prepareStatement(sql);
            //3.执行sql:执行前替换占位符
            ps.setInt(1,id);
            rs = ps.executeQuery();
            //4.如果是查询语句，需要处理结果集ResultSet
            Image image = null;
            while (rs.next()){
                image = new Image();
                image.setImageId(rs.getInt("image_id"));
                image.setImageName(rs.getString("image_name"));
                image.setContentType(rs.getString("content_type"));
                image.setMd5(rs.getString("md5"));
                image.setPath(rs.getString("path"));
            }
            return image;
        } catch (SQLException e) {
            throw new RuntimeException("查询单个图片出错!",e);
        }finally {
            //5.释放资源
            Util.close(c,ps,rs);
        }
    }

    //删除图片
    public static int delete(int imageId) {
         // 1. 创建数据库连接
        Connection connection = Util.getConnection();
        PreparedStatement statement = null;
        String sql = "delete from image_table where image_id=?";
        try {
            //设置不自动提交(开启事务)
            connection.setAutoCommit(false);
            // 2. 执行 SQL 语句
            statement = connection.prepareStatement(sql);
            statement.setInt(1, imageId);
            int n =  statement.executeUpdate();
            //TODO:删除本地图片文件
            //提交
            connection.commit();
            return n;
        } catch (Exception e) {
            try {
                //执行出错，回滚操作
                connection.rollback();
            } catch (SQLException se) {
                throw new RuntimeException("删除图片回滚出错"+imageId,e);
            }
            throw new RuntimeException("删除图片出错"+imageId,e);
        } finally {
            Util.close(connection, statement);
        }
    }
}
