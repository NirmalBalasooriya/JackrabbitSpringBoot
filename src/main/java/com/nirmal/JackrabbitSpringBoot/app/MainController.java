package com.nirmal.JackrabbitSpringBoot.app;

import org.apache.tika.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.beans.factory.annotation.Value;

import javax.jcr.*;

import org.apache.jackrabbit.commons.JcrUtils;
import java.io.InputStream;

@Controller
@Component
public class MainController {

    Logger logger = LoggerFactory.getLogger(MainController.class);

    @Value("${jackrabbit.username}")
    private String username;

    @Value("${jackrabbit.userpassword}")
    private String userPassword;

    @Value("${jackrabbit.url}")
    private String jackrabbitUrl;

    @GetMapping("/uploadResource/{resourceName}")
    @ResponseBody
    public Response uploadResource(@PathVariable("resourceName")String resourceName) throws Exception {
        logger.info("Upload resource resourceName : " + resourceName);
        JackRabitResource jackRabitResource=new JackRabitResource();
        resourceName=resourceName+".jpg";
        Response response = new Response("0", "Resource does not exist", null);
        if (new ClassPathResource(resourceName).exists()){
            uploadFile(resourceName);
            response = new Response("1", "Resource Added Successfully", null);
        }
        return response;
    }

    @GetMapping(
            value = "/getImage/{resourceName}",
            produces = MediaType.IMAGE_JPEG_VALUE
    )
    public @ResponseBody byte[] getImage(@PathVariable("resourceName")String resourceName) throws Exception {
        logger.info("Get Image resourceName : " + resourceName);
        resourceName=resourceName+".jpg";
        if (new ClassPathResource(resourceName).exists()){
            try {
                return getContent(resourceName);
            }catch (PathNotFoundException e){
                return null;
            }

        }
        return null;
    }

    public void uploadFile(String name) throws Exception{
        Repository repository = JcrUtils.getRepository(jackrabbitUrl);
        Session session = repository.login(
                new SimpleCredentials(username, userPassword.toCharArray()));
        try{
            Resource resource = new ClassPathResource(name);
            InputStream stream = resource.getInputStream();
            Node folder = session.getRootNode();
            Node file = folder.addNode(name,"nt:file");
            Node content = file.addNode("jcr:content","nt:resource");
            Binary binary = session.getValueFactory().createBinary(stream);
            content.setProperty("jcr:data",binary);
            content.setProperty("jcr:mimeType","image/gif");
            session.save();
        }finally{
            session.logout();
        }
    }

    public byte[] getContent(String name) throws Exception{

        Repository repository = JcrUtils.getRepository(jackrabbitUrl);
        Session session = repository.login(
                new SimpleCredentials(username, userPassword.toCharArray()));
        Node folder = session.getRootNode();
        Node file=folder.getNode(name);
        Node content=file.getNode("jcr:content");
        String path = content.getPath();
        Binary bin = session.getNode(path).getProperty("jcr:data").getBinary();
        InputStream stream = bin.getStream();
        return IOUtils.toByteArray(stream);
    }
}
