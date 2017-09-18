package com.nonage.controller.action;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nonage.dao.CartDAO;

public class CartDeleteAction implements Action {

  @Override
  public void execute(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String url = "NonageServlet?command=cart_list";
    
    //배열안에 체크된 cart seq을 차례로 넣어줌
    String[] cseqArr = request.getParameterValues("cseq");
    
    System.out.println(Arrays.toString(cseqArr));
    
    for(String cseq:cseqArr){
      System.out.println(cseq);
      CartDAO cartDAO = CartDAO.getInstance();
      cartDAO.deleteCart(Integer.parseInt(cseq));
    }
    request.getRequestDispatcher(url).forward(request, response);
  }
}
