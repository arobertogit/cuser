package fii.odiunu.rest;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ojrobert on 1/18/16.
 */
@Path("fs")
public class FileSystemRest {
    private static final String SERVER_UPLOAD_LOCATION_FOLDER = "/cuser/upload";

    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listFiles() {
        File[] files = new File(SERVER_UPLOAD_LOCATION_FOLDER).listFiles();
        if (files == null) {
            return Response.ok("No files found")
                    .build();
        }
        List<String> collect = Arrays.stream(files).map(File::getName).collect(Collectors.toList());
        return Response.ok(collect)
                .build();
    }

    @POST
    @Path("/upload")
//    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(@FormDataParam("file") InputStream fileInputStream,
                               @FormDataParam("file") FormDataContentDisposition contentDispositionHeader) {

        String filePath = SERVER_UPLOAD_LOCATION_FOLDER + contentDispositionHeader.getFileName();
        saveFile(fileInputStream, filePath);
        String output = "File saved to server location : " + filePath;

        return Response.status(200)
                .entity(output)
                .build();
    }

    private void saveFile(InputStream uploadedInputStream, String serverLocation) {
        try {
            byte[] bytes = new byte[1024];
            OutputStream outputStream = new FileOutputStream(new File(serverLocation));
            int read;
            while ((read = uploadedInputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
