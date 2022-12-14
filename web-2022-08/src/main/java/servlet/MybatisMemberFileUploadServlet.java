package servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import member.MemberVo;
import member.MybatisMemberDao;
import member.MybatisPageVo;

@MultipartConfig(
	location          = "c:/temp",
	maxFileSize       = -1,
	maxRequestSize    = -1,
	fileSizeThreshold = 4096
)
@WebServlet(urlPatterns = {"/mmfs.do"})
public class MybatisMemberFileUploadServlet extends HttpServlet {
	
	public static String path = "C:\\Users\\K\\eclipse-workspace\\web-2022-08\\src\\main\\webapp\\upload\\";

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		switch(req.getParameter("job")) {
		case "insert":
			insert(req, resp);
			break;
		case "update":
			update(req, resp);
			break;
		}
	}
	
	public void insert(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Collection<Part> parts = req.getParts();
		MemberVo bVo = new MemberVo();
		MybatisPageVo pVo = new MybatisPageVo();
		pVo.setNowPage(Integer.parseInt(req.getParameter("nowPage")));
		pVo.setFindStr(req.getParameter("findStr"));
		
		for(Part p : parts) {
			if(p.getHeader("Content-Disposition").contains("filename=")) { // file일 경우
				if(p.getSize() > 0) {
					String sysFile = new Date().getTime() + "-" + p.getSubmittedFileName();
					String oriFile = p.getSubmittedFileName();
					bVo.setSysFile(sysFile);
					bVo.setOriFile(oriFile);
					
					p.write(path + sysFile);
					p.delete();
				}
			} else {
				String tag = p.getName();
				String value = req.getParameter(tag);
				
				switch(tag) {
				case "id":
					bVo.setId(value);
					break;
				case "name":
					bVo.setName(value);
					break;
				case "gender":
					bVo.setGender(value);
					break;
				case "phone":
					bVo.setPhone(value);
					break;
				case "mDate":
					bVo.setmDate(value);
					break;
				}
			}
		}
		
		MybatisMemberDao dao = new MybatisMemberDao();
		String msg = dao.insert(bVo);
		List<MemberVo> list = dao.select(pVo);
		
		req.setAttribute("msg", msg);
		req.setAttribute("pVo", pVo);
		req.setAttribute("list", list);
		RequestDispatcher rd = req.getRequestDispatcher("mybatis/member_select.jsp");
		rd.include(req, resp);
	}
	
	public void update(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Collection<Part> parts = req.getParts();
		MemberVo bVo = new MemberVo();
		MybatisPageVo pVo = new MybatisPageVo();
		
		for(Part p : parts) {
			if(p.getHeader("Content-Disposition").contains("filename=")) {
				if(p.getSize() > 0) {
					String sysFile = new Date().getTime() + "-" + p.getSubmittedFileName();
					String oriFile = p.getSubmittedFileName();
					bVo.setSysFile(sysFile);
					bVo.setOriFile(oriFile);
					
					p.write(path + sysFile);
					p.delete();
					
					//기존 프로필 사진 파일을 삭제
					String delFile = req.getParameter("delFile");
					File file = new File(path + delFile);
					if(file.exists()) file.delete();
				}
			}
		}
		
		bVo.setId(req.getParameter("id"));
		bVo.setName(req.getParameter("name"));
		bVo.setPhone(req.getParameter("phone"));
		bVo.setGender(req.getParameter("gender"));
		bVo.setmDate(req.getParameter("mDate"));
		
		pVo.setNowPage(Integer.parseInt(req.getParameter("nowPage")));
		pVo.setFindStr(req.getParameter("findStr"));
		
		MybatisMemberDao dao = new MybatisMemberDao();
		String msg = dao.update(bVo);
		List<MemberVo> list = dao.select(pVo);
		
		if( !msg.equals("") ) {
			PrintWriter out = resp.getWriter();
			out.print("<script>");
			out.print("    alert('" + msg + "')");
			out.print("</script>");
		}
		
		req.setAttribute("list", list);
		req.setAttribute("pVo", pVo);
		RequestDispatcher rd = req.getRequestDispatcher("mybatis/member_select.jsp");
		rd.include(req, resp);
	}
	
}
