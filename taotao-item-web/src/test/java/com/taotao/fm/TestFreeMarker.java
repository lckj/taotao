package com.taotao.fm;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.*;

public class TestFreeMarker {

//    @Test
    public void testFreeMarker() throws Exception{
        //1、创建一个模板
        //2、创建一个Configuration对象
        Configuration configuration = new Configuration(Configuration.getVersion());
        //3、设置模板所在路径(放模板的文件夹)
        configuration.setDirectoryForTemplateLoading(new File("D:\\workspace\\taotao\\taotao-item-web\\src\\main\\webapp\\WEB-INF\\ftl"));
        //4、设置模板的字符集
        configuration.setDefaultEncoding("UTF-8");
        //5、使用configuration加载一个模板，需要指定模板文件名称
        Template template = configuration.getTemplate("hello.ftl");
        //6、创建一个数据集一般为pojo或者map推荐使用map
        Map<String ,Object> data = new HashMap<>();
        data.put("hello","freemarker3");
        //7、创建一个writer对象，指定一个文件的输出路径及文件名
        Writer writer = new FileWriter(new File("D:\\temp\\freemarker\\html\\hello.html"));
        //8、使用template的process方法输出文件
        template.process(data,writer);
        //关闭资源
        writer.close();
        /***********************************************
         ****************pojo演示***********************
         ***********************************************/
        Template templateStu = configuration.getTemplate("student.ftl");
        Writer writerStu = new FileWriter(new File("D:\\temp\\freemarker\\html\\student.html"));
        Student student = new Student(9527,"苏东坡","nanjing",28);
        data.put("student",student);
        /***********************************************
         ****************pojoList演示***********************
         ***********************************************/
        List<Student> stuList = new ArrayList<>();
        stuList.add(new Student(9527,"苏东坡1","nanjing",28));
        stuList.add(new Student(9528,"苏东坡2","nanjing",28));
        stuList.add(new Student(9529,"苏东坡3","nanjing",28));
        stuList.add(new Student(9529,"苏东坡4","nanjing",28));
        stuList.add(new Student(9526,"苏东坡5","nanjing",28));
        data.put("stuList",stuList);

        /***********************************************
         ****************日期演示***********************
         ***********************************************/
        data.put("date",new Date());
        templateStu.process(data,writerStu);
        writerStu.close();
    }

}
