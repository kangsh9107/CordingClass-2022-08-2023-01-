package servlet;

import java.awt.image.BufferedImage;
import java.awt.image.renderable.ParameterBlock;
import java.io.IOException;
import java.util.Collection;

import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

@MultipartConfig(
	location          = "c:/temp",
	maxFileSize       = -1,
	maxRequestSize    = -1,
	fileSizeThreshold = 4096
)
@WebServlet(urlPatterns = {"/thumnail.do"})
public class TumbNailServlet extends HttpServlet {
	String uploadPath = "c:/upload/";

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		StringBuffer info = new StringBuffer();
		Collection<Part> parts = req.getParts();
		
		if(parts == null) return;
		
		for(Part p : parts) {
			if(p.getHeader("Content-Disposition").contains("filename=")) {
				if(p.getSize() > 0) {
					String uploadFile = p.getSubmittedFileName();
					
					info.append("<li>file size : " + p.getSize() + "</li>");
					info.append("<li>file name : " + uploadFile  + "</li>");
					
					p.write(uploadPath + uploadFile);
					p.delete();
					
					// thumbnail 만들기
					ParameterBlock pb = new ParameterBlock();
					pb.add(uploadPath + uploadFile);
					RenderedOp op = JAI.create("fileuploadd", pb);
					
					BufferedImage bi = op.getAsBufferedImage(); // 원본 이미지 버퍼
					BufferedImage thumb = new BufferedImage(bi.getWidth()/3,
															 bi.getHeight()/3,
															 BufferedImage.TYPE_INT_ARGB);
				}
			}
		}
	}

}









