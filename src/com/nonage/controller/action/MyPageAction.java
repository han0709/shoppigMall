package com.nonage.controller.action;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.nonage.dao.OrderDAO;
import com.nonage.dto.MemberVO;
import com.nonage.dto.OrderVO;

public class MyPageAction implements Action {

  @Override
  public void execute(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String url = "mypage/mypage.jsp";

    HttpSession session = request.getSession();
    MemberVO loginUser = (MemberVO) session.getAttribute("loginUser");

    if (loginUser == null) {
      url = "NonageServlet?command=login_form";
    } else {
      OrderDAO orderDAO = OrderDAO.getInstance();
      //주문한 주문번호들을 가져옴.
      ArrayList<Integer> oseqList = orderDAO.selectSeqOrderIng(loginUser.getId());

      ArrayList<OrderVO> orderList = new ArrayList<OrderVO>();

      for (int oseq : oseqList) {
    	
    	//주문번호, 사용자아이디에 따른 상품 리스트,정보 가져옴
    	//  													         주문자                  주문결과   주문번호
        ArrayList<OrderVO> orderListIng = orderDAO.listOrderById(loginUser.getId(), "1", oseq);

        OrderVO orderVO = orderListIng.get(0);//list라인중 첫번째 라인만 가져옴
        System.out.println("orderVO"+orderVO);
        
        //상품 첫번째 이름 외 총 LIST라인의 수
        orderVO.setPname(orderVO.getPname() + " 외 "+ orderListIng.size() + "건");
        
        int totalPrice = 0;
        for (OrderVO ovo : orderListIng) {
          totalPrice += ovo.getPrice2() * ovo.getQuantity();
        }
        
        //price2에 총 금액을 넣어줌
        orderVO.setPrice2(totalPrice);
        
        //수정한 OrderVo 값들을 새로운 arrayList인 orderList에 넣어줌
        orderList.add(orderVO);
      }
      request.setAttribute("title", "진행 중인 주문 내역");
      request.setAttribute("orderList", orderList);
    }
    request.getRequestDispatcher(url).forward(request, response);
  }
}
