package com.nonage.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import util.DBManager;

import com.nonage.dto.CartVO;
import com.nonage.dto.OrderVO;

public class OrderDAO {

	private OrderDAO() {
	}

	private static OrderDAO instance = new OrderDAO();

	public static OrderDAO getInstance() {
		return instance;
	}

	// 사용자가 주문
	//cartList는 장바구니에 넣은 상품들을 해당 아이디 리스트에 따른것들을  cartVO에 넣은 것들임.
	public int insertOrder(ArrayList<CartVO> cartList, String id) {
		int maxOseq = 0;

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs;

		try {
			conn = DBManager.getConnection();
			
			//id는 세션에서 가져온 것임 ex) 이름만 sql에서 insertOrder로 바꿔준것임
			//주문 번호생성과 주문한 사람을 삽입
			String insertOrder = "insert into orders(oseq, id) values(orders_seq.nextval, ?)";
			pstmt = conn.prepareStatement(insertOrder);
			pstmt.setString(1, id);
			pstmt.executeUpdate();
			pstmt.close();
			
			//바로 위에서 INSERT한 라스트 오더를 가져옴.
			String selectMaxOseq = "select max(oseq) from orders";
			pstmt = conn.prepareStatement(selectMaxOseq);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				maxOseq = rs.getInt(1);
				System.out.println("maxOseq == "+ maxOseq);
			}		
			
			//여러개의 cartVO 리스트를 하나씩의 레코드로 쪼개줌
			for (CartVO cartVO : cartList) {
				insertOrderDetail(cartVO, maxOseq);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBManager.close(conn, pstmt);
		}
		return maxOseq;
	}

	public void insertOrderDetail(CartVO cartVO, int maxOseq) {
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			conn = DBManager.getConnection();

			String insertOrderDetail = "insert into order_detail(odseq, oseq, "
					+ "pseq, quantity) values(order_detail_seq.nextval, ?, ?, ?)";
			pstmt = conn.prepareStatement(insertOrderDetail);
			pstmt.setInt(1, maxOseq);//주문한 주문번호
			pstmt.setInt(2, cartVO.getPseq());//제품번호
			pstmt.setInt(3, cartVO.getQuantity());//수량
			pstmt.executeUpdate();
			pstmt.close();
			
			//장바구니 상태를 주문한것으로 바꿈.
			String updateCartResult = "update cart set result=2 where cseq=?";
			pstmt = conn.prepareStatement(updateCartResult);
			pstmt.setInt(1, cartVO.getCseq());
			pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBManager.close(conn, pstmt);
		}
	}

	// 사용자가 주문 내역 검색
	//                                     사용자 아이디         주문처리                주문번호
	public ArrayList<OrderVO> listOrderById(String id, String result, int oseq) {
		ArrayList<OrderVO> orderList = new ArrayList<OrderVO>();
		String sql = "select * from order_view where id=? and result like '%'||?||'%' and oseq=?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setString(2, result);
			pstmt.setInt(3, oseq);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				OrderVO orderVO = new OrderVO();
				orderVO.setOdseq(rs.getInt("ODSEQ"));
				orderVO.setOseq(rs.getInt("OSEQ"));
				orderVO.setId(rs.getString("ID"));
				orderVO.setIndate(rs.getTimestamp("INDATE"));
				orderVO.setMname(rs.getString("MNAME"));
				orderVO.setZipNum(rs.getString("ZIP_NUM"));
				orderVO.setAddress(rs.getString("ADDRESS"));
				orderVO.setPhone(rs.getString("PHONE"));
				orderVO.setPseq(rs.getInt("PSEQ"));
				orderVO.setQuantity(rs.getInt("QUANTITY"));
				orderVO.setPname(rs.getString("PNAME"));
				orderVO.setPrice2(rs.getInt("PRICE2"));
				orderVO.setResult(rs.getString("RESULT"));
				orderList.add(orderVO);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBManager.close(conn, pstmt, rs);
		}
		return orderList;
	}

	// 현재 진행 중인 주문 내역만 조회                                     String id는 세션에서 가져옴.
	public ArrayList<Integer> selectSeqOrderIng(String id) {
		ArrayList<Integer> oseqList = new ArrayList<Integer>();
		
		//result=1인것은 아직 무통장입금이 확인되지 않은 제품들을 의미함.	
		//DISTINCT를 사용하면 중복 행이 제거되고 고유한 제품 ID만 나타납니다.(sql에서 확인한번 해볼것)
		String sql = "select distinct oseq from order_view where id=? and result='1' order by oseq desc";
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = DBManager.getConnection();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				oseqList.add(rs.getInt("oseq"));//주문번호들이 담겨있음.
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBManager.close(con, pstmt, rs);
		}
		return oseqList;
	}

	/* *
	 * 관리자 모드에서 사용되는 메소드 * *
	 */
	//                                         검색창의 키워드값(주문자이름)
	public ArrayList<OrderVO> listOrder(String member_name) {
		ArrayList<OrderVO> orderList = new ArrayList<OrderVO>();
		
		//                                           주문자이름
		String sql = "select * from order_view where mname like '%'||?||'%' " +
				"order by result, oseq desc";

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql);
			if (member_name == "") {
				pstmt.setString(1, "%");
			} else {
				pstmt.setString(1, member_name);
			}
			rs = pstmt.executeQuery();
			while (rs.next()) {
				OrderVO orderVO = new OrderVO();
				orderVO.setOdseq(rs.getInt("ODSEQ"));
				orderVO.setOseq(rs.getInt("OSEQ"));
				orderVO.setId(rs.getString("ID"));
				orderVO.setPseq(rs.getInt("PSEQ"));
				orderVO.setMname(rs.getString("MNAME"));
				orderVO.setPname(rs.getString("PNAME"));
				orderVO.setQuantity(rs.getInt("QUANTITY"));
				orderVO.setZipNum(rs.getString("ZIP_NUM"));
				orderVO.setAddress(rs.getString("ADDRESS"));
				orderVO.setPhone(rs.getString("PHONE"));
				orderVO.setIndate(rs.getTimestamp("INDATE"));
				orderVO.setPrice2(rs.getInt("PRICE2"));
				orderVO.setResult(rs.getString("RESULT"));//order_detail테이블의 결과값임
				orderList.add(orderVO);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBManager.close(conn, pstmt, rs);
		}
		return orderList;
	}
	
	
	//                               주문디테일 시퀀스 번호
	public void updateOrderResult(String odseq) {
		String sql = "update order_detail set result='2' where odseq=?";

		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, odseq);
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBManager.close(conn, pstmt);
		}
	}
}
