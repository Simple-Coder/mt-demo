package com.mt.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilters;
import org.apache.commons.net.ftp.FTPReply;

public class FTPUtil {
    private FTPClient ftpClient;
    public static final int BINARY_FILE_TYPE = FTP.BINARY_FILE_TYPE;
    public static final int ASCII_FILE_TYPE = FTP.ASCII_FILE_TYPE;

    public FTPUtil(FTPClient ftpClient) {
        this.ftpClient = ftpClient;
    }

    /**
     * 查看当前状态
     *
     * @throws IOException
     */
    public String getStatus() throws IOException {
        return ftpClient.getStatus();
    }

    /**
     * 断开连接
     *
     * @throws IOException
     */
    public void disconnect() throws IOException {
        ftpClient.disconnect();
    }

    /**
     * 设置传输文件类型:FTP.BINARY_FILE_TYPE | FTP.ASCII_FILE_TYPE
     * 二进制文件或文本文件
     *
     * @param fileType
     * @throws IOException
     */
    public void setFileType(int fileType) throws IOException {
        ftpClient.setFileType(fileType);
    }

    /**
     * 关闭连接
     *
     * @throws IOException
     */
    public void closeServer() throws IOException {
        if (ftpClient != null && ftpClient.isConnected()) {
            ftpClient.logout();//退出FTP服务器
            ftpClient.disconnect();//关闭FTP连接 
        }
    }

    /**
     * 转移到FTP服务器工作目录
     *
     * @param path
     * @return
     * @throws IOException
     */
    public boolean changeDirectory(String path) throws IOException {
        return ftpClient.changeWorkingDirectory(path);
    }

    /**
     * 在服务器上创建目录
     *
     * @param pathName
     * @return
     * @throws IOException
     */
    public boolean createDirectory(String pathName) throws IOException {
        return ftpClient.makeDirectory(pathName);
    }

    /**
     * 在服务器上删除目录,不包含文件
     *
     * @param path
     * @return
     * @throws IOException
     */
    public boolean removeDirectory(String path) throws IOException {
        return ftpClient.removeDirectory(path);
    }

    /**
     * 删除所有文件和目录
     *
     * @param path
     * @param isAll true:删除所有文件和目录
     * @return
     * @throws IOException
     */
    public boolean removeDirectory(String path, boolean isAll)
            throws IOException {

        if (!isAll) {
            return removeDirectory(path);
        }

        FTPFile[] ftpFileArr = ftpClient.listFiles(path);
        if (ftpFileArr == null || ftpFileArr.length == 0) {
            return removeDirectory(path);
        }
        for (FTPFile ftpFile : ftpFileArr) {
            String name = ftpFile.getName();
            if (ftpFile.isDirectory()) {
                //System.out.println("* [sD]Delete subPath ["+path + "/" + name+"]");
                removeDirectory(path + "/" + name, true);
            } else if (ftpFile.isFile()) {
                //System.out.println("* [sF]Delete file ["+path + "/" + name+"]");
                deleteFile(path + "/" + name);
            } else if (ftpFile.isSymbolicLink()) {

            } else if (ftpFile.isUnknown()) {

            }
        }
        return ftpClient.removeDirectory(path);
    }

