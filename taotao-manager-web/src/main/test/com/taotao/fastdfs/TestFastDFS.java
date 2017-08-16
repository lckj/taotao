package com.taotao.fastdfs;

import com.taotao.utils.FastDFSClient;
import org.csource.fastdfs.*;

public class TestFastDFS {
    public void upLoadFile() throws Exception{
        //1、添加jar包
        //2、创建一个配置文件 配置tracker服务器地址
//        3、加载配置文件
        ClientGlobal.init("D:\\workspace\\taotao\\taotao-manager-web\\src\\main\\resources\\resource\\client.conf");
//        4、创建一个trackerClient对象
        TrackerClient trackerClient = new TrackerClient();
//        5、使用trackerClient对象获取trackerServer对象
        TrackerServer trackerServer = trackerClient.getConnection();
//        6、创建一个staoragerServer的引用
        StorageServer storageServer = null;
//        7、创建一个storagerClient
        StorageClient storageClient = new StorageClient(trackerServer,storageServer);
//        8、使用storagerClient上传文件
        String[] jpgs = storageClient.upload_file("C:\\Users\\wangcongjun\\Desktop\\image\\1.jpg", "jpg", null);
        for(String s:jpgs){
            System.out.println(s);
        }
    }

    public static void main(String[] args) throws Exception{
//        new TestFastDFS().upLoadFile();
        FastDFSClient fastDFSClient = new FastDFSClient("D:\\workspace\\taotao\\taotao-manager-web\\src\\main\\resources\\resource\\client.conf");
        String s = fastDFSClient.uploadFile("C:\\Users\\wangcongjun\\Desktop\\image\\2.jpg");
        System.out.println(s);
    }
}
