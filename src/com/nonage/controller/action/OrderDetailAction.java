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

public class OrderDetailAction implements Action {

  @Override
  public void execute(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String url = "mypage/orderDetail.jsp";
    
    HttpSession session = request.getSession();
    MemberVO loginUser = (MemberVO) session.getAttribute("loginUser");
    if (loginUser == null) {
      url = "NonageServlet?command=login_form";
    } else {
      
      //주문번호를 가져옴.
      int oseq=Integer.parseInt(request.getParameter("oseq"));
      OrderDAO orderDAO = OrderDAO.getInstance();
      
      //ex) 주문번호(oseq)가 29인 모든 값을 가져옴.                          result(주문상태) 가 1,2 모두 가져옴
      ArrayList<OrderVO> orderList = orderDAO.listOrderById(loginUser.getId(), "%", oseq);
      
      int totalPrice=0;
      for(OrderVO ovo :orderList){//한열씩 가져옴.
        totalPrice+=ovo.getPrice2()*ovo.getQuantity();
      }
      request.setAttribute("orderDetail", orderList.get(0));  
      request.setAttribute("orderList", orderList);
      request.setAttribute("totalPrice", totalPrice);
      System.out.println(orderList);
    }
    request.getRequestDispatcher(url).forward(request, response);
  }    
}
