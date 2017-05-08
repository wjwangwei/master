package com.nehow.util;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import java.io.StringWriter;

/**
 * Created by andrew on 4/24/2017.
 */
public class VelocityUtil {
    public static String buildVoucherFromTemplateFile(String fileName, Object param) throws Exception {
        VelocityEngine ve = new VelocityEngine();
        ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        ve.setProperty(Velocity.ENCODING_DEFAULT, "UTF-8");
        ve.setProperty(Velocity.INPUT_ENCODING, "UTF-8");
        ve.setProperty(Velocity.OUTPUT_ENCODING, "GBK");
        ve.init();
        Template t = ve.getTemplate(fileName);
        VelocityContext ctx = new VelocityContext();
        ctx.put("request", param);
        StringWriter sw = new StringWriter();
        t.merge(ctx, sw);
        String content = sw.toString();
        return content;
    }
}
