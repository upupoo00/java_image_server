package org.example.servlet;

import org.example.dao.ImageDAO;
import org.example.model.Image;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

@WebServlet("/imageShow")
public class ImageShowServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //1.解析请求数据：imageId
        String id = req.getParameter("imageId");
        //2.业务处理：（1）根据id查图片path字段 （2）通过path找本地图片文件
        Image image = ImageDAO.queryOne(Integer.parseInt(id));
        //图片是以二进制放在body，同时要指定Content-Type
        resp.setContentType(image.getContentType());
        //本地图片的绝对路径
        String path = ImageServlet.IMAGE_DIR + image.getPath();
        //io输入流读数据
        FileInputStream fis = new FileInputStream(path);
        //3.返回响应数据：服务器本地图片的二进制数据
        OutputStream os = resp.getOutputStream();//输出流都是输出到body
        byte[] bytes = new byte[1024*16];
        int len;
        while ((len=fis.read(bytes)) != -1){
            os.write(bytes,0,len);
        }
        os.flush();
        fis.close();
        os.close();
    }
}
