package member;

import java.io.File;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import mybatis.MyFactory;
import servlet.MybatisMemberFileUploadServlet;

public class MybatisMemberDao {
	SqlSession sqlSession;
	MybatisPageVo pVo;
	
	public MybatisMemberDao() {
		sqlSession = MyFactory.getFactory().openSession();
	}
	
	public String insert(MemberVo bVo) {
		String msg = "";
		int cnt = sqlSession.insert("member.insert", bVo);
		
		if(cnt > 0) {
			sqlSession.commit();
			msg = "์ ์ฅ ์ฑ๊ณต๐๐";
		} else {
			sqlSession.rollback();
			msg = "์ ์ฅ ์ค๋ฅ๐ข๐ข";
		}
		
		//sqlSession.close();
		return msg;
	}
	
	public List<MemberVo> select(MybatisPageVo pVo) {
		//sqlSession = MyFactory.getFactory().openSession();
		List<MemberVo> list = null;
		
		//1) ๊ฒ์์ด์ ํด๋นํ๋ ๋ฐ์ดํฐ ๊ฐ์(totSize)
		int totSize = sqlSession.selectOne("member.totSize", pVo.getFindStr());
		
		//2) page๊ณ์ฐ
		pVo.setTotSize(totSize);
		pVo.compute(); //MybatisPageVo์์ setTotSize ์์์ compute๋ฅผ ๋ถ๋ฅด๋ฉด ์ฌ๊ธฐ์ ํ  ํ์ ์์ด์ง๋ค. ์ด๊ฒ ์ข ๋ ์บก์ํ๊ฐ ๋๊ณ  ์๋ํํฐ ๊ฐ๋ฐ์๋ฅผ ๋ฐฐ๋ คํ๋ ๋ฐฉ๋ฒ์ด๋ค.
		this.pVo = pVo;
		
		//3) select
		list = sqlSession.selectList("member.select", pVo);
		
		sqlSession.close();
		return list;
	}
	
	public MemberVo view(String id) {
		MemberVo bVo = null;
		bVo = sqlSession.selectOne("member.view", id);
		
		sqlSession.close();
		return bVo;
	}
	
	public String delete(String id, String delFile) {
		String msg = "";
		int cnt = sqlSession.delete("member.delete", id); //resultType์ด ์์ผ๋ฉด ๋ฐํํ์ด int๋ค.
		
		if(cnt > 0) {
			sqlSession.commit();
			File file = new File(MybatisMemberFileUploadServlet.path + delFile);
			if(file.exists()) file.delete();
			msg = "์ญ์  ์ฑ๊ณต๐๐";
		} else {
			sqlSession.rollback();
			msg = "์ญ์  ์ค๋ฅ๐ข๐ข";
		}
		
		//delete ํ ๋ฐ๋ก select๋ฅผ ํ ๊ฑฐ๋๊น sqlSession.close();๋ฅผ ๋ฐ๋ก ํ๋ฉด ์๋๋ค.
		return msg;
	}
	
	public String update(MemberVo bVo) {
		String msg = "";
		int cnt = sqlSession.update("member.update", bVo);
		
		if(cnt > 0) {
			sqlSession.commit();
			
		} else {
			sqlSession.rollback();
			msg = "์์  ์ค๋ฅ๐ข๐ข";
		}
		
		//update ํ ๋ฐ๋ก select๋ฅผ ํ ๊ฑฐ๋๊น sqlSession.close();๋ฅผ ๋ฐ๋ก ํ๋ฉด ์๋๋ค.
		return msg;
	}

	public MybatisPageVo getpVo() { return pVo; }
	
}
