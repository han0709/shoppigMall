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
      //�ֹ��� �ֹ���ȣ���� ������.
      ArrayList<Integer> oseqList = orderDAO.selectSeqOrderIng(loginUser.getId());

      ArrayList<OrderVO> orderList = new ArrayList<OrderVO>();

      for (int oseq : oseqList) {
    	
    	//�ֹ���ȣ, ����ھ��̵� ���� ��ǰ ����Ʈ,���� ������
    	//  													         �ֹ���                  �ֹ����   �ֹ���ȣ
        ArrayList<OrderVO> orderListIng = orderDAO.listOrderById(loginUser.getId(), "1", oseq);

        OrderVO orderVO = orderListIng.get(0);//list������ ù��° ���θ� ������
        System.out.println("orderVO"+orderVO);
        
        //��ǰ ù��° �̸� �� �� LIST������ ��
        orderVO.setPname(orderVO.getPname() + " �� "+ orderListIng.size() + "��");
        
        int totalPrice = 0;
        for (OrderVO ovo : orderListIng) {
          totalPrice += ovo.getPrice2() * ovo.getQuantity();
        }
        
        //price2�� �� �ݾ��� �־���
        orderVO.setPrice2(totalPrice);
        
        //������ OrderVo ������ ���ο� arrayList�� orderList�� �־���
        orderList.add(orderVO);
      }
      request.setAttribute("title", "���� ���� �ֹ� ����");
      request.setAttribute("orderList", orderList);
    }
    request.getRequestDispatcher(url).forward(request, response);
  }
}