package com.fss.service.impl;

import com.fss.service.UploadService;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class UploadServiceImpl implements UploadService{
    private final static String allowSuffix = "doc,docx,xls,xlsx,ppt,pptx,rar,zip,pdf,txt";//允许文件格式
    private final static long allowSize = 100L;//允许文件大小

    private String getFileNameNew() {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        return fmt.format(new Date());
    }

    @Override
    public String upload(MultipartFile file, String destDir, HttpServletRequest request) throws Exception {
        String realName = "";
        String path = request.getContextPath();
        String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path;
        try {
            String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
            int length = allowSuffix.indexOf(suffix);
            if (length == -1) {
                throw new Exception("请上传允许格式的文件");
            }
            if (file.getSize() > allowSize * 1024 * 1024) {
                throw new Exception("您上传的文件大小已经超出范围");
            }

            String realPath = request.getSession().getServletContext().getRealPath("/");
            File destFile = new File(realPath + destDir);
            if (!destFile.exists()) {
                destFile.mkdirs();
            }
            String fileNameNew = getFileNameNew() + "." + suffix;
            File f = new File(destFile.getAbsoluteFile() + "/" + fileNameNew);
            file.transferTo(f);
            f.createNewFile();

            realName = fileNameNew;
            return realName;
        } catch (Exception e) {
            throw e;
        }
    }
}
