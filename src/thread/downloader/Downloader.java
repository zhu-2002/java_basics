package thread.downloader;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Downloader {
    private int threadNum ;
    /**
     * 下载单个文件保存到本地
     * @param source 原图片的网址
     * @param targetDir 目标目录，要确保已存在
     */
    public void downloader(String source,String targetDir){
        InputStream is = null ;
        OutputStream os = null ;
        try {
            String fileName = source.substring(source.lastIndexOf("/")+1) ;
            File tarFile = new File( targetDir+"/"+fileName) ;
            if( !tarFile.exists()){
                tarFile.createNewFile() ;
            }
            URL url = new URL(source) ;
            URLConnection connection = url.openConnection() ;
            is = connection.getInputStream() ;
            os = new FileOutputStream(tarFile) ;
            byte[] bs = new byte[1024] ;
            int len =  0 ;
            while ( (len = is.read(bs)) != -1 ){
                os.write(bs,0,len);
            }
            System.out.println("[INFO]图片下载完毕"+source+"\n\t ->"+tarFile.getParent()+"("+Math.round(tarFile.length()/1024)+"kb)");
        }catch ( IOException e ){
            e.printStackTrace();
        }finally {
            try {
                if( is != null ){
                    is.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
            try {
                if( os != null ){
                    os.close();
                }
            }catch ( IOException e ){
                e.printStackTrace();
            }
        }
    }

    /**
     * 从指定文件中读取下载地址，批量下载网络资源
     * @param tarDir 下载文件的存储目录
     * @param downloadTxt download.txt完整目录
     */
    public void multiDownloaderFromFile(String tarDir,String downloadTxt){
        File dir = new File(tarDir) ;
        if ( !dir.exists() ){
            dir.mkdirs() ;
            System.out.println("[INFO]发现下载目录["+dir.getPath()+"]不存在，已自动创建");
        }
        List<String> resources = new ArrayList<>() ;
        BufferedReader reader = null ;
        ExecutorService threadPool = null ;
        try {
            reader = new BufferedReader(new FileReader(downloadTxt)) ;
            String line = null ;
            while ( (line = reader.readLine())!= null ){
                resources.add(line) ;
                System.out.println(line);
            }
            threadPool = Executors.newFixedThreadPool(this.threadNum) ;
            Downloader that = this  ;
            for( String res : resources ){
                threadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        that.downloader(res,tarDir);
                    }
                });
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if ( reader != null ){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if ( threadPool != null ){
                threadPool.shutdown();
            }
        }
    }
    /**
     * 启动多线程下载
     * @param propDir config.properties目录地址
     */
    public void start ( String propDir){
        File proFile = new File(propDir+"/config.properties") ;
        Properties properties = new Properties() ;
        Reader reader = null ;
        try {
            reader = new FileReader(proFile) ;
            properties.load(reader);
            String threadNum = properties.getProperty("thread-num") ;
            this.threadNum = Integer.parseInt(threadNum) ;
            String tarDir = properties.getProperty("tar-dir") ;
            this.multiDownloaderFromFile(tarDir,propDir+"/download.txt") ;
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if ( reader != null ){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static void main(String[] args) {
        Downloader downloader = new Downloader() ;
        downloader.start("./src/thread/downloader");
    }
}