    /**
     * 检查目录在服务器上是否存在 true：存在  false：不存在
     *
     * @param path
     * @return
     * @throws IOException
     */
    public boolean existDirectory(String path) throws IOException {
        boolean flag = false;
        FTPFile[] ftpFileArr = ftpClient.listFiles(null, FTPFileFilters.DIRECTORIES);
        for (FTPFile ftpFile : ftpFileArr) {
            if (ftpFile.isDirectory()
                    && ftpFile.getName().equalsIgnoreCase(path)) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    /**
     * 检查文件在服务器上是否存在 true：存在  false：不存在
     *
     * @param fileName 文件名称
     * @return
     * @throws IOException
     */
    public boolean existFile(String fileName) throws IOException {
        boolean flag = false;
        FTPFile[] ftpFiles = ftpClient.listFiles(fileName);
        if (ftpFiles == null || ftpFiles.length == 0) {
            return false;
        }
        for (FTPFile ftpFile : ftpFiles) {
            if (ftpFile.isFile()) {
                return true;
            }
        }
        return flag;
    }
    
    /**
     * 判断FTP上是否存在该文件 true：存在  false：不存在
     * 
     * fileName 路径+文件名 例：/home/ap/sit/ebsit/app/bsm/pointEve/259184.txt
     * fc :链接服务器
     */
/*    public boolean isFileDoes(String fileName, FTPClient fc) throws Exception{
    	String[] listNames = fc.listNames(fileName);
    	if(listNames==null){
			return false;
		}
		else {
			return true;
		}
    }*/
    public boolean isFileDoes(String fileName) throws Exception{
    	String[] listNames = ftpClient.listNames(fileName);
    	if(listNames==null || listNames.length == 0){
			return false;
		}
		else {
			return true;
		}
    }
    /**
     * 获取路径的目录列表(仅目录)
     *
     * @param path
     * @return
     * @throws IOException
     */
    public List<String> getDirList(String path) throws IOException {
        FTPFile[] ftpFiles = ftpClient.listFiles(path);
        List<String> retList = new ArrayList<String>();
        if (ftpFiles == null || ftpFiles.length == 0) {
            return retList;
        }
        for (FTPFile ftpFile : ftpFiles) {
            if (ftpFile.isDirectory()) {
                retList.add(ftpFile.getName());
            }
        }
        return retList;
    }

    /**
     * 得到文件列表,listFiles返回包含目录和文件，它返回的是一个FTPFile数组
     * listNames()：只包含目录的字符串数组
     * String[] fileNameArr = ftpClient.listNames(path);
     *
     * @param path:服务器上的文件目录:/DF4
     */
    public List<String> getFileList(String path) throws IOException {
        FTPFile[] ftpFiles = ftpClient.listFiles(path);
        List<String> retList = new ArrayList<String>();
        if (ftpFiles == null || ftpFiles.length == 0) {
            return retList;
        }
        for (FTPFile ftpFile : ftpFiles) {
            if (ftpFile.isFile()) {
                retList.add(ftpFile.getName());
            }
        }
        return retList;
    }

    /**
     * 删除服务器上的文件
     *
     * @param pathName
     * @return
     * @throws IOException
     */
    public boolean deleteFile(String pathName) throws IOException {
        return ftpClient.deleteFile(pathName);
    }

    /**
     * 上传文件到ftp服务器
     * 在进行上传和下载文件的时候，设置文件的类型最好是：
     * ftpUtil.setFileType(FtpUtil.BINARY_FILE_TYPE)
     * localFilePath:本地文件路径和名称
     * remoteFileName:服务器文件名称
     */
    public boolean uploadFile(String localFilePath, String remoteFileName)
            throws IOException {
        boolean flag = false;
        InputStream iStream = null;
        try {
            iStream = new FileInputStream(localFilePath);
            flag = ftpClient.storeFile(remoteFileName, iStream);
        } catch (IOException e) {
            e.printStackTrace();
            flag = false;
            return flag;
        } finally {
            if (iStream != null) {
                iStream.close();
            }
        }
        return flag;
    }

    /**
     * 上传文件到ftp服务器，上传新的文件名称和原名称一样
     *
     * @param fileName：文件名称
     * @return
     * @throws IOException
     */
    public boolean uploadFile(String fileName) throws IOException {
        return uploadFile(fileName, fileName);
    }

    /**
     * 上传文件到ftp服务器
     *
     * @param iStream 输入流
     * @param newName 新文件名称
     * @return
     * @throws IOException
     */
    public boolean uploadFile(InputStream iStream, String newName)
            throws IOException {
        boolean flag = false;
        try {
            flag = ftpClient.storeFile(newName, iStream);
        } catch (IOException e) {
            flag = false;
            return flag;
        } finally {
            if (iStream != null) {
                iStream.close();
            }
        }
        return flag;
    }

    /**
     * 从ftp服务器上下载文件到本地
     *
     * @param remoteFileName：ftp服务器上文件名称
     * @param localFileName：本地文件名称
     * @return
     * @throws IOException
     */
    public boolean download(String remoteFileName, String localFileName)
            throws IOException {
        boolean flag = false;
        File outfile = new File(localFileName);
        OutputStream oStream = null;
        try {
            oStream = new FileOutputStream(outfile);
            flag = ftpClient.retrieveFile(remoteFileName, oStream);
        } catch (IOException e) {
            flag = false;
            return flag;
        } finally {
            if (oStream != null) {
                oStream.close();
            }
            ftpClient.disconnect();
        }
        return flag;
    }

    /**
     * 从ftp服务器(windows)上下载文件到本地
     *
     * @param remotePath：ftp服务器上文件路径
     * @param localPath：本地文件路径
     * @param url：ftp                Host
     * @param port：ftp               Port
     * @param username：用户名
     * @param password：密码
     * @param filename：文件名称
     * @return
     * @throws IOException
     */
    public static boolean downWindowsload(
            String remotePath,
            String localPath,
            String url,
            int port,
            String username,
            String password,
            String filename) throws IOException {
        boolean flag = false;
        FTPClient ftp = new FTPClient();
        int reply;
        ftp.connect(url, port);
        ftp.login(username, password);

        reply = ftp.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftp.disconnect();
            return flag;
        }

        //更改路径
        ftp.changeWorkingDirectory(remotePath);

        FTPFile[] fsFiles = ftp.listFiles();
        for (FTPFile ftpFile : fsFiles) {
            if (ftpFile.getName().equals(filename)) {
                File f = new File(localPath);
                if (!f.exists()) {
                    if(f.mkdirs()){

                    }
                }
                File localFile = new File(f, filename);
                OutputStream os = new FileOutputStream(localFile);
                ftp.retrieveFile(filename, os);
                os.close();
            }
        }
        ftp.logout();
        flag = true;
        ftp.disconnect();

        return flag;
    }
    
    /**
     * 判断ftp服务器上某以文件是否存在
     *
     * @param
     * @return
     * @throws IOException
     */
    public static boolean isExist(  
    		String remotePath,
    		String url,
            int port,
            String username,
            String password,
            String filename){
    	FTPClient ftp = new FTPClient();
    	File f = null;
    	try {
			ftp.login(username, password);
			ftp.changeWorkingDirectory(remotePath);
			f = new File( filename);
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return f.exists();
    }


    /**
     * 从ftp服务器上下载文件到本地
     *
     * @param sourceFileName：服务器资源文件名称
     * @return InputStream 输入流
     * @throws IOException
     */
    public InputStream downFile(String sourceFileName) throws IOException {
        return ftpClient.retrieveFileStream(sourceFileName);
    }

    /**
     * 返回上一级目录
     *
     * @return Boolean 目录切换是否成功
     * @throws IOException
     */
    public Boolean changeToParentDirectory() throws IOException {
        return ftpClient.changeToParentDirectory();
    }
    public  boolean fileIsExist(  
    		String remotePath,
    		String url,
            int port,
            String username,
            String password,
            String filename){
    	File f = null;
    	try {
    		ftpClient.connect(url, port);
    		ftpClient.login(username, password);
    		ftpClient.changeWorkingDirectory(remotePath);
			f = new File( filename);
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return f.exists();
    }

}
