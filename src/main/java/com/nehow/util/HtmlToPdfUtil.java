package com.nehow.util;

import com.itextpdf.text.pdf.BaseFont;
import lombok.extern.slf4j.Slf4j;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * Created by andrew on 4/24/2017.
 */
@Slf4j
public class HtmlToPdfUtil {
    public static File htmlToPdf(String htmlPath, String rootUrl, String pdfName) throws Exception{
        String exportPdfUrl = rootUrl + "/" + pdfName;
        File outFile = new File(exportPdfUrl);
        if(outFile.exists()){
            outFile.delete();
        }
        //outFile.createNewFile();

        ITextRenderer renderer = new ITextRenderer();
        //String url = new File(htmlPath).toURI().toURL().toString();
        String url = new File(htmlPath).toURI().toString();
        try {
            renderer.setDocument(url);
            System.out.println(url);
        } catch (Exception e) {
            log.error("exception", e);
            System.out.println("生成PDF失败 URL地址为=" + url + ",exportPdfUrl="+exportPdfUrl);
            throw e;
        }

        // 解决中文支持问题
        ITextFontResolver fontResolver = renderer.getFontResolver();
        //String fontResource = HtmlToPdfUtil.class.getClassLoader().getResource("").getPath() + "font/msyh.ttc";
        String fontResource = HtmlToPdfUtil.class.getClassLoader().getResource("font/msyh.ttc").toURI().toString();
        System.out.println("font resource:" + fontResource);
        fontResolver.addFont(fontResource, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        renderer.layout();
        try(OutputStream os = new FileOutputStream(exportPdfUrl)) {
            renderer.createPDF(os);
            os.flush();
            os.close();
            renderer.finishPDF();
        }catch (Exception e) {
            log.error("Pdf转换异常", e);
        }

        return outFile;
    }
}
