package com.example.bootdemo.tools.pdf;

import cn.hutool.core.io.FileUtil;
import com.example.bootdemo.tools.Exception.CommonException;
import com.itextpdf.text.pdf.BaseFont;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Locale;
import java.util.UUID;


/**
 * PDF工具类
 *
 * @author HY
 */
@Slf4j
@Component
public class PDFUtil {

    private static String DEFAULT_ENCODING = "utf-8";
    private static String PDF_TYPE = "application/pdf";
    private static boolean DEFAULT_NOCACHE = true;
    private static String HEADER_ENCODING = "utf-8";
    private static String HEADER_NOCACHE = "no-cache";

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    public String creatPdf(String ftlName, Object root, String filename) throws CommonException {
        OutputStream out = null;
        //相对路径
        Configuration cfg = new Configuration();
        try {
            cfg.setLocale(Locale.CHINA);
            cfg.setEncoding(Locale.CHINA, "UTF-8");
            //设置编码
            cfg.setDefaultEncoding("UTF-8");
            Template template = freeMarkerConfigurer.getConfiguration().getTemplate(ftlName);
            template.setEncoding("UTF-8");
            ITextRenderer iTextRenderer = new ITextRenderer();
            //设置字体
            ITextFontResolver fontResolver = iTextRenderer.getFontResolver();
            fontResolver.addFont(new ClassPathResource("fonts/simsun.ttc").getPath(), BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            Writer writer = new StringWriter();
            //数据填充模板
            template.process(root, writer);
            //设置输出文件内容及路径
            String str = writer.toString();
            iTextRenderer.setDocumentFromString(str);
            String url1 = PDFUtil.class.getClassLoader().getResource("static").toURI().toString();
            iTextRenderer.getSharedContext().setBaseURL(url1);
            iTextRenderer.layout();
            //生成PDF
            filename = encodingFilename(filename);
            out = new FileOutputStream(getAbsoluteFile(filename));
            iTextRenderer.createPDF(out, false);
            iTextRenderer.finishPDF();
            return filename;
        } catch (Exception e) {
            log.error("导出pdf失败", e);
            throw new CommonException("导出pdf失败，请联系网站管理员！");
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public static String getAbsoluteFile(String filename) {
        String downloadPath = FileUtil.getTmpDirPath() + File.separator + filename;
        File desc = new File(downloadPath);
        if (!desc.getParentFile().exists()) {
            desc.getParentFile().mkdirs();
        }
        return downloadPath;
    }

    /**
     * 编码文件名
     */
    public static String encodingFilename(String filename) {
        filename = UUID.randomUUID().toString() + "_" + filename + ".pdf";
        return filename;
    }

    public static void renderPdf(HttpServletResponse response, final byte[] bytes, final String filename) {
        initResponseHeader(response, PDF_TYPE);
        setFileDownloadHeader(response, filename, ".pdf");
        if (null != bytes) {
            try {
                response.getOutputStream().write(bytes);
                response.getOutputStream().flush();
            } catch (IOException e) {
                throw new IllegalArgumentException(e);
            }
        }
    }

    /**
     * 分析并设置contentType与headers.
     * StringUtils工具类所属jar
     *  <dependency>
     *      <groupId>org.apache.commons</groupId>
     *      <artifactId>commons-lang3</artifactId>
     *  </dependency>
     */
    private static HttpServletResponse initResponseHeader(HttpServletResponse response, final String contentType, final String... headers) {
        // 分析headers参数
        String encoding = DEFAULT_ENCODING;
        boolean noCache = DEFAULT_NOCACHE;
        for (String header : headers) {
            String headerName = StringUtils.substringBefore(header, ":");
            String headerValue = StringUtils.substringAfter(header, ":");
            if (StringUtils.equalsIgnoreCase(headerName, HEADER_ENCODING)) {
                encoding = headerValue;
            } else if (StringUtils.equalsIgnoreCase(headerName, HEADER_NOCACHE)) {
                noCache = Boolean.parseBoolean(headerValue);
            } else {
                throw new IllegalArgumentException(headerName + "不是一个合法的header类型");
            }
        }
        // 设置headers参数
        String fullContentType = contentType + ";charset=" + encoding;
        response.setContentType(fullContentType);
        if (noCache) {
            // Http 1.0 header
            response.setDateHeader("Expires", 0);
            response.addHeader("Pragma", "no-cache");
            // Http 1.1 header
            response.setHeader("Cache-Control", "no-cache");
        }
        return response;
    }

    /**
     * 设置让浏览器弹出下载对话框的Header.
     *
     * @param
     */
    public static void setFileDownloadHeader(HttpServletResponse response, String fileName, String fileType) {
        try {
            // 中文文件名支持
            String encodedfileName = new String(fileName.getBytes("GBK"), "ISO8859-1");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedfileName + fileType + "\"");
        } catch (UnsupportedEncodingException e) {
        }
    }
}
